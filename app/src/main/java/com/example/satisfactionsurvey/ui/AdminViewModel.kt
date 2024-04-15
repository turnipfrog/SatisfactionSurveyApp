package com.example.satisfactionsurvey.ui

import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satisfactionsurvey.MainActivity
import com.example.satisfactionsurvey.R
import com.example.satisfactionsurvey.data.Vote
import com.example.satisfactionsurvey.data.VotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.LocalDate


class AdminViewModel(private val votesRepository: VotesRepository) : ViewModel() {
    var startDate by mutableStateOf(LocalDate.of(2024, 4, 1))
        private set

    var sdcard = Environment.getExternalStorageDirectory().absolutePath

    var endDate by mutableStateOf(LocalDate.now())
        private set

    var voteListFromInterval: List<Vote> = listOf()
        private set

    private val _adminUiState = MutableStateFlow(AdminUiState())
    val adminUiState = _adminUiState.asStateFlow()

    init {
        updateVoteListFromInterval()
        updateAdminUiState()
    }

    fun updateAdminUiState() {
        viewModelScope.launch {
            val newList = votesRepository.getVotesByDateIntervalStream(startDate, endDate).map { AdminUiState(it) }.first().voteList
            _adminUiState.update {
                it.copy(
                    voteList = newList
                )
            }
        }
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

    fun updateStartDate(date: LocalDate) {
        startDate = date
    }

    fun updateEndDate(date: LocalDate) {
        endDate = date
    }

    fun createFolderIfNotExist() {
        val folder = File(sdcard, "trivsel_csv")
        if (!folder.exists()) {
            folder.mkdir()
        }
    }

    fun writeCsvFile(votes: List<Vote>, activity: MainActivity) {
        val fileName = "$startDate-to-$endDate.csv"
        val outputFile = sdcard + "/trivsel_csv/$fileName"
        createFolderIfNotExist()
        try {
            FileOutputStream(outputFile).apply {
                writeCsv(votes)
            }
            Toast.makeText(
                activity,
                ".csv fil gemt i $outputFile",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                activity,
                activity.getString(R.string.csv_file_not_created),
                Toast.LENGTH_LONG
            ).show()
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
        writer.close()
    }

    fun averageGrade(votes: List<Vote>): Double {
        val sum = votes.fold(0.0) { acc, vote ->  acc + vote.grade }
        return sum / votes.size
    }


}
data class AdminUiState(var voteList: List<Vote> = listOf())