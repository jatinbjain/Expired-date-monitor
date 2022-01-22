package com.example.expireddatemonitor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    companion object{
        const val RC_SIGNIN=25
    }
    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient:GoogleSignInClient
    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if(account!=null) {
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnSignIn=findViewById<Button>(R.id.btnSgnIn)
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail().requestIdToken(getString(R.string.client_id))
            .build()
        googleSignInClient=GoogleSignIn.getClient(this,gso )
        auth = Firebase.auth
        btnSignIn.setOnClickListener(){
            doSignIn()
        }

    }
    private fun doSignIn(){
        val signIntent=googleSignInClient.signInIntent
        startActivityForResult(signIntent, RC_SIGNIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== RC_SIGNIN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account=task.getResult(ApiException::class.java)
                 doAuthentication(account!!.idToken)
            }
            catch (e:ApiException){
                    Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
            }
        }
    }
     private fun doAuthentication(idToken:String?){
         val credentials=GoogleAuthProvider.getCredential(idToken,null)
         auth.signInWithCredential(credentials)
             .addOnCompleteListener(this){ task->

                 if(task.isSuccessful){
                    startActivity(Intent(this,HomeActivity::class.java))
                 }
                 else{
                     val text="Auth Fail"
                     Toast.makeText(this,text, Toast.LENGTH_LONG).show()
                 }
             }

     }
}