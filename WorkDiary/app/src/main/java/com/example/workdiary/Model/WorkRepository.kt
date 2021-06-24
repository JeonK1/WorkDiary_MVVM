package com.example.workdiary.Model

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

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
        return ConvertWorkToWorkName().execute(allWorks).get()
    }

    fun getWorkInfoAll(): LiveData<ArrayList<WorkInfo>> {
        return ConvertWorkToWorkInfo().execute(allWorks).get()
    }

    fun getWorkDateAll(): LiveData<ArrayList<String>> {
        return GetWorkDateAsyncTask(workDao).execute().get()
    }

    fun getDoneWorkAll(date:String): LiveData<ArrayList<Work>> {
        return GetDoneWorkAsyncTask(workDao).execute(date).get()
    }

    fun getDiaryInfoAll(): LiveData<ArrayList<DiaryInfo>> {
        return ConvertWorkToDiaryInfo().execute().get()
    }

    fun getSetNameAll(title:String): LiveData<ArrayList<String>> {
        return GetSetNameAllAsyncTask(workDao).execute(title).get()
    }

    fun setWorkCheck(wId: Int) {
        val work = getWork(wId).value
        if(work!=null){
            // wId에 해당하는 work 값이 유효할때, wIsDone 을 1로 업데이트함
            work.wIsDone = 1
            update(work)
        }
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

    private inner class GetWorkDateAsyncTask(workDao: WorkDao): AsyncTask<Void, Void, LiveData<ArrayList<String>>>(){
        val workDao = workDao
        override fun doInBackground(vararg params: Void): LiveData<ArrayList<String>> {
            return workDao.getWorkDateAll()
        }
    }

    private inner class GetDoneWorkAsyncTask(workDao: WorkDao): AsyncTask<String, Void, LiveData<ArrayList<Work>>>(){
        val workDao = workDao
        override fun doInBackground(vararg params: String): LiveData<ArrayList<Work>> {
            return workDao.getDoneWork(params[0])
        }
    }

    private inner class ConvertWorkToWorkInfo: AsyncTask<LiveData<ArrayList<Work>>, Void, LiveData<ArrayList<WorkInfo>>>(){
        override fun doInBackground(vararg params: LiveData<ArrayList<Work>>): LiveData<ArrayList<WorkInfo>> {
            val workInfoListLiveData:LiveData<ArrayList<WorkInfo>> = MutableLiveData()
            val allWork = params[0]
            if(allWork.value != null){
                for (work in allWork.value!!)
                    if(work.wIsDone==0) {
                        // 아직 완료되지 않은 작업들만 가져오기
                        workInfoListLiveData.value!!.add(
                            WorkInfo(
                                wId = work.wId,
                                workTitle = work.wTitle,
                                workSetName = work.wSetName,
                                workDate = work.wDate,
                                workStartTime = work.wStartTime,
                                workEndTime = work.wEndTime,
                                workMoney = work.wMoney
                            )
                        )
                    }
            }
            return workInfoListLiveData
        }
    }

    private inner class ConvertWorkToWorkName: AsyncTask<LiveData<ArrayList<Work>>, Void, LiveData<ArrayList<String>>>(){
        override fun doInBackground(vararg params: LiveData<ArrayList<Work>>): LiveData<ArrayList<String>> {
            val nameListLiveData:LiveData<ArrayList<String>> = MutableLiveData()
            val allWork = params[0]
            if(allWork.value != null){
                for (work in allWork.value!!)
                    nameListLiveData.value!!.add(work.wTitle)
            }
            return nameListLiveData
        }
    }

    private inner class ConvertWorkToDiaryInfo: AsyncTask<Void, Void, LiveData<ArrayList<DiaryInfo>>>(){
        override fun doInBackground(vararg params: Void): LiveData<ArrayList<DiaryInfo>> {
            val diaryInfoListLiveData:LiveData<ArrayList<DiaryInfo>> = MutableLiveData()
            val workDateList = getWorkDateAll()
            if(workDateList.value!!.size>0){
                for (workDate in workDateList.value!!){
                    val workInfoList = ArrayList<WorkInfo>()
                    val workList = getDoneWorkAll(workDate).value!!
                    for (work in workList){
                        workInfoList.add(WorkInfo(
                            wId = work.wId,
                            workTitle = work.wTitle,
                            workSetName = work.wSetName,
                            workDate = work.wDate,
                            workStartTime = work.wStartTime,
                            workEndTime = work.wEndTime,
                            workMoney = work.wMoney
                        ))
                    }

                    diaryInfoListLiveData.value!!.add(DiaryInfo(
                        year = workDate.split("/")[0].toInt(),
                        month = workDate.split("/")[1].toInt(),
                        workList = workInfoList
                    ))
                }
            }
            return diaryInfoListLiveData
        }
    }
}