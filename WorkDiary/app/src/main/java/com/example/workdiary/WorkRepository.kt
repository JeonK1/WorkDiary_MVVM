package com.example.workdiary

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class WorkRepository(application: Application) {
    companion object{
        lateinit var workDao: WorkDao
        lateinit var allWorks: LiveData<List<Work>>
    }

    init {
        val database = WorkDatabase.getInstance(application)
        workDao = database.workDao()
        allWorks = workDao.getAllWork()
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

    fun getAllWorks(): LiveData<List<Work>>{
        return allWorks
    }

    private inner class InsertAsyncTask(workDao: WorkDao): AsyncTask<Work, Void, Void>(){
        val workDao = workDao
        override fun doInBackground(vararg params: Work): Void? {
            workDao.insert(params[0])
            return null
        }
    }

    private inner class UpdateAsyncTask(workDao: WorkDao): AsyncTask<Work, Void, Void>(){
        val workDao = workDao
        override fun doInBackground(vararg params: Work): Void? {
            workDao.update(params[0])
            return null
        }
    }

    private inner class DeleteAsyncTask(workDao: WorkDao): AsyncTask<Work, Void, Void>(){
        val workDao = workDao
        override fun doInBackground(vararg params: Work): Void? {
            workDao.delete(params[0])
            return null
        }
    }
}