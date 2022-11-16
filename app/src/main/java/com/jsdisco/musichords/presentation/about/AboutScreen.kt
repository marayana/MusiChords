package com.jsdisco.musichords.presentation.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsdisco.musichords.R
import com.jsdisco.musichords.ui.theme.*

@Composable fun AboutScreen(
    onSetAppBarTitle: (String) -> Unit,
    onSetShowBackBtn: (Boolean) -> Unit
) {

    val annotatedStr = buildAnnotatedString {
        val linkText = stringResource(id = R.string.about_link_privacy_policy)
        append(linkText)

        addStyle(
            style = SpanStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSecondary
            ), start = 0, end = linkText.length)
        addStyle(
            style = ParagraphStyle(lineHeight = 30.sp), start = 0, end = linkText.length
        )
        addStyle(
            style = SpanStyle(
                color = BlueLink,
                textDecoration = TextDecoration.Underline,
            ),start = 0, end = 4
        )

        addStringAnnotation(
            tag = "URL",
            annotation = "https://jsdisco.dev/musichords/privacypolicy.html",
            start = 0,
            end = 4
        )
    }
    val uriHandler = LocalUriHandler.current

    val about1 = stringResource(id = R.string.about_1)
    val about2 = stringResource(id = R.string.about_2)
    val about3 = stringResource(id = R.string.about_3)
    val about4 = stringResource(id = R.string.about_4)
    val about5 = stringResource(id = R.string.about_5)
    val about6 = stringResource(id = R.string.about_6)
    val about7 = stringResource(id = R.string.about_7)
    val about8 = stringResource(id = R.string.about_8)



    val title = stringResource(id = R.string.title_about)
    LaunchedEffect(Unit) {
        onSetAppBarTitle(title)
        onSetShowBackBtn(false)
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp)
        .padding(top = 10.dp, bottom = 65.dp)
        .verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = about1,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = about2,
            style = MaterialTheme.typography.h2,
            color = BlueUI
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = about3,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = about4,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = about5,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = about6,
            style = MaterialTheme.typography.h2,
            color = BlueUI
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = about7,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(30.dp))
        Divider()
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = about8,
            style = MaterialTheme.typography.h2,
            color = BlueUI
        )
        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = annotatedStr,
            onClick = {
                annotatedStr.getStringAnnotations("URL", it, it)
                    .firstOrNull()?.let {stringAnnotation ->
                        uriHandler.openUri(stringAnnotation.item)
                    }
            })
        Spacer(modifier = Modifier.height(20.dp))
    }
}