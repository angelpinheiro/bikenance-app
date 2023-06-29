package com.anxops.bkn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.anxops.bkn.storage.BknDataStore
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.firebase.messaging.FirebaseMessaging
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(var dataStore: BknDataStore) : ViewModel() {

    fun saveFirebaseToken(t: String) {
        viewModelScope.launch {
            dataStore.saveFirebaseToken(t)
        }
    }
}


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("MainActivity onCreate")

        setContent {
            app()
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            it.result?.let { token ->
                Log.d("MainActivity", "Fetching FCM registration token $token")
                viewModel.saveFirebaseToken(token)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        println("MainActivity onNewIntent")
    }

    private fun handleAction(intent: Intent?, navController: NavHostController) {
        println("Intent action: ${intent?.action}")
        when(intent?.action) {
            "VIEW_RIDES" -> {

            }
        }
    }

}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun app() {

    val navController = rememberNavController()

    navController.addOnDestinationChangedListener { nc, destination, bundle ->
        Log.d("NavController", "Navigate to ${destination.route} ${bundle.toString()}")
    }

    BikenanceAndroidTheme {
        DestinationsNavHost(
            navController = navController,
            navGraph = BknNavigator.rootNavGraph()
        )
    }
}


