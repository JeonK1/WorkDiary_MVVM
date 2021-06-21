package com.example.workdiary.Model

import android.app.Application
import android.os.AsyncTask
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
        InsertAsyncTask(workDao).execute(work)
    }

    fun update(work:Work){
        UpdateAsyncTask(workDao).execute(work)
    }

    fun delete(work:Work){
        DeleteAsyncTask(workDao).execute(work)
    }

    fun getWorkAll(): LiveData<ArrayList<Work>> {
        return allWorks
    }

    fun getWork(id:Int): LiveData<Work> {
        // find Work by wId
        return GetWorkAsyncTask(workDao).execute(id).get()
    }

    fun getWork(title: String, setName:String): LiveData<Work> {
        // find Work by title and setName
        return GetWorkAsyncTask(workDao).execute(title, setName).get()
    }

    fun getRecentWork(): LiveData<Work> {
        return GetRecentWorkAsyncTask(workDao).execute().get()
    }

    fun getWorkNameAll(): LiveData<ArrayList<String>> {
        return GetWorkNameAllAsyncTask(workDao).execute().get()
    }

    fun getSetNameAll(title:String): LiveData<ArrayList<String>> {
        return GetSetNameAllAsyncTask(workDao).execute(title).get()
    }



    fun clear(){
        ClearAsyncTask(workDao).execute()
    }

    private inner class InsertAsyncTask(workDao:WorkDao): AsyncTask<Work, Void, Void>() {
        val workDao = workDao

        override fun doInBackground(vararg params: Work): Void? {
            workDao.insert(params[0])
            return null
        }
    }
    private inner class UpdateAsyncTask(workDao:WorkDao): AsyncTask<Work, Void, Void>() {
        val workDao = workDao

        override fun doInBackground(vararg params: Work): Void? {
            workDao.update(params[0])
            return null
        }
    }
    private inner class DeleteAsyncTask(workDao:WorkDao): AsyncTask<Work, Void, Void>() {
        val workDao = workDao

        override fun doInBackground(vararg params: Work): Void? {
            workDao.delete(params[0])
            return null
        }
    }

    private inner class GetWorkAsyncTask(workDao:WorkDao): AsyncTask<Any, Void, LiveData<Work>>() {
        val workDao = workDao
        override fun doInBackground(vararg params: Any): LiveData<Work>? {
            when (params.size) {
                1 -> {
                    // param 개수가 1개 일때는 id 로 검색하기
                    return workDao.getWork(
                        id = params[0] as Int
                    )
                }
                2 -> {
                    // param 개수가 2개 일때는 title 과 setName 으로 검색하기
                    return workDao.getWork(
                        title = params[0] as String,
                        setName = params[1] as String
                    )
                }
                else -> {
                    return null
                }
            }
        }
    }

    private inner class GetRecentWorkAsyncTask(workDao: WorkDao): AsyncTask<Void, Void, LiveData<Work>>(){
        val workDao = workDao
        override fun doInBackground(vararg params: Void): LiveData<Work> {
            return workDao.getRecentWork()
        }
    }

    private inner class GetWorkNameAllAsyncTask(workDao: WorkDao): AsyncTask<Void, Void, LiveData<ArrayList<String>>>() {
        val workDao = workDao
        override fun doInBackground(vararg params: Void): LiveData<ArrayList<String>> {
            return workDao.getWorkNameAll()
        }
    }

    private inner class GetSetNameAllAsyncTask(workDao: WorkDao): AsyncTask<String, Void, LiveData<ArrayList<String>>>() {
        val workDao = workDao
        override fun doInBackground(vararg params: String): LiveData<ArrayList<String>> {
            return workDao.getSetNameAll(params[0])
        }
    }

    private inner class ClearAsyncTask(workDao: WorkDao): AsyncTask<Void, Void, Void>(){
        val workDao = workDao
        override fun doInBackground(vararg params: Void): Void? {
            workDao.clear()
            return null
        }
    }
}