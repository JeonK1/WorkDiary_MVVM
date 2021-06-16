package com.example.workdiary.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workdiary.Adapter.DiaryAdapter
import com.example.workdiary.Fragment.Presenter.DiaryContract
import com.example.workdiary.Fragment.Presenter.DiaryPresenter
import com.example.workdiary.R
import com.example.workdiary.SQLite.DBManager

/************* DiaryFragment.kt *************/
// 요약 : 일지 부분 Fragment, 현재까지 완료된 일지 내용을 확인할 수 있다.
/******************************************/

class DiaryFragment : Fragment(), DiaryContract.View {

    lateinit var presenter: DiaryPresenter
    lateinit var diaryRecyclerView: RecyclerView
    lateinit var diaryAdapter: DiaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_work, container, false)
        diaryRecyclerView = root.findViewById(R.id.rv_work_recyclerView) // recyclerView 설정하기
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = DiaryPresenter(this)
        recyclerViewInit()
        textViewInit()
    }

    private fun recyclerViewInit() {
        // recyclerView의 내용을 완료된 일정들을 추가하기
        val dbManager = DBManager(context!!)
        val workList = dbManager.getDiaryAll()
        diaryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        diaryAdapter = DiaryAdapter(workList)
        diaryRecyclerView.adapter = diaryAdapter
    }

    private fun textViewInit() {
        // 완료된 일정이 하나도 존재하지 않을경우 [아직 완료된 노동일정이 없어요] 라는 문구 보여주기
        if(diaryAdapter.items.size!=0) {
            presenter.isNoItems(false)
        } else {
            presenter.isNoItems(true)
        }
    }

    override fun showNoItems() {
        // [아직 완료된 노동일정이 없어요] 라는 문구 보여주기 
        activity!!.findViewById<TextView>(R.id.tv_main_comment).text = "아직 완료된 노동일정이 없어요"
        activity!!.findViewById<TextView>(R.id.tv_main_comment).visibility = View.VISIBLE
    }

    override fun hideNoItems() {
        // [아직 완료된 노동일정이 없어요] 라는 문구 숨기기
        activity!!.findViewById<TextView>(R.id.tv_main_comment).text = "아직 완료된 노동일정이 없어요"
        activity!!.findViewById<TextView>(R.id.tv_main_comment).visibility = View.INVISIBLE
    }


}
