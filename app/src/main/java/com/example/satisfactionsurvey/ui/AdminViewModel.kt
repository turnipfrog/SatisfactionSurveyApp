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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class AdminViewModel(private val votesRepository: VotesRepository) : ViewModel() {
    var startDate by mutableStateOf(LocalDate.of(2024, 4, 1))
        private set

    var endDate by mutableStateOf(LocalDate.now())
        private set

    var adminUiState: StateFlow<AdminUiState> =
        votesRepository.getVotesByDateIntervalStream(startDate, endDate).map { AdminUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AdminUiState()
            )

    fun updateAdminUiState() {
        votesRepository.getVotesByDateIntervalStream(startDate, endDate).map { AdminUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AdminUiState()
            )
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
//    var uiState by mutableStateOf(AdminUiState())
//        private set
//
//
//    var averageGrade by mutableStateOf(0.0)
//        private set
//
    fun updateStartDate(date: LocalDate){
        startDate = date
    }

    fun updateEndDate(date: LocalDate){
        endDate = date
    }
//
//    suspend fun updateUiState() {
//
//        uiState = AdminUiState(
//            voteListFromInterval = listOf()/*getAllVotesFromInterval().toVoteDetailsList()*/,
//            averageGrades = AverageGrades(2.0, 5.0)/*getAvgGrades()*/,
//            startDate = startDate,
//            endDate = endDate
//        )
//    }
//
//    suspend fun getAllVotesFromInterval(): List<Vote> =
//        votesRepository.getVotesByDateIntervalStream(startDate, endDate).flattenToList()
//
//    suspend fun getAvgGrades(): AverageGrades {
//        val averageGradeAllTime = averageUsingFold(getAllVotes())
//        val averageGradeInterval = averageUsingFold(getAllVotesFromInterval())
//
//        Log.d("FINDMETAG", averageGradeAllTime.toString())
//        Log.d("FINDMETAG2", averageGradeInterval.toString())
//        return AverageGrades(
//            averageAllTime = averageGradeAllTime,
//            averageInterval = averageGradeInterval
//        )
//    }
//
//    suspend fun getAllVotes(): List<Vote> = votesRepository.getAllVotesStream().flattenToList()
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    suspend fun <T> Flow<List<T>>.flattenToList() =
//        flatMapConcat { it.asFlow() }.toList()
//
//    fun List<Vote>.toVoteDetailsList(): List<VoteDetails> {
//        return this.map { VoteDetails(it.id, it.grade, it.optionalText, it.choiceDate) }
//    }
//
//    private fun averageUsingFold(votes: List<Vote>): Double {
//        val sum = votes.fold(0.0) { sum, item -> sum + item.grade}
//        val average = sum / votes.size
//        return average
//    }
}
//
//data class AdminUiState(
//    val voteListFromInterval: List<VoteDetails> = listOf(),
//    val averageGrades: AverageGrades = AverageGrades(),
//    val startDate: LocalDate = LocalDate.of(2024, 4, 1),
//    val endDate: LocalDate = LocalDate.now()
//)
//
//data class VoteDetails(
//    val id: Int = 0,
//    val grade: Int = 0,
//    val optionalText: String? = "",
//    val choiceDate: LocalDate = LocalDate.of(0, 0, 1)
//)
//
//data class AverageGrades(
//    val averageAllTime: Double = 0.0,
//    val averageInterval: Double = 0.0
//)
data class AdminUiState(val voteList: List<Vote> = listOf())