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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workdiary.Activity.MainActivity
import com.example.workdiary.Activity.Presenter.MainPresenter
import com.example.workdiary.Adapter.WorkAdapter
import com.example.workdiary.Fragment.Presenter.WorkContract
import com.example.workdiary.Fragment.Presenter.WorkPresenter
import com.example.workdiary.R
import com.example.workdiary.SQLite.DBManager
import kotlinx.android.synthetic.main.dialog_box.view.*
import kotlinx.android.synthetic.main.fragment_work.*
import kotlinx.android.synthetic.main.item_work.*
import java.util.*

/************* WorkFragment.kt *************/
// 요약 : 노동 부분 Fragment, 현재까지 완료되지 않은 노동 내용을 확인할 수 있다.
/******************************************/

class WorkFragment_bkup : Fragment(), WorkContract.View {

    lateinit var presenter: WorkPresenter
    lateinit var workRecyclerView: RecyclerView
    lateinit var workAdapter: WorkAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_work, container, false)
        workRecyclerView = root.findViewById(R.id.rv_work_recyclerView)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewInit()
        presenter = WorkPresenter(context!!, this, workAdapter, workAdapter)
        listenerInit()
        initNoItemTextView()
    }

    private fun initNoItemTextView() {
        // 완료된 일정이 하나도 존재하지 않을경우 [아직 등록된 일정이 없어요] 라는 문구 보여주기
        if(workAdapter.items.size!=0) {
            presenter.isNoItems(false)
        } else {
            presenter.isNoItems(true)
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
                presenter.clickItem(view.findViewById(R.id.ll_itemwork_btnlayout))
            }
        }
        workAdapter.delBtnClickListener = object : WorkAdapter.OnDelBtnClickListener{
            override fun OnDeleteBtnClick(
                holder: WorkAdapter.MyViewHolder,
                view: View,
                position: Int
            ) {
                // item의 삭제버튼 클릭시, DialogView 생성
                presenter.clickDelBtn(context!!, position, holder.date.text.toString(), holder.title.text.toString())
            }
        }
        workAdapter.okBtnClickListener = object : WorkAdapter.OnOKBtnClickListener{
            override fun OnOkBtnClick(holder: WorkAdapter.MyViewHolder, view: View, position: Int) {
                // item의 확인버튼 클릭시, DialogView 생성
                presenter.clickOkBtn(context!!, position)
            }
        }
        workRecyclerView.adapter = workAdapter
    }

    private fun recyclerViewInit() {
        // recyclerView의 내용을 등록된(완료되지 않은) 일정들을 추가하기
        val dbManager = DBManager(context!!)
        val workList = dbManager.getWorkAll()
        workRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        workAdapter = WorkAdapter(workList)
    }

    override fun showLayout(layout:LinearLayout) {
        layout.visibility = View.VISIBLE
    }

    override fun hideLayout(layout:LinearLayout) {
        layout.visibility = View.GONE
    }

    override fun showNoItems() {
        activity!!.findViewById<TextView>(R.id.tv_main_comment).text = "새로운 노동일정을 추가해주세요"
        activity!!.findViewById<TextView>(R.id.tv_main_comment).visibility = View.VISIBLE
    }

    override fun hideNoItems() {
        activity!!.findViewById<TextView>(R.id.tv_main_comment).text = "새로운 노동일정을 추가해주세요"
        activity!!.findViewById<TextView>(R.id.tv_main_comment).visibility = View.INVISIBLE
    }

    override fun crateDialog(context: Context, position:Int, action: String, title: String, contents: String) {
        // 다이얼로그 창 띄우기
        val mDialogView = LayoutInflater.from(this@WorkFragment_bkup.context!!).inflate(R.layout.dialog_box, null)
        val mBuilder = AlertDialog.Builder(this@WorkFragment_bkup.context!!).setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setGravity(Gravity.CENTER)
        mDialogView.tv_dialog_title.text = title
        mDialogView.tv_dialog_context.text = contents
        mDialogView.tv_dialog_ok.text = "예"
        mDialogView.tv_dialog_no.text = "아니오"
        mDialogView.tv_dialog_ok.setOnClickListener {
            // 확인 버튼 누름
            when(action){
                "DEL" -> {
                    // item 삭제하기
                    presenter.deleteWork(context, position)
                }
                "OK" -> {
                    // item 확인 처리하기
                    presenter.checkWork(context, position)
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
                presenter.addWorkResentData(context)
            }
        }
    }
}
