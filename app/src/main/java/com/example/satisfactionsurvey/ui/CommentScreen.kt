package com.example.satisfactionsurvey.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satisfactionsurvey.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommentScreen(
    voteViewModel: VoteViewModel,
    onConfirmClicked: (String) -> Unit,
    onCancelClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.you_may_comment),
            fontSize = 40.sp,
            modifier = modifier.padding(bottom = 20.dp)
        )
        TextField(
            value = voteViewModel.userComment,
            onValueChange = { voteViewModel.updateUserComment(it) },
            label = { Text(stringResource(id = R.string.make_a_comment))},
            textStyle = LocalTextStyle.current.copy(fontSize = 28.sp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }),
        )
            Button(
                onClick = { onConfirmClicked(voteViewModel.userComment) },
                modifier = Modifier
                    .padding(24.dp)
                    .size(width = 240.dp,height = 80.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.confirm),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                    )
            }
            Button(
                onClick = onCancelClicked,
                modifier =  Modifier.size(width = 240.dp,height = 80.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                    )
            }
    }
}

@Preview
@Composable
fun CommentScreenPreview() {
    CommentScreen(
        voteViewModel = VoteViewModel(),
        onCancelClicked = {},
        onConfirmClicked = {}
    )
}