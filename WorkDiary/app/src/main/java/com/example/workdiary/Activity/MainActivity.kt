package com.example.workdiary.Activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.workdiary.Activity.Presenter.MainContract
import com.example.workdiary.Activity.Presenter.MainPresenter
import com.example.workdiary.Fragment.DiaryFragment
import com.example.workdiary.R
import com.example.workdiary.Fragment.WorkFragment
import kotlinx.android.synthetic.main.activity_main.*

/************* MainActivity.kt *************/
// 요약 : 메인 페이지, DiaryFragment와 WorkFragment를 포함하고 있다.
/******************************************/

class MainActivity : AppCompatActivity(), MainContract.View {

    // by lazy는 var 에서 lateinit을 사용하는 것 처럼 val에서 늦은 초기화를 위해 사용하는 문법이다.
    private val presenter: MainPresenter by lazy {
        MainPresenter(this)
    }

    val ADD_WORK_ACTIVITY = 105 // AddWorkActivity를 실행할 때의 flag 값 
    val diaryFragment = DiaryFragment()
    val workFragment = WorkFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentInit()
        buttonInit()
    }

    private fun fragmentInit() {
        // fragment 초기 화면을 workFragment로 선택
        setFragment(workFragment)
    }

    private fun buttonInit() {
        // button listener 초기화
        val diaryBtn:TextView = tv_main_diaryBtn // '일지' 버튼
        val workBtn:TextView = tv_main_workBtn // '노동' 버튼
        tv_main_diaryBtn.setOnClickListener {
            // '일지' 버튼 클릭
            presenter.clickDiaryBtn(diaryBtn, workBtn, diaryFragment)
        }
        tv_main_workBtn.setOnClickListener {
            // '노동' 버튼 클릭
            presenter.clickWorkBtn(diaryBtn, workBtn, workFragment)
        }
        tv_main_addBtn.setOnClickListener {
            // '일정 추가하기' 버튼 클릭
            presenter.clickAddWorkBtn()
        }
    }

    override fun setFragment(fragment: Fragment){
        // fragment를 다른 fragment로 세팅
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.fl_main_fragment, fragment)
        fragmentTransaction.commit()
    }

    override fun btnOn(textView:TextView){
        // TextView 버튼 ON의 디자인으로 바꿔주기
        textView.setBackgroundResource(R.drawable.top_rounded_rectangle_white)
        textView.setTextColor(resources.getColor(R.color.colorBlack))
    }

    override fun btnOff(textView: TextView){
        // TextView 버튼 OFF의 디자인으로 바꿔주기
        textView.setBackgroundResource(R.drawable.top_rounded_rectangle_black)
        textView.setTextColor(resources.getColor(R.color.colorWhite))
    }

    override fun hideAddWorkBtn(){
        // AddWorkActivity로 이동하는 버튼 안보이게 하기
        tv_main_addBtn.visibility = View.GONE
    }

    override fun showAddWorkBtn(){
        // AddWorkActivity로 이동하는 버튼 보이게 하기
        tv_main_addBtn.visibility = View.VISIBLE
    }

    override fun showAddWorkActivity() {
        // AddWorkActivity 실행
        val intent = Intent(this, AddWorkActivity::class.java)
        startActivityForResult(intent, ADD_WORK_ACTIVITY)
    }

    override fun sendActivityResultToFragment(requestCode: Int, resultCode: Int, data: Intent?) {
        // requestCode fragment로 전달하기
        val fragment = supportFragmentManager.findFragmentById(R.id.fl_main_fragment)
        if(fragment!=null){
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 다른 Activity에서 되돌아왔을 때
        if(requestCode == ADD_WORK_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                // AddWorkActivity에서 넘어온 값들 fragment로 넘겨주기
                presenter.getRequestCode(requestCode, resultCode, data)
            }
        }
    }
}
