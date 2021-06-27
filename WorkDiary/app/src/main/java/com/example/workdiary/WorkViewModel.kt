package com.example.workdiary

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class WorkViewModel(application: Application): ViewModel() {
    private val repository by lazy {
        WorkRepository(application)
    }

    private val allWorks: LiveData<List<Work>> by lazy {
        loadWorks()
    }

    private fun loadWorks():LiveData<List<Work>> {
        return repository.getAllWorks()
    }

    fun insert(work: Work) {
        repository.insert(work)
    }

    fun update(work: Work) {
        repository.update(work)
    }

    fun delete(work: Work) {
        repository.delete(work)
    }

    fun getAllWork(): LiveData<List<Work>> {
        return allWorks
    }
}