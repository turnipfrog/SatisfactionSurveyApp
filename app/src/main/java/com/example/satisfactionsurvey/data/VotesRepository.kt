package com.example.satisfactionsurvey.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.Date


interface VotesRepository {
    fun getAllVotesStream() : Flow<List<Vote>>

    fun getVotesByDateIntervalStream(startDate: Date, endDate: Date) : Flow<List<Vote>>

    suspend fun insertVote(vote: Vote)

    suspend fun deleteVote(vote: Vote)

    suspend fun deleteAllVotes()

    suspend fun updateVote(vote: Vote)



}