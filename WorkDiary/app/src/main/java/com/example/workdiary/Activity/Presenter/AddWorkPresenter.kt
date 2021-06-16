package com.example.workdiary.Activity.Presenter

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import com.example.workdiary.SQLite.DBManager
import java.util.*
import kotlin.collections.ArrayList

class AddWorkPresenter(private val context:Context, private val view: AddWorkContract.View) : AddWorkContract.Presenter{
    private var dbManager:DBManager = DBManager(context)

    override fun titleChanged(curTitle:String) {
        val setNameList = dbManager.getSetNameAll(curTitle)
        if(setNameList.size>0) {
            // title에 적은게 db에 잇는거면 set쪽에 List 적용하기
            view.setSetACTV(setNameList)
        } else {
            // title에 적은게 db에 없으며 set쪽에 빈List 적용하기
            view.setSetACTV(ArrayList())
        }
    }

    override fun setChanged(curTitle:String, curSet:String) {
        val workInfo = dbManager.getWork(curTitle, curSet)
        if(workInfo!=null) {
            view.setStartTime(
                hour = workInfo?.workStartTime.split(":")[0].toInt(),
                minute = workInfo?.workStartTime.split(":")[1].toInt()
            )
            view.setEndTime(
                hour = workInfo?.workEndTime.split(":")[0].toInt(),
                minute = workInfo?.workEndTime.split(":")[1].toInt()
            )
            view.setMoney(
                money = workInfo.workMoney
            )
        }
    }

    override fun setDatePicker(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        view.setDateTextView(
            month = cal.get(Calendar.MONTH) + 1,
            day = cal.get(Calendar.DAY_OF_MONTH),
            dayofweek = cal.get(Calendar.DAY_OF_WEEK)
        )
    }

    override fun setStartTimePicker(hourOfDay: Int, minute: Int) {
        view.setStartTime(
            hour = hourOfDay,
            minute = minute
        )
        view.setEndTime(
            hour = hourOfDay + 1,
            minute = minute
        )
    }

    override fun setEndTimePicker(hourOfDay: Int, minute: Int) {
        view.setEndTime(
            hour = hourOfDay,
            minute = minute
        )
    }

    override fun clickTimePicker(listener: TimePickerDialog.OnTimeSetListener, hour: Int, minute: Int) {
        view.showTimePicker(
            listener = listener,
            hour = hour,
            minute = minute
        )
    }

    override fun clickDatePicker(listener: DatePickerDialog.OnDateSetListener) {
        view.showDatePicker(
            listener = listener
        )
    }

    override fun clickLowestMoney() {
        view.setMoney(8590)
    }

    override fun clickBackBtn() {
        view.finishView(Activity.RESULT_CANCELED)
    }

    override fun clickSaveBtn(title:String, set:String, year:Int, month:Int, day:Int, startTime:String, endTime:String, money:Int) {
        dbManager.addWork(
            title = title,
            setName = set,
            date = "$year/${"%02d".format(month)}/${"%02d".format(day)}",
            startTime = startTime,
            endTime = endTime,
            money = money
        )
        view.finishView(Activity.RESULT_OK)
    }
}