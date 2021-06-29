package com.example.workdiary.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.workdiary.data.Work
import com.example.workdiary.data.WorkRepository

class WorkViewModel(application: Application): ViewModel() {
    private val repository by lazy {
        WorkRepository(application)
    }

    private val allWorks: LiveData<List<Work>> by lazy {
        repository.getAllWorks()
    }

    fun insert(work: Work) {
        repository.insert(work)
    }

    fun update(work: Work) {
        repository.update(work)
    }

    fun setIsDone(work: Work) {
        work.wIsDone = 1
        update(work)
    }

    fun delete(work: Work) {
        repository.delete(work)
    }

    fun getAllWork(): LiveData<List<Work>> {
        return allWorks
    }


}