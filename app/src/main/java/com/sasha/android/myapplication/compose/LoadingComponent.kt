package com.sasha.android.myapplication.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sasha.android.myapplication.ui.theme.Blue500

@Composable
fun LoadingComponent() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            color = Blue500,
            modifier = Modifier.size(128.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewComponent() {
    LoadingComponent()
}