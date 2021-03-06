package com.example.workdiary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class DiaryViewModelFactory(application: Application): ViewModelProvider.Factory {
    private val application by lazy {
        application
    }
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DiaryViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DiaryViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}