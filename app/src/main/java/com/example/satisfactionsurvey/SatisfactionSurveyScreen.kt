package com.example.satisfactionsurvey

import android.os.CountDownTimer
import android.widget.Toast
import androidx.annotation.DrawableRes
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.satisfactionsurvey.ui.CommentScreen
import com.example.satisfactionsurvey.ui.ThankYouScreen
import com.example.satisfactionsurvey.ui.VoteScreen
import com.example.satisfactionsurvey.ui.VoteViewModel
import java.time.LocalDate


enum class SatisfactionSurveyScreen(@StringRes val title: Int) {
    Start(title = R.string.start_screen_appbar_string),
    Comment(title = R.string.comment_screen_title),
    ThankYou(title = R.string.thank_you_screen_title)
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
    navController: NavHostController = rememberNavController()
) {
    val viewModel: VoteViewModel = viewModel()

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
                    onButtonClicked = {
                        viewModel.updateGrade(it)
                        navController.navigate(SatisfactionSurveyScreen.Comment.name)
                    }
                )
            }
            composable(route = SatisfactionSurveyScreen.Comment.name) {
                CommentScreen(
                    onConfirmClicked = {
                        viewModel.updateOptionalText(it)
                        viewModel.updateChoiceDate(LocalDate.now())
                        navController.navigate(SatisfactionSurveyScreen.ThankYou.name)
                        waitAndNavigateToStart(navController)
//                        navController.popBackStack(SatisfactionSurveyScreen.Start.name, inclusive = false)
//                        Toast.makeText(activity,
//                            activity.getString(R.string.toast_confirm_message),
//                            Toast.LENGTH_LONG).show()
                    },
                    onCancelClicked = { resetVoteAndNavigateToStart(viewModel, navController) }
                )
            }
            composable(route = SatisfactionSurveyScreen.ThankYou.name) {
                ThankYouScreen(R.drawable.sun)
            }
        }
    }
}

private fun resetVoteAndNavigateToStart(
    viewModel: VoteViewModel,
    navController: NavHostController
) {
    viewModel.resetVote()
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