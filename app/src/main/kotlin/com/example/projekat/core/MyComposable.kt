package com.example.projekat.core

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AppIconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null
) {
    IconButton(onClick = onClick) {
        Icon(imageVector = imageVector, contentDescription = contentDescription)
    }
}
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Breeds", style = MaterialTheme.typography.labelLarge)
        },
        navigationIcon = {
            AppIconButton(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = onBackClick)
        },
    )
}

@Composable
fun ProgressBarOurs(
    index: Int,
    size: Int
) {
    val progress by animateFloatAsState(
        targetValue = (index + 1) / size.toFloat(),
        label = ""
    )
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(),
        progress = { progress },
    )
}