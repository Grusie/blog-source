package com.grusie.testnavigation

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FirstScreen(onNextBtnClicked: () -> Unit) {
    Button(onClick = { onNextBtnClicked() }) {
        Text(text = "secondScreen")
    }
}