package com.anxops.bkn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
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

        setContent {
            app()
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            it.result?.let { token ->
                viewModel.saveFirebaseToken(token)
            }
        }
    }
}

@Composable
fun app() {
    val navController = rememberNavController()
    BikenanceAndroidTheme(darkTheme = true) {
        DestinationsNavHost(
            navController = navController, navGraph = BknNavigator.rootNavGraph()
        )
    }
}


