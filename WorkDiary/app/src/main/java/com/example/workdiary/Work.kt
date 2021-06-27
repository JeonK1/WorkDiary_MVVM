package com.example.workdiary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Work(
    @PrimaryKey(autoGenerate = true) val wId:Int,           // 고유 id
    @ColumnInfo(name = "wTitle") val wTitle:String,         // 노동 이름
    @ColumnInfo(name = "wSetName") val wSetName:String,     // 노동 파트 명
    @ColumnInfo(name = "wDate") val wDate:String,           // 노동하는 날짜
    @ColumnInfo(name = "wStartTime") val wStartTime:String, // 노동 시작 시간
    @ColumnInfo(name = "wEndTime") val wEndTime:String,     // 노동 끝 시간
    @ColumnInfo(name = "wMoney") val wMoney:Int,            // 시급
    @ColumnInfo(name = "wIsDone") val wIsDone:Int           // 완료 여부 (0:false, 1:true)
) {
}