package com.example.satisfactionsurvey.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val votesRepository: VotesRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineVotesRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [VotesRepository]
     */
    override val votesRepository: VotesRepository by lazy {
        OfflineVotesRepository(SurveyDatabase.getDatabase(context).voteDao())
    }
}