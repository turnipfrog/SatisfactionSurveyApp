package com.example.satisfactionsurvey.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.satisfactionsurvey.SatisfactionSurveyApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            VoteViewModel(/*SatisfactionSurveyApplication().container.votesRepository*/)
        }

        initializer {
            AuthenticationViewModel()
        }
 }
}