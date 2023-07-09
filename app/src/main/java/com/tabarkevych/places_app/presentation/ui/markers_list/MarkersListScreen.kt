package com.tabarkevych.places_app.presentation.ui.markers_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.model.MarkerUi
import com.tabarkevych.places_app.presentation.navigation.NavDestinationHelper
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.theme.Salomie
import com.tabarkevych.places_app.presentation.ui.markers_list.components.MarkersListEmptyPlaceholder
import com.tabarkevych.places_app.presentation.ui.markers_list.components.MarkersListScreenLargeMarkerCard
import com.tabarkevych.places_app.presentation.ui.markers_list.components.MarkersListScreenMarkerCard
import com.tabarkevych.places_app.presentation.ui.markers_list.components.MarkersListSignIn
import com.tabarkevych.places_app.presentation.ui.root.components.LoadingPlaceHolder
import com.tabarkevych.places_app.presentation.ui.root.components.PrimaryFloatingActionButton
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun MarkersListScreenRoute(
    navController: NavController,
    onShowSignIn: () -> Unit,
    viewModel: MarkersListViewModel = hiltViewModel()
) {
    val markers: LazyPagingItems<MarkerUi> = viewModel.markersState.collectAsLazyPagingItems()

    val markerPreviewState = viewModel.markerPreviewState.collectAsStateWithLifecycle()

    val isUserSignIn = viewModel.isUserSignInState.collectAsStateWithLifecycle(false)

    MarkersListScreen(
        markers,
        markerPreviewState,
        isUserSignIn,
        navController,
        onSignInClick = {
            onShowSignIn()
        },
        onUpdatePreviewClick = {
            viewModel.updateMarkersPreview()
        })
}


@Composable
fun MarkersListScreen(
    markers: LazyPagingItems<MarkerUi>,
    markerPreviewState: State<MarkersListViewModel.MarkerPreview>,
    isUserSignIn: State<Boolean>,
    navController: NavController,
    onSignInClick: () -> Unit,
    onUpdatePreviewClick: () -> Unit,
) {

    Box(modifier = Modifier.fillMaxSize().background(Salomie)) {
        if (!isUserSignIn.value) {
            MarkersListSignIn(
                Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Center)
            ) { onSignInClick() }

        } else {

            when (markers.loadState.refresh) {
                LoadState.Loading -> {
                    LoadingPlaceHolder()
                }

                is LoadState.Error -> {}

                else -> {
                    if (markers.itemSnapshotList.isEmpty()) {
                        MarkersListEmptyPlaceholder{
                            navController.navigateUp()
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(items = markers) { marker ->
                                marker?.let {
                                    when (markerPreviewState.value) {
                                        MarkersListViewModel.MarkerPreview.SmallPreview -> MarkersListScreenLargeMarkerCard(
                                            it
                                        ) {
                                            navController.navigate(NavDestinationHelper.MarkerDetailsScreen.route + "/${marker.timestamp}")
                                        }

                                        else -> MarkersListScreenMarkerCard(it) {
                                            navController.navigate(NavDestinationHelper.MarkerDetailsScreen.route + "/${marker.timestamp}")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomEnd)
                    .background(Color.Transparent)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {

                PrimaryFloatingActionButton(
                    Modifier.padding(10.dp),
                    if (markerPreviewState.value == MarkersListViewModel.MarkerPreview.SmallPreview)
                        painterResource(R.drawable.ic_horizontal_grid_24dp)
                    else
                        painterResource(R.drawable.ic_box_24dp)
                ) {
                    onUpdatePreviewClick()
                }

            }
        }
    }
}

@DevicePreviews
@Composable
private fun LoadingPlaceHolderPreview() {
    PlacesAppTheme {
        MarkersListScreen(
            MutableStateFlow(PagingData.empty<MarkerUi>()).collectAsLazyPagingItems(),
            remember { mutableStateOf(MarkersListViewModel.MarkerPreview.SmallPreview) },
            remember { mutableStateOf(false) },
            rememberNavController(),
            {},
            {})
    }
}

