package com.codelab.basics

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codelab.basics.ui.theme.BasicsCodelabTheme

// Helper to get ID from string name
fun getDrawableIdByName(context: Context, name: String): Int {
    val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
    return if (resId != 0) resId else android.R.drawable.ic_menu_help // Default if not found
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("PROGRAM_START", "Program has started");
        super.onCreate(savedInstanceState)
        val dbHelper = PokemonDBHelper(this)
        setContent {
            BasicsCodelabTheme {
                MyApp(modifier = Modifier.fillMaxSize(), dbHelper = dbHelper)
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier, dbHelper: PokemonDBHelper) {
    val windowInfo = rememberWindowInfo()
    var pokemonList by remember { mutableStateOf(dbHelper.findAll()) }
    var index by remember { mutableIntStateOf(-1) }
    var showMaster = (index == -1)

    fun refreshData() { pokemonList = dbHelper.findAll() }

    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
            // PHONE LAYOUT
            if (showMaster || index !in pokemonList.indices) {
                ShowPageMaster(pokemonList, { index = it }, dbHelper, { refreshData() })
            } else {
                // Update arguments here:
                ShowPageDetails(
                    pokemon = pokemonList[index],
                    pokemonList = pokemonList,       // Passed list
                    updateIndex = { index = it },
                    dbHelper = dbHelper,             // Passed DB
                    onDataChanged = { refreshData() }, // Passed Refresh logic
                    modifier = Modifier,
                    index = index
                )
            }
        } else {
            // TABLET LAYOUT
            Row(Modifier.fillMaxSize()) {
                ShowPageMaster(pokemonList, { index = it }, dbHelper, { refreshData() }, Modifier.weight(1f))
                if (index in pokemonList.indices) {
                    // Update arguments here too:
                    ShowPageDetails(
                        pokemon = pokemonList[index],
                        pokemonList = pokemonList,
                        updateIndex = { index = it },
                        dbHelper = dbHelper,
                        onDataChanged = { refreshData() },
                        modifier = Modifier.weight(1f),
                        index = index
                    )
                } else {
                    Box(Modifier.weight(1f).fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Select a Pokemon")
                    }
                }
            }
        }
    }
}

@Composable
fun ShowPageMaster(
    pokemonList: List<Pokemon>,
    updateIndex: (Int) -> Unit,
    dbHelper: PokemonDBHelper,
    onDataChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Calculate Favorite (Most Accessed) dynamically
    val favoritePokemon = pokemonList.maxByOrNull { it.accessCount }
    val context = LocalContext.current

    Column(modifier = modifier.padding(8.dp)) {

        // --- Favorite Section with BIG Image ---
        if (favoritePokemon != null) {
            ElevatedCard(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Dynamic Large Image
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(100.dp), // Big square size
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Image(
                            painter = painterResource(id = getDrawableIdByName(context, favoritePokemon.imageFileName)),
                            contentDescription = "Favorite Pokemon",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // 2. Text Details
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "FAVORITE",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = favoritePokemon.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = "${favoritePokemon.accessCount} Total Views",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        // --- List Section ---
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(items = pokemonList) { pos, pokemon ->
                ShowEachListItem(pokemon, pos, updateIndex, dbHelper, onDataChanged)
            }

            // Disclaimer Footer
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Disclaimer: All images are sourced from Wikipedia and Bulbapedia.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ShowEachListItem(
    pokemon: Pokemon,
    pos: Int,
    updateIndex: (Int) -> Unit,
    dbHelper: PokemonDBHelper,
    onDataChanged: () -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    // --- UI Change 4: Elevated Card ---
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {

                // --- UI Change 2: List Image ---
                // Inside ShowEachListItem
                Image(
                    painter = painterResource(id = getDrawableIdByName(context, pokemon.imageFileName)),
                    contentDescription = pokemon.name,
                    // CHANGE THIS:
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        // Optional: Add padding if the image touches the circle edges too much
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = pokemon.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(text = "Power: ${pokemon.powerLevel}", style = MaterialTheme.typography.bodyMedium)
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore, contentDescription = null)
                }
            }

            if (expanded) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    Text("Description: ${pokemon.description}")
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            dbHelper.incAccessCount(pokemon.id)
                            onDataChanged()
                            updateIndex(pos)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("View Details")
                    }
                }
            }
        }
    }
}

@Composable
fun ShowPageDetails(
    pokemon: Pokemon,
    pokemonList: List<Pokemon>,    // NEW: Needed to find the Next/Prev IDs
    updateIndex: (Int) -> Unit,
    dbHelper: PokemonDBHelper,     // NEW: Needed to save the count
    onDataChanged: () -> Unit,     // NEW: Needed to refresh the screen
    modifier: Modifier = Modifier,
    index: Int
) {
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {

        // Hero Image
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(250.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Image(
                painter = painterResource(id = getDrawableIdByName(context, pokemon.imageFileName)),
                contentDescription = pokemon.name,
                contentScale = ContentScale.Fit, // Fixed scaling
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = pokemon.name, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Card
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text("Pokedex #: ${pokemon.pokedexNumber}", style = MaterialTheme.typography.titleMedium)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Power Level: ${pokemon.powerLevel}", style = MaterialTheme.typography.titleMedium)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Views: ${pokemon.accessCount}", style = MaterialTheme.typography.titleMedium)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(pokemon.description, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Navigation Buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { updateIndex(-1) }) { Text("Back") }
            Row {
                // PREV BUTTON
                if (index > 0) {
                    Button(onClick = {
                        val prevPokemon = pokemonList[index - 1]
                        dbHelper.incAccessCount(prevPokemon.id) // 1. Count it
                        onDataChanged()                         // 2. Refresh it
                        updateIndex(index - 1)                  // 3. Move to it
                    }) { Text("Prev") }
                }

                Spacer(Modifier.width(8.dp))

                // NEXT BUTTON
                if (index < pokemonList.size - 1) {
                    Button(onClick = {
                        val nextPokemon = pokemonList[index + 1]
                        dbHelper.incAccessCount(nextPokemon.id) // 1. Count it
                        onDataChanged()                         // 2. Refresh it
                        updateIndex(index + 1)                  // 3. Move to it
                    }) { Text("Next") }
                }
            }
        }
    }
}