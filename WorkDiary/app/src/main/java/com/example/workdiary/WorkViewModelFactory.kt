package com.example.workdiary

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class WorkViewModelFactory(application: Application): ViewModelProvider.Factory {
    private val application by lazy {
        application
    }
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WorkViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return WorkViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}