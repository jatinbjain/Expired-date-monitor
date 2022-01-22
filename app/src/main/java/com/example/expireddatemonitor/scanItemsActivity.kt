package com.example.expireddatemonitor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class scanItemsActivity : AppCompatActivity() {
    private var firebaseAuth: FirebaseAuth? = null
    var scantosearch: ImageButton? = null
    var searchbtn: Button? = null
    var adapter: Adapter? = null
    var mrecyclerview: RecyclerView? = null
    var mdatabaseReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_items)
        firebaseAuth = FirebaseAuth.getInstance()
        val users = firebaseAuth!!.getCurrentUser()
        val finaluser = users!!.email
        val resultemail = finaluser!!.replace(".", "")
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Items")
        resultsearcheview = findViewById(R.id.searchfield)
        scantosearch = findViewById(R.id.imageButtonsearch)
        searchbtn = findViewById(R.id.searchbtnn)
        mrecyclerview = findViewById(R.id.recyclerViews)
        val manager = LinearLayoutManager(this)
        val run = mrecyclerview?.run {
            setLayoutManager(manager)
            setHasFixedSize(true)
        }
       // mrecyclerview.setLayoutManager(LinearLayoutManager(this))
        scantosearch?.run {
        //    mrecyclerview.setLayoutManager(LinearLayoutManager(this))
            setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext,ScanCodeActivitysearch::class.java)) })
        }
        searchbtn?.run {
         //   mrecyclerview.setLayoutManager(LinearLayoutManager(this))
            scantosearch?.run { setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext, ScanCodeActivitysearch::class.java)) }) }
            setOnClickListener(View.OnClickListener {
                val searchtext = com.example.expireddatemonitor.scanItemsActivity.Companion.resultsearcheview?.getText().toString()
                firebasesearch(searchtext)
            })
        }
    }

    fun firebasesearch(searchtext: String) {
        val firebaseSearchQuery = mdatabaseReference!!.orderByChild("itembarcode").startAt(searchtext).endAt(searchtext + "\uf8ff")
        val firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Items, UsersViewHolder> = object : FirebaseRecyclerAdapter<Items, UsersViewHolder>(Items::class.java,
            R.layout.list_layout,
            UsersViewHolder::class.java,
            firebaseSearchQuery) {
            override fun populateViewHolder(viewHolder: UsersViewHolder, model: Items, position: Int) {
                viewHolder.setDetails(applicationContext, model.itembarcode, model.itemcategory, model.itemname, model.itemexpire)
            }
        }
        mrecyclerview!!.adapter = firebaseRecyclerAdapter
    }

    class UsersViewHolder(var mView: View) : RecyclerView.ViewHolder(mView) {
        fun setDetails(ctx: Context?, itembarcode: String?, itemcategory: String?, itemname: String?, itemprice: String?) {
            val item_barcode = mView.findViewById<View>(R.id.viewitembarcode) as TextView
            val item_name = mView.findViewById<View>(R.id.viewitemname) as TextView
            val item_category = mView.findViewById<View>(R.id.viewitemcategory) as TextView
            val item_expire = mView.findViewById<View>(R.id.viewitemexpire) as TextView
            item_barcode.text = itembarcode
            item_category.text = itemcategory
            item_name.text = itemname
            item_expire.text = itemprice
        }
    }

    companion object {
        var resultsearcheview: EditText? = null
    }
}