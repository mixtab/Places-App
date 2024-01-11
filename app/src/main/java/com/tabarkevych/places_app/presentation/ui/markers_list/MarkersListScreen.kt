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
import com.google.android.gms.maps.model.LatLng
import com.tabarkevych.places_app.R
import com.tabarkevych.places_app.presentation.extensions.activityViewModel
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.model.MarkerUi
import com.tabarkevych.places_app.presentation.navigation.NavRouteDestination
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.theme.Salomie
import com.tabarkevych.places_app.presentation.ui.markers_list.components.MarkersListEmptyPlaceholder
import com.tabarkevych.places_app.presentation.ui.markers_list.components.MarkersListScreenLargeMarkerCard
import com.tabarkevych.places_app.presentation.ui.markers_list.components.MarkersListScreenMarkerCard
import com.tabarkevych.places_app.presentation.ui.markers_list.components.MarkersListSignIn
import com.tabarkevych.places_app.presentation.ui.base.components.LoadingPlaceHolder
import com.tabarkevych.places_app.presentation.ui.base.components.PrimaryFloatingActionButton
import com.tabarkevych.places_app.presentation.ui.map.MapViewModel
import com.tabarkevych.places_app.presentation.ui.settings.BackgroundColorType
import com.tabarkevych.places_app.presentation.ui.settings.TextColorType
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun MarkersListScreenRoute(
    navController: NavController,
    onShowSignIn: () -> Unit,
    viewModel: MarkersListViewModel = hiltViewModel(),
    mapViewModel: MapViewModel = activityViewModel()
) {
    val markers: LazyPagingItems<MarkerUi> = viewModel.markersState.collectAsLazyPagingItems()

    val markerPreviewState = viewModel.markerPreviewState.collectAsStateWithLifecycle()

    val isUserSignIn = viewModel.isUserSignInState.collectAsStateWithLifecycle(false)

    val bgColorState = viewModel.markersListBackgroundColor.collectAsStateWithLifecycle()

    val textColorState = viewModel.markersListTextColor.collectAsStateWithLifecycle()

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
        },
        onBuildRouteClick = {
            mapViewModel.createRouteToLocation(it)
        },
        bgColorState,
        textColorState
    )
}


@Composable
fun MarkersListScreen(
    markers: LazyPagingItems<MarkerUi>,
    markerPreviewState: State<MarkersListViewModel.MarkerPreview>,
    isUserSignIn: State<Boolean>,
    navController: NavController,
    onSignInClick: () -> Unit,
    onUpdatePreviewClick: () -> Unit,
    onBuildRouteClick: (LatLng) -> Unit,
    bgColorState: State<Color>,
    textColorState: State<Color>
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColorState.value)
    ) {
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

                is LoadState.Error -> {
                    MarkersListEmptyPlaceholder(
                        Modifier
                            .padding(horizontal = 16.dp)
                            .align(Alignment.Center)
                    ) {
                        navController.navigateUp()
                    }
                }

                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items = markers) { marker ->
                            marker?.let { it ->
                                when (markerPreviewState.value) {
                                    MarkersListViewModel.MarkerPreview.SmallPreview -> MarkersListScreenLargeMarkerCard(
                                        it,
                                        textColorState,
                                        onMarkerClick = {
                                            navController.navigate(NavRouteDestination.MarkerDetails.route + "/${marker.timestamp}")
                                        },
                                        onBuildRouteCLick = {
                                            onBuildRouteClick.invoke(it)
                                            navController.popBackStack(
                                                NavRouteDestination.Map.route,
                                                false
                                            )
                                        }
                                    )

                                    else -> MarkersListScreenMarkerCard(it, textColorState,
                                        onMarkerClick = {
                                            navController.navigate(NavRouteDestination.MarkerDetails.route + "/${marker.timestamp}")
                                        },
                                        onBuildRouteCLick = {
                                            onBuildRouteClick.invoke(it)
                                            navController.popBackStack(
                                                NavRouteDestination.Map.route,
                                                false
                                            )
                                        })
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
            {},
            {},
            remember {
                mutableStateOf(Color(BackgroundColorType.SALOMIE.color))
            },
            remember {
                mutableStateOf(Color(TextColorType.MIRAGE.color))
            }
        )
    }
}

