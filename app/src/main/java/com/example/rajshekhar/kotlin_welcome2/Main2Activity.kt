package com.example.rajshekhar.kotlin_welcome2

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main2.*
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import org.json.JSONException
import org.json.JSONObject
import com.facebook.login.LoginManager
import java.util.*
import java.util.Arrays.asList
import com.facebook.Profile.getCurrentProfile
import com.facebook.Profile.getCurrentProfile
import com.facebook.internal.ImageRequest
import com.facebook.GraphResponse
import com.facebook.GraphRequest
import com.facebook.AccessToken
import com.facebook.FacebookException
import com.facebook.FacebookCallback
import com.facebook.CallbackManager
import java.util.Arrays.asList
import com.facebook.Profile.getCurrentProfile
import com.facebook.internal.ImageRequest.getProfilePictureUri
import android.widget.TextView
import com.squareup.picasso.Picasso


class Main2Activity : AppCompatActivity() {

    internal lateinit var loginButton: LoginButton

    var callbackManager: CallbackManager? = null
    var imageView: ImageView? = null
    var txtUsername: TextView? = null
    var txtEmail:TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        loginButton = findViewById(R.id.login_button);
        imageView = findViewById(R.id.imageView);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);




        val loggedOut = AccessToken.getCurrentAccessToken() == null

        if (!loggedOut) {
            Picasso.with(this).load(Profile.getCurrentProfile().getProfilePictureUri(200, 200)).into(imageView)
            Log.d("TAG", "Username is: " + Profile.getCurrentProfile().name)

            //Using Graph API
            getUserProfile(AccessToken.getCurrentAccessToken())
        }

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"))
        callbackManager = CallbackManager.Factory.create()

        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                val loggedIn = AccessToken.getCurrentAccessToken() == null
                Log.d("API123", loggedIn.toString() + " ??")

            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getUserProfile(currentAccessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(
                currentAccessToken) { `object`, response ->
            Log.d("TAG", `object`.toString())
            try {
                   `object`.getString("first_name")
                val last_name = `object`.getString("last_name")
                val email = `object`.getString("email")
                val id = `object`.getString("id")
                val image_url = "https://graph.facebook.com/$id/picture?type=normal"

                txtUsername!!.text = "First Name: $\nLast Name: $last_name"
                txtEmail!!.text = email
                Picasso.with(this).load(image_url).into(imageView);


            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "first_name,last_name,email,id")
        request.parameters = parameters
        request.executeAsync()

    }
}



































//class Main2Activity : AppCompatActivity() {
//    private val profileTracker: ProfileTracker? = null
//
//    private var loginButton: LoginButton? = null
//    private var callbackManager: CallbackManager? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main2)
//
//        callbackManager = CallbackManager.Factory.create()
//        loginButton = findViewById(R.id.loginButton) as LoginButton
//
//
//
//        loginButton!!.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
//            override fun onSuccess(loginResult: LoginResult) {
//                goMainScreen()
//            }
//
//            override fun onCancel() {
//                Toast.makeText(applicationContext, R.string.cancel_login, Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onError(error: FacebookException) {
//                Toast.makeText(applicationContext, R.string.error_login, Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun goMainScreen() {
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        callbackManager!!.onActivityResult(requestCode, resultCode, data)
//    }
//
//    private val callback = object : FacebookCallback<LoginResult> {
//        override fun onSuccess(loginResult: LoginResult) {
//            val accessToken = loginResult.accessToken
//            val profile = Profile.getCurrentProfile()
//            displayMessage(profile)
//
//        }
//
//        override fun onCancel() {
//
//        }
//
//        override fun onError(e: FacebookException) {
//
//        }
//    }



//    private fun setFacebookData(loginResult:LoginResult) {
//val request = GraphRequest.newMeRequest(
//loginResult.accessToken
//) { `object`, response ->
//    // Application code
//    try {
//        Log.i("Response", response.toString())
//
//        val email = response.jsonObject.getString("email")
//        val firstName = response.jsonObject.getString("first_name")
//        val lastName = response.jsonObject.getString("last_name")
//        var profileURL = ""
//        if (Profile.getCurrentProfile() != null) {
//            profileURL = ImageRequest.getProfilePictureUri(Profile.getCurrentProfile().id, 400, 400).toString()
//        }
//
//        //TODO put your code here
//    } catch (e:JSONException) {
//        Toast.makeText(this@ActivitySignIn, R.string.error_occurred_try_again, Toast.LENGTH_SHORT).show()
//    }
//}
//        val parameters = Bundle()
//        parameters.putString("fields", "id,email,first_name,last_name")
//        request.parameters = parameters
//        request.executeAsync()
//}

//    private fun displayMessage(profile: Profile?) {
//
//    }


    /* fun facebookLogin() {
         LoginManager.getInstance().logInWithReadPermissions(this@MainActivity, Arrays.asList("public_profile"))

         LoginManager.getInstance().registerCallback(callbackManager, object:FacebookCallback<LoginResult> {
             override fun onSuccess(loginResult:LoginResult) {
                 Log.i("MainActivity", "@@@onSuccess")
                 val request = GraphRequest.newMeRequest(
                         loginResult.accessToken
                 ) { `object`, response ->
                     Log.i("MainActivity", "@@@response: " + response.toString())

                     try {
                         val name = `object`.getString("name")
                     } catch (e: JSONException) {
                         e.printStackTrace()
                     }
                 }
                 val parameters = Bundle()
                 parameters.putString("fields", "id,name,email,gender, birthday")
                 request.parameters = parameters
                 request.executeAsync()
             }

             override fun onCancel() {
                 Log.i("MainActivity", "@@@onCancel")
             }

             override fun onError(error:FacebookException) {
                 Log.i("MainActivity", "@@@onError: " + error.message)
             }
         })
}*/

































//        loginButton = findViewById(R.id.facebook_login) as LoginButton
//        loginButton!!.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
//            override fun onSuccess(loginResult: LoginResult) {
//                goMainScreen()
//            }
//
//            override fun onCancel() {
//                Toast.makeText(applicationContext, R.string.cancel_login, Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onError(error: FacebookException) {
//                Toast.makeText(applicationContext, R.string.error_login, Toast.LENGTH_SHORT).show()
//            }
//        })
