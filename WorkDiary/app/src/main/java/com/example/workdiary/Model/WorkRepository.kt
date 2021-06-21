package com.example.workdiary.Model

import android.app.Application
import android.os.AsyncTask
import android.text.PrecomputedText
import androidx.lifecycle.LiveData

class WorkRepository(application: Application) {
    companion object{
        lateinit var workDao:WorkDao
        lateinit var allWorks:LiveData<ArrayList<Work>>
    }
    init {
        val database = WorkDatabase.getInstance(application)
        workDao = database.workDao()
        allWorks = workDao.getWorkAll()
    }

    fun insert(work:Work){
        InsertNoteAsyncTask(workDao).execute(work)
    }

    fun update(work:Work){
        UpdateNoteAsyncTask(workDao).execute(work)
    }

    fun delete(work:Work){
        DeleteNoteAsyncTask(workDao).execute(work)
    }

    fun getAllWorks(): LiveData<ArrayList<Work>> {
        return allWorks
    }

    fun getWork(id:Int): LiveData<Work> {
        val work = GetWorkNoteAsyncTask(workDao).execute(id)
        return work.get()
    }

    //Todo : 여기 오버라이딩 하는 부분은 어떻게 처리해줘야할까
//    fun getWork(title: String, setName:String): LiveData<Work> {
//        val work = GetWorkNoteAsyncTask(workDao).execute(title, setName)
//        return work.get()
//    }

    private inner class InsertNoteAsyncTask(workDao:WorkDao): AsyncTask<Work, Void, Void>() {
        var workDao:WorkDao = workDao

        override fun doInBackground(vararg params: Work): Void? {
            workDao.insert(params[0])
            return null
        }
    }
    private inner class UpdateNoteAsyncTask(workDao:WorkDao): AsyncTask<Work, Void, Void>() {
        var workDao:WorkDao = workDao

        override fun doInBackground(vararg params: Work): Void? {
            workDao.update(params[0])
            return null
        }
    }
    private inner class DeleteNoteAsyncTask(workDao:WorkDao): AsyncTask<Work, Void, Void>() {
        var workDao:WorkDao = workDao

        override fun doInBackground(vararg params: Work): Void? {
            workDao.delete(params[0])
            return null
        }
    }

    private inner class GetWorkNoteAsyncTask(workDao:WorkDao): AsyncTask<Int, Void, LiveData<Work>>() {
        var workDao:WorkDao = workDao

        override fun doInBackground(vararg params: Int?): LiveData<Work> {
            val work = workDao.getWork(params[0]!!)
            return work
        }
    }
}