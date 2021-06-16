package com.example.workdiary.Adapter.Contract

import com.example.workdiary.Model.WorkInfo

interface WorkForDiaryAdapterContract {
    interface View {
        fun notifyAdapter()
    }

    interface Model {
        fun getItems(position: Int): WorkInfo
    }
}