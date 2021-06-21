package com.example.workdiary.Model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WorkDao {
    @Insert
    fun insert(work: Work)

    @Update
    fun update(work: Work)

    @Delete
    fun delete(work: Work)

    @Query("select * from Work where wId=:id")
    fun getWork(id:Int): LiveData<Work>

    @Query("select * from Work where wTitle=:title and wSetName=:setName")
    fun getWork(title: String, setName:String): LiveData<Work>

    @Query("select * from Work")
    fun getWorkAll():LiveData<ArrayList<Work>>

    @Query("select wTitle from Work group by wTitle order by wTitle asc")
    fun getWorkNameAll():LiveData<ArrayList<String>>

    @Query("select wSetName from work where wTitle=:title group by wSetName order by wSetName asc")
    fun getSetNameAll(title: String):LiveData<ArrayList<String>>

    @Query("select * from Work where wIsDone=0 order by wDate, wStartTime asc")
    fun getWorkInfoAll():LiveData<ArrayList<Work>>  // todo : Work to WorkInfo

    @Query("select substr(wDate,0,8) from Work where wIsDone=1 group by substr(wDate,0,8)  order by wDate DESC")
    fun getDiaryInfoAll(): LiveData<ArrayList<Work>>  // todo : Work to DiaryInfo

    @Query("select * from Work order by wId DESC limit 1")
    fun getRecentWork(): LiveData<Work>

    /*
    // todo : setWorkCheck 는 repository에서 작업해주자. getWork로 wId에 해당하는거 받아서 wIsDone 작업 후 update로 넘겨주면 될듯
    fun setWorkCheck(wId: Int){
        val query = "update $tableName set wIsDone=1 where wId=${wId}"
        myDatabase.execSQL(query)
    }
     */

    @Query("delete from Work")
    fun clear()
}