package com.example.workdiary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workdiary.R
import com.example.workdiary.data.Work
import java.text.SimpleDateFormat
import java.util.*

class WorkAdapter(var items:List<Work>):RecyclerView.Adapter<WorkAdapter.MyViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(holder: MyViewHolder, view:View, position: Int)
        fun OnDeleteBtnClick(holder: MyViewHolder, view:View, position: Int)
        fun OnOkBtnClick(holder: MyViewHolder, view:View, position: Int)
    }

    var onItemClickListener: OnItemClickListener? = null

    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tv_itemwork_title)
        var setName: TextView = itemView.findViewById(R.id.tv_itemwork_set)
        var date: TextView = itemView.findViewById(R.id.tv_itemwork_date)
        var dday: TextView = itemView.findViewById(R.id.tv_itemwork_dday)
        var workStartNEnd: TextView = itemView.findViewById(R.id.tv_itemwork_startNend)
        var workTime: TextView = itemView.findViewById(R.id.tv_itemwork_workTime)
        var deleteBtn: TextView = itemView.findViewById(R.id.tv_itemwork_delete)
        var okBtn: TextView = itemView.findViewById(R.id.tv_itemwork_okBtn)

        init {
            itemView.setOnClickListener {
                onItemClickListener?.OnItemClick(this, it, adapterPosition)
            }
            deleteBtn.setOnClickListener {
                onItemClickListener?.OnDeleteBtnClick(this, it, adapterPosition)
            }
            okBtn.setOnClickListener {
                onItemClickListener?.OnOkBtnClick(this, it, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_work, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //dday ?????????
        val sf = SimpleDateFormat("yyyy/MM/dd")
        val workDate = sf.parse(items[position].wDate)
        val today = with(Calendar.getInstance()) {
            sf.parse("${get(Calendar.YEAR)}/${get(Calendar.MONTH)+1}/${get(Calendar.DATE)}")
        }
        val workDday = if(today.time < workDate.time) "D-"+((workDate.time - today.time) / (60*60*24*1000)).toString() else "????????? ???????????????"

        //???????????? ?????????
        val startTimeHour = items[position].wStartTime.split(":")[0].toInt()
        val startTimeMin = items[position].wStartTime.split(":")[1].toInt()
        val startTimeStamp = startTimeHour*60 +startTimeMin
        val endTimeHour = items[position].wEndTime.split(":")[0].toInt()
        val endTimeMin = items[position].wEndTime.split(":")[1].toInt()
        val endTimeStamp = endTimeHour*60 + endTimeMin
        val workTimeHour = (endTimeStamp-startTimeStamp)/60
        val workTimeMin = (endTimeStamp-startTimeStamp)%60

        //xml??? ????????????
        holder.title.text = items[position].wTitle
        holder.setName.text = items[position].wSetName
        holder.date.text = """${items[position].wDate.split("/")[1]}??? ${items[position].wDate.split("/")[2]}???"""
        holder.dday.text = workDday
        holder.workStartNEnd.text = """${"%02d".format(startTimeHour)}:${"%02d".format(startTimeMin)} ~ ${"%02d".format(endTimeHour)}:${"%02d".format(endTimeMin)}"""
        holder.workTime.text = """( ${workTimeHour}h ${"%02d".format(workTimeMin)}m )"""
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setWorks(works:List<Work>) {
        this.items = works
        notifyDataSetChanged()
    }
}