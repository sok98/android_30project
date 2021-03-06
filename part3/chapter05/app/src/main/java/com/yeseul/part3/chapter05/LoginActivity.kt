package com.yeseul.part3.chapter05

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yeseul.part3.chapter05.DBKey.Companion.USERS
import com.yeseul.part3.chapter05.DBKey.Companion.USER_ID
import com.yeseul.part3.chapter05.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create()

        initEmailAndPasswordEditText()
        initSignUpButton()
        initLoginButton()
        initFacebookLoginButton()


    }

    private fun initEmailAndPasswordEditText() {
        binding.emailEditText.addTextChangedListener {
            val enable =
                binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
            binding.loginButton.isEnabled = enable
            binding.signUpButton.isEnabled = enable
        }
        binding.passwordEditText.addTextChangedListener {
            val enable =
                binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
            binding.loginButton.isEnabled = enable
            binding.signUpButton.isEnabled = enable
        }
    }

    private fun initSignUpButton() {
        binding.signUpButton.setOnClickListener {
            val email = getInputEmail()
            val password = getInputPassword()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "??????????????? ??????????????????. ????????? ????????? ?????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                    } else {
                        if (password.length < 6) {
                            Toast.makeText(this, "??????????????? 6?????? ?????? ??????????????????.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "??????????????? ??????????????????. ?????? ????????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }

    private fun initLoginButton() {
        binding.loginButton.setOnClickListener {
            val email = getInputEmail()
            val password = getInputPassword()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        handleSuccessLogin()
                    } else {
                        Toast.makeText(this, "???????????? ??????????????????. ????????? ?????? ??????????????? ??????????????????", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun initFacebookLoginButton() {
        binding.facebookLoginButton.setPermissions("email", "public_profile")
        binding.facebookLoginButton.registerCallback(callbackManager, object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this@LoginActivity) { task ->
                        if (task.isSuccessful){
                            handleSuccessLogin()
                        } else {
                            Toast.makeText(this@LoginActivity, "???????????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(this@LoginActivity, "???????????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            }

            override fun onCancel() {}

        })
    }

    private fun getInputEmail(): String {
        return binding.emailEditText.text.toString()
    }

    private fun getInputPassword(): String {
        return binding.passwordEditText.text.toString()
    }

    private fun handleSuccessLogin() {
        if(auth.currentUser == null) {
            Toast.makeText(this, "???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, "???????????? ?????????????????????.", Toast.LENGTH_SHORT).show()

        val userId = auth.currentUser?.uid.orEmpty()
        val currentUserDB = Firebase.database.reference.child(USERS).child(userId)
        val user = mutableMapOf<String, Any>()
        user[USER_ID] = userId
        currentUserDB.updateChildren(user)

        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}