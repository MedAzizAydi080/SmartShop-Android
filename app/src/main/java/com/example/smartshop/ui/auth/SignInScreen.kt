package com.example.smartshop.ui.auth

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun SignInScreen(
    onSignInSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "App Logo",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 48.dp)
        )

        Text(
            text = "Sign-In",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email (phone for mobile accounts)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Forgot Password Button
        TextButton(
            onClick = {
                if (email.isBlank()) {
                    errorMessage = "Please enter your email first"
                    return@TextButton
                }
                
                isLoading = true
                errorMessage = null
                
                scope.launch {
                    try {
                        auth.sendPasswordResetEmail(email).await()
                        Toast.makeText(context, "Password reset email sent to $email", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Failed to send reset email"
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(
                text = "Forgot Password?",
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
        } else {
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Please fill in all fields"
                        return@Button
                    }

                    isLoading = true
                    errorMessage = null
                    
                    scope.launch {
                        try {
                            auth.signInWithEmailAndPassword(email, password).await()
                            Toast.makeText(context, "Sign In Successful!", Toast.LENGTH_SHORT).show()
                            onSignInSuccess()
                        } catch (e: Exception) {
                            errorMessage = e.message ?: "Sign in failed"
                        } finally {
                            isLoading = false
                        }
                    }

                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.Black
                )
            ) {
                Text("Sign In")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateToSignUp,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Text("Create account")
            }
        }
    }
}