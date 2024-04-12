package com.example.satisfactionsurvey.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.util.Date

data class VoteUiState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val grade: Int = 0,
    val optionalText: String? = null,
    val choiceDate: Date = java.sql.Date.valueOf(LocalDate.now().toString())
)