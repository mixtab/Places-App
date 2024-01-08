package com.tabarkevych.places_app.presentation.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import com.tabarkevych.places_app.domain.model.RouteInfo
import com.tabarkevych.places_app.extensions.activityViewModel
import com.tabarkevych.places_app.presentation.DevicePreviews
import com.tabarkevych.places_app.presentation.model.MarkerUi
import com.tabarkevych.places_app.presentation.navigation.NavRouteDestination
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.ui.map.components.AddMarkerDialog
import com.tabarkevych.places_app.presentation.ui.base.components.LoadingPlaceHolder
import com.tabarkevych.places_app.presentation.ui.map.components.BasePlacesAppMap
import com.tabarkevych.places_app.presentation.ui.map.components.MapBottomSheetScaffold
import com.tabarkevych.places_app.presentation.ui.map.components.PlacesAppSearchTextField
import kotlinx.coroutines.launch

@Composable
internal fun MapScreenRoute(
    navController: NavController,
    onShowSignIn: () -> Unit,
    viewModel: MapViewModel = activityViewModel()
) {

    val allMarkerState = viewModel.markersState.collectAsStateWithLifecycle(mutableListOf())
    val userLocationState = viewModel.userLocationState.collectAsStateWithLifecycle()
    val isUserSignInState = viewModel.isUserSignInState.collectAsStateWithLifecycle(false)
    val showLoadingState = viewModel.showLoadingState.collectAsStateWithLifecycle(false)

    val routeToDestinationState =
        viewModel.routeDestinationState.collectAsStateWithLifecycle(null)

    val zoomToPlaceEvent = viewModel.zoomToPlaceEvent.collectAsStateWithLifecycle(null)


    MapScreen(navController,
        allMarkerState,
        userLocationState,
        isUserSignInState,
        showLoadingState,
        routeToDestinationState,
        zoomToPlaceEvent,
        onShowSignIn = {
            onShowSignIn()
        },
        onGetUserLocation = {
            viewModel.getUserLocation()
        },
        onAddMarker = { latLng, images, title, description ->
            viewModel.addMarker(latLng, images, title, description)
        },
        onDismissRoute = {
            viewModel.removeRoute()
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun MapScreen(
    navController: NavController,
    markerState: State<List<MarkerUi>>,
    userLocationState: State<Location?>,
    isUserSignIn: State<Boolean>,
    showLoading: State<Boolean>,
    routeState: State<RouteInfo?>,
    zoomToPlaceEvent: State<LatLng?>,
    onShowSignIn: () -> Unit,
    onGetUserLocation: () -> Unit,
    onAddMarker: (LatLng, List<Uri>?, String, String) -> Unit,
    onDismissRoute: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onGetUserLocation()
            }
        })

    fun checkLocationPermission() = when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) -> onGetUserLocation()

        else -> locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }


    val markerLatLng = remember { mutableStateOf<LatLng?>(null) }

    SideEffect {
        checkLocationPermission()
    }
    val bottomSheetState = rememberBottomSheetScaffoldState()

    Box(modifier = Modifier.fillMaxSize()) {

        Surface(modifier = Modifier.fillMaxSize()) {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            ModalNavigationDrawer(
                drawerContent = {
                    ModalDrawerSheet {
                        MapNavigationDrawerItems.values().forEach {
                            NavigationDrawerItem(
                                label = { Text(text = stringResource(id = it.title)) },
                                selected = false,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        navController.navigate(it.route)
                                    }
                                })
                        }
                    }

                },
                drawerState = drawerState,
                gesturesEnabled = drawerState.isOpen
            ) {
                val selectedMarker = remember { mutableStateOf<MarkerUi?>(null) }
                MapBottomSheetScaffold(
                    scope,
                    bottomSheetState,
                    selectedMarker
                ) {
                    Scaffold {
                        it.calculateBottomPadding()
                        BasePlacesAppMap(
                            isUserSignIn = isUserSignIn,
                            markerLatLng = markerLatLng,
                            userLocationState = userLocationState,
                            markerState = markerState,
                            routeState = routeState,
                            zoomToPlaceEvent = zoomToPlaceEvent,
                            scope = scope,
                            onShowSignIn = { onShowSignIn() },
                            onMarkerClick = {
                                scope.launch {
                                    selectedMarker.value = it
                                    bottomSheetState.bottomSheetState.expand()
                                }
                                /*  navController.navigate(NavRouteDestination.MarkerDetailsScreen.route + "/${it}")*/
                            },
                            onDismissRoute = { onDismissRoute.invoke() }
                        )

                        PlacesAppSearchTextField(
                            inputEnabled = false,
                            iconVector = Icons.Default.Menu,
                            onIconClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            },
                            onFieldClick = {
                                navController.navigate(NavRouteDestination.SearchScreen.route)
                            }
                        )
                    }
                }
            }

        }

        markerLatLng.value?.let { latLng ->
            AddMarkerDialog(openDialogCustom = markerLatLng) { images, title, description ->
                onAddMarker(latLng, images, title, description)
            }
        }
        if (showLoading.value) {
            LoadingPlaceHolder()
        }

    }
}


@DevicePreviews
@Composable
private fun LoadingPlaceHolderPreview() {
    PlacesAppTheme {
        MapScreen(
            rememberNavController(),
            remember { mutableStateOf(listOf()) },
            remember { mutableStateOf(Location("")) },
            remember { mutableStateOf(false) },
            remember { mutableStateOf(false) },
            remember { mutableStateOf(null) },
            remember { mutableStateOf(LatLng(0.0, 0.0)) },
            {},
            {},
            { _, _, _, _ -> },
            {})
    }
}