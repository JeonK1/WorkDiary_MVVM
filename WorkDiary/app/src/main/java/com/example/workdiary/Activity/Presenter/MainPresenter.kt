package com.example.workdiary.Activity.Presenter

import android.content.Intent
import android.widget.TextView
import androidx.fragment.app.Fragment

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {
    override fun clickDiaryBtn(diaryBtn:TextView, workBtn:TextView, fragment:Fragment){
        view.setFragment(fragment)
        view.btnOn(diaryBtn)
        view.btnOff(workBtn)
        view.hideAddWorkBtn()
    }
    override fun clickWorkBtn(diaryBtn:TextView, workBtn:TextView, fragment:Fragment){
        view.setFragment(fragment)
        view.btnOn(workBtn)
        view.btnOff(diaryBtn)
        view.showAddWorkBtn()
    }
    override fun clickAddWorkBtn(){
        view.showAddWorkActivity()
    }

    override fun getRequestCode(requestCode: Int, resultCode: Int, data: Intent?) {
        view.sendActivityResultToFragment(requestCode, resultCode, data)
    }
}