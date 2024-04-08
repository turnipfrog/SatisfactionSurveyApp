package com.example.satisfactionsurvey.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.satisfactionsurvey.R

@Composable
fun ThankYouScreen(
    @DrawableRes imageId: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = imageId),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(1f)
    )
}

@Preview
@Composable
fun ThankYouScreenPreview() {
    ThankYouScreen(R.drawable.sun)
}