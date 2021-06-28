package com.example.workdiary.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.workdiary.R
import com.example.workdiary.data.Work
import kotlinx.android.synthetic.main.activity_add_work.*
import java.util.*
import kotlin.collections.ArrayList

class AddWorkActivity : AppCompatActivity() {
    companion object {
        const val LOWEST_MONEY = 8590
        const val ADD_WORK_VALUE = "newWork"
    }
    private val DAY_OF_WEEK: ArrayList<String> = arrayListOf("", "일", "월", "화", "수", "목", "금", "토")
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var startTimeSetListener: TimePickerDialog.OnTimeSetListener
    lateinit var endTimeSetListener: TimePickerDialog.OnTimeSetListener
    var workYear=0 // 현재 선택된 날짜의 년
    var workMonth=0 // 현재 선택된 날짜의 월
    var workDay=0 // 현재 선택된 날짜의 일

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_work)
        val cal = Calendar.getInstance()
        workYear = cal.get(Calendar.YEAR)
        workMonth = cal.get(Calendar.MONTH)+1
        workDay = cal.get(Calendar.DAY_OF_MONTH)

        // 날자 text 적용
        tv_addwork_pickDate.text =
            "${workMonth}월 ${workDay}일 (${DAY_OF_WEEK[cal.get(Calendar.DAY_OF_WEEK)]})"
        tv_addwork_pickStartTime.text =
            "${"%02d".format(cal.get(Calendar.HOUR_OF_DAY))}:${"%02d".format(0)}"
        tv_addwork_pickEndTime.text =
            "${"%02d".format(cal.get(Calendar.HOUR_OF_DAY)+1)}:${"%02d".format(0)}"

        // datePicker, timePicker 초기값
        dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                // 날짜 설정 완료 시
                // Todo : instance 안부르고 하는 방법
                workYear = year
                workMonth = month
                workDay = dayOfMonth

                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                tv_addwork_pickDate.text =
                    "${workMonth}월 ${workDay}일 (${DAY_OF_WEEK[cal.get(Calendar.DAY_OF_WEEK)]})"
            }
        startTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                // 시작시간 설정 완료 시
                tv_addwork_pickStartTime.text =
                    "${"%02d".format(hourOfDay)}:${"%02d".format(minute)}"
            }
        endTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                // 끝시간 설정 완료 시
                tv_addwork_pickEndTime.text =
                    "${"%02d".format(hourOfDay)}:${"%02d".format(minute)}"
            }

        tv_addwork_pickDate.setOnClickListener {
            // 날짜 설정
            DatePickerDialog(this,
                dateSetListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        tv_addwork_pickStartTime.setOnClickListener {
            // 시작시간
            TimePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog,
                startTimeSetListener,
                tv_addwork_pickStartTime.text.toString().split(":")[0].toInt(), // hour
                tv_addwork_pickStartTime.text.toString().split(":")[1].toInt(), // minute
                android.text.format.DateFormat.is24HourFormat(this)
            ).show()
        }

        tv_addwork_pickEndTime.setOnClickListener {
            // 시작시간
            TimePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog,
                endTimeSetListener,
                tv_addwork_pickEndTime.text.toString().split(":")[0].toInt(), // hour
                tv_addwork_pickEndTime.text.toString().split(":")[1].toInt(), // minute
                android.text.format.DateFormat.is24HourFormat(this)
            ).show()
        }

        btn_addwork_inputLowestMoney.setOnClickListener {
            // 최저시급
            et_addwork_money.setText(LOWEST_MONEY.toString())
        }

        tv_addwork_saveBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("newWork", Work(
                    wTitle = act_addwork_title.text.toString(),
                    wSetName = act_addwork_set.text.toString(),
                    wDate = "${"%02d".format(workYear)}/${"%02d".format(workMonth)}/${"%02d".format(workDay)}",
                    wStartTime = tv_addwork_pickStartTime.text.toString(),
                    wEndTime = tv_addwork_pickEndTime.text.toString(),
                    wMoney = et_addwork_money.text.toString().toInt(),
                    wIsDone = 0)
                )
            }
            val intent = Intent().apply {
                putExtras(bundle)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        //Todo : AutoCompleteText 설정 Init 추가
    }
}