package com.example.workdiary.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.workdiary.data.Work

@Dao
interface WorkDao {
    @Insert
    fun insert(work: Work)

    @Update
    fun update(work: Work)

    @Delete
    fun delete(work: Work)

    @Query("select * from Work where wIsDone=0")
    fun getWorkInfoAll():LiveData<List<Work>>

    @Query("select * from Work where wIsDone=1 order by wDate DESC, wStartTime DESC")
    fun getDiaryInfoAll(): LiveData<List<Work>>

    @Query("select wTitle from Work order by wTitle ASC")
    fun getTitleNames(): List<String>

    @Query("select wSetName from Work where wTitle=:title order by wSetName ASC")
    fun getSetNames(title:String): List<String>

    @Query("select * from Work where wTitle=:title and wSetName=:setName order by wDate DESC, wStartTime DESC")
    fun getWorks(title:String, setName: String): List<Work>

}