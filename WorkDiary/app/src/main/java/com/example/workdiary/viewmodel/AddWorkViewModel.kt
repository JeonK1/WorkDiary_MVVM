package com.example.workdiary.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workdiary.data.Work
import com.example.workdiary.data.WorkRepository
import java.util.*

class AddWorkViewModel(application: Application): ViewModel() {
    private val repository by lazy {
        WorkRepository(application)
    }

    private var newWork: Work
    private val date = MutableLiveData<String>()
    private val startTime = MutableLiveData<String>()
    private val endTime = MutableLiveData<String>()

    init {
        // date, startTime, endTime 초기값 초기화하기
        // date : 현재 년/월/일
        // startTime : 현재 시:00
        // endTime : 현재 시+1:00
        Calendar.getInstance().apply {
            date.value = "${get(Calendar.YEAR)}/${"%02d".format(get(Calendar.MONTH)+1)}/${"%02d".format(get(Calendar.DAY_OF_MONTH))}"
            startTime.value = "${"%02d".format(get(Calendar.HOUR_OF_DAY))}:${"%02d".format(0)}"
            endTime.value = "${"%02d".format(get(Calendar.HOUR_OF_DAY)+1)}:${"%02d".format(0)}"
        }
        newWork = Work(
            wTitle = "",
            wSetName = "",
            wDate = date.value.toString(),
            wStartTime = startTime.value.toString(),
            wEndTime = endTime.value.toString(),
            wMoney = 0,
            wIsDone = 0
        )
    }

    fun getTitleNames(): List<String> {
        return repository.getTitleNames()
    }

    fun getSetNames(title:String): List<String> {
        return repository.getSetNames(title)
    }

    fun getWorks(title:String, setName:String): List<Work> {
        return repository.getWorks(title, setName)
    }

    fun getNewWork(): Work{
        return newWork
    }

    fun setNewWork(work: Work){
        newWork = work
    }

    fun getDateLiveData(): MutableLiveData<String>{
        return date
    }

    fun getStartTimeLiveData(): MutableLiveData<String>{
        return startTime
    }

    fun getEndTimeLiveData(): MutableLiveData<String>{
        return endTime
    }
}