package com.example.covid_19tracker

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.animation.AnimationUtils
import org.json.JSONObject
import android.os.Bundle
import android.widget.EditText


class StateListAdapter(private val context: Context, private var items: ArrayList<String>, private val responseFinal: JSONObject) :
    RecyclerView.Adapter<StateListAdapter.ViewHolder>(){
    private var districtlist: ArrayList<districinfo>? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var llLayout: LinearLayout = view.findViewById(R.id.llstate)
        var tvState: TextView = view.findViewById(R.id.tvState)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(LayoutInflater.from(context).inflate
            (R.layout.state_list,parent,false)
        )
    }
    override fun onBindViewHolder(holder: StateListAdapter.ViewHolder, position: Int) {
        val model = items[position]
        holder.llLayout.setOnClickListener {
            districtlist = ArrayList<districinfo>()
            val jsonParentObj = responseFinal.getJSONObject(model.toString())
            val district: Any = jsonParentObj["districtData"]
            val jsonObject1 = JSONObject(district.toString().trim())
            val keys1: Iterator<String> = jsonObject1.keys()
            while(keys1.hasNext()){
                val key1 = keys1.next()
                if (jsonObject1.get(key1) is JSONObject) {
                    Log.i("SKHST_7896", key1 + "------>" + jsonObject1.get(key1))
                    val active: Any = (jsonObject1.get(key1) as JSONObject).get("active")
                    val confirmed: Any = (jsonObject1.get(key1) as JSONObject).get("confirmed")
                    val deceased: Any = (jsonObject1.get(key1) as JSONObject).get("deceased")
                    val recovered: Any = (jsonObject1.get(key1) as JSONObject).get("recovered")
                    val districtObj = districinfo(key1,active.toString(),deceased.toString(),confirmed.toString(),recovered.toString())
                    districtlist!!.add(districtObj)
                    Log.i("SKHST_7896", "$key1 ------> $active")
                }
            }
            val intent = Intent(it.context,districtList::class.java)
            intent.putExtra("districtList",districtlist)
            it.context.startActivity(intent)
//            for(i in 0..districtList!!.size-1){
//                Log.i("VIJ", districtList!![i].active)
//            }
        }

        if(position%2==0){
            holder.llLayout.setBackgroundResource(R.drawable.ripple_effect_background_even)
        }
        else{
            holder.llLayout.setBackgroundResource(R.drawable.ripple_effect_background)
        }
        holder.tvState.text = model.toString()



    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun filterlist(filteredList: ArrayList<String>) {
        this.items = filteredList
        notifyDataSetChanged()
    }

}