package com.example.satisfactionsurvey.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class OfflineVotesRepository(private val voteDao: VoteDao) : VotesRepository {
    override fun getAllVotesStream(): Flow<List<Vote>> = voteDao.getAllVotes()

    override fun getVotesByDateIntervalStream(startDate: LocalDate, endDate: LocalDate) : Flow<List<Vote>> = voteDao.getFromDateInterval(startDate, endDate)

    override suspend fun deleteAllVotes() = voteDao.deleteAll()

    override suspend fun deleteVote(vote: Vote) = voteDao.delete(vote)

    override suspend fun insertVote(vote: Vote) = voteDao.insert(vote)

    override suspend fun updateVote(vote: Vote) = voteDao.update(vote)
}