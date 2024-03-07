package com.grusie.testnavigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grusie.testnavigation.ui.theme.TestNavigationTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val testItems = Array(30) { it }

            TestNavigationTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.First.name,
                        modifier = Modifier.padding(it)
                    ) {
                        composable(route = Screen.First.name) {
                            FirstScreen(onNextBtnClicked = { navController.navigate(Screen.Second.name) })
                        }

                        composable(
                            route = Screen.Second.name
                        ) {
                            SecondScreen(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                items = testItems,
                                onItemClicked = { item -> navController.navigate("${Screen.Third.name}/$item") })
                        }

                        composable(
                            route = "${Screen.Third.name}/{item}",
                            arguments = listOf(navArgument("item") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val receivedItem = backStackEntry.arguments?.getString("item") ?: ""

                            val context = LocalContext.current
                            ThirdScreen(
                                onBackBtnClicked = {
                                    navController.popBackStack(
                                        Screen.First.name,
                                        false
                                    )
                                },
                                item = receivedItem,
                                onSharedBtnClicked = { text1, text2 ->
                                    shareData(context, text1, text2)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun shareData(context: Context, text1: String, text2: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, text1)
        putExtra(Intent.EXTRA_TEXT, text2)
    }

    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.app_name)
        )
    )
}

@Composable
fun SecondScreen(modifier: Modifier, items: Array<Int>, onItemClicked: (Int) -> (Unit)) {
    LazyColumn(modifier = modifier) {
        items(items = items) {
            Text(text = "$it 번째", modifier.clickable { onItemClicked(it) })
        }
    }
}

@Composable
fun ThirdScreen(
    onBackBtnClicked: () -> Unit,
    item: String,
    onSharedBtnClicked: (String, String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { onBackBtnClicked() }) {
            Text(text = "backButton")
        }
        Button(onClick = { onSharedBtnClicked("text1", item) }) {
            Text(text = "sharedButton")
        }
    }
}
