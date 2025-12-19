package com.example.smartshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.smartshop.ui.auth.SignInScreen
import com.example.smartshop.ui.auth.SignUpScreen
import com.example.smartshop.ui.products.ProductListScreen
import com.example.smartshop.ui.products.ProductEntryScreen
import com.example.smartshop.ui.products.StatisticsScreen
import com.example.smartshop.ui.products.ProductViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartshop.data.model.Product
import com.example.smartshop.ui.theme.SmartShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartShopTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    val productViewModel: ProductViewModel = viewModel()
                    
                    androidx.compose.foundation.layout.Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(navController = navController, startDestination = "signin") {
                            composable("signin") {
                                SignInScreen(
                                    onSignInSuccess = {
                                        navController.navigate("productList") {
                                            popUpTo("signin") { inclusive = true }
                                        }
                                    },
                                    onNavigateToSignUp = {
                                        navController.navigate("signup")
                                    }
                                )
                            }
                            composable("signup") {
                                SignUpScreen(
                                    onSignUpSuccess = {
                                        navController.navigate("productList") {
                                            popUpTo("signin") { inclusive = true }
                                        }
                                    },
                                    onNavigateToSignIn = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                            composable("productList") {
                                ProductListScreen(
                                    onAddProduct = { navController.navigate("productEntry") },
                                    onEditProduct = { product -> 
                                        navController.navigate("productEntry?productId=${product.id}") 
                                    },
                                    onViewStats = { navController.navigate("statistics") },
                                    viewModel = productViewModel
                                )
                            }
                            composable("statistics") {
                                StatisticsScreen(
                                    onNavigateBack = { navController.popBackStack() },
                                    viewModel = productViewModel
                                )
                            }
                            composable(
                                route = "productEntry?productId={productId}",
                                arguments = listOf(navArgument("productId") { 
                                    type = NavType.IntType
                                    defaultValue = -1 
                                })
                            ) { backStackEntry ->
                                val productId = backStackEntry.arguments?.getInt("productId") ?: -1
                                val products = productViewModel.allProducts.collectAsState().value
                                val productToEdit = products.find { it.id == productId }
                                
                                ProductEntryScreen(
                                    productToEdit = productToEdit,
                                    onNavigateBack = { navController.popBackStack() },
                                    viewModel = productViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
