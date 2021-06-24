package com.example.workdiary.Activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.example.workdiary.Activity.Presenter.AddWorkContract
import com.example.workdiary.Activity.Presenter.AddWorkPresenter
import com.example.workdiary.R
import com.example.workdiary.SQLite.DBManager
import kotlinx.android.synthetic.main.activity_add_work.*
import java.util.*
import kotlin.collections.ArrayList

/************* AddWorkActivity.kt *************/
// 요약 : 일정 추가 페이지, 저장하기/뒤로가기를 하면 MainActivity로 돌아온다.
/******************************************/

class AddWorkActivity : AppCompatActivity(), AddWorkContract.View {

    private val presenter: AddWorkPresenter by lazy {
        AddWorkPresenter(applicationContext, this)
    }

    private val DAY_OF_WEEK: ArrayList<String> = arrayListOf("", "일", "월", "화", "수", "목", "금", "토")
    lateinit var dateSetListener:DatePickerDialog.OnDateSetListener
    lateinit var startTimeSetListener:TimePickerDialog.OnTimeSetListener
    lateinit var endTimeSetListener:TimePickerDialog.OnTimeSetListener
    var workYear="" // 현재 선택된 날짜의 년
    var workMonth="" // 현재 선택된 날짜의 월
    var workDay="" // 현재 선택된 날짜의 일

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_work)
        calendarInit()
        listenerInit()
        initAutoCompleteText()
        pickerInit()
        buttonInit()
    }

    private fun calendarInit() {
        // 일정 추가하기 화면 처음 들어갔을때의 초기값을 현재 시간을 참고함
        val cal = Calendar.getInstance()
        
        // workYear, workMonth, workDay를 현재 날짜로 초기화
        updateDate(
            year = cal.get(Calendar.YEAR),
            month = cal.get(Calendar.MONTH)+1,
            day = cal.get(Calendar.DAY_OF_MONTH)
        )

        // [03월 11일(목)] 이런식으로 쓰여있는 날짜버튼 내용 현재 날짜로 초기화
        setDateTextView(
            month = cal.get(Calendar.MONTH)+1,
            day = cal.get(Calendar.DAY_OF_MONTH),
            dayofweek = cal.get(Calendar.DAY_OF_WEEK)
        )

        // 현재시간이 HH시 MM분일 때, 설정 초기 시간을 HH시 00분으로 설정
        if(cal.get(Calendar.AM_PM)==1){
            // PM일때
            setStartTime(
                hour = cal.get(Calendar.HOUR)+12,
                minute = 0
            )
            setEndTime(
                hour = cal.get(Calendar.HOUR)+12+1,
                minute = 0
            )
        } else {
            // AM일때
            setStartTime(
                hour = cal.get(Calendar.HOUR),
                minute = 0
            )
            setEndTime(
                hour = cal.get(Calendar.HOUR)+1,
                minute = 0
            )
        }
    }

    private fun listenerInit() {
        // 제목 / 세트 내용이 변경되었을 때의 Listener 초기화
        act_addwork_title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 제목이 입력되었을때, 세트입력 autoCompleteListener에 List 추가
                presenter.titleChanged(
                    curTitle =  act_addwork_title.text.toString()
                )
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        act_addwork_set.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                // 세트가 입력되었을때, 현재 제목/세트 에 맞는 중복된 일정이 존재할 경우 해당 내용으로 시작시간, 끝시간, 시급을 맞춰옴
                presenter.setChanged(
                    curTitle = act_addwork_title.text.toString(),
                    curSet = act_addwork_set.text.toString()
                )
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
    }

    private fun initAutoCompleteText() {
        // 현재 DB에 있는 일정들의 제목을 autoCompleteText에 등록함
        val dbManager = DBManager(applicationContext)
        val workNameList = dbManager.getWorkNameAll()
        setTitleACTV(workNameList)
    }

    private fun pickerInit() {
        // DatePicker와 TimePicker(시작시간, 끝시간)의 초기값을 현재 시각을 바탕으로 초기화함
        dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                presenter.setDatePicker(
                    year = year,
                    monthOfYear = monthOfYear,
                    dayOfMonth = dayOfMonth
                )
            }
        startTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                presenter.setStartTimePicker(
                    hourOfDay = hourOfDay,
                    minute = minute
                )
            }
        endTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                presenter.setEndTimePicker(
                    hourOfDay = hourOfDay,
                    minute = minute
                )
            }
    }

    private fun buttonInit() {
        // 버튼이 클릭 되엇을 때의 Listener 초기화
        ll_addwork_pickStartTime.setOnClickListener {
            // 시작시간 설정 버튼 클릭
            presenter.clickTimePicker(
                listener = startTimeSetListener,
                hour = tv_addwork_startTime.text.toString().split(":")[0].toInt(),
                minute = tv_addwork_startTime.text.toString().split(":")[1].toInt()
            )
        }
        ll_addwork_pickEndTime.setOnClickListener {
            // 끝시간 설정 버튼 클릭
            presenter.clickTimePicker(
                listener = endTimeSetListener,
                hour = tv_addwork_startTime.text.toString().split(":")[0].toInt(),
                minute = tv_addwork_startTime.text.toString().split(":")[1].toInt()
            )
        }
        ll_addwork_pickDate.setOnClickListener {
            // 날짜 설정 버튼 클릭
            presenter.clickDatePicker(
                listener = dateSetListener
            )
        }
        btn_addwork_inputLowestMoney.setOnClickListener {
            // 최저시급 설정 버튼 클릭
            presenter.clickLowestMoney()
        }
        tv_addwork_saveBtn.setOnClickListener {
            // 저장하기 버튼 클릭
            presenter.clickSaveBtn(
                title = act_addwork_title.text.toString(),
                set = act_addwork_set.text.toString(),
                year = workYear.toInt(),
                month = workMonth.toInt(),
                day = workDay.toInt(),
                startTime = tv_addwork_startTime.text.toString(),
                endTime = tv_addwork_endTime.text.toString(),
                money = et_addwork_money.text.toString().toInt()
            )
        }
        ib_addwork_backBtn.setOnClickListener {
            // 뒤로가기 버튼 클릭
            presenter.clickBackBtn()
        }
    }

    override fun setDateTextView(month: Int, day: Int, dayofweek: Int) {
        // 날짜설정 버튼의 텍스트 수정 함수
        tv_addwork_mon.text = "${"%02d".format(month)}월"
        tv_addwork_day.text = "${"%02d".format(day)}일"
        tv_addwork_dayofweek.text = "(${DAY_OF_WEEK[dayofweek]})"
    }

    override fun setMoney(money: Int) {
        // 시급 텍스트 수정 함수
        et_addwork_money.setText(money.toString())
    }

    override fun setStartTime(hour: Int, minute: Int) {
        // 시작시간 텍스트 수정 함수
        tv_addwork_startTime.text = "${"%02d".format(hour)}:${"%02d".format(minute)}"
    }

    override fun setEndTime(hour: Int, minute: Int) {
        // 끝시간 텍스트 수정 함수
        tv_addwork_endTime.text = "${"%02d".format(hour)}:${"%02d".format(minute)}"
    }

    override fun setSetACTV(stringList: ArrayList<String>) {
        // 세트 AutoCompleteTextView 에 arrayList 설정
        act_addwork_set.setAdapter(
            ArrayAdapter(
                applicationContext,
                android.R.layout.simple_dropdown_item_1line,
                stringList
            )
        )
    }

    override fun setTitleACTV(stringList: ArrayList<String>) {
        // 제목 AutoCompleteTextView 에 arrayList 설정
        act_addwork_title.setAdapter(
            ArrayAdapter(
                applicationContext,
                android.R.layout.simple_dropdown_item_1line,
                stringList
            )
        )
    }

    override fun showTimePicker(listener: TimePickerDialog.OnTimeSetListener, hour: Int, minute: Int) {
        // TimePicker 팝업
        TimePickerDialog(this,
            android.R.style.Theme_Holo_Light_Dialog,
            listener,
            hour,
            minute,
            android.text.format.DateFormat.is24HourFormat(this)
        ).show()
    }

    override fun showDatePicker(listener: DatePickerDialog.OnDateSetListener) {
        // DatePicker 팝업
        DatePickerDialog(this,
            listener,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun finishView(resultCode:Int) {
        // resultCode를 세팅 후 현 Activity Finish 하기
        setResult(resultCode)
        finish()
    }

    override fun updateDate(year: Int, month: Int, day: Int) {
        // 현재 설정중인 날짜(년, 월, 일) 업데이트하기
        workYear = year.toString()
        workMonth = month.toString()
        workDay = day.toString()
    }
}