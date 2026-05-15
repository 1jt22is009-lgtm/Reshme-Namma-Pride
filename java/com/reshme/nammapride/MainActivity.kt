package com.reshme.nammapride

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.reshme.nammapride.domain.repository.ReshmeRepository
import com.reshme.nammapride.ui.navigation.ReshmeNavHost
import com.reshme.nammapride.ui.theme.ReshmeTheme

val LocalRepository = staticCompositionLocalOf<ReshmeRepository> { error("Repository not provided") }

class MainActivity : ComponentActivity() {
    private val notificationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= 33) notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        val repo = (application as ReshmeApp).repository
        setContent {
            CompositionLocalProvider(LocalRepository provides repo) {
                ReshmeTheme { ReshmeNavHost() }
            }
        }
    }
}
