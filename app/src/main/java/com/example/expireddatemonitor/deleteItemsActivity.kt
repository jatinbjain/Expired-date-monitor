package com.example.expireddatemonitor

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class deleteItemsActivity : AppCompatActivity() {
    private var firebaseAuth: FirebaseAuth? = null
    var scantodelete: Button? = null
    var deletebtn: Button? = null
    var databaseReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_items)
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        resultdeleteview = findViewById(R.id.barcodedelete)
        scantodelete = findViewById(R.id.buttonscandelete)
        deletebtn = findViewById(R.id.deleteItemToTheDatabasebtn)
        scantodelete?.run { setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext, ScanCodeActivitydel::class.java)) }) }
        deletebtn?.run { setOnClickListener(View.OnClickListener { deletefrmdatabase() }) }
    }

    fun deletefrmdatabase() {
        val deletebarcodevalue = resultdeleteview!!.text.toString()
        val users = firebaseAuth!!.currentUser
        val finaluser = users!!.email
        val resultemail = finaluser!!.replace(".", "")
        if (!TextUtils.isEmpty(deletebarcodevalue)) {
            databaseReference!!.child(resultemail).child("Items").child(deletebarcodevalue).removeValue()
            Toast.makeText(this@deleteItemsActivity, "Item is Deleted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@deleteItemsActivity, "Please scan Barcode", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmField
        var resultdeleteview: TextView? = null
    }
}