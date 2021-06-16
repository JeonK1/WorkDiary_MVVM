package com.example.workdiary.Activity.Presenter

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.AutoCompleteTextView

interface AddWorkContract {
    interface View {
        fun setDateTextView(month:Int, day:Int, dayofweek:Int)
        fun setMoney(money:Int)
        fun setStartTime(hour:Int, minute:Int)
        fun setEndTime(hour:Int, minute:Int)
        fun setSetACTV(stringList:ArrayList<String>)
        fun setTitleACTV(stringList:ArrayList<String>)
        fun showTimePicker(listener: TimePickerDialog.OnTimeSetListener, hour:Int, minute: Int)
        fun showDatePicker(listener: DatePickerDialog.OnDateSetListener)
        fun finishView(reusltCode:Int)
        fun updateDate(year: Int, month: Int, day: Int)
    }

    interface Presenter {
        fun titleChanged(curTitle:String)
        fun setChanged(curTitle:String, curSet:String)
        fun setDatePicker(year:Int, monthOfYear:Int, dayOfMonth:Int)
        fun setStartTimePicker(hourOfDay:Int, minute:Int)
        fun setEndTimePicker(hourOfDay:Int, minute:Int)
        fun clickTimePicker(listener: TimePickerDialog.OnTimeSetListener, hour: Int, minute: Int)
        fun clickDatePicker(listener: DatePickerDialog.OnDateSetListener)
        fun clickLowestMoney()
        fun clickBackBtn()
        fun clickSaveBtn(title:String, set:String, year:Int, month:Int, day:Int, startTime:String, endTime:String, money:Int)
    }
}