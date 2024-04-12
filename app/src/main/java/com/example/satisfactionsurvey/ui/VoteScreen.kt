package com.example.satisfactionsurvey.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satisfactionsurvey.R
import kotlin.math.floor

@Composable
fun VoteScreen(
    onAdminImageClicked: () -> Unit,
    onButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.admin_icon),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(top = 8.dp, end = 8.dp)
                .align(Alignment.TopEnd)
                .clickable { onAdminImageClicked.invoke() }
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.vote_question),
            textAlign = TextAlign.Center,
            fontSize = 50.sp,
            modifier = modifier.padding(bottom = 20.dp)
        )
        VoteButtons(
            onButtonClicked = onButtonClicked,
            amount = 10)
    }
}

@Composable
fun VoteButtons(
    onButtonClicked: (Int) -> Unit,
    amount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth(1f)
    ) {
        for (i in 1..amount) {
            VoteButton(onButtonClicked, i)
            Spacer(modifier = Modifier.padding(end = 16.dp))
        }
    }
}

@Composable
fun VoteButton(
    onButtonClicked: (Int) -> Unit,
    number: Int,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val buttonSize = floor(screenWidth/12.5)
    val buttonTextSize = floor(buttonSize/2)

    OutlinedButton(
        onClick = { onButtonClicked(number) },
        modifier = Modifier.size(buttonSize.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp)
        ) {
        Text(
            text = number.toString(),
            fontSize = buttonTextSize.sp
            )
    }
}

@Preview
@Composable
fun VoteScreenPreview() {
    VoteScreen(
        onAdminImageClicked = {},
        onButtonClicked = { }
    )
}