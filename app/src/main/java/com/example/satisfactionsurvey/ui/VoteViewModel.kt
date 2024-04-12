package com.example.satisfactionsurvey.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.satisfactionsurvey.data.Vote
import com.example.satisfactionsurvey.data.VoteUiState
import com.example.satisfactionsurvey.data.VotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

public class VoteViewModel(private val votesRepository: VotesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(VoteUiState())
    val uiState: StateFlow<VoteUiState> = _uiState.asStateFlow()

    var userComment by mutableStateOf("")
        private set

    fun updateUserComment(optionalText: String) {
        userComment = optionalText
    }

    fun resetUserComment() {
        userComment = ""
    }

    fun updateGrade(grade: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                grade = grade
            )
        }
    }

    fun updateOptionalText(optionalText: String) {

        _uiState.update { currentState ->
            currentState.copy(
                optionalText = optionalText
            )
        }
    }

    fun updateChoiceDate(date: LocalDate) {
        _uiState.update { currentState ->
            currentState.copy(
                choiceDate = java.sql.Date.valueOf(date.toString())
            )
        }
    }

    fun resetVote() {
        _uiState.value = VoteUiState()
    }

    suspend fun saveVote() {
        val vote = Vote(
            id= 0,
            grade = _uiState.value.grade,
            optionalText = _uiState.value.optionalText,
            choiceDate = _uiState.value.choiceDate
            )
        votesRepository.insertVote(vote)
    }
}