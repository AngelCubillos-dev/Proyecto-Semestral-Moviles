package com.example.mastermartini.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mastermartini.model.ContentCatalog
import com.example.mastermartini.model.ContentItem
import com.example.mastermartini.model.ContentType
import com.example.mastermartini.ui.components.AppBottomBar
import com.example.mastermartini.ui.screens.auth.LoginScreen
import com.example.mastermartini.ui.screens.auth.RegisterScreen
import com.example.mastermartini.ui.screens.contact.ContactScreen
import com.example.mastermartini.ui.screens.detail.CourseDetailScreen
import com.example.mastermartini.ui.screens.detail.RecipeDetailScreen
import com.example.mastermartini.ui.screens.detail.VideoDetailScreen
import com.example.mastermartini.ui.screens.explore.ExploreScreen
import com.example.mastermartini.ui.screens.favorites.FavoritesScreen
import com.example.mastermartini.ui.screens.home.HomeScreen
import com.example.mastermartini.ui.screens.profile.ProfileScreen
import com.example.mastermartini.viewmodel.AppViewModel
import com.example.mastermartini.viewmodel.ContentViewModel

private fun NavHostController.navigateTopLevel(route: String) {
    navigate(route) {
        popUpTo(Routes.HOME) { inclusive = false }
        launchSingleTop = true
    }
}

private fun NavHostController.openContent(item: ContentItem) {
    val route = when (item.type) {
        ContentType.Recipe -> Routes.recipe(item.id)
        ContentType.Video -> Routes.video(item.id)
        ContentType.Course -> Routes.course(item.id)
    }
    navigate(route) { launchSingleTop = true }
}

@Composable
fun AppNavigation(appViewModel: AppViewModel = viewModel(), contentViewModel: ContentViewModel = viewModel()) {
    val navController = rememberNavController()
    // El destino inicial se calcula una sola vez: si cambiara, NavHost reconstruiría el grafo.
    val startDestination = remember { if (appViewModel.userId.value != null) Routes.HOME else Routes.LOGIN }
    val userId by appViewModel.userId.collectAsState()
    val user by appViewModel.currentUser.collectAsState()
    val largeText by appViewModel.largeText.collectAsState()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val query by contentViewModel.query.collectAsState()
    val filter by contentViewModel.filter.collectAsState()
    val results by contentViewModel.results.collectAsState()
    val favoriteIds by contentViewModel.favoriteIds.collectAsState()
    val favorites by contentViewModel.favorites.collectAsState()

    // Guardia de rutas: sin sesión solo se accede a login/registro; con sesión se salta a Inicio.
    LaunchedEffect(userId, currentRoute) {
        if (currentRoute == null) return@LaunchedEffect
        if (userId == null && currentRoute !in Routes.authRoutes) {
            navController.navigate(Routes.LOGIN) { popUpTo(navController.graph.id) { inclusive = true } }
        } else if (userId != null && currentRoute in Routes.authRoutes) {
            navController.navigate(Routes.HOME) { popUpTo(navController.graph.id) { inclusive = true } }
        }
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = currentRoute in Routes.mainRoutes,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                AppBottomBar(currentRoute = currentRoute, onNavigate = navController::navigateTopLevel)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(tween(250)) + slideInHorizontally(tween(300)) { it / 8 } },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(250)) },
            popExitTransition = { fadeOut(tween(200)) + slideOutHorizontally(tween(300)) { it / 8 } }
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(onGoToRegister = { navController.navigate(Routes.REGISTER) })
            }
            composable(Routes.REGISTER) {
                RegisterScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.HOME) {
                HomeScreen(
                    user = user,
                    favoriteIds = favoriteIds,
                    onOpenType = { type ->
                        contentViewModel.onQueryChange("")
                        contentViewModel.onFilterChange(type)
                        navController.navigateTopLevel(Routes.EXPLORE)
                    },
                    onOpenContent = navController::openContent,
                    onToggleFavorite = contentViewModel::toggleFavorite,
                    onCompleteProfile = { navController.navigateTopLevel(Routes.PROFILE) }
                )
            }
            composable(Routes.EXPLORE) {
                ExploreScreen(
                    query = query,
                    filter = filter,
                    results = results,
                    favoriteIds = favoriteIds,
                    onQueryChange = contentViewModel::onQueryChange,
                    onFilterChange = contentViewModel::onFilterChange,
                    onClear = contentViewModel::clearSearch,
                    onOpenContent = navController::openContent,
                    onToggleFavorite = contentViewModel::toggleFavorite
                )
            }
            composable(Routes.FAVORITES) {
                FavoritesScreen(
                    favorites = favorites,
                    onOpenContent = navController::openContent,
                    onToggleFavorite = contentViewModel::toggleFavorite,
                    onGoExplore = { navController.navigateTopLevel(Routes.EXPLORE) }
                )
            }
            composable(Routes.CONTACT) { ContactScreen() }
            composable(Routes.PROFILE) {
                ProfileScreen(
                    largeText = largeText,
                    onLargeTextChange = appViewModel::setLargeText,
                    onLogout = appViewModel::logout
                )
            }
            composable(Routes.RECIPE, arguments = listOf(navArgument(Routes.ARG_ID) { type = NavType.StringType })) { entry ->
                val id = entry.arguments?.getString(Routes.ARG_ID).orEmpty()
                RecipeDetailScreen(
                    recipe = ContentCatalog.find(id),
                    isFavorite = id in favoriteIds,
                    onToggleFavorite = { contentViewModel.toggleFavorite(id) },
                    onOpenVideo = { navController.navigate(Routes.video(it)) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.COURSE, arguments = listOf(navArgument(Routes.ARG_ID) { type = NavType.StringType })) { entry ->
                val id = entry.arguments?.getString(Routes.ARG_ID).orEmpty()
                CourseDetailScreen(
                    course = ContentCatalog.find(id),
                    isFavorite = id in favoriteIds,
                    onToggleFavorite = { contentViewModel.toggleFavorite(id) },
                    onOpenLesson = { navController.navigate(Routes.video(it)) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.VIDEO, arguments = listOf(navArgument(Routes.ARG_ID) { type = NavType.StringType })) { entry ->
                val id = entry.arguments?.getString(Routes.ARG_ID).orEmpty()
                VideoDetailScreen(
                    video = ContentCatalog.find(id),
                    isFavorite = id in favoriteIds,
                    onToggleFavorite = { contentViewModel.toggleFavorite(id) },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
