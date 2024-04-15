package com.example.satisfactionsurvey

import android.os.CountDownTimer
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.satisfactionsurvey.ui.AdminScreen
import com.example.satisfactionsurvey.ui.AdminViewModel
import com.example.satisfactionsurvey.ui.AppViewModelProvider
import com.example.satisfactionsurvey.ui.AuthenticationViewModel
import com.example.satisfactionsurvey.ui.CommentScreen
import com.example.satisfactionsurvey.ui.CredentialScreen
import com.example.satisfactionsurvey.ui.ThankYouScreen
import com.example.satisfactionsurvey.ui.VoteScreen
import com.example.satisfactionsurvey.ui.VoteViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class SatisfactionSurveyScreen(@StringRes val title: Int) {
    Start(title = R.string.start_screen_appbar_string),
    Comment(title = R.string.comment_screen_title),
    ThankYou(title = R.string.thank_you_screen_title),
    Credential(title = R.string.credential_screen),
    Admin(title = R.string.admin_screen),
    ConfirmDelete(title = R.string.confirm_delete_all)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SatisfactionSurveyAppBar(
    currentScreen: SatisfactionSurveyScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun SatisfactionSurveyApp(
    activity: MainActivity,
    viewModel: VoteViewModel = viewModel(factory = AppViewModelProvider.Factory),
    adminViewModel: AdminViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
) {
    //val viewModel: VoteViewModel = viewModel()
    val authenticationViewModel: AuthenticationViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = SatisfactionSurveyScreen.valueOf(
        backStackEntry?.destination?.route ?:SatisfactionSurveyScreen.Start.name
    )
    Scaffold(
        topBar = {
            SatisfactionSurveyAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->


        NavHost(
            navController = navController,
            startDestination = SatisfactionSurveyScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = SatisfactionSurveyScreen.Start.name) {
                VoteScreen(
                    onAdminImageClicked = { navController.navigate(SatisfactionSurveyScreen.Credential.name )},
                    onButtonClicked = {
                        viewModel.updateGrade(it)
                        navController.navigate(SatisfactionSurveyScreen.Comment.name)
                    }
                )
            }
            composable(route = SatisfactionSurveyScreen.Comment.name) {
                CommentScreen(
                    voteViewModel = viewModel,
                    onConfirmClicked = {
                        viewModel.updateOptionalText(it)
                        viewModel.updateChoiceDate(LocalDate.now())
                        navController.navigate(SatisfactionSurveyScreen.ThankYou.name)
                        coroutineScope.launch {
                            viewModel.saveVote()
                            viewModel.resetUserComment()
                        }
                        waitAndNavigateToStart(navController)
                    },
                    onCancelClicked = { resetVoteAndNavigateToStart(viewModel, navController) }
                )
            }
            composable(route = SatisfactionSurveyScreen.ThankYou.name) {
                ThankYouScreen(R.drawable.sun)
            }
            composable(route = SatisfactionSurveyScreen.Credential.name) {
                CredentialScreen(
                    authenticationViewModel = authenticationViewModel,
                    screenText = R.string.enter_password,
                    onDonePressed = {
                        if (authenticationViewModel.validatePassword(it)) {
                            navController.navigate(SatisfactionSurveyScreen.Admin.name)
                        }
                        else {
                            Toast.makeText(activity,
                                activity.getString(R.string.wrong_password),
                                Toast.LENGTH_LONG).show()
                            navController.popBackStack(SatisfactionSurveyScreen.Start.name, inclusive = false)
                        }
                        authenticationViewModel.resetTextField()
                    }
                )
            }
            composable(route = SatisfactionSurveyScreen.Admin.name) {
                AdminScreen(
                    fromDate = R.string.start_date_text,
                    toDate = R.string.end_date_text,
                    onExportClick = {
                        adminViewModel.updateVoteListFromInterval()
                        adminViewModel.writeCsvFile(
                            votes = adminViewModel.voteListFromInterval,
                            activity = activity
                        )
                    },
                    onDeleteClick = {
                        navController.navigate(SatisfactionSurveyScreen.ConfirmDelete.name)
                    },
                    onBackClick = {
                        navController.popBackStack(SatisfactionSurveyScreen.Start.name, inclusive = false)
                    }
                )
            }
            composable(route = SatisfactionSurveyScreen.ConfirmDelete.name) {
                CredentialScreen(
                    authenticationViewModel = authenticationViewModel,
                    onDonePressed = {
                        if (authenticationViewModel.validatePassword(it)) {
                            adminViewModel.deleteAllVotes()
                            navController.navigate(SatisfactionSurveyScreen.Admin.name)
                        }
                        else {
                            Toast.makeText(activity,
                                activity.getString(R.string.wrong_password),
                                Toast.LENGTH_LONG).show()
                            navController.navigate(SatisfactionSurveyScreen.Admin.name)
                        }
                        authenticationViewModel.resetTextField()
                    },
                    screenText = R.string.type_code_delete_all
                )
            }
        }
    }
}

private fun resetVoteAndNavigateToStart(
    viewModel: VoteViewModel,
    navController: NavHostController
) {
    viewModel.resetVote()
    viewModel.resetUserComment()
    navController.popBackStack(SatisfactionSurveyScreen.Start.name, inclusive = false)
}

private fun waitAndNavigateToStart(
    navController: NavHostController
) {
    object : CountDownTimer(5000, 1000) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            navController.popBackStack(SatisfactionSurveyScreen.Start.name, inclusive = false)
        }
    }.start()
}