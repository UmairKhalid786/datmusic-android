/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber
import tm.alashow.common.compose.collectEvent
import tm.alashow.datmusic.ui.album.AlbumDetail
import tm.alashow.datmusic.ui.artist.ArtistDetail
import tm.alashow.datmusic.ui.downloads.Downloads
import tm.alashow.datmusic.ui.search.Search
import tm.alashow.datmusic.ui.settings.Settings
import tm.alashow.navigation.LeafScreen
import tm.alashow.navigation.LocalNavigator
import tm.alashow.navigation.NavigationEvent
import tm.alashow.navigation.Navigator
import tm.alashow.navigation.RootScreen
import tm.alashow.navigation.composableScreen

@OptIn(InternalCoroutinesApi::class)
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    navigator: Navigator
) {
    collectEvent(navigator.queue) { event ->
        Timber.i("Navigation event: $event")
        when (event) {
            is NavigationEvent.Destination -> navController.navigate(event.route)
            is NavigationEvent.Back -> navController.navigateUp()
            else -> Unit
        }
    }

    CompositionLocalProvider(LocalNavigator provides navigator) {
        NavHost(
            navController = navController,
            startDestination = RootScreen.Search.route
        ) {
            addSearchRoot(navController)
            addDownloadsRoot(navController)
            addSettingsRoot(navController)
        }
    }
}

private fun NavGraphBuilder.addSearchRoot(navController: NavController) {
    navigation(
        route = RootScreen.Search.route,
        startDestination = LeafScreen.Search.route
    ) {
        addSearch(navController)
        addArtistDetails(navController)
        addAlbumDetails(navController)
    }
}

private fun NavGraphBuilder.addDownloadsRoot(navController: NavController) {
    navigation(
        route = RootScreen.Downloads.route,
        startDestination = LeafScreen.Downloads.route
    ) {
        addDownloads(navController)
    }
}

private fun NavGraphBuilder.addSettingsRoot(navController: NavController) {
    navigation(
        route = RootScreen.Settings.route,
        startDestination = LeafScreen.Settings.route
    ) {
        addSettings(navController)
    }
}

private fun NavGraphBuilder.addSearch(navController: NavController) {
    composableScreen(LeafScreen.Search) {
        Search()
    }
}

private fun NavGraphBuilder.addSettings(navController: NavController) {
    composableScreen(LeafScreen.Settings) {
        Settings()
    }
}

private fun NavGraphBuilder.addDownloads(navController: NavController) {
    composableScreen(LeafScreen.Downloads) {
        Downloads()
    }
}

private fun NavGraphBuilder.addArtistDetails(navController: NavController) {
    composableScreen(LeafScreen.ArtistDetails) {
        ArtistDetail()
    }
}

private fun NavGraphBuilder.addAlbumDetails(navController: NavController) {
    composableScreen(LeafScreen.AlbumDetails) {
        AlbumDetail()
    }
}

/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
internal fun NavController.currentScreenAsState(): State<RootScreen> {
    val selectedItem = remember { mutableStateOf<RootScreen>(RootScreen.Search) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == RootScreen.Search.route } -> {
                    selectedItem.value = RootScreen.Search
                }
                destination.hierarchy.any { it.route == RootScreen.Downloads.route } -> {
                    selectedItem.value = RootScreen.Downloads
                }
                destination.hierarchy.any { it.route == RootScreen.Settings.route } -> {
                    selectedItem.value = RootScreen.Settings
                }
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}
