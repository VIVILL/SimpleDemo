package com.example.paging3.data

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateConverter {
    @TypeConverter
    fun toDate(time: String): LocalDateTime {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return  LocalDateTime.parse(time, dateTimeFormatter)
    }

    @TypeConverter
    fun toTimestamp(date: LocalDateTime): String {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return date.format(dateTimeFormatter)
    }

}