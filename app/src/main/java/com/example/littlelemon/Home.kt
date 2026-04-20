package com.example.littlelemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun Home(navController: NavController) {
    val database = AppDatabase.getDatabase(LocalContext.current)
    val menuItems by database.menuItemDao().getAll().observeAsState(emptyList())

    HomeContent(navController, menuItems)
}

@Composable
fun HomeContent(navController: NavController, menuItems: List<MenuItemRoom>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Header(navController)
        HeroSection()
        MenuItemsList(menuItems)
    }
}

@Composable
fun Header(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.width(40.dp))
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.height(40.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Profile",
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    navController.navigate("Profile")
                }
        )
    }
}

@Composable
fun HeroSection() {
    Column(
        modifier = Modifier
            .background(Color(0xFF495E57))
            .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 16.dp)
    ) {
        Text(
            text = "Little Lemon",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF4CE14)
        )
        Text(
            text = "Chicago",
            fontSize = 24.sp,
            color = Color.White
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
        ) {
            Text(
                text = "We are a family-owned Mediterranean restaurant, focused on traditional recipes served with a modern twist",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier
                    .weight(0.6f)
                    .padding(end = 8.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.heroimage),
                contentDescription = "Hero Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        }
        // Placeholder for Search (future exercise)
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.White, shape = MaterialTheme.shapes.small)
        )
    }
}

@Composable
fun MenuItemsList(items: List<MenuItemRoom>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        items(
            items = items,
            itemContent = { menuItem ->
                MenuItem(menuItem)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            }
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuItem(item: MenuItemRoom) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = item.description,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp),
                maxLines = 2
            )
            Text(
                text = "$${item.price}",
                fontWeight = FontWeight.SemiBold
            )
        }
        GlideImage(
            model = item.image,
            contentDescription = item.title,
            modifier = Modifier
                .size(100.dp)
                .padding(start = 8.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val sampleItems = listOf(
        MenuItemRoom(1, "Greek Salad", "The famous greek salad of crispy lettuce...", "12.99", "", "starters"),
        MenuItemRoom(2, "Bruschetta", "Our Bruschetta is made from grilled bread...", "7.99", "", "starters")
    )
    HomeContent(navController = rememberNavController(), menuItems = sampleItems)
}
