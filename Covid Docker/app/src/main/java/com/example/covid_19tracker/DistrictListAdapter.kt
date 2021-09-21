package com.example.covid_19tracker

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DistrictListAdapter(private val context: Context, private val items: ArrayList<districinfo>) : RecyclerView.Adapter<DistrictListAdapter.ViewHolder>(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var llLayout: LinearLayout = view.findViewById(R.id.districtDetails)
        var tvDistrictName: TextView = view.findViewById(R.id.districtName)
        var tvActive: TextView = view.findViewById(R.id.districtActiveInfo)
        var tvDeceased: TextView = view.findViewById(R.id.districtDeceasedInfo)
        var tvConfirmed: TextView = view.findViewById(R.id.districtConfirmedInfo)
        var tvRecovered: TextView = view.findViewById(R.id.districtRecoveredInfo)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(
            LayoutInflater.from(context).inflate
            (R.layout.district_list,parent,false)
        )
    }
    override fun onBindViewHolder(holder: DistrictListAdapter.ViewHolder, position: Int) {
        val model = items[position]


        val dName: String = model.districtName
        val active: String = model.active
        val deceased: String = model.deceased
        val confirmed: String = model.confirmed
        val recovered: String = model.confirmed

        holder.tvDistrictName.text = dName
        holder.tvActive.text = active
        holder.tvDeceased.text = deceased
        holder.tvConfirmed.text = confirmed
        holder.tvRecovered.text = recovered

        if(position%2==0){
            holder.llLayout.setBackgroundResource(R.drawable.llbackgroundeven)
        }
        else{
            holder.llLayout.setBackgroundResource(R.drawable.llbackground)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}