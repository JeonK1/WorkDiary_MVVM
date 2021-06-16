package com.example.workdiary.Fragment.Presenter

interface DiaryContract {
    interface View {
        fun showNoItems()
        fun hideNoItems()
    }
    interface Presenter {
        fun isNoItems(flag: Boolean)
    }
}