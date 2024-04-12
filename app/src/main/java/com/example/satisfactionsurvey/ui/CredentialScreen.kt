package com.example.satisfactionsurvey.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satisfactionsurvey.R

@Composable
fun CredentialScreen(
    authenticationViewModel: AuthenticationViewModel,
    onDonePressed: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.enter_password),
            fontSize = 40.sp,
            modifier = modifier.padding(bottom = 20.dp)
        )
        TextField(
            value = authenticationViewModel.passwordAttempt,
            onValueChange = { authenticationViewModel.updatePasswordAttempt(it) },
            label = { Text(stringResource(id = R.string.password))},
            singleLine = true,
            placeholder = { Text("Password") },
            visualTransformation = if (authenticationViewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            textStyle = LocalTextStyle.current.copy(fontSize = 28.sp),
            trailingIcon = {
                val image = if (authenticationViewModel.passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Localized description for accessibility services
                val description = if (authenticationViewModel.passwordVisible) "Hide password" else "Show password"

                // Toggle button to hide or display password
                IconButton(onClick = { authenticationViewModel.togglePasswordVisible() }) {
                    Icon(imageVector = image, description)
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = { onDonePressed(authenticationViewModel.passwordAttempt) }
            )
        )
    }
}

@Preview
@Composable
fun CredentialScreenPreview() {
    CredentialScreen(
        authenticationViewModel = AuthenticationViewModel(),
        {}
    )
}