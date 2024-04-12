package com.example.satisfactionsurvey.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.satisfactionsurvey.SatisfactionSurveyApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            VoteViewModel(satisfactionSurveyApplication().container.votesRepository)
        }

        initializer {
            AuthenticationViewModel()
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [SatisfactionSurveyApplication].
 */
fun CreationExtras.satisfactionSurveyApplication(): SatisfactionSurveyApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as SatisfactionSurveyApplication)