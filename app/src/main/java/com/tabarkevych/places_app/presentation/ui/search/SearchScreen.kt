package com.tabarkevych.places_app.presentation.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.extensions.activityViewModel
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.navigation.NavRouteDestination
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.theme.Salomie
import com.tabarkevych.places_app.presentation.ui.map.MapViewModel
import com.tabarkevych.places_app.presentation.ui.map.components.PlacesAppSearchTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreenRoute(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel(),
    mapViewModel: MapViewModel = activityViewModel()
) {
    val searchUiState =
        viewModel.searchUiState.collectAsStateWithLifecycle(SearchViewModel.SearchUiState())

    LaunchedEffect(Unit) {
        viewModel.showSearchResultOnMapEvent.collectLatest {
            mapViewModel.zoomToPlaceAction(it)
            navController.popBackStack(NavRouteDestination.MapScreen.route, false)
        }
    }
    SearchScreen(
        navController,
        searchUiState,
        onSearchValueChanged = { viewModel.fetchSearchResults(it) },
        onSearchResultClick = {
            viewModel.fetchPlaceDetails(it)
        })
}


@Composable
fun SearchScreen(
    navController: NavController,
    searchUiState: State<SearchViewModel.SearchUiState>,
    onSearchValueChanged: ((String) -> Unit),
    onSearchResultClick: ((String) -> Unit)
) {
    val inputValueState = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Salomie)
    ) {
        PlacesAppSearchTextField(
            focusRequester,
            inputEnabled = true,
            iconVector = Icons.Default.ArrowBack,
            onIconClick = {

            },
            onFieldClick = {},
            inputValueState = inputValueState,
            onValueChanged = {
                onSearchValueChanged.invoke(it)
            }
        )

        if (inputValueState.value.isEmpty()) {
            Text(text = "Recents", modifier = Modifier.padding(16.dp))

            Divider(color = Color.LightGray)

            if (searchUiState.value.searchHistory.isEmpty()) {
                Column(
                    Modifier
                        .padding(20.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Image(
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.CenterHorizontally),
                        painter = painterResource(id = R.drawable.ic_markers),
                        contentDescription = ""
                    )
                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = "You haven't searched yet",
                        fontSize = 24.sp
                    )
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = searchUiState.value.searchResults, key = { it.id }) {
                    Column(modifier = Modifier.clickable {
                        onSearchResultClick.invoke(it.id)
                    }) {
                        Row {
                            Image(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .align(Alignment.CenterVertically),
                                imageVector = Icons.Default.Search,
                                contentDescription = ""
                            )
                            Column {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = it.title
                                )
                                Row {
                                    Text(
                                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                                        text = it.getUiDistance() + " · "
                                    )
                                    Text(
                                        modifier = Modifier.padding(bottom = 8.dp),
                                        text = it.description
                                    )
                                }

                            }
                        }
                        Divider(color = Color.LightGray)
                    }
                }
            }
        }
    }
}

@DevicePreviews
@Composable
private fun LoadingPlaceHolderPreview() {
    PlacesAppTheme {
        SearchScreen(
            rememberNavController(),
            remember { mutableStateOf(SearchViewModel.SearchUiState()) },
            {}, {}
        )
    }
}
