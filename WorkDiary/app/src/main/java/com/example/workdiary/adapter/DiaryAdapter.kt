package com.example.workdiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workdiary.R
import com.example.workdiary.data.DiaryInfo
import com.example.workdiary.data.Work
import java.text.DecimalFormat

class DiaryAdapter(var items:List<DiaryInfo>): RecyclerView.Adapter<DiaryAdapter.MyViewHolder>() {
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
        holder.workList.adapter = DiaryDetailAdapter(items[position].workList)
        //totalMoney 계산하기
        var totalMoney = 0
        for(i in 0 until items[position].workList.size){
            //노동시간 구하기
            val startTimeStamp = items[position].workList[i].wStartTime.split(":")[0].toInt()*60 +
                    items[position].workList[i].wStartTime.split(":")[1].toInt()
            val endTimeStamp = items[position].workList[i].wEndTime.split(":")[0].toInt()*60 +
                    items[position].workList[i].wEndTime.split(":")[1].toInt()
            val workTimeHour = (endTimeStamp-startTimeStamp)/60
            val workTimeMin = if((endTimeStamp-startTimeStamp)%60 >= 30) 0.5 else 0.0

            totalMoney += (items[position].workList[i].wMoney * workTimeHour) +
                    (items[position].workList[i].wMoney * workTimeMin).toInt()
        }
        val decimalFormat = DecimalFormat("###,###.##")
        val totalMoneyStr = decimalFormat.format(totalMoney)
        holder.totalMoney.text = "Total : "+totalMoneyStr+"원"
    }

    fun setDiaryList(diaryList:List<DiaryInfo>) {
        this.items = diaryList
        notifyDataSetChanged()
    }
}