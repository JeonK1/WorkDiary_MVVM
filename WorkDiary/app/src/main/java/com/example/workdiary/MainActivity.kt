package com.example.workdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    lateinit var workViewModel:WorkViewModel
    lateinit var workAdapter: WorkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // recyclerView 초기화
        val recyclerView = findViewById<RecyclerView>(R.id.rv_work_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        workAdapter = WorkAdapter(listOf()) // empty list adapter
        recyclerView.adapter = workAdapter

        // ViewModel 설정
        workViewModel = ViewModelProvider(this, WorkViewModelFactory(application)).get(WorkViewModel::class.java)
        workViewModel.getAllWork().observe(this, Observer { works ->
            workAdapter.setWorks(works)
        })
    }
}