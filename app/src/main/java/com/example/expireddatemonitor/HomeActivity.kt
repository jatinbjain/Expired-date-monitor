package com.example.expireddatemonitor


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.Button
import android.widget.TextView

import androidx.cardview.widget.CardView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var googleSignInClient: GoogleSignInClient
    private var firebaseAuth: FirebaseAuth? = null
    var firebasenameview: TextView? = null
    var toast: Button? = null
    private var addItems: CardView? = null
    private var deleteItems: CardView? = null
    private var scanItems: CardView? = null
    private var viewInventory: CardView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val txtEmail=findViewById<TextView>(R.id.txtEmail)
        val btnSignOut=findViewById<Button>(R.id.btnSignOut)
        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestIdToken(getString(R.string.client_id))
                .build()
        googleSignInClient= GoogleSignIn.getClient(this,gso )
        txtEmail.text=Firebase.auth.currentUser!!.email
        btnSignOut.setOnClickListener{
            Firebase.auth.signOut()
            googleSignInClient.signOut().addOnCompleteListener(this){
                finish()
            }
        }
        //firebasenameview = findViewById(R.id.firebasename)
        firebaseAuth = FirebaseAuth.getInstance()
        val users = firebaseAuth!!.getCurrentUser()
        val finaluser = users!!.email
        val result = finaluser!!.substring(0, finaluser.indexOf("@"))
        val resultemail = result.replace(".", "")
        //firebasenameview.setText("Welcome, $resultemail")
        addItems = findViewById<View>(R.id.addItems) as CardView
        deleteItems = findViewById<View>(R.id.deleteItems) as CardView
        scanItems = findViewById<View>(R.id.scanItems) as CardView
        viewInventory = findViewById<View>(R.id.viewInventory) as CardView
        addItems!!.setOnClickListener(this)
        deleteItems!!.setOnClickListener(this)
        scanItems!!.setOnClickListener(this)
        viewInventory!!.setOnClickListener(this)
    }
    override fun onClick(view: View) {
        val i: Intent
        when (view.id) {
            R.id.addItems -> {
                i = Intent(this, additemActivity::class.java)
                startActivity(i)
            }
            R.id.deleteItems -> {
                i = Intent(this, deleteItemsActivity::class.java)
                startActivity(i)
            }
            R.id.scanItems -> {
                i = Intent(this, scanItemsActivity::class.java)
                startActivity(i)
            }
            R.id.viewInventory -> {
                i = Intent(this, viewProductActivity::class.java)
                startActivity(i)
            }
            else -> {
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}


