package com.example.paging3.data

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// 定义数据类
data class Article(
    val id: Int,
    val title: String,
    val description: String,
    val created: LocalDateTime,
)

//生成 时间 string
val Article.createdText: String get() = articleDateFormatter.format(created)
private val articleDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
