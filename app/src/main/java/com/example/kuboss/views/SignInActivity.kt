package com.example.kuboss.views

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.kuboss.MainActivity
import com.example.kuboss.R
import com.example.kuboss.extensions.Extensions.toast
import com.example.kuboss.utils.FirebaseUtils.firebaseAuth
/** fix missing imports **/
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    lateinit var signInEmail: String
    lateinit var signInPassword: String
    lateinit var signInInputsArray: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signInInputsArray = arrayOf(etLoginEmail.editText!!, etLoginPassword.editText!!)
        btn_login.setOnClickListener {
            signInUser()
        }
    }

    private fun notEmpty(): Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()

    private fun signInUser() {
        signInEmail = etLoginEmail.editText?.text.toString().trim()
        signInPassword = etLoginPassword.editText?.text.toString().trim()

        if (notEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {
                        //startActivity(Intent(this, HomeActivity::class.java))
                        startActivity(Intent(this, MainActivity::class.java))
                        toast("signed in successfully")
                        finish()
                    } else {
                        toast("Invalid email and password combination.")
                    }
                }
        } else {
            signInInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        }
    }
}