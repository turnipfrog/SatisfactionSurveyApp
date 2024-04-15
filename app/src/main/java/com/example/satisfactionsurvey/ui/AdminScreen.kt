package com.example.satisfactionsurvey.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.satisfactionsurvey.R
import com.example.satisfactionsurvey.data.Vote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun AdminScreen(
    @StringRes fromDate: Int,
    @StringRes toDate: Int,
//    onStartDatePicked: (LocalDate) -> Unit,
//    onEndDatePicked: (LocalDate) -> Unit,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit,
    onExportClick: () -> Unit,
    adminViewModel: AdminViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val adminUiState by adminViewModel.adminUiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row {
                Buttons(
                    fromDate = fromDate,
                    toDate = toDate,
                    onStartDatePicked = {
                        adminViewModel.updateEndDate(it)
                        adminViewModel.updateVoteListFromInterval()
                        adminViewModel.updateAdminUiState()
                    },
                    onEndDatePicked = {
                        adminViewModel.updateEndDate(it)
                        adminViewModel.updateVoteListFromInterval()
                        adminViewModel.updateAdminUiState()
                    },
                    adminViewModel = adminViewModel
                )
                AdminButtons(
                    onDeleteButtonClick = onDeleteClick,
                    onBackButtonClick = onBackClick,
                    onExportButtonClick = onExportClick,
                    deleteButtonText = R.string.delete_all,
                    backButtonText = R.string.back_button,
                    exportButtonText = R.string.export
                )
                FolderDestinationPicker()
            }
            VoteBody(
                voteList = adminUiState.voteList,
                onVoteClick = {},
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(1f)
            )
        }
    }
}

@Composable
fun Buttons(
    fromDate: Int,
    toDate: Int,
    onStartDatePicked: (LocalDate) -> Unit,
    onEndDatePicked: (LocalDate) -> Unit,
    adminViewModel: AdminViewModel,
    modifier: Modifier = Modifier
) {
    Row {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = modifier.padding(end = 16.dp)
        ) {
            Text(
                text = stringResource(fromDate),
                textAlign = TextAlign.Center
            )
            MyDatePickerDialog(
                onDatePicked = onStartDatePicked,
                dateParam = adminViewModel.startDate
            )
        }
        Column {
            Text(
                text = stringResource(toDate),
                textAlign = TextAlign.Center
            )
            MyDatePickerDialog(
                onDatePicked = onEndDatePicked,
                dateParam = adminViewModel.endDate
            )
        }
    }
}

@Composable
private fun VoteBody(
    voteList: List<Vote>,
    onVoteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
    ) {
        if (voteList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_vote_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            VoteList(
                voteList = voteList,
                onVoteClick = { onVoteClick(it.id) },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun VoteList(
    voteList: List<Vote>,
    onVoteClick: (Vote) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = voteList, key = { it.id }) { vote ->
            VoteItem(vote = vote,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onVoteClick(vote) })
        }
    }
}

@Composable
private fun VoteItem(
    vote: Vote,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = vote.choiceDate.toString(),
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = vote.grade.toString(),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            val optionaltext = vote.optionalText ?: ""
            Text(
                text = optionaltext,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


@Composable
fun MyDatePickerDialog(
    onDatePicked: (LocalDate) -> Unit,
    dateParam: LocalDate,
    modifier: Modifier = Modifier
) {
    var date by remember {
        mutableStateOf(dateParam)
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    Box(contentAlignment = Alignment.Center) {
        Button(onClick = { showDatePicker = true }) {
            Text(text = date.toString())
        }
    }

    if (showDatePicker) {
        MyDatePickerDialog(
            onDateSelected = {
                date = it
                onDatePicked(date)
                             },
            onDismiss = { showDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToLocalDate(it)
    } ?: LocalDate.of(2024, 4, 1)

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

private fun convertMillisToLocalDate(it: Long): LocalDate {
    return Instant
        .ofEpochMilli(it)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

@Composable
fun AdminButtons(
    onDeleteButtonClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    onExportButtonClick: () -> Unit,
    @StringRes deleteButtonText: Int,
    @StringRes backButtonText: Int,
    @StringRes exportButtonText: Int,
    modifier: Modifier = Modifier
) {
    Row () {
        Column(modifier = Modifier.padding(start = 80.dp)) {
            Spacer(modifier = Modifier.padding(bottom = 22.dp))
            Button(
                onClick = onExportButtonClick
            ) {
                Text(text = stringResource(exportButtonText))
            }
        }
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Spacer(modifier = Modifier.padding(bottom = 22.dp))
            Button(
                onClick = onDeleteButtonClick
            ) {
                Text(text = stringResource(deleteButtonText))

            }
        }
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Spacer(modifier = Modifier.padding(bottom = 22.dp))
            Button(
                onClick = onBackButtonClick
            ) {
                Text(text = stringResource(backButtonText))
            }
        }
    }
}

@Composable
fun FolderDestinationPicker(){
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "*text/csv*"
    intent.addCategory(Intent.CATEGORY_OPENABLE)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = activityResult.data?.data
        }
    }
    Button(onClick = {
        launcher.launch(intent)
    }) {
        Text(text = "Take a file")
    }
}

@Preview
@Composable
fun VoteItemPreview() {
    val vote = Vote(1, 8, "Test kommentar her", LocalDate.now())
    VoteItem(vote)
}