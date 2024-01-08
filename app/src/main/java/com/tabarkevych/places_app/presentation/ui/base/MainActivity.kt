package com.tabarkevych.places_app.presentation.ui.base

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.runtime.SideEffect
import androidx.navigation.compose.rememberNavController
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.tabarkevych.places_app.presentation.navigation.NavRouteDestination
import com.tabarkevych.places_app.presentation.theme.BayLeaf
import com.tabarkevych.places_app.presentation.theme.PlacesAppTheme
import com.tabarkevych.places_app.presentation.ui.base.components.PlacesAppNavHost
import com.tabarkevych.places_app.presentation.ui.base.components.BottomBar
import com.tabarkevych.places_app.presentation.ui.base.components.HandleActivityEvent
import com.tabarkevych.places_app.presentation.ui.sign_in.GoogleAuthUiClient
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val googleAuthUiClient by lazy { GoogleAuthUiClient(context = applicationContext) }

    private val signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
        val response = it.idpResponse
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                if (response == null) {
                    Log.e("MainActivity", "Sign in failed. response = $response")
                } else {
                    Log.d("MainActivity", "Sign in success. response = $response")
                }
            }
            else -> if (response != null) {
                Log.e("MainActivity", "Result Error. response = $response")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlacesAppTheme {

                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = BayLeaf,
                        darkIcons = false
                    )
                }

                val navController = rememberNavController()
                Scaffold(bottomBar = { BottomBar(navController) }) { padding ->

                    PlacesAppNavHost(
                        navController = navController,
                        startDestination = NavRouteDestination.MapScreen.route,
                        padding
                    ) { event ->

                        when (event) {
                            is HandleActivityEvent.UserSignIn -> googleAuthUiClient.showAuth(
                                signInLauncher
                            )
                        }
                    }
                }
            }
        }
    }


}


