package com.example.workdiary.data

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData

class WorkRepository(application: Application) {
    companion object {
        lateinit var workDao: WorkDao
        lateinit var allWorks: LiveData<List<Work>>
        lateinit var allDiary: LiveData<List<Work>>
    }

    init {
        val database = WorkDatabase.getInstance(application)
        workDao = database.workDao()
        allWorks = workDao.getWorkInfoAll()
        allDiary = workDao.getDiaryInfoAll()
    }

    fun insert(work: Work) {
        InsertAsyncTask(workDao).execute(work)
    }

    fun update(work: Work) {
        UpdateAsyncTask(workDao).execute(work)
    }

    fun delete(work: Work) {
        DeleteAsyncTask(workDao).execute(work)
    }

    fun getDiaryAll(): LiveData<List<Work>> {
        return allDiary
    }

    fun getAllWorks(): LiveData<List<Work>> {
        return allWorks
    }

    fun getTitleNames(): List<String> {
        return GetTitleNames(workDao).execute().get()
    }

    fun getSetNames(title:String): List<String> {
        return GetSetNames(workDao).execute(title).get()
    }

    fun getWorks(title:String, setName:String): List<Work> {
        return GetWorks(workDao).execute(title, setName).get()
    }

    private inner class InsertAsyncTask(workDao: WorkDao) : AsyncTask<Work, Void, Void>() {
        val workDao = workDao
        override fun doInBackground(vararg params: Work): Void? {
            workDao.insert(params[0])
            return null
        }
    }

    private inner class UpdateAsyncTask(workDao: WorkDao) : AsyncTask<Work, Void, Void>() {
        val workDao = workDao
        override fun doInBackground(vararg params: Work): Void? {
            workDao.update(params[0])
            return null
        }
    }

    private inner class DeleteAsyncTask(workDao: WorkDao) : AsyncTask<Work, Void, Void>() {
        val workDao = workDao
        override fun doInBackground(vararg params: Work): Void? {
            workDao.delete(params[0])
            return null
        }
    }

    private inner class GetTitleNames(workDao: WorkDao) : AsyncTask<Void, Void, List<String>>() {
        val workDao = workDao
        override fun doInBackground(vararg params: Void): List<String> {
            return workDao.getTitleNames()
        }
    }

    private inner class GetSetNames(workDao: WorkDao) : AsyncTask<String, Void, List<String>>() {
        val workDao = workDao
        override fun doInBackground(vararg params: String): List<String> {
            val title = params[0]
            return workDao.getSetNames(title)
        }
    }

    private inner class GetWorks(workDao: WorkDao) : AsyncTask<String, Void, List<Work>>() {
        val workDao = workDao
        override fun doInBackground(vararg params: String): List<Work> {
            val title = params[0]
            val setName = params[1]
            return workDao.getWorks(title, setName)
        }
    }
}