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
}