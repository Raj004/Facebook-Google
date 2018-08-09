package com.example.rajshekhar.kotlin_welcome2

import android.content.Intent
import android.content.pm.PackageInstaller
import android.media.tv.TvInputService
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.facebook.*
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import kotlinx.android.synthetic.main.activity_main.*
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import org.json.JSONException
import java.util.*
import android.provider.Contacts.People
import android.service.textservice.SpellCheckerService
import android.support.annotation.NonNull
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private val TAG = MainActivity::class.java.simpleName
    var gmailId: String? = null
    var gmailName: String? = null
    var gmailUserEmailId: String? = null
    private val RC_SIGN_IN = 7
    private var callbackManager: CallbackManager? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onClicKListener()

        //google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
        // Customizing G+ button
        google_login.setSize(SignInButton.SIZE_STANDARD)
        google_login.setScopes(gso.scopeArray)

        //facebook
        callbackManager = CallbackManager.Factory.create()

    }
    private fun onClicKListener() {
        google_login.setOnClickListener(this)
        facebook_login.setOnClickListener(this)
        text_or.setOnClickListener(this)
        login_welcome.setOnClickListener(this)
        sign_welcome.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.google_login -> googleLogin()
            R.id.facebook_login -> facebookLogin()
            R.id.text_or -> textOr()
            R.id.login_welcome -> appLogin()
            R.id.sign_welcome -> signUp()
        }
    }

    private fun textOr() {
        Toast.makeText(this,
                "Or Sign with email", Toast.LENGTH_LONG).show(); }

    private fun signUp() {
        Toast.makeText(this,
                "Sign Up", Toast.LENGTH_LONG).show();
    }

    private fun appLogin() {
        var intent = Intent(this@MainActivity, Main2Activity::class.java)
        startActivity(intent)
        Toast.makeText(this,
                "App Login", Toast.LENGTH_LONG).show();
    }

    private fun googleLogin() {
        Toast.makeText(this,
                "Google Login", Toast.LENGTH_LONG).show();

        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
        Log.e("Google-----", signInIntent.toString())


        val opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient)
        if (opr.isDone) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            val result = opr.get()

            handleSignInResult(result)
            Log.e("Details Google",result.toString())

        } else {
            opr.setResultCallback(object : ResultCallback<GoogleSignInResult> {
                override fun onResult(googleSignInResult: GoogleSignInResult) {
                    handleSignInResult(googleSignInResult)

                }
            })
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult?) {
        if (result!!.isSuccess()) {
            val acct = result.signInAccount
            gmailId = acct!!.getId()
            gmailName = acct!!.getDisplayName()
            gmailUserEmailId = acct!!.getEmail()
          //  Log.e("gmailId", "is -->" + gmailId);
            Log.e("gmailName", "is -->" + gmailName);
            Log.e("gmailUserEmailId", "is -->" + gmailUserEmailId);

            //Add your next activity in here
            var intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()

        } else {
            //  Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
        }
    }
    private fun facebookLogin() {
        callbackManager = CallbackManager.Factory.create()
        FacebookSdk.sdkInitialize(this@MainActivity)
        initCallbackManager()
        if (isLoggedIn()) {
            //  goMainScreen()

        } else

            LoginManager.getInstance().logInWithReadPermissions(this@MainActivity, Arrays.asList("public_profile"))
    }

    fun isLoggedIn(): Boolean {
        return AccessToken.getCurrentAccessToken() != null &&AccessToken.isCurrentAccessTokenActive()

    }
    //facebbok Manager
    private fun initCallbackManager() {
        val loggedOut = AccessToken.getCurrentAccessToken() == null

        if (!loggedOut) {
            Log.e("TAG", "Username is: " + Profile.getCurrentProfile().name)
            //Using Graph API
            //   getUserProfile(AccessToken.getCurrentAccessToken())
        }

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {

            }

            override fun onSuccess(loginResult: LoginResult) {
                AccessToken.getCurrentAccessToken()
                Profile.getCurrentProfile()
            }

            override fun onError(e: FacebookException) {

            }
        })
    }
//    private fun goMainScreen() {
//        var intent = Intent(this@MainActivity, HomeActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
//    }


    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d(TAG, "onConnectionFailed:" + p0);
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
            Log.e("Google-----", result.toString())

        }
        callbackManager!!.onActivityResult(requestCode, resultCode, data)

    }


  }