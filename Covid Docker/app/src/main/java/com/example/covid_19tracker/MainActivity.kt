package com.example.covid_19tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.Locale.filter

class MainActivity : AppCompatActivity() {
    private var mQueue: RequestQueue? = null
    private var stateList: ArrayList<String>? = null
    private var itemAdapter: StateListAdapter? = null
    private var responseFinal: JSONObject? = null
    var filteredList: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jsonParse()
        findViewById<EditText>(R.id.searchStates).addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, After: Int) {
            }

            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

        })

    }

    private fun filter(searchState: String) {
        filteredList = ArrayList<String>()
        for(item: String in stateList!!){
            if(item.lowercase().contains(searchState.lowercase())){
                filteredList!!.add(item)
            }
        }

        if(filteredList!=null){
            itemAdapter!!.filterlist(filteredList!!)
        }

    }

    private fun setupRecyclerView() {
        var rvStateList = findViewById<RecyclerView>(R.id.rvStateList)

        rvStateList.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        itemAdapter = StateListAdapter(this, stateList!!,responseFinal!!)
        rvStateList.adapter = itemAdapter
    }

    private fun jsonParse() {
        stateList = ArrayList()
        Log.i("SKHST_4894" , "inside1")
        val url = "https://data.covid19india.org/state_district_wise.json" //  "https://api.myjson.com/bins/kp9wz"
        var data = ""
        mQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.i("SKHST_4894" , "inside2")
                try {
                    Log.i("SKHST_4894" , "inside3")
                    val jsonObject = JSONObject(response.toString().trim())
                    responseFinal = response
                    val keys: Iterator<String> = jsonObject.keys()

                    while (keys.hasNext()) {
                        val key = keys.next()
                        if (jsonObject.get(key) is JSONObject) {
                            // do something with jsonObject here
                                stateList!!.add(key)
                            Log.i("SKHST_7896",key + "------>" + jsonObject.get(key))
//                            val jsonParentObj = response.getJSONObject(key)
//                            val district: Any = jsonParentObj["districtData"]
//                            val jsonObject1 = JSONObject(district.toString().trim())
//                            val keys1: Iterator<String> = jsonObject1.keys()
//                            while(keys1.hasNext()){
//                                val key1 = keys1.next()
//                                if(jsonObject1.get(key1) is JSONObject){
//                                    Log.i("SKHST_7896",key1 + "------>" + jsonObject1.get(key1))
//                                }
//                            }
                        }
                    }
                    setupRecyclerView()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }



}

