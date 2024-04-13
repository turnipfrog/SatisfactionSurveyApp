package com.example.satisfactionsurvey.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


interface VotesRepository {
    fun getAllVotesStream() : Flow<List<Vote>>

    fun getVotesByDateIntervalStream(startDate: LocalDate, endDate: LocalDate) : Flow<List<Vote>>

    suspend fun insertVote(vote: Vote)

    suspend fun deleteVote(vote: Vote)

    suspend fun deleteAllVotes()

    suspend fun updateVote(vote: Vote)



}