package com.example.satisfactionsurvey.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDate

@Entity(tableName = "votes")
@TypeConverters(Converters::class)
data class Vote(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val grade: Int,
    val optionalText: String?,
    val choiceDate: LocalDate
)
