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

    @Query("select * from Work")
    fun getAllWork():LiveData<List<Work>> // recyclerView에 넣어놓고 관찰할거라 LiveData임

    @Query("select * from Work where wId=:wId")
    fun getWork(wId: Int): Work
}