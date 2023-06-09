const processFile = require("../middleware/upload")
const mysql = require('mysql')
const { format } = require("util")
const { Storage } = require("@google-cloud/storage")
const storage = new Storage({ keyFilename: "aksa-intar-bbeb3b0fdd2e.json" })
const bucket = storage.bucket("aksa-intar")
const sizeOf = require('buffer-image-size')
const dateFormat = require('date-and-time')

const connection = mysql.createConnection({
  host: '127.0.0.1',
  user: 'root',
  database: 'aksa-intar',
  password: ''
})

const upload = async (req, res) => {
  try {
    await processFile(req, res);

    if (!req.file) {
      return res.status(400).send({ message: "Please upload a file!" });
    }

    // Create a new blob in the bucket and upload the file data.
    const blob = bucket.file(req.file.originalname);
    const dimensions = sizeOf(req.file.buffer)
    const now = dateFormat.format(new Date(), "YYYY-MM-DD")
    const blobStream = blob.createWriteStream({
      resumable: false,
      metadata: {
        contentType: req.file.mimetype
      }
    });

    blobStream.on("error", (err) => {
      res.status(500).send({ message: err.message });
    });

    blobStream.on("finish", async (data) => {
      // Create URL for directly file access via HTTP.
      const publicUrl = format(
        `https://storage.googleapis.com/${bucket.name}/${blob.name}`
      );

      try {
        // Make the file public
        await bucket.file(req.file.originalname).makePublic();
      } catch {
        return res.status(500).send({
          message:
            `Uploaded the file successfully: ${req.file.originalname}, but public access is denied!`,
          url: publicUrl,
        });
      }
      const query = "INSERT INTO main (file_name, height, width, date_captured, categories, file_url) values (?, ?, ?, ?, ?, ?)"
      connection.query(query, [req.file.originalname, dimensions.height, dimensions.width, now, req.body.category, publicUrl], (err, rows, fields) => {
        if (err) {
          bucket.file(req.file.originalname).delete()
          res.status(500).send({message: err.sqlMessage})
        } else {
          res.status(200).send({
            message: "Uploaded the file successfully: " + req.file.originalname,
            url: publicUrl
          });
        }
    })
    });

    blobStream.end(req.file.buffer);
  } catch (err) {
    if (err.code == "LIMIT_FILE_SIZE") {
      return res.status(500).send({
        message: "File size cannot be larger than 5MB!",
      });
    }
    res.status(500).send({
      message: `Could not upload the file: ${req.file.originalname}. ${err}`,
    });
  }
};
const getListFiles = async (req, res) => {
  try {
    const [files] = await bucket.getFiles();
    let fileInfos = [];

    files.forEach((file) => {
      fileInfos.push({
        name: file.name,
        url: file.metadata.mediaLink,
      });
    });

    res.status(200).send(fileInfos);
  } catch (err) {
    console.log(err);

    res.status(500).send({
      message: "Unable to read list of files!",
    });
  }
};
const download = async (req, res) => {
  try {
    const [metaData] = await bucket.file(req.params.name).getMetadata();
    res.redirect(metaData.mediaLink);
    
  } catch (err) {
    res.status(500).send({
      message: "Could not download the file. " + err,
    });
  }
};

module.exports = {
  upload,
  getListFiles,
  download
};