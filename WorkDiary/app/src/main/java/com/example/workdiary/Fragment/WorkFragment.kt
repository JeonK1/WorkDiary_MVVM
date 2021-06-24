package com.example.workdiary.Fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workdiary.Activity.MainActivity
import com.example.workdiary.Activity.Presenter.MainPresenter
import com.example.workdiary.Adapter.WorkAdapter
import com.example.workdiary.Fragment.Presenter.WorkContract
import com.example.workdiary.Fragment.Presenter.WorkPresenter
import com.example.workdiary.Model.WorkInfo
import com.example.workdiary.R
import com.example.workdiary.SQLite.DBManager
import kotlinx.android.synthetic.main.dialog_box.view.*
import kotlinx.android.synthetic.main.fragment_work.*
import kotlinx.android.synthetic.main.item_work.*
import java.util.*
import kotlin.collections.ArrayList

/************* WorkFragment.kt *************/
// 요약 : 노동 부분 Fragment, 현재까지 완료되지 않은 노동 내용을 확인할 수 있다.
/******************************************/

class WorkFragment : Fragment(){
    lateinit var workRecyclerView: RecyclerView
    lateinit var workAdapter: WorkAdapter
    private val model: WorkViewModel by lazy {
        ViewModelProvider(this).get(WorkViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_work, container, false)
        workRecyclerView = root.findViewById(R.id.rv_work_recyclerView)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // recyclerView 초기화
        workRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        
        // model 초기화
        model.getAllWorkInfo().observe(viewLifecycleOwner, Observer<ArrayList<WorkInfo>>{
            workAdapter = WorkAdapter(it) // recyclerView
            initNoItemTextView(it) // 완료된 일정이 존재하지 않는지 체크 때
        })

        // listener 초기화
        listenerInit()
    }

    private fun initNoItemTextView(workList: ArrayList<WorkInfo>) {
        // 완료된 일정이 하나도 존재하지 않을경우 [아직 등록된 일정이 없어요] 라는 문구 보여주기
        if(workList.size!=0) {
            hideNoItems()
        } else {
            showNoItems()
        }
    }

    private fun listenerInit() {
        // 클릭했을 때의 Listener 초기화
        workAdapter.itemClickListener = object : WorkAdapter.OnItemClickListener {
            override fun OnItemClick(
                holder: WorkAdapter.MyViewHolder,
                view: View,
                position: Int
            ) {
                // item 클릭시, 클릭하면 삭제/확인 버튼 show/hide 하기
                val btn_layout = view.findViewById<LinearLayout>(R.id.ll_itemwork_btnlayout)
                if(btn_layout.visibility == View.VISIBLE){
                    btn_layout.visibility = View.GONE
                } else {
                    btn_layout.visibility = View.VISIBLE
                }
            }
        }
        workAdapter.delBtnClickListener = object : WorkAdapter.OnDelBtnClickListener{
            override fun OnDeleteBtnClick(
                holder: WorkAdapter.MyViewHolder,
                view: View,
                position: Int
            ) {
                // item의 삭제버튼 클릭시, DialogView 생성
                crateDialog(
                    context = context!!,
                    position = position,
                    action = "DEL",
                    title = "노동일정 제거",
                    contents = "${holder.date.text}의 ${holder.title.text} 노동 일정을 제거할까요??"
                )
            }
        }
        workAdapter.okBtnClickListener = object : WorkAdapter.OnOKBtnClickListener{
            override fun OnOkBtnClick(holder: WorkAdapter.MyViewHolder, view: View, position: Int) {
                // item의 확인버튼 클릭시, DialogView 생성
                crateDialog(
                    context = context!!,
                    position = position,
                    action = "OK",
                    title = "노동 완료",
                    contents = "노동 기록을 일지로 옮길까요??"
                )
            }
        }
        workRecyclerView.adapter = workAdapter
    }

    private fun showNoItems() {
        activity!!.findViewById<TextView>(R.id.tv_main_comment).text = "새로운 노동일정을 추가해주세요"
        activity!!.findViewById<TextView>(R.id.tv_main_comment).visibility = View.VISIBLE
    }

    private fun hideNoItems() {
        activity!!.findViewById<TextView>(R.id.tv_main_comment).text = "새로운 노동일정을 추가해주세요"
        activity!!.findViewById<TextView>(R.id.tv_main_comment).visibility = View.INVISIBLE
    }

    private fun crateDialog(context: Context, position:Int, action: String, title: String, contents: String) {
        // 다이얼로그 창 띄우기
        val mDialogView = LayoutInflater.from(this@WorkFragment.context!!).inflate(R.layout.dialog_box, null)
        val mBuilder = AlertDialog.Builder(this@WorkFragment.context!!).setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setGravity(Gravity.CENTER)
        mDialogView.tv_dialog_title.text = title
        mDialogView.tv_dialog_context.text = contents
        mDialogView.tv_dialog_ok.text = "예"
        mDialogView.tv_dialog_no.text = "아니오"
        mDialogView.tv_dialog_ok.setOnClickListener {
            // 확인 버튼 누름
            val workInfo =  model.getAllWorkInfo().value!![position]
            when(action){
                "DEL" -> {
                    // item 삭제하기
                    model.delete(workInfo)
                }
                "OK" -> {
                    // item 확인 처리하기
                    model.setIsDone(workInfo)
                }
            }
            mAlertDialog.dismiss()
        }
        mDialogView.tv_dialog_no.setOnClickListener{
            // 취소 버튼 누름
            mAlertDialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // MainActivity에서 onActivityResult의 내용이 넘어왔을 때
        val ADD_WORK_ACTIVITY = (activity as MainActivity).ADD_WORK_ACTIVITY
        if(requestCode == ADD_WORK_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                // AddWorkActivity를 통해 일정이 추가되었음을 확인 >> DB에 가장 최근에 등록된 내용 recyclerView에 추가하기
//                presenter.addWorkResentData(context)
            }
        }
    }
}
