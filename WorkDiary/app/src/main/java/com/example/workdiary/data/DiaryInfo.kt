package com.example.workdiary.data

data class DiaryInfo(
    val year: Int,
    val month: Int,
    val workList: MutableList<Work>
) {
    constructor(year: Int, month: Int, work: Work):this(year, month, mutableListOf(work))
}