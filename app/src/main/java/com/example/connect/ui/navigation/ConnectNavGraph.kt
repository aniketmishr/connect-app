package com.example.connect.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInOutElastic
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.connect.ui.presentation.ContactDetailsDestination
import com.example.connect.ui.presentation.ContactDetailsScreen
import com.example.connect.ui.presentation.ContactListDestination
import com.example.connect.ui.presentation.ContactListScreen
import com.example.connect.ui.presentation.EditContactDestination
import com.example.connect.ui.presentation.EditContactScreen
import com.example.connect.ui.presentation.EditNoteDestination
import com.example.connect.ui.presentation.EditNoteScreen
import com.example.connect.ui.presentation.SearchContactDestination
import com.example.connect.ui.presentation.SearchContactScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ConnectNavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = ContactListDestination.route
       )
    {
        composable(route = ContactListDestination.route,
            exitTransition = {
                         slideOutHorizontally(
                            targetOffsetX = {fullWidth -> -fullWidth/2} ,
                             animationSpec = tween(300)
                         )+
                         fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = {fullWidth -> -fullWidth/2},
                    animationSpec = tween(300)
                )+
                        fadeIn(animationSpec = tween(300))
            }){
            ContactListScreen(
                navigateToAddContact = {navController.navigate("${EditContactDestination.route}/$it")},
                navigateToSearch = {navController.navigate(SearchContactDestination.route)},
                navigateToContactDetails = { navController.navigate("${ContactDetailsDestination.route}/$it") }
            )
        }


        composable(route = ContactDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ContactDetailsDestination.contactIdArg){
                type = NavType.IntType
            }),
            enterTransition = {
                              slideInHorizontally(
                                  initialOffsetX = {fullWidth -> fullWidth/2},
                                  animationSpec = tween(300)
                              ) +
                                      fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = {fullWidth -> fullWidth/2 },
                    animationSpec = tween(300)
                )+
                        fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = {fullWidth -> -fullWidth/2},
                    animationSpec = tween(300)
                ) +
                        fadeIn(animationSpec = tween(300))
            },

            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = {fullWidth -> -fullWidth/2 },
                    animationSpec = tween(300)
                )+
                        fadeOut(animationSpec = tween(300))
            }
        ){
            ContactDetailsScreen(onNavigateBackClick = {navController.popBackStack()},
                onEditClick = {navController.navigate("${EditContactDestination.route}/$it")},
                onNewNoteClick = {navController.navigate("${EditNoteDestination.route}/$it/${0}")},
                onNoteClicked = {contactId, noteId->
                    navController.navigate("${EditNoteDestination.route}/${contactId}/${noteId}")
                }
            )
        }

        composable(route = EditContactDestination.routeWithArgs,
            arguments = listOf(navArgument(EditContactDestination.contactIdArg){
                type = NavType.IntType
            }),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = {fullWidth -> fullWidth/2},
                    animationSpec = tween(300)
                )+
                        fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = {fullWidth -> fullWidth/2 },
                    animationSpec = tween(300)
                )+
                        fadeOut(animationSpec = tween(300))
            }


        ) {
            EditContactScreen( onNavigateBack = { navController.popBackStack()})
        }

        composable(route = EditNoteDestination.routeWithArgs,
            arguments = listOf(
                navArgument(EditNoteDestination.noteIdArg){ type= NavType.IntType },
                navArgument(EditNoteDestination.contactIdArg){type = NavType.IntType}
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = {fullWidth -> fullWidth/2},
                    animationSpec = tween(300)
                )+
                        fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = {fullWidth -> fullWidth/2 },
                    animationSpec = tween(300)
                )+
                        fadeOut(animationSpec = tween(300))
            }
            ){
            EditNoteScreen(onNavigateBack =  { navController.popBackStack() })
        }

        composable(
            route = SearchContactDestination.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = {fullWidth -> fullWidth/2},
                    animationSpec = tween(300)
                )+
                        fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = {fullWidth -> fullWidth/2 },
                    animationSpec = tween(300)
                )+
                        fadeOut(animationSpec = tween(300))
            }
            ){
            SearchContactScreen(
                onNavigateBackClick = {navController.popBackStack()},
                navigateToContactDetails = {navController.navigate("${ContactDetailsDestination.route}/$it")}
            )
        }
    }
}