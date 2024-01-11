package com.tabarkevych.places_app.presentation.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.domain.model.SearchResult
import com.tabarkevych.places_app.presentation.extensions.activityViewModel
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
            navController.popBackStack(NavRouteDestination.Map.route, false)
        }
    }
    SearchScreen(
        navController,
        searchUiState,
        onSearchValueChanged = { viewModel.fetchSearchResults(it) },
        onSearchResultClick = {
            viewModel.fetchPlaceDetails(it)
        },
        onRemoveSearchHistoryClick = {
            viewModel.removeSearchHistory()
        })
}


@Composable
fun SearchScreen(
    navController: NavController,
    searchUiState: State<SearchViewModel.SearchUiState>,
    onSearchValueChanged: ((String) -> Unit),
    onSearchResultClick: ((SearchResult) -> Unit),
    onRemoveSearchHistoryClick: () -> Unit
) {
    val inputValueState = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        PlacesAppSearchTextField(
            focusRequester,
            inputEnabled = true,
            iconVector = Icons.Default.ArrowBack,
            onIconClick = { navController.navigateUp() },
            onFieldClick = {},
            inputValueState = inputValueState,
            onValueChanged = {
                onSearchValueChanged.invoke(it)
            }
        )

        if (inputValueState.value.isEmpty()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.search_history_title),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                )
                if (searchUiState.value.searchHistory.isNotEmpty()) {
                    Image(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                onRemoveSearchHistoryClick.invoke()
                            },
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete history",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                }
            }

            Divider(color = Color.LightGray)

            if (searchUiState.value.searchHistory.isEmpty()) {
                Column(
                    Modifier
                        .padding(top = 40.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Image(
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.CenterHorizontally),
                        painter = painterResource(id = R.drawable.ic_markers),
                        contentDescription = "empty search history",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = "You haven't searched yet",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 24.sp
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(items = searchUiState.value.searchHistory, key = { it.timestamp }) {
                        Column(modifier = Modifier.clickable {
                            onSearchResultClick.invoke(
                                SearchResult(
                                    it.id,
                                    it.title,
                                    it.subtitle,
                                    0
                                )
                            )
                        }) {
                            Row {
                                Image(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .align(Alignment.CenterVertically),
                                    painter = painterResource(id = R.drawable.ic_markers),
                                    contentDescription = "",
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                )
                                Column {
                                    Text(
                                        modifier = Modifier.padding(8.dp),
                                        text = it.title,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                                        text = it.subtitle,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                }
                            }
                            Divider(color = Color.LightGray)
                        }
                    }
                }

            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = searchUiState.value.searchResults, key = { it.id }) {
                    Column(modifier = Modifier.clickable {
                        onSearchResultClick.invoke(it)
                    }) {
                        Row {
                            Image(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .align(Alignment.CenterVertically),
                                imageVector = Icons.Default.Search,
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                            Column {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = it.title,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Row {
                                    Text(
                                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                                        text = it.getUiDistance() + " · ",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        modifier = Modifier.padding(bottom = 8.dp),
                                        text = it.description,
                                        color = MaterialTheme.colorScheme.primary
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
            {}, {},{}
        )
    }
}
