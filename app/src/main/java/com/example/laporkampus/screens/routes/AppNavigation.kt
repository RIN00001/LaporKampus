package com.example.laporkampus.screens.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.laporkampus.datas.enums.PagesEnum
import com.example.laporkampus.frontend.model.ui.UserProfileUiModel
import com.example.laporkampus.frontend.model.ui.UserRole
import com.example.laporkampus.frontend.view.EditProfileScreen
import com.example.laporkampus.frontend.view.LearningMaterialsScreen
import com.example.laporkampus.frontend.view.ProfileScreen
import com.example.laporkampus.frontend.view.SavedMaterialsScreen
import com.example.laporkampus.frontend.viewmodel.ProfileGateway
import com.example.laporkampus.screens.uistates.AuthenticationUiStatus
import com.example.laporkampus.screens.viewmodels.AuthenticationViewModel
import com.example.laporkampus.screens.viewmodels.ReportUserViewModel
import com.example.laporkampus.screens.viewmodels.ReportStaffViewModel
import com.example.laporkampus.screens.views.DashboardStaffView
import com.example.laporkampus.screens.views.LoginView
import com.example.laporkampus.screens.views.MakeReportView
import com.example.laporkampus.screens.views.RegisterView
import com.example.laporkampus.screens.views.ReportDetailScreen
import com.example.laporkampus.screens.views.ReportListScreen
import com.example.laporkampus.screens.views.StaffReportDetailView
import com.example.laporkampus.screens.views.UserDashboardScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val authViewModel: AuthenticationViewModel = viewModel(factory = AuthenticationViewModel.Factory)
    val status = authViewModel.authenticationUiStatus
    val userData = (status as? AuthenticationUiStatus.Success)?.userData?.user
    val profileGateway = remember(status) {
        object : ProfileGateway {
            override suspend fun getProfile(): Result<UserProfileUiModel> {
                val currentUser = (status as? AuthenticationUiStatus.Success)?.userData?.user
                    ?: return Result.failure(IllegalStateException("No active user session"))
                return Result.success(
                    UserProfileUiModel(
                        id = currentUser.id.toString(),
                        name = currentUser.name,
                        email = currentUser.email,
                        role = if (currentUser.role == "STAFF") UserRole.STAFF else UserRole.STUDENT,
                        profileImageUrl = null
                    )
                )
            }

            override suspend fun updateProfile(name: String, profileImageUrl: String?): Result<UserProfileUiModel> {
                val currentUser = (status as? AuthenticationUiStatus.Success)?.userData?.user
                    ?: return Result.failure(IllegalStateException("No active user session"))
                return Result.success(
                    UserProfileUiModel(
                        id = currentUser.id.toString(),
                        name = name,
                        email = currentUser.email,
                        role = if (currentUser.role == "STAFF") UserRole.STAFF else UserRole.STUDENT,
                        profileImageUrl = profileImageUrl
                    )
                )
            }

            override suspend fun logout(): Result<Unit> {
                return Result.success(Unit)
            }
        }
    }

    LaunchedEffect(status) {
        if (status is AuthenticationUiStatus.Success) {
            when(status.userData.user.role) {
                "STAFF" -> {
                    navController.navigate(PagesEnum.StaffGraph.name) {
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

    NavHost(navController = navController, startDestination = PagesEnum.AuthGraph.name, modifier = modifier) {

        // Authentication Routes
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

        // Mahasiswa Routes
        navigation(
            startDestination = PagesEnum.UserDashboard.name,
            route = PagesEnum.MahasiswaGraph.name
        ) {
            composable(route = PagesEnum.UserDashboard.name) {
                if (userData != null) {
                    UserDashboardScreen(
                        user = userData,
                        onNavigateToMyReports = {
                            navController.navigate(PagesEnum.ReportList.name)
                        },
                        onNavigateToDetail = { reportId ->
                            navController.navigate("${PagesEnum.ReportDetail.name}/$reportId")
                        },
                        onLogout = {
                            authViewModel.resetViewModel()
                            navController.navigate(PagesEnum.AuthGraph.name) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onNavigateToCreateReport = {
                            navController.navigate(PagesEnum.CreateReport.name)
                        },
                        onNavigateToProfile = {
                            navController.navigate(PagesEnum.Profile.name)
                        },
                        onNavigateToLearningMaterials = {
                            navController.navigate(PagesEnum.LearningMaterials.name)
                        },
                        onNavigateToSavedMaterials = {
                            navController.navigate(PagesEnum.SavedMaterials.name)
                        }
                    )
                }
            }

            composable(route = PagesEnum.Profile.name) {
                ProfileScreen(
                    onNavigateToEditProfile = {
                        navController.navigate(PagesEnum.EditProfile.name)
                    },
                    onNavigateToLogin = {
                        authViewModel.resetViewModel()
                        navController.navigate(PagesEnum.AuthGraph.name) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    viewModel = viewModel(
                        key = "profile_vm",
                        factory = com.example.laporkampus.frontend.viewmodel.ProfileViewModelFactory(profileGateway)
                    )
                )
            }

            composable(route = PagesEnum.EditProfile.name) {
                EditProfileScreen(
                    currentName = userData?.name.orEmpty(),
                    currentImageUrl = null,
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = viewModel(
                        key = "edit_profile_vm",
                        factory = com.example.laporkampus.frontend.viewmodel.EditProfileViewModelFactory(profileGateway)
                    )
                )
            }

            // Report List Route
            composable(route = PagesEnum.ReportList.name) {
                val reportViewModel: ReportUserViewModel = viewModel(factory = ReportUserViewModel.Factory)
                // FIX: Mengambil userData dari status login aktif untuk dikirim ke List Screen
                val userData = (status as? AuthenticationUiStatus.Success)?.userData?.user

                ReportListScreen(
                    user = userData, // Teruskan ke parameter screen
                    viewModel = reportViewModel,
                    onReportClick = { reportId ->
                        navController.navigate("${PagesEnum.ReportDetail.name}/$reportId")
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // Report Detail Route
            composable(
                route = "${PagesEnum.ReportDetail.name}/{reportId}",
                arguments = listOf(navArgument("reportId") { type = NavType.IntType })
            ) { backStackEntry ->
                val reportId = backStackEntry.arguments?.getInt("reportId") ?: 0
                val reportViewModel: ReportUserViewModel = viewModel(factory = ReportUserViewModel.Factory)

                ReportDetailScreen(
                    reportId = reportId,
                    viewModel = reportViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // Create Report Route (Telah Diperbarui)
            composable(route = PagesEnum.CreateReport.name) {
                val reportViewModel: ReportUserViewModel = viewModel(factory = ReportUserViewModel.Factory)

                MakeReportView(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    viewModel = reportViewModel
                )
            }
        }

        // Staff Routes
        navigation(
            startDestination = PagesEnum.StaffDashboard.name,
            route = PagesEnum.StaffGraph.name
        ) {
            composable(route = PagesEnum.StaffDashboard.name) {
                if (userData != null) {
                    DashboardStaffView(
                        user = userData,
                        onLogout = {
                            authViewModel.resetViewModel()
                            navController.navigate(PagesEnum.AuthGraph.name) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onReportClick = { reportId ->
                            navController.navigate("${PagesEnum.StaffReportDetail.name}/$reportId")
                        }
                    )
                }
            }

            // Staff Report Detail Route
            composable(
                route = "${PagesEnum.StaffReportDetail.name}/{reportId}",
                arguments = listOf(navArgument("reportId") { type = NavType.IntType })
            ) { backStackEntry ->
                val reportId = backStackEntry.arguments?.getInt("reportId") ?: 0
                val staffViewModel: ReportStaffViewModel =
                    viewModel(factory = ReportStaffViewModel.Factory)

                StaffReportDetailView(
                    reportId = reportId,
                    viewModel = staffViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onValidateSuccess = {
                        staffViewModel.clearStatus()
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}