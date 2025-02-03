package com.example.emchulacity

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.emchulacity.ui.theme.EmchulaCityTheme

@Composable
fun EnchulaAPP() {
        Scaffold (
            topBar = {EnchulaTopBar()}
        ){ innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                ImagesPanel(
                    modifier = Modifier.padding(dimensionResource(R.dimen.images_Padding))
                        .weight(1f)
                )
                ButtonRow(
                    modifier = Modifier.fillMaxWidth()
                        .weight(1f)
                )
            }
        }
}

@Composable
fun ButtonRow(
    modifier: Modifier = Modifier
){
    Row (
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ){
        Button(
            onClick = { /*TODO*/ },
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_camera_24),
                contentDescription = null,
            )
            Text(
                text = stringResource(R.string.camera),
            )
        }
        Button( onClick = {/*TODO*/}) { Icon(
            imageVector = Icons.AutoMirrored.Filled.List,
            contentDescription = null,
        )
            Text(
                text = stringResource(R.string.gallerie),
            )}
    }
}

@Composable
fun ImagesPanel(
    modifier: Modifier = Modifier
){
    Card(modifier = modifier){
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight(1f)
        ) {
            Image(
                painter = painterResource(R.drawable.imgpresentacion1),
                contentScale = ContentScale.Crop,
                contentDescription = "Imagen de presentación 1",
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(R.drawable.imgpresentacion2),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.weight(1f)
                        .fillMaxWidth()
                )
                Image(
                    painter = painterResource(R.drawable.imgpresentacion3),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.weight(1f)
                        .fillMaxHeight()
                )
            }
        }
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
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                ),
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