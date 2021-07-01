package com.example.workdiary.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.workdiary.R
import com.example.workdiary.fragment.WorkFragment
import com.example.workdiary.fragment.DiaryFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object{
        const val ADD_WORK_ACTIVITY = 105
    }

    private val diaryFragment = DiaryFragment()
    private val workFragment = WorkFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentInit()
        buttonInit()
    }

    private fun fragmentInit() {
        // 첫 fragment는 workFragment로 setting
        setFragment(workFragment)
    }

    private fun buttonInit() {
        tv_main_diaryBtn.setOnClickListener {
            // 일지 버튼 클릭 시
            setFragment(diaryFragment)
            hideAddWorkBtn()
            tv_main_diaryBtn.setBackgroundResource(R.drawable.top_rounded_rectangle_white)
            tv_main_diaryBtn.setTextColor(resources.getColor(R.color.colorBlack))
            tv_main_workBtn.setBackgroundResource(R.drawable.top_rounded_rectangle_black)
            tv_main_workBtn.setTextColor(resources.getColor(R.color.colorWhite))
        }
        tv_main_workBtn.setOnClickListener {
            // 노동 버튼 클릭 시
            setFragment(workFragment)
            showAddWorkBtn()
            tv_main_workBtn.setBackgroundResource(R.drawable.top_rounded_rectangle_white)
            tv_main_workBtn.setTextColor(resources.getColor(R.color.colorBlack))
            tv_main_diaryBtn.setBackgroundResource(R.drawable.top_rounded_rectangle_black)
            tv_main_diaryBtn.setTextColor(resources.getColor(R.color.colorWhite))
        }
        tv_main_addBtn.setOnClickListener {
            // 노동 추가 버튼 클릭 시
            val intent = Intent(this, AddWorkActivity::class.java)
            startActivityForResult(intent, ADD_WORK_ACTIVITY)
        }
    }

    private fun showAddWorkBtn() {
        // AddWorkActivity로 이동하는 버튼 보이게 하기
        tv_main_addBtn.visibility = View.VISIBLE
    }

    private fun hideAddWorkBtn() {
        // AddWorkActivity로 이동하는 버튼 안보이게 하기
        tv_main_addBtn.visibility = View.GONE
    }

    private fun setFragment(fragment: Fragment) {
        // fragment setting
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.fl_main_fragment, fragment)
        fragmentTransaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_WORK_ACTIVITY){
            if(resultCode == Activity.RESULT_OK) {
                // AddWorkActivity 에서 온 값 fragment로 전송
                val fragment = supportFragmentManager.findFragmentById(R.id.fl_main_fragment)
                fragment?.onActivityResult(requestCode, resultCode, data) // fragment null 아니면 넘겨주기
            }
        }
    }
}