package id.co.drakorid.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import id.co.drakorid.tv.navigation.DrakorNavGraph
import id.co.drakorid.tv.ui.theme.DrakorTVTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrakorTVTheme {
                DrakorNavGraph()
            }
        }
    }
}
