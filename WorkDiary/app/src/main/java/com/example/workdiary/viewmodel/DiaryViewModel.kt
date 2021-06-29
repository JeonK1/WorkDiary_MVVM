package com.example.workdiary.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.workdiary.data.Work
import com.example.workdiary.data.WorkRepository

class DiaryViewModel(application: Application): ViewModel() {
    private val repository by lazy {
        WorkRepository(application)
    }

    private val allDiary: LiveData<List<Work>> by lazy {
        repository.getDiaryAll()
    }

    fun getAllDiaryInfo(): LiveData<List<Work>> {
        return allDiary
    }
}