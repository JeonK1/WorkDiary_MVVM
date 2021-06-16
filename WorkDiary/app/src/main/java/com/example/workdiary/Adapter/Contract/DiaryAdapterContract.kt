package com.example.workdiary.Adapter.Contract

import com.example.workdiary.Model.DiaryInfo
import com.example.workdiary.Model.WorkInfo

interface DiaryAdapterContract {
    interface View {
        fun notifyAdapter()
    }

    interface Model {
        fun getItems(position: Int): DiaryInfo
    }
}