package com.tabarkevych.places_app.domain.manager.routing

import android.content.Context
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import com.tabarkevych.places_app.BuildConfig
import com.tabarkevych.places_app.domain.base.Result
import com.tabarkevych.places_app.domain.errors.Error
import com.tabarkevych.places_app.domain.mapper.toSearchResult
import com.tabarkevych.places_app.domain.model.RouteInfo
import com.tabarkevych.places_app.domain.model.SearchResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RoutingManager @Inject constructor(
    @ApplicationContext val context: Context
) {

    private val placesClient: PlacesClient by lazy {
        Places.initialize(context, BuildConfig.GOOGLE_API_KEY)
        return@lazy Places.createClient(context)
    }

    fun calculateRoute(
        startPosition: LatLng,
        endPosition: LatLng,
        travelMode: TravelMode = TravelMode.DRIVING
    ): Flow<Result<RouteInfo>> = callbackFlow {
        val mGeoApiContext = GeoApiContext.Builder()
            .apiKey(BuildConfig.GOOGLE_API_KEY)
            .connectTimeout(DEFAULT_REQUEST_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_REQUEST_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_REQUEST_TIME_OUT, TimeUnit.SECONDS)
            .build()

        DirectionsApi.newRequest(mGeoApiContext)
            .mode(travelMode)
            .origin(
                com.google.maps.model.LatLng(
                    startPosition.latitude,
                    startPosition.longitude
                )
            )
            .destination(
                com.google.maps.model.LatLng(
                    endPosition.latitude,
                    endPosition.longitude
                )
            ).setCallback(object : PendingResult.Callback<DirectionsResult> {
                override fun onResult(result: DirectionsResult) {
                    val route: MutableList<LatLng> = mutableListOf()
                    result.routes.first().overviewPolyline.decodePath().forEach {
                        route.add(LatLng(it.lat, it.lng))
                    }
                    val routeInfo = RouteInfo(result.routes.first().legs.first().duration,result.routes.first().legs.first().distance,route)


                    trySendBlocking(Result.Success(routeInfo))

                }

                override fun onFailure(e: Throwable) {
                    trySendBlocking(
                        Result.Failure(Error.SomethingWentWrongError())
                    )
                }
            })

        awaitClose { }
    }

    fun fetchPlacesByText(
        searchValue: String,
        userPosition: LatLng,
        searchTypes: List<String> = listOf(PlaceTypes.CITIES),
        countries: List<String> = listOf("UA", "PL")
    ): Flow<Result<List<SearchResult>>> = callbackFlow {


        val token = AutocompleteSessionToken.newInstance()

        val request =
            FindAutocompletePredictionsRequest.builder()
                .setTypesFilter(searchTypes)
                .setOrigin(userPosition)
                .setSessionToken(token)
                .setQuery(searchValue)
                .setCountries(countries)
                .build()
        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                trySend(Result.Success(response.autocompletePredictions.toSearchResult()))
            }.addOnFailureListener { exception: Exception? ->
                trySend(Result.Failure(Error.SomethingWentWrongError()))
            }

        awaitClose()

    }

    fun fetchPlaceInfoById(placeId: String): Flow<Result<LatLng>> = callbackFlow {
        val request =
            FetchPlaceRequest.newInstance(
                placeId,
                listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
            )

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val latLng = response.place.latLng
                if (latLng == null) {
                    trySend(Result.Failure(Error.SomethingWentWrongError()))
                } else {
                    trySend(Result.Success(latLng))
                }
            }.addOnFailureListener {
                trySend(Result.Failure(Error.SomethingWentWrongError()))
            }

        awaitClose()
    }

    companion object {
        const val DEFAULT_REQUEST_TIME_OUT = 30L
    }
}