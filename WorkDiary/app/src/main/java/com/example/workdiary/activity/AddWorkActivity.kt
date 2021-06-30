package com.example.workdiary.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.workdiary.R
import com.example.workdiary.data.Work
import com.example.workdiary.viewmodel.AddWorkViewModel
import com.example.workdiary.viewmodel.AddWorkViewModelFactory
import kotlinx.android.synthetic.main.activity_add_work.*
import java.util.*
import kotlin.collections.ArrayList

class AddWorkActivity : AppCompatActivity() {
    companion object {
        val DAY_OF_WEEK = arrayListOf("", "일", "월", "화", "수", "목", "금", "토")
        const val LOWEST_MONEY = 8590
        const val ADD_WORK_VALUE = "NEW_WORK"
    }

    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var startTimeSetListener: TimePickerDialog.OnTimeSetListener
    lateinit var endTimeSetListener: TimePickerDialog.OnTimeSetListener
    lateinit var addWorkViewModel: AddWorkViewModel
    private val cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_work)

        addWorkViewModel =
            ViewModelProvider(this, AddWorkViewModelFactory(application)).get(
                AddWorkViewModel::class.java
            )

        addWorkViewModel.getDateLiveData().observe(this, Observer { date_str ->
            // wDate update
            val newWork = addWorkViewModel.getNewWork().also {
                it.wDate = date_str
            }
            addWorkViewModel.setNewWork(newWork)

            // textView update
            val year = date_str.split("/")[0].toInt()
            val month = date_str.split("/")[1].toInt()
            val date = date_str.split("/")[2].toInt()
            cal.set(year, month, date)
            tv_addwork_pickDate.text =
                "${"%02d".format(month)}월 ${"%02d".format(date)}일 (${DAY_OF_WEEK[cal.get(Calendar.DAY_OF_WEEK)]})"
        })

        addWorkViewModel.getStartTimeLiveData().observe(this, Observer { time_str ->
            // wStartTime update
            val newWork = addWorkViewModel.getNewWork().also {
                it.wStartTime = time_str
            }
            addWorkViewModel.setNewWork(newWork)

            // textView update
            val time = time_str.split(":")[0].toInt()
            val min = time_str.split(":")[1].toInt()
            tv_addwork_pickStartTime.text = "${"%02d".format(time)}:${"%02d".format(min)}"
        })

        addWorkViewModel.getEndTimeLiveData().observe(this, Observer { time_str ->
            // wEndTime update
            val newWork = addWorkViewModel.getNewWork().also {
                it.wEndTime = time_str
            }
            addWorkViewModel.setNewWork(newWork)

            // textView update
            val time = time_str.split(":")[0].toInt()
            val min = time_str.split(":")[1].toInt()
            tv_addwork_pickEndTime.text = "${"%02d".format(time)}:${"%02d".format(min)}"
        })

        // datePicker, timePicker 초기값
        dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                // 날짜 설정 완료 시
                addWorkViewModel.getDateLiveData().value =
                    "${"%02d".format(year)}/${"%02d".format(month)}/${"%02d".format(dayOfMonth)}"
            }
        startTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                // 시작시간 설정 완료 시
                addWorkViewModel.getStartTimeLiveData().value =
                    "${"%02d".format(hourOfDay)}:${"%02d".format(minute)}"
            }
        endTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                // 끝시간 설정 완료 시
                addWorkViewModel.getEndTimeLiveData().value =
                    "${"%02d".format(hourOfDay)}:${"%02d".format(minute)}"
            }

        act_addwork_title.setAdapter(
            ArrayAdapter(
                applicationContext,
                android.R.layout.simple_dropdown_item_1line,
                addWorkViewModel.getTitleNames()
            )
        )

        act_addwork_title.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                val wTitle = s.toString()
                act_addwork_set.setAdapter(
                    ArrayAdapter(
                        applicationContext,
                        android.R.layout.simple_dropdown_item_1line,
                        addWorkViewModel.getSetNames(wTitle)
                    )
                )
            }
        })

        act_addwork_set.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                val wTitle = act_addwork_title.text.toString()
                val wSetName = s.toString()
                val works = addWorkViewModel.getWorks(wTitle, wSetName)
                if(works.isNotEmpty()){
                    // wStartTime, wEndTime, wMoney setting
                    tv_addwork_pickStartTime.text = works[0].wStartTime
                    tv_addwork_pickEndTime.text = works[0].wEndTime
                    et_addwork_money.setText(works[0].wMoney.toString())
                }
            }
        })

        tv_addwork_pickDate.setOnClickListener {
            // 날짜 설정
            val date_str = addWorkViewModel.getDateLiveData().value!!
            DatePickerDialog(this,
                dateSetListener,
                date_str.split("/")[0].toInt(),
                date_str.split("/")[1].toInt(),
                date_str.split("/")[2].toInt(),
            ).show()
        }

        tv_addwork_pickStartTime.setOnClickListener {
            // 시작시간
            val time_str = addWorkViewModel.getStartTimeLiveData().value!!
            TimePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog,
                startTimeSetListener,
                time_str.split(":")[0].toInt(), // hour
                time_str.split(":")[1].toInt(), // minute
                android.text.format.DateFormat.is24HourFormat(this)
            ).show()
        }

        tv_addwork_pickEndTime.setOnClickListener {
            // 끝시간
            val time_str = addWorkViewModel.getStartTimeLiveData().value!!
            TimePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog,
                endTimeSetListener,
                time_str.split(":")[0].toInt(), // hour
                time_str.split(":")[1].toInt(), // minute
                android.text.format.DateFormat.is24HourFormat(this)
            ).show()
        }

        btn_addwork_inputLowestMoney.setOnClickListener {
            // 최저시급
            et_addwork_money.setText(LOWEST_MONEY.toString())
        }

        tv_addwork_saveBtn.setOnClickListener {
            val newWork = addWorkViewModel.getNewWork().also {
                // wTitle, wSetName, wMoney update
                it.wTitle = act_addwork_title.text.toString()
                it.wSetName = act_addwork_set.text.toString()
                it.wMoney = et_addwork_money.text.toString().toInt()
            }
            val bundle = Bundle().apply {
                putSerializable(ADD_WORK_VALUE, newWork)
            }
            val intent = Intent().apply {
                putExtras(bundle)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        ib_addwork_backBtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

}