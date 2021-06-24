package com.example.workdiary.Fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.workdiary.Model.Work
import com.example.workdiary.Model.WorkInfo
import com.example.workdiary.Model.WorkRepository

class WorkViewModel(application: Application) : AndroidViewModel(application) {
    private val repository:WorkRepository by lazy {
        WorkRepository(application)
    }
    private val allWorks: LiveData<ArrayList<WorkInfo>> by lazy {
        loadWorks()
    }

    private fun loadWorks():LiveData<ArrayList<WorkInfo>> {
        return repository.getWorkInfoAll()
    }

    fun insert(workInfo: WorkInfo){
        repository.insert(Work(
            wId = workInfo.wId,
            wTitle = workInfo.workTitle,
            wSetName = workInfo.workSetName,
            wDate = workInfo.workDate,
            wStartTime = workInfo.workStartTime,
            wEndTime = workInfo.workEndTime,
            wMoney = workInfo.workMoney,
            wIsDone = 0
        ))
    }

    fun setIsDone(workInfo: WorkInfo){
        repository.update(Work(
            wId = workInfo.wId,
            wTitle = workInfo.workTitle,
            wSetName = workInfo.workSetName,
            wDate = workInfo.workDate,
            wStartTime = workInfo.workStartTime,
            wEndTime = workInfo.workEndTime,
            wMoney = workInfo.workMoney,
            wIsDone = 1
        ))
    }

    fun delete(workInfo: WorkInfo){
        repository.delete(Work(
            wId = workInfo.wId,
            wTitle = workInfo.workTitle,
            wSetName = workInfo.workSetName,
            wDate = workInfo.workDate,
            wStartTime = workInfo.workStartTime,
            wEndTime = workInfo.workEndTime,
            wMoney = workInfo.workMoney,
            wIsDone = 0
        ))
    }

    fun getAllWorkInfo(): LiveData<ArrayList<WorkInfo>> {
        return allWorks
    }
}