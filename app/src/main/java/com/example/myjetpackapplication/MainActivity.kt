package com.example.myjetpackapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.example.myjetpackapplication.ui.theme.MyJetpackApplicationTheme
import com.example.presentation.navigation.RMNavGraph
import com.example.presentation.viewmodels.ViewModelfactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactoryProvider: ViewModelfactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as BaseApplication).appComponent.inject(this)
        enableEdgeToEdge()
        setContent {
            MyJetpackApplicationTheme {
                var viewmodelprovider =ViewModelProvider(LocalContext.current as ComponentActivity,
                    viewModelFactoryProvider)
                RMNavGraph(viewmodelprovider)
            }
        }
    }
}
