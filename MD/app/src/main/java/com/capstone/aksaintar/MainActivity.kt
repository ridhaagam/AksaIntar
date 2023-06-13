package com.capstone.aksaintar

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.capstone.aksaintar.ui.theme.AksaIntarTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pub.devrel.easypermissions.EasyPermissions

private const val SHARED_PREF_NAME = "login_session"
private const val KEY_EMAIL = "email"
private const val KEY_IS_GUEST = "is_guest"

class MainActivity : ComponentActivity() {
    private val _googleAccount = MutableStateFlow<GoogleSignInAccount?>(null)
    val googleAccount: StateFlow<GoogleSignInAccount?> = _googleAccount
    private var signInLauncher: ActivityResultLauncher<Intent>
    private lateinit var sharedPreferences: SharedPreferences

    init {
        signInLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        println(account)
                        _googleAccount.value = account
                        saveLoginSession(account)
                    } catch (e: ApiException) {
                        Log.w(TAG, getString(R.string.sign_in_failed), e)
                    }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        setContent {
            AksaIntarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    val context = LocalContext.current
                    AksaIntarApp(
                        startGoogleSignIn = ::startGoogleSignIn,
                        googleAccount = googleAccount,
                        context = context
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        restoreLoginSession()
    }

    private fun saveLoginSession(account: GoogleSignInAccount?) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_EMAIL, account?.email)
        editor.putBoolean(KEY_IS_GUEST, false)
        editor.apply()
    }

    private fun restoreLoginSession() {
        val email = sharedPreferences.getString(KEY_EMAIL, null)
        val isGuest = sharedPreferences.getBoolean(KEY_IS_GUEST, true)

        _googleAccount.value = if (isGuest) null else GoogleSignIn.getLastSignedInAccount(this)
    }

    fun startGoogleSignIn() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
