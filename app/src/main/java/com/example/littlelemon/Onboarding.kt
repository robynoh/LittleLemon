package com.example.littlelemon

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController



@Composable
fun Onboarding(navController: NavHostController) {

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val context = LocalContext.current
    val sharedPreferences =
        context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)

    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // 🔹 Top Logo Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF4F4F4))
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Little Lemon Logo",
                modifier = Modifier.height(40.dp).width(185.dp)
            )
        }

        // 🔹 Green Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF495E57))
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Let's get to know you",
                color = Color.White,
                fontSize = 20.sp
            )
        }

        // 🔹 Form Section
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Personal information",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF495E57)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // First Name
            Text("First name")
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Last Name
            Text("Last name")
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email
            Text("Email")
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // 🔹 Register Button
            Button(
                     modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF4CE14),
                    contentColor = Color.Black
                ),
                onClick = {

                    if (firstName.isBlank() ||
                        lastName.isBlank() ||
                        email.isBlank()
                    ) {
                        message = "Registration unsuccessful. Please enter all data."
                    } else {

                        sharedPreferences.edit()
                            .putString("firstName", firstName)
                            .putString("lastName", lastName)
                            .putString("email", email)
                            .apply()

                        message = "Registration successful!"

                        navController.navigate(HomeDestination.route) {
                            popUpTo(OnboardingDestination.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            ) {
                Text("Register")
            }

            Text(
                text = message,
                color = if (message.contains("unsuccessful"))
                    MaterialTheme.colorScheme.error
                else
                    Color(0xFF2E7D32)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    val navController = rememberNavController()
    Onboarding(navController = navController)
}