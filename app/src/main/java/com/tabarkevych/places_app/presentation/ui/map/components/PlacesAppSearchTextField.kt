package com.tabarkevych.places_app.presentation.ui.map.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.theme.Mirage
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesAppSearchTextField(
    focusRequester: FocusRequester = FocusRequester(),
    inputEnabled: Boolean,
    iconVector: ImageVector,
    onIconClick: () -> Unit,
    onFieldClick: () -> Unit,
    inputValueState: MutableState<String>? = null,
    onValueChanged: ((String) -> Unit)? = null
) {

    Row {
        OutlinedTextField(
            value = inputValueState?.value ?: "",
            placeholder = { Text(text = "Search here") },
            onValueChange = {
                inputValueState?.value = it
                onValueChanged?.invoke(it)
            },
            leadingIcon = {
                Icon(iconVector, "", Modifier.clickable {
                    onIconClick.invoke()
                })
            },
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier.focusRequester(focusRequester)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .height(56.dp)
                .clickable { onFieldClick.invoke() },
            enabled = inputEnabled,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledBorderColor = Color.Transparent,
                disabledLeadingIconColor = Color.Black,
                disabledTextColor = Color.Black,
                containerColor = Color.White,
                unfocusedBorderColor = Color.White,
                textColor = Mirage,
                placeholderColor = Mirage,
                disabledPlaceholderColor = Mirage,
                cursorColor = Mirage
            )
        )
    }
}


@DevicePreviews
@Composable
private fun PlacesAppSearchTextFieldPreview() {
    PlacesAppTheme {
        PlacesAppSearchTextField(FocusRequester(),false, Icons.Default.Menu, {}, {}, remember { mutableStateOf("") })
    }
}
