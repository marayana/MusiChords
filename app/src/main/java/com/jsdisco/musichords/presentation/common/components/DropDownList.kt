package com.jsdisco.musichords.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jsdisco.musichords.R


@Composable
fun DropDownList(
    isOpen: Boolean = false,
    list: List<String>,
    handleDropDown: (Boolean) -> Unit,
    selectOption: (String) -> Unit,
    selectedOption: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
            .padding(top = 10.dp)
            .background(MaterialTheme.colors.surface)
            .clickable(onClick = { handleDropDown(true) })
    ) {
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)){

            val (label, iconView) = createRefs()

            Text(text = selectedOption,
                modifier = Modifier
                .fillMaxWidth()
                .constrainAs(label) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(iconView.start)
                    width = Dimension.fillToConstraints
                })

            val displayIcon: Painter = painterResource(
                id = R.drawable.ic_arrow_dropdown
            )

            Icon(
                painter = displayIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp, 20.dp)
                    .constrainAs(iconView) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                tint = MaterialTheme.colors.onSurface
            )

            DropdownMenu(
                expanded = isOpen,
                onDismissRequest = { handleDropDown(false)},
                modifier = Modifier
                    .width(150.dp)
            ) {
                list.forEach{ item ->
                    DropdownMenuItem(onClick = {
                        selectOption(item)
                        handleDropDown(false)
                    }) {
                        Text(text = item)
                    }
                }
            }
        }

    }
}