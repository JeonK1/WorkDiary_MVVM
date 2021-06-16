package com.example.workdiary.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.workdiary.Model.DiaryInfo
import com.example.workdiary.Model.WorkInfo
import java.util.ArrayList

/**
 * *** TableName : worktable ***
 * wId integer (primary key, autoincrement)
 * wTitle text
 * wSetName text
 * wDate text
 * wStartTime text
 * wEndTime text
 * wMoney integer
 * wIsDone integer
 */

class DBManager {

    val tableName = "worktable"
    var dbHelper : DBHelper
    var myDatabase : SQLiteDatabase

    constructor(context: Context){
        // 생성자
        dbHelper = DBHelper(context, "db_workdiary.db", null, 1)
        myDatabase = dbHelper.writableDatabase
    }

    fun addWork(title:String, setName:String, date:String, startTime:String, endTime:String, money:Int){
        val contentValues = ContentValues()
        contentValues.put("wTitle", title)
        contentValues.put("wSetName", setName)
        contentValues.put("wDate", date)
        contentValues.put("wStartTime", startTime)
        contentValues.put("wEndTime", endTime)
        contentValues.put("wMoney", money)
        contentValues.put("wIsDone", 0)
        myDatabase.insert("worktable", null, contentValues)
    }

    fun getWorkAll(): ArrayList<WorkInfo> {
        val query = "select * From $tableName where wIsDone=0 order by wDate, wStartTime asc"
        val cursor = myDatabase.rawQuery(query, null)
        cursor.moveToFirst()
        val workList = ArrayList<WorkInfo>()
        while(!cursor.isAfterLast){
            workList.add(
                WorkInfo(
                    cursor.getInt(cursor.getColumnIndex("wId")),
                    cursor.getString(cursor.getColumnIndex("wTitle")),
                    cursor.getString(cursor.getColumnIndex("wSetName")),
                    cursor.getString(cursor.getColumnIndex("wDate")),
                    cursor.getString(cursor.getColumnIndex("wStartTime")),
                    cursor.getString(cursor.getColumnIndex("wEndTime")),
                    cursor.getInt(cursor.getColumnIndex("wMoney"))
                )
            )
            cursor.moveToNext()
        }
        return workList
    }

    fun getWorkNameAll(): ArrayList<String> {
        val query = "select wTitle From $tableName group by wTitle order by wTitle asc"
        val cursor = myDatabase.rawQuery(query, null)
        cursor.moveToFirst()
        val workNameList = ArrayList<String>()
        while (!cursor.isAfterLast) {
            workNameList.add(cursor.getString(cursor.getColumnIndex("wTitle")))
            cursor.moveToNext()
        }
        return workNameList
    }

    fun getSetNameAll(title: String): ArrayList<String>{
        val query = "select wSetName From $tableName where wTitle=\"$title\" group by wSetName order by wSetName asc"
        val cursor = myDatabase.rawQuery(query, null)
        cursor.moveToFirst()
        val setNameList = ArrayList<String>()
        while (!cursor.isAfterLast) {
            setNameList.add(cursor.getString(cursor.getColumnIndex("wSetName")))
            cursor.moveToNext()
        }
        return setNameList
    }

    fun deleteWork(wId: Int){
        val query = "delete from $tableName where wId=${wId}"
        myDatabase.execSQL(query)
    }

    fun setWorkCheck(wId: Int){
        val query = "update $tableName set wIsDone=1 where wId=${wId}"
        myDatabase.execSQL(query)
    }

    fun getDiaryAll(): ArrayList<DiaryInfo>{
        // 연도/달 단위가 뭐가 있는지 다 불러오기
        val query = "select substr(wDate,0,8) from $tableName where wIsDone=1 group by substr(wDate,0,8)  order by wDate DESC"
        val cursor = myDatabase.rawQuery(query, null)
        cursor.moveToFirst()
        val diaryMonthList = ArrayList<String>()
        while(!cursor.isAfterLast){
            diaryMonthList.add(cursor.getString(0))
            cursor.moveToNext()
        }

        val diaryList = ArrayList<DiaryInfo>()
        for(i in 0 until diaryMonthList.size){
            // 연도/달 에 따른 모든 알바내용 가져와서 workInfoList로 만들어주기
            val workInfoList = ArrayList<WorkInfo>()
            val query2 = "select * from $tableName where wDate like '${diaryMonthList[i]}%' and wIsDone=1"
            val cursor2 = myDatabase.rawQuery(query2, null)
            cursor2.moveToFirst()
            while(!cursor2.isAfterLast){
                workInfoList.add(WorkInfo(
                    cursor2.getInt(cursor2.getColumnIndex("wId")),
                    cursor2.getString(cursor2.getColumnIndex("wTitle")),
                    cursor2.getString(cursor2.getColumnIndex("wSetName")),
                    cursor2.getString(cursor2.getColumnIndex("wDate")),
                    cursor2.getString(cursor2.getColumnIndex("wStartTime")),
                    cursor2.getString(cursor2.getColumnIndex("wEndTime")),
                    cursor2.getInt(cursor2.getColumnIndex("wMoney"))
                ))
                cursor2.moveToNext()
            }
            // workInfoList 바탕으로 diaryList 제작
            diaryList.add(DiaryInfo(
                diaryMonthList[i].split("/")[0].toInt(),
                diaryMonthList[i].split("/")[1].toInt(),
                workInfoList
            ))
        }
        return diaryList
    }

    fun getWork(title: String, setName:String):WorkInfo?{
        val query = "select * from $tableName where wTitle=\"${title}\" and wSetName=\"${setName}\""
        val cursor = myDatabase.rawQuery(query, null)
        if(cursor.count!=0) {
            cursor.moveToFirst()
            return WorkInfo(
                cursor.getInt(cursor.getColumnIndex("wId")),
                cursor.getString(cursor.getColumnIndex("wTitle")),
                cursor.getString(cursor.getColumnIndex("wSetName")),
                cursor.getString(cursor.getColumnIndex("wDate")),
                cursor.getString(cursor.getColumnIndex("wStartTime")),
                cursor.getString(cursor.getColumnIndex("wEndTime")),
                cursor.getInt(cursor.getColumnIndex("wMoney"))
            );
        }
        return null
    }

    fun getRecentWork(): WorkInfo? {
        val query = "select * from $tableName order by wId DESC limit 1"
        val cursor = myDatabase.rawQuery(query, null)
        if(cursor.count!=0) {
            cursor.moveToFirst()
            return WorkInfo(
                cursor.getInt(cursor.getColumnIndex("wId")),
                cursor.getString(cursor.getColumnIndex("wTitle")),
                cursor.getString(cursor.getColumnIndex("wSetName")),
                cursor.getString(cursor.getColumnIndex("wDate")),
                cursor.getString(cursor.getColumnIndex("wStartTime")),
                cursor.getString(cursor.getColumnIndex("wEndTime")),
                cursor.getInt(cursor.getColumnIndex("wMoney"))
            );
        }
        return null
    }

    fun clear(){
        val query = "delete from $tableName"
        myDatabase.execSQL(query)
    }
}