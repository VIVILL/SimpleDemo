package com.example.paging3.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

// 定义数据类
@Entity
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    @TypeConverters(DateConverter::class)
    //val created: Date,
    val created: LocalDateTime,
)

//生成 时间 string
val Article.createdText: String get() = articleDateFormatter.format(created)
private val articleDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
