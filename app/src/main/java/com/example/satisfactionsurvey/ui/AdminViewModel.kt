package com.example.satisfactionsurvey.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satisfactionsurvey.data.Vote
import com.example.satisfactionsurvey.data.VotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.LocalDate

class AdminViewModel(private val votesRepository: VotesRepository) : ViewModel() {
    var startDate by mutableStateOf(LocalDate.of(2024, 4, 1))
        private set

    var endDate by mutableStateOf(LocalDate.now())
        private set

    var voteListFromInterval: List<Vote> = listOf()

    var adminUiState: StateFlow<AdminUiState> =
        votesRepository.getVotesByDateIntervalStream(startDate, endDate).map { AdminUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AdminUiState()
            )

    fun updateAdminUiState() {
        votesRepository.getVotesByDateIntervalStream(startDate, endDate).map { AdminUiState(it) }
    }

    fun updateVoteListFromInterval() {
        viewModelScope.launch {
            voteListFromInterval = votesRepository.getVotesByDateIntervalStream(startDate, endDate).first()
        }
    }

    fun deleteAllVotes() {
        viewModelScope.launch {
            votesRepository.deleteAllVotes()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun updateStartDate(date: LocalDate) {
        startDate = date
    }

    fun updateEndDate(date: LocalDate) {
        endDate = date
    }

    fun writeCsvFile(votes: List<Vote>) {
        val fileName = "$startDate-to-$endDate.csv"
        FileOutputStream(fileName).apply {
            writeCsv(votes)
        }
    }

    fun OutputStream.writeCsv(votes: List<Vote>) {
        val avgGrade = averageGrade(votes)
        val writer = bufferedWriter()
        writer.write(""""Dato", "Vurdering", "Kommentar"""")
        writer.newLine()
        votes.forEach {
            writer.write("${it.choiceDate},${it.grade},${it.optionalText}")
        }
        repeat(2) {writer.newLine() }
        writer.write("Gennemsnitslig vurdering: $avgGrade")
        writer.flush()
    }

    fun averageGrade(votes: List<Vote>): Double {
        val sum = votes.fold(0.0) { acc, vote ->  acc + vote.grade }
        return sum / votes.size
    }
}
data class AdminUiState(val voteList: List<Vote> = listOf())