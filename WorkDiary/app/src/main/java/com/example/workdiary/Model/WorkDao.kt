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

    @Query("select substr(wDate,0,8) from Work where wIsDone=1 group by substr(wDate,0,8)  order by wDate DESC")
    fun getWorkDateAll(): LiveData<ArrayList<String>>

    @Query("select * from Work where substr(wDate,0,8)=:date and wIsDone=1")
    fun getDoneWork(date:String): LiveData<ArrayList<Work>>

    @Query("select * from Work order by wId DESC limit 1")
    fun getRecentWork(): LiveData<Work>

    @Query("delete from Work")
    fun clear()
}