package com.example.littlelemon

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun Profile(navController: NavController) {

    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences(
        "LittleLemon",
        Context.MODE_PRIVATE
    )

    val firstName = sharedPreferences.getString("firstName", "")
    val lastName = sharedPreferences.getString("lastName", "")
    val email = sharedPreferences.getString("email", "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.Start
    ) {

        // Header Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Little Lemon Logo",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 30.dp)
                .height(40.dp)
                .width(185.dp)
        )

        Text(
            text = "Personal information",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "First name")
        Text(text = firstName ?: "")

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Last name")
        Text(text = lastName ?: "")

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Email")
        Text(text = email ?: "")

        Spacer(modifier = Modifier.weight(1f))

        Button(


            onClick = {

                // Clear SharedPreferences
                sharedPreferences.edit().clear().apply()

                // Navigate to Onboarding
                navController.navigate("Onboarding") {
                    popUpTo("Home") { inclusive = true }
                }

            },
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF4CE14),
                contentColor = Color.Black
            )
        ) {
            Text("Log out")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    // NavController cannot run in preview
    val navController = rememberNavController()
    Profile(navController = navController)
}

