package com.tabarkevych.places_app.presentation.ui.settings.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.model.SettingsDropDownItem
import com.tabarkevych.places_app.presentation.theme.Mirage
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SettingsOutlinedDropDown(
    modifier: Modifier,
    textColor: Color,
    label: String,
    isDropDownExpanded: MutableState<Boolean>,
    selectedValue: State<SettingsDropDownItem>,
    dropDownItems: List<SettingsDropDownItem>,
    leadingIcon: @Composable (() -> Unit)? = null,
    onItemClick: (SettingsDropDownItem) -> Unit
) {


    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isDropDownExpanded.value,
        onExpandedChange = { isDropDownExpanded.value = it }) {
        BasicTextField(
            modifier = modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = selectedValue.value.title,
            onValueChange = {

            },
            readOnly = true,
            textStyle = LocalTextStyle.current.copy(color = textColor)
        ) {

            TextFieldDefaults.OutlinedTextFieldDecorationBox(

                value = selectedValue.value.title,
                visualTransformation = VisualTransformation.None,
                innerTextField = it,
                singleLine = true,
                enabled = true,
                label = { Text(text = label) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropDownExpanded.value)
                },
                leadingIcon = {
                    leadingIcon?.invoke()
                },
                interactionSource = MutableInteractionSource(),
                // keep vertical paddings but change the horizontal
                contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                    start = 8.dp, end = 8.dp
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = textColor,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

        }
        ExposedDropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = { isDropDownExpanded.value = false }) {
            dropDownItems.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.title) },
                    onClick = {
                        isDropDownExpanded.value = false
                        onItemClick.invoke(it)
                    },
                    colors = MenuDefaults.itemColors(textColor = MaterialTheme.colorScheme.primary)
                )

            }
        }
    }

}


@DevicePreviews
@Composable
private fun SettingsOutlinedDropDownPreview() {
    PlacesAppTheme {
        SettingsOutlinedDropDown(
            modifier = Modifier,
            Mirage,
            "text color",
            remember { mutableStateOf(false) },
            remember { mutableStateOf(SettingsDropDownItem("test","test")) },
            listOf(SettingsDropDownItem("test","test"),SettingsDropDownItem("test","test")),
            {
                Icon(
                    painter = painterResource(id = R.drawable.ic_markers),
                    contentDescription = "",
                    tint = Color(0xFFFF9EBA)
                )
            }
        ) {}
    }
}
