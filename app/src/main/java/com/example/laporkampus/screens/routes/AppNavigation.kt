package com.example.laporkampus.screens.routes

import android.content.pm.LauncherApps
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.laporkampus.datas.enums.PagesEnum
import com.example.laporkampus.screens.uistates.AuthenticationUiStatus
import com.example.laporkampus.screens.views.AdminDashboardScreen
import com.example.laporkampus.screens.views.RegisterView
import com.example.laporkampus.screens.views.UserDashboardScreen
import com.example.laporkampus.screens.viewsmodels.AuthenticationViewModel
import com.example.todolistapp.views.LoginView

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val authViewModel: AuthenticationViewModel = viewModel(factory = AuthenticationViewModel.Factory)
    val status = authViewModel.authenticationUiStatus

    LaunchedEffect(status) {
        if (status is AuthenticationUiStatus.Success) {
            when(status.userData.user?.role) {
                "STAFF" -> {
                    navController.navigate(PagesEnum.MahasiswaGraph.name) {
                        popUpTo(PagesEnum.AuthGraph.name) { inclusive = true }
                    }
                }
                "MAHASISWA" -> {
                    navController.navigate(PagesEnum.MahasiswaGraph.name) {
                        popUpTo(PagesEnum.AuthGraph.name) { inclusive = true }
                    }
                }
            }
        }
    }

    // Authentication Routes
    NavHost(navController = navController, startDestination = PagesEnum.AuthGraph.name, modifier = modifier) {
        navigation(
            startDestination = PagesEnum.Login.name,
            route = PagesEnum.AuthGraph.name
        ) {
            composable(route = PagesEnum.Login.name) {
                LoginView(authViewModel, navController = navController)
            }
            composable(route = PagesEnum.Register.name) {
                RegisterView(authViewModel, navController = navController, context = context)
            }
        }

        navigation(
            startDestination = PagesEnum.UserDashboard.name,
            route = PagesEnum.MahasiswaGraph.name
        ) {
            composable(route = PagesEnum.UserDashboard.name) {
                val userData = (status as? AuthenticationUiStatus.Success)?.userData?.user
                if (userData != null) {
                    UserDashboardScreen(
                        user = userData,
                        onLogout = {
                            authViewModel.resetViewModel()
                            navController.navigate(PagesEnum.AuthGraph.name) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }
            // Add more routes from this
        }

        navigation(
            startDestination = PagesEnum.AdminDashboard.name,
            route = PagesEnum.StaffGraph.name
        ) {
            composable(route = PagesEnum.AdminDashboard.name) {
                val userData = (status as? AuthenticationUiStatus.Success)?.userData?.user
                if(userData != null) {
                    AdminDashboardScreen(
                        user = userData,
                        onLogout = {
                            authViewModel.resetViewModel()
                            navController.navigate(PagesEnum.AuthGraph.name) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}