package com.example.expireddatemonitor

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils

import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class additemActivity : AppCompatActivity(),DatePickerDialog.OnDateSetListener {
    var day=0
    var month=0
    var year=0
    var savedDay=0
    var savedMonth=0
    var savedYear=0
    private var itemname: EditText? = null
    private var itemcategory: EditText? = null
    private var itemexpire: Button? = null
    private var itembarcode: TextView? = null
    private var showdate1: TextView? = null
    private var firebaseAuth: FirebaseAuth? = null
    var scanbutton: Button? = null
    var additemtodatabase: Button? = null
    var databaseReference: DatabaseReference? = null
    var databaseReferencecat: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_additem)
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReferencecat = FirebaseDatabase.getInstance().getReference("Users")

        additemtodatabase = findViewById(R.id.additembuttontodatabase)
        scanbutton = findViewById(R.id.buttonscan)
        itemname = findViewById(R.id.edititemname)
        itemcategory = findViewById(R.id.editcategory)
        itemexpire = findViewById(R.id.editexpire)
        itembarcode = findViewById(R.id.barcodeview)
        resulttextview = findViewById(R.id.barcodeview)
        showdate1=findViewById(R.id.showdate)



        scanbutton?.run {
            setOnClickListener(View.OnClickListener {
                startActivity(Intent(applicationContext, ScanCodeActivity::class.java)) })
        }
        additemtodatabase?.run { setOnClickListener(View.OnClickListener { additem() }) }

        pickDate()
    }
    private fun getDateTimeCalendar(){
        val cal:Calendar= Calendar.getInstance()
        day=cal.get(Calendar.DAY_OF_MONTH)
        month=cal.get(Calendar.MONTH)
        year=cal.get(Calendar.YEAR)
    }
    private fun pickDate(){
        itemexpire?.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(this,this,year,month,day).show()
        }
    }



    fun additem() {
        val itemnameValue = itemname!!.text.toString()
        val itemcategoryValue = itemcategory!!.text.toString()
        val itemexpireValue = showdate1!!.text.toString()
        val itembarcodeValue = itembarcode!!.text.toString()
        val users = firebaseAuth!!.currentUser
        val finaluser = users!!.email
        val resultemail = finaluser!!.replace(".", "")
        if (itembarcodeValue.isEmpty()) {
            itembarcode!!.error = "It's Empty"
            itembarcode!!.requestFocus()
            return
        }
        if (!TextUtils.isEmpty(itemnameValue) && !TextUtils.isEmpty(itemcategoryValue) && !TextUtils.isEmpty(itemexpireValue)) {
            val items = Items(itemnameValue, itemcategoryValue, itemexpireValue, itembarcodeValue)



            databaseReferencecat!!.child(resultemail).child("Items").child(itembarcodeValue).setValue(items)
            itemname!!.setText("")
            itembarcode!!.text = ""
            showdate1!!.setText("")
            itembarcode!!.text = ""
            Toast.makeText(this@additemActivity, "$itemnameValue Added", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@additemActivity, "Please Fill all the fields", Toast.LENGTH_SHORT).show()
        }
    }






    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmField
        var resulttextview: TextView? = null
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        savedDay=p3
        savedMonth=p2
        savedYear=p1
        getDateTimeCalendar()
        showdate1!!.text="$savedDay-$savedMonth-$savedYear"
    }


}