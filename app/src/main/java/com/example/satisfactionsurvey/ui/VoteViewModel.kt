package com.example.satisfactionsurvey.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.satisfactionsurvey.model.VoteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class VoteViewModel : ViewModel() {
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
                choiceDate = date
            )
        }
    }

    fun resetVote() {
        _uiState.value = VoteUiState()
    }
}