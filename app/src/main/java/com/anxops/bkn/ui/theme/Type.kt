package com.anxops.bkn.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.anxops.bkn.R

//
//
val fonts = FontFamily(
    Font(R.font.mukta_extralight, weight = FontWeight.ExtraLight),
    Font(R.font.mukta_light, weight = FontWeight.Light),
    Font(R.font.mukta_regular, weight = FontWeight.Normal),
    Font(R.font.mukta_medium, weight = FontWeight.Medium),
    Font(R.font.mukta_semibold, weight = FontWeight.SemiBold),
    Font(R.font.mukta_bold, weight = FontWeight.Bold),
    Font(R.font.mukta_extrabold, weight = FontWeight.ExtraBold),
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    h1 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 26.sp,
//        letterSpacing = (-1.5).sp
    ),
    h2 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
//        letterSpacing = (-0.5).sp
    ),
    h3 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize = 15.sp,
        color = BknGrey,
//        letterSpacing = 0.sp
    ),
    h4 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        color = BknGrey,
    ),
    h5 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 14.sp,
        color = BknGrey,
    ),
    h6 = TextStyle(
        fontFamily = fonts,
        fontSize = 12.sp,
    ),
    subtitle1 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = BknGrey,
    ),
    subtitle2 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Thin,
        fontSize = 15.sp,
        color = BknGrey,
    ),
    button = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
    )
)