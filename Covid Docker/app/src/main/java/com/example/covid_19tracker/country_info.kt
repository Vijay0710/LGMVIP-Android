package com.example.covid_19tracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text

class country_info : AppCompatActivity() {
    private var mQueue: RequestQueue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_info)
        jsonParse()
        findViewById<Button>(R.id.btnCheckout).setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun jsonParse() {
        val url = "https://corona.lmao.ninja/v2/countries/India?yesterday=true&strict=true&query"
        mQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val jsonObject = JSONObject(response.toString().trim())
                    findViewById<TextView>(R.id.tvActive).text = jsonObject.get("active").toString()
                    findViewById<TextView>(R.id.tvDeceased).text = jsonObject.get("deaths").toString()
                    findViewById<TextView>(R.id.tvRecovered).text = jsonObject.get("recovered").toString()
                    findViewById<TextView>(R.id.tvConfirmed).text = jsonObject.get("cases").toString()
//                    Log.i("CASES",jsonObject.get("cases").toString())
//                    Log.i("DEATHS",jsonObject.get("deaths").toString())
//                    Log.i("RECOVERED",jsonObject.get("recovered").toString())
//                    Log.i("ACTIVE",jsonObject.get("active").toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }
}