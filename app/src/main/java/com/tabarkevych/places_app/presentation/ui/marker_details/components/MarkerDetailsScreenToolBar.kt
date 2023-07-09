package com.tabarkevych.places_app.presentation.ui.marker_details.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.presentation.theme.BayLeaf
import com.tabarkevych.places_app.presentation.theme.Mirage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkerDetailsScreenToolBar(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onRemoveClick: () -> Unit
) {

    val dopDownExpanded = remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(text = "Info")
        },
        navigationIcon = {
            IconButton(onClick = {
                onBackClick()
            }) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back_24dp), contentDescription = "Back btn")
            }
        },
        actions = {
            IconButton(onClick = {
                dopDownExpanded.value = true
            }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Menu btn")
            }
            DropdownMenu(
                expanded = dopDownExpanded.value,
                onDismissRequest = {
                    dopDownExpanded.value = false
                }
            ) {
                DropdownMenuItem(
                    onClick = {
                        dopDownExpanded.value = false
                        onEditClick()
                    },
                    text = { Text(text = "Edit") }
                )

                DropdownMenuItem(
                    onClick = {
                        dopDownExpanded.value = false
                        onRemoveClick()
                    },
                    text = { Text(text = "Remove") }
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = BayLeaf, titleContentColor = Mirage),
    )

}