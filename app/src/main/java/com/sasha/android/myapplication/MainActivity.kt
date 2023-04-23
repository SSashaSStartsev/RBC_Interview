package com.sasha.android.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sasha.android.myapplication.ui.theme.RBC_InterviewTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RBC_InterviewTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppNavHost()
                }
            }
        }
    }
}


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "weather_forecast"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("weather_forecast") {
            WeatherForecastAdapter(onNavigateToDetails = {
                navController.navigate("weather_details/${it}")
            })
        }
        composable("weather_details/{id}", arguments = listOf(navArgument("id") {
            type = NavType.IntType
        })) {
            WeatherDetailsAdapter(
                onBackClick = {navController.navigateUp()}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RBC_InterviewTheme {
        AppNavHost()
    }
}