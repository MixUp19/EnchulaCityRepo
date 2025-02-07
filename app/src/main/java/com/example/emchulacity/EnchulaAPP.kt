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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.emchulacity.nav.Navigation
import com.example.emchulacity.nav.Screens
import com.example.emchulacity.ui.theme.EmchulaCityTheme

@Composable
fun EnchulaAPP() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val canNavigate = backStackEntry?.destination?.route != Screens.lobby.name
        Scaffold (
            topBar = {
                EnchulaTopBar(
                    canNavigate = canNavigate,
                    navigateUp = { navController.popBackStack() },
                )
            }
        ){ innerPadding ->
                Navigation(
                    navController = navController,
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
    val modifierText =
        if(canNavigate)
            Modifier.fillMaxWidth().
            padding(end = dimensionResource(R.dimen.top_bar_padding))
        else
            Modifier.fillMaxWidth()
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = modifierText
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