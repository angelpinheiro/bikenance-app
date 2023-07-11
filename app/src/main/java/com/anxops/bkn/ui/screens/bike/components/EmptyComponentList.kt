package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anxops.bkn.R

@Composable
fun EmptyComponentList(onClickAction: () -> Unit = {}) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Your bike  does not has any attached components",
            modifier = Modifier.padding(vertical = 30.dp, horizontal = 30.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        )

        Image(
            painter = painterResource(id = R.drawable.ic_undraw_not_found),
            contentDescription = "Not found",
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .background(Color.Transparent)
                .padding(top = 0.dp)
        )

        OutlinedButton(onClick = { onClickAction() }, modifier = Modifier.padding(top = 20.dp)) {
            Text(
                text = "Proceed with initial setup",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h3,
            )
        }
    }
}