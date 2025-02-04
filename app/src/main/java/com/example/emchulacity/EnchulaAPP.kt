package com.example.emchulacity

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.emchulacity.nav.Navigation
import com.example.emchulacity.ui.theme.EmchulaCityTheme

@Composable
fun EnchulaAPP() {
        Scaffold (
            topBar = {EnchulaTopBar()}
        ){ innerPadding ->

                Navigation(
                    modifier = Modifier.padding(innerPadding)
                )
        }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnchulaTopBar(
    modifier: Modifier = Modifier,
    canNavigate: Boolean = false,
    navigateUp: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            if (canNavigate) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun PreviewEnchulaAPP() {
    EmchulaCityTheme {
        EnchulaAPP()
    }
}
@Preview
@Composable
fun PreviewEnchulaAPPDark() {
    EmchulaCityTheme(darkTheme = true) {
        EnchulaAPP()
    }
}