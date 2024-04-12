package com.example.satisfactionsurvey.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
@TypeConverters(Converters::class)
interface VoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vote: Vote)

    @Update
    suspend fun update(vote: Vote)

    @Delete
    suspend fun delete(vote: Vote)

    @Query("SELECT * from votes ORDER BY id ASC")
    fun getAllVotes(): Flow<List<Vote>>

    @Query("DELETE FROM votes")
    suspend fun deleteAll()

    @Query("SELECT * FROM votes WHERE choiceDate >= :startDate AND choiceDate <= :endDate")
    fun getFromDateInterval(startDate: Date, endDate: Date): Flow<List<Vote>>
}