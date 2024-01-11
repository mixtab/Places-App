package com.tabarkevych.places_app.presentation.ui.marker_details.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            Text(
                text = stringResource(id = R.string.marker_info_title),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                onBackClick()
            }) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "arrow back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
            IconButton(onClick = {
                dopDownExpanded.value = true
            }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Menu btn", tint = MaterialTheme.colorScheme.primary)
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
                    text = { Text(text = "Edit", color = MaterialTheme.colorScheme.primary) }
                )

                DropdownMenuItem(
                    onClick = {
                        dopDownExpanded.value = false
                        onRemoveClick()
                    },
                    text = { Text(text = "Remove", color = MaterialTheme.colorScheme.primary) }
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
    )

}