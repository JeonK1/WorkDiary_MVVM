package com.example.workdiary.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workdiary.Adapter.Contract.WorkForDiaryAdapterContract
import com.example.workdiary.R
import com.example.workdiary.Model.WorkInfo
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WorkForDiaryAdapter(val items:ArrayList<WorkInfo>): RecyclerView.Adapter<WorkForDiaryAdapter.MyViewHolder>(), WorkForDiaryAdapterContract.Model, WorkForDiaryAdapterContract.View {
    interface  OnItemClickListener{
        fun OnItemClick(holder: MyViewHolder, view: View, position: Int): DiaryAdapter.OnLongItemClickListener?
    }

    var itemClickListener: OnItemClickListener?=null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tv_itemworkfordiary_title)
        var workDay: TextView = itemView.findViewById(R.id.tv_itemworkfordiary_day)
        var workTime: TextView = itemView.findViewById(R.id.tv_itemworkfordiary_workTime)
        var money: TextView = itemView.findViewById(R.id.tv_itemworkfordiary_money)

        init {
            itemView.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, adapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_work_for_diary, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //dday 구하기
        val sf = SimpleDateFormat("yyyy/MM/dd")
        val today = Calendar.getInstance()
        val workDate = sf.parse(items[position].workDate)
        val workDday = (today.time.time - workDate.time) / (60*60*24*1000)

        //노동시간 구하기
        val startTimeStamp = items[position].workStartTime.split(":")[0].toInt()*60 +
                items[position].workStartTime.split(":")[1].toInt()
        val endTimeStamp = items[position].workEndTime.split(":")[0].toInt()*60 +
                items[position].workEndTime.split(":")[1].toInt()
        val workTimeHour = (endTimeStamp-startTimeStamp)/60
        val workTimeMin = if((endTimeStamp-startTimeStamp)%60 >= 30) 30 else 0

        //xml에 적용하기
        holder.title.text = items[position].workTitle
        holder.workDay.text = items[position].workDate.split("/")[2].toInt().toString()+"일"
        holder.workTime.text = "${workTimeHour}시간 "+"%02d".format(workTimeMin)+"분"
        holder.money.text = "${items[position].workMoney}원"
    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun getItems(position: Int): WorkInfo {
        return items[position]
    }
}