package com.example.littlelemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    // ✅ Search state
    var searchPhrase by remember { mutableStateOf("") }

    // ✅ Category state
    var selectedCategory by remember { mutableStateOf("") }

    // ✅ Unique categories
    val categories = menuItems.map { it.category }.distinct()

    // ✅ Combined filtering
    val filteredItems = menuItems.filter { item ->
        val matchesSearch =
            searchPhrase.isBlank() ||
                    item.title.contains(searchPhrase, ignoreCase = true)

        val matchesCategory =
            selectedCategory.isBlank() ||
                    item.category.equals(selectedCategory, ignoreCase = true)

        matchesSearch && matchesCategory
    }

    HomeContent(
        navController = navController,
        menuItems = filteredItems,
        searchPhrase = searchPhrase,
        onSearchChanged = { searchPhrase = it },
        categories = categories,
        selectedCategory = selectedCategory,
        onCategorySelected = {
            selectedCategory = if (selectedCategory == it) "" else it
        }
    )
}

@Composable
fun HomeContent(
    navController: NavController,
    menuItems: List<MenuItemRoom>,
    searchPhrase: String,
    onSearchChanged: (String) -> Unit,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Header(navController)
        HeroSection(searchPhrase, onSearchChanged)
        CategorySection(categories, selectedCategory, onCategorySelected)
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
fun HeroSection(
    searchPhrase: String,
    onSearchChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color(0xFF495E57))
            .padding(12.dp)
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

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = searchPhrase,
            onValueChange = onSearchChanged,
            placeholder = { Text("Enter Search Phrase") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}

@Composable
fun CategorySection(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(12.dp)) {

        Text(
            text = "ORDER FOR DELIVERY!",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { category ->

                val isSelected = category.equals(selectedCategory, ignoreCase = true)

                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(
                            if (isSelected) Color(0xFF495E57)
                            else Color.LightGray
                        )
                        .clickable {
                            onCategorySelected(category)
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = category.replaceFirstChar { it.uppercase() },
                        color = if (isSelected) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun MenuItemsList(items: List<MenuItemRoom>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        items(items) { menuItem ->
            MenuItem(menuItem)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }
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
            Text(item.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                item.description,
                color = Color.Gray,
                maxLines = 2
            )
            Text("$${item.price}", fontWeight = FontWeight.SemiBold)
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
        MenuItemRoom(1, "Greek Salad", "The famous greek salad...", "12.99", "", "starters"),
        MenuItemRoom(2, "Bruschetta", "Grilled bread...", "7.99", "", "starters")
    )

    HomeContent(
        navController = rememberNavController(),
        menuItems = sampleItems,
        searchPhrase = "",
        onSearchChanged = {},
        categories = listOf("starters"),
        selectedCategory = "",
        onCategorySelected = {}
    )
}