package com.example.emchulacity.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.emchulacity.R
import com.example.emchulacity.ui.theme.EmchulaCityTheme

@Composable
fun LobbyScreen(
    cameraNavigate: () -> Unit = {},
    galleriesNavigate: () -> Unit = {},
    modifier: Modifier =Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        ImagesPanel(
            modifier = Modifier.padding(dimensionResource(R.dimen.images_Padding))
                .weight(1f)
        )
        ButtonRow(
            cameraNavigate,
            galleriesNavigate,
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
fun ButtonRow(
    cameraNavigate: () -> Unit = {},
    galleriesNavigate: () -> Unit = {},
    modifier: Modifier = Modifier
){
    Row (
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ){
        Button(
            onClick = { cameraNavigate() },
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_camera_24),
                contentDescription = null,
            )
            Text(
                text = stringResource(R.string.camera),
            )
        }
        Button( onClick = {galleriesNavigate()}) { Icon(
            imageVector = Icons.AutoMirrored.Filled.List,
            contentDescription = null,
        )
            Text(
                text = stringResource(R.string.gallerie),
            )
        }
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

@Preview
@Composable
fun PreviewEnchulaAPP() {
    EmchulaCityTheme {
        LobbyScreen()
    }
}
@Preview
@Composable
fun PreviewEnchulaAPPDark() {
    EmchulaCityTheme(darkTheme = true) {
        LobbyScreen()
    }
}
