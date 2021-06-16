package com.example.workdiary.Fragment.Presenter

class DiaryPresenter(
    private val view: DiaryContract.View
) : DiaryContract.Presenter {
    override fun isNoItems(flag: Boolean) {
        if(flag){
            view.showNoItems()
        } else {
            view.hideNoItems()
        }
    }

}