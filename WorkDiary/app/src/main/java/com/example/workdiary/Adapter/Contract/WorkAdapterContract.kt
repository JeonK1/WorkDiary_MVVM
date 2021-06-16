package com.example.workdiary.Adapter.Contract

import com.example.workdiary.Model.WorkInfo

interface WorkAdapterContract {
    interface View {
        fun notifyAdapter()
    }

    interface Model {
        fun getItems(position: Int): WorkInfo
        fun deleteItems(position: Int)
        fun addItems(workInfo: WorkInfo)
    }
}