package com.example.workdiary.Fragment.Presenter

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.example.workdiary.Adapter.Contract.WorkAdapterContract
import com.example.workdiary.Model.WorkInfo
import com.example.workdiary.R
import com.example.workdiary.SQLite.DBManager
import kotlinx.android.synthetic.main.dialog_box.view.*

class WorkPresenter(
    private val context: Context,
    private val view: WorkContract.View,
    private val adapterModel: WorkAdapterContract.Model,
    private val adapterView: WorkAdapterContract.View
) : WorkContract.Presenter {

    val dbManager = DBManager(context)

    override fun clickItem(layout: LinearLayout) {
        if(layout.visibility == View.VISIBLE){
            view.hideLayout(layout)
        } else {
            view.showLayout(layout)
        }
    }

    override fun clickDelBtn(context: Context, position:Int, workDate:String, workTitle:String) {
        view.crateDialog(
            context = context,
            position = position,
            action = "DEL",
            title = "노동일정 제거",
            contents = "${workDate}의 ${workTitle} 노동 일정을 제거할까요??"
        )
    }

    override fun clickOkBtn(context: Context, position:Int) {
        view.crateDialog(
            context = context,
            position = position,
            action = "OK",
            title = "노동 완료",
            contents = "노동 기록을 일지로 옮길까요??"
        )
    }

    override fun deleteWork(context: Context, position: Int) {
        dbManager.deleteWork(adapterModel.getItems(position).wId)
        adapterModel.deleteItems(position)
        adapterView.notifyAdapter()
    }

    override fun checkWork(context: Context, position: Int) {
        dbManager.setWorkCheck(adapterModel.getItems(position).wId)
        adapterModel.deleteItems(position)
        adapterView.notifyAdapter()
    }

    override fun isNoItems(flag: Boolean) {
        if(flag){
            view.showNoItems()
        } else {
            view.hideNoItems()
        }
    }

    override fun addWorkResentData(context: Context?) {
        val workInfo = dbManager.getRecentWork()
        if(workInfo!=null) {
            adapterModel.addItems(workInfo)
        }
    }
}