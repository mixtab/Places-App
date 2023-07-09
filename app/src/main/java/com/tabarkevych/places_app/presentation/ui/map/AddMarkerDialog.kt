package com.tabarkevych.places_app.presentation.ui.map

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.LatLng
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.presentation.theme.Salomie
import com.tabarkevych.places_app.presentation.ui.root.components.PrimaryButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMarkerDialog(
    openDialogCustom: MutableState<LatLng?>,
    onPhotoAdded: (Uri?, String, String) -> Unit
) {

    var textTitle by rememberSaveable { mutableStateOf("") }
    var textDescription by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(Uri.parse("android.resource://" + context.packageName + "/" + R.drawable.ic_add_image)) }
    var isImageAdded by rememberSaveable { mutableStateOf(false) }

    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            isImageAdded = true
            imageUri = uri
        }
    }
    Dialog(onDismissRequest = { openDialogCustom.value = null }) {
        Card(
         colors=  CardDefaults.cardColors(containerColor = Salomie),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column {
                Text(
                    text = "Add new place",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 20.sp,
                )

                AsyncImage(
                    model = imageUri,
                    modifier = Modifier
                        .padding(top = 35.dp)
                        .height(70.dp)
                        .fillMaxWidth()
                        .clickable { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    contentDescription = "Select Image",
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    value = textTitle,
                    onValueChange = { textTitle = it },
                    label = { Text("*Title", color = Color.Black) },
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    colors = TextFieldDefaults.outlinedTextFieldColors(),
                    maxLines = 1
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    value = textDescription,
                    onValueChange = { textDescription = it },
                    label = { Text("*Description", color = Color.Black) },
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    colors = TextFieldDefaults.outlinedTextFieldColors(),
                    maxLines = 1
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp)
                ) {
                    Text(
                        text = "Cancel",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                openDialogCustom.value = null
                            },
                    )
                    PrimaryButton(modifier = Modifier.weight(1f), text = "Complete") {
                        if (isImageAdded) {
                            onPhotoAdded(imageUri, textTitle, textDescription)
                            openDialogCustom.value = null
                        }
                    }
                }
            }
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@Preview(name = "Custom Dialog")
@Composable
fun AddMarkerDialogPreview() {
    AddMarkerDialog(openDialogCustom = mutableStateOf(null)) { _, _, _ -> }
}