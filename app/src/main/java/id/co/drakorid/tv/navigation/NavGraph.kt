package id.co.drakorid.tv.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import id.co.drakorid.tv.ui.screens.auth.AuthScreen
import id.co.drakorid.tv.ui.screens.category.CategoryScreen
import id.co.drakorid.tv.ui.screens.detail.MovieDetailScreen
import id.co.drakorid.tv.ui.screens.home.HomeScreen
import id.co.drakorid.tv.ui.screens.player.PlayerScreen
import id.co.drakorid.tv.ui.screens.search.SearchScreen

object Routes {
    const val HOME = "home"
    const val AUTH = "auth"
    const val SEARCH = "search"
    const val CATEGORY = "category"
    const val DETAIL = "detail/{movieId}"
    const val PLAYER = "player/{episodeId}?title={title}"

    fun detail(movieId: String) = "detail/$movieId"
    fun player(episodeId: String, title: String) =
        "player/$episodeId?title=${android.net.Uri.encode(title)}"
}

@Composable
fun DrakorNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Routes.detail(movieId))
                },
                onSearchClick = {
                    navController.navigate(Routes.SEARCH)
                },
                onCategoryClick = {
                    navController.navigate(Routes.CATEGORY)
                }
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Routes.detail(movieId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.CATEGORY) {
            CategoryScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Routes.detail(movieId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
        ) { backStackEntry ->
            MovieDetailScreen(
                onPlay = { episodeId ->
                    val title = backStackEntry.savedStateHandle["title"] ?: ""
                    navController.navigate(Routes.player(episodeId, title))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.PLAYER,
            arguments = listOf(
                navArgument("episodeId") { type = NavType.StringType },
                navArgument("title") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            PlayerScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.AUTH) {
            AuthScreen(
                onLoggedIn = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
