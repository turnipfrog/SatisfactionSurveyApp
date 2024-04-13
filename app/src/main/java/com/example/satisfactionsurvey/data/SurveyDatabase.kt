package com.example.satisfactionsurvey.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Vote::class], version = 2, exportSchema = false)
abstract class SurveyDatabase : RoomDatabase() {
    abstract fun voteDao() : VoteDao

    companion object {
        @Volatile
        private var Instance: SurveyDatabase? = null

        fun getDatabase(context: Context): SurveyDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, SurveyDatabase::class.java, "survey_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}