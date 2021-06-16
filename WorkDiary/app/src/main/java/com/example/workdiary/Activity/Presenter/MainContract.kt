package com.example.workdiary.Activity.Presenter

import android.content.Intent
import android.widget.TextView
import androidx.fragment.app.Fragment

interface MainContract {
    interface View {
        fun setFragment(fragment: Fragment)
        fun btnOn(textView: TextView)
        fun btnOff(textView: TextView)
        fun hideAddWorkBtn()
        fun showAddWorkBtn()
        fun showAddWorkActivity()
        fun sendActivityResultToFragment(requestCode: Int, resultCode: Int, data: Intent?)
    }

    interface Presenter {
        fun clickDiaryBtn(diaryBtn:TextView, workBtn:TextView, fragment:Fragment)
        fun clickWorkBtn(diaryBtn:TextView, workBtn:TextView, fragment:Fragment)
        fun clickAddWorkBtn()
        fun getRequestCode(requestCode: Int, resultCode: Int, data: Intent?)
    }
}