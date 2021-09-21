package com.example.covid_19tracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class districtList : AppCompatActivity() {
    private var itemAdapter: DistrictListAdapter? = null
    private var districtList: ArrayList<districinfo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_district_list)
//        findViewById<EditText>(R.id.searchStates)!!.text.clear()
//        Log.i("VIJSEARCHSTATE", findViewById<EditText>(R.id.searchStates)!!.text.toString())

//        val distrctList: ArrayList<districinfo> = intent.get
        districtList = intent.getSerializableExtra("districtList") as ArrayList<districinfo>?
//        districtList = ArrayList<districinfo>()
        for(i in 0 until districtList!!.size){
                Log.i("VIJ", districtList!![i].active)
        }
        setUpRecyclerView(districtList!!)
    }
    private fun setUpRecyclerView(districtList: ArrayList<districinfo>) {
        var rvDistrictList = findViewById<RecyclerView>(R.id.rvDistrictWiseInfo)

        rvDistrictList.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        itemAdapter = DistrictListAdapter(this, districtList)
        rvDistrictList.adapter = itemAdapter
    }

}