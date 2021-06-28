package com.example.workdiary.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workdiary.R
import com.example.workdiary.viewmodel.WorkViewModel
import com.example.workdiary.viewmodel.WorkViewModelFactory
import com.example.workdiary.activity.AddWorkActivity
import com.example.workdiary.activity.MainActivity
import com.example.workdiary.adapter.WorkAdapter
import com.example.workdiary.data.Work

class WorkFragment : Fragment() {
    lateinit var workViewModel: WorkViewModel
    lateinit var workAdapter: WorkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_work, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // recyclerView 초기화
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_work_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        workAdapter = WorkAdapter(listOf()) // empty list adapter
        recyclerView.adapter = workAdapter

        // ViewModel 설정
        workViewModel = ViewModelProvider(this, WorkViewModelFactory(requireActivity().application)).get(
            WorkViewModel::class.java)
        workViewModel.getAllWork().observe(viewLifecycleOwner, Observer { works ->
            workAdapter.setWorks(works)
        })

        listenerInit()
    }

    private fun listenerInit() {
        workAdapter.onItemClickListener = object : WorkAdapter.OnItemClickListener {
            override fun OnItemClick(holder: WorkAdapter.MyViewHolder, view: View, position: Int) {
                // item 클릭 시, 버튼 있는 화면 보여주기/숨기기
                val btnLayout = view.findViewById<LinearLayout>(R.id.ll_itemwork_btnlayout)
                when(btnLayout.visibility){
                    View.VISIBLE -> {
                        btnLayout.visibility = View.GONE
                    }
                    View.GONE -> {
                        btnLayout.visibility = View.VISIBLE
                    }
                }
            }

            override fun OnDeleteBtnClick(holder: WorkAdapter.MyViewHolder, view: View, position: Int
            ) {
                // item 삭제 버튼 클릭 시, item 제거
                with(workViewModel) {
                    var work = getAllWork().value!![position]
                    delete(work)
                }
            }

            override fun OnOkBtnClick(holder: WorkAdapter.MyViewHolder, view: View, position: Int) {
                // item 확인 버튼 클릭 시, item isDone값 1로 만들기
                with(workViewModel) {
                    var work = getAllWork().value!![position]
                    setIsDone(work)
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MainActivity.ADD_WORK_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                // DB에 추가하기
                with(workViewModel){
                    val newWork = data?.getSerializableExtra(AddWorkActivity.ADD_WORK_VALUE) as Work
                    insert(newWork)
                }
            }
        }
    }
}