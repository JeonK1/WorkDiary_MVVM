package com.example.workdiary.Adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workdiary.Adapter.Contract.DiaryAdapterContract
import com.example.workdiary.Model.DiaryInfo
import com.example.workdiary.R
import kotlinx.android.synthetic.main.dialog_box.view.*
import java.text.DecimalFormat

class DiaryAdapter(val items:ArrayList<DiaryInfo>): RecyclerView.Adapter<DiaryAdapter.MyViewHolder>(), DiaryAdapterContract.View, DiaryAdapterContract.Model {
    interface  OnLongItemClickListener{
        fun OnLongItemClick(holder: MyViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnLongItemClickListener?=null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tv_itemdiary_title)
        var totalMoney: TextView = itemView.findViewById(R.id.tv_itemdiary_totalMoney)
        var workList: RecyclerView = itemView.findViewById(R.id.rv_itemdiary_workRecyclerView)
        var bottomView: View = itemView.findViewById(R.id.view_itemdiary_bottomLine)
        init {
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_diary, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(items.size-1 == position){
            holder.bottomView.visibility = View.GONE
        }
        holder.title.text = items[position].year.toString() + "년 " + items[position].month + "월"
        holder.workList.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL, false)
        holder.workList.adapter = WorkForDiaryAdapter(items[position].workList)
        //totalMoney 계산하기
        var totalMoney = 0
        for(i in 0 until items[position].workList.size){
            //노동시간 구하기
            val startTimeStamp = items[position].workList[i].workStartTime.split(":")[0].toInt()*60 +
                    items[position].workList[i].workStartTime.split(":")[1].toInt()
            val endTimeStamp = items[position].workList[i].workEndTime.split(":")[0].toInt()*60 +
                    items[position].workList[i].workEndTime.split(":")[1].toInt()
            val workTimeHour = (endTimeStamp-startTimeStamp)/60
            val workTimeMin = if((endTimeStamp-startTimeStamp)%60 >= 30) 0.5 else 0.0

            totalMoney += (items[position].workList[i].workMoney * workTimeHour) +
                    (items[position].workList[i].workMoney * workTimeMin).toInt()
        }
        val decimalFormat = DecimalFormat("###,###.##")
        val totalMoneyStr = decimalFormat.format(totalMoney)
        holder.totalMoney.text = "Total : "+totalMoneyStr+"원"
    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun getItems(position: Int): DiaryInfo {
        return items[position]
    }
}