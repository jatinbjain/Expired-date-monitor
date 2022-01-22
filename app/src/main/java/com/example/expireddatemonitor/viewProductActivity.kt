package com.example.expireddatemonitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class viewProductActivity : AppCompatActivity() {
    private var firebaseAuth: FirebaseAuth? = null
    var mrecyclerview: RecyclerView? = null
    var mdatabaseReference: DatabaseReference? = null
    private var totalnoofitem: TextView? = null
    private var totalnoofsum: TextView? = null
    private var counttotalnoofitem = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_product)
        firebaseAuth = FirebaseAuth.getInstance()
        val users = firebaseAuth!!.getCurrentUser()
        val finaluser = users!!.email
        val resultemail = finaluser!!.replace(".", "")
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Items")
        mrecyclerview = findViewById(R.id.recyclerViews)
        val manager = LinearLayoutManager(this)
        mrecyclerview?.run {
            setLayoutManager(manager)
            setHasFixedSize(true)
        }
        //mrecyclerview.setLayoutManager(LinearLayoutManager(this))
        mdatabaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    counttotalnoofitem = dataSnapshot.childrenCount.toInt()
                    totalnoofitem?.setText(Integer.toString(counttotalnoofitem))
                } else {
                    totalnoofitem?.setText("0")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        mdatabaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var sum = 0
                for (ds in dataSnapshot.children) {
                    val map = ds.value as Map<String, Any>?
                    val price = map!!["itemprice"]

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
    override fun onStart() {
        super.onStart()
        val firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Items, scanItemsActivity.UsersViewHolder> = object : FirebaseRecyclerAdapter<Items, scanItemsActivity.UsersViewHolder>(Items::class.java,
            R.layout.list_layout,
            scanItemsActivity.UsersViewHolder::class.java,
            mdatabaseReference) {
            override fun populateViewHolder(viewHolder: scanItemsActivity.UsersViewHolder, model: Items, position: Int) {
                viewHolder.setDetails(applicationContext, model.itembarcode, model.itemcategory, model.itemname, model.itemexpire)
            }
        }
        mrecyclerview!!.adapter = firebaseRecyclerAdapter
    }
}