package com.example.workdiary.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Work(
    @PrimaryKey(autoGenerate = true) val wId: Int,           // 고유 id
    @ColumnInfo(name = "wTitle") var wTitle: String,         // 노동 이름
    @ColumnInfo(name = "wSetName") var wSetName: String,     // 노동 파트 명
    @ColumnInfo(name = "wDate") var wDate: String,           // 노동하는 날짜
    @ColumnInfo(name = "wStartTime") var wStartTime: String, // 노동 시작 시간
    @ColumnInfo(name = "wEndTime") var wEndTime: String,     // 노동 끝 시간
    @ColumnInfo(name = "wMoney") var wMoney: Int,            // 시급
    @ColumnInfo(name = "wIsDone") var wIsDone: Int           // 완료 여부 (0:false, 1:true)
) : Serializable {
    constructor(
        wTitle: String,
        wSetName: String,
        wDate: String,
        wStartTime: String,
        wEndTime: String,
        wMoney: Int,
        wIsDone: Int
    ) : this(0, wTitle, wSetName, wDate, wStartTime, wEndTime, wMoney, wIsDone)
}