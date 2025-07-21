package com.twokingssolutions.diabeeto.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.model.NavRoutes
import com.twokingssolutions.diabeeto.model.SearchSuggestion
import com.twokingssolutions.diabeeto.model.SearchSuggestionType
import com.twokingssolutions.diabeeto.viewModel.ProductDatabaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterTextView(
    navController: NavController,
    productDatabaseViewModel: ProductDatabaseViewModel
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var searchQuery by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf<List<SearchSuggestion>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            productDatabaseViewModel.getSearchSuggestions(searchQuery) {
                suggestions = it
                expanded = it.isNotEmpty()
            }
        } else {
            suggestions = emptyList()
            expanded = false
        }
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isBlank()) {
            expanded = false
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                .focusRequester(focusRequester),
            placeholder = { Text("Search products...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = colorResource(R.color.secondary_colour)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = ""
                        suggestions = emptyList()
                        expanded = false
                        focusManager.clearFocus()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.secondary_colour),
                unfocusedBorderColor = colorResource(R.color.secondary_colour).copy(alpha = 0.5f),
                cursorColor = colorResource(R.color.secondary_colour)
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    navController.navigate(NavRoutes.SearchResultsRoute(queryString = searchQuery))
                    expanded = false
                    focusManager.clearFocus()
                }
            ),
            singleLine = true
        )
        if (suggestions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                suggestions.forEach { suggestion ->
                    DropdownMenuItem(
                        text = { Text(suggestion.name) },
                        onClick = {
                            searchQuery = suggestion.name
                            expanded = false
                            focusManager.clearFocus()
                            when (suggestion.type) {
                                SearchSuggestionType.DEPARTMENT -> navController.navigate(
                                    NavRoutes.SearchResultsRoute("department:${suggestion.name}")
                                )
                                else -> navController.navigate(
                                    NavRoutes.SearchResultsRoute(suggestion.name)
                                )
                            }
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.clickable {
                navController.navigate(NavRoutes.SearchResultsRoute(queryString = "browse_all"))
            },
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.tertiary_colour)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Text(
                text = "Browse All Products",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                color = colorResource(R.color.white_colour)
            )
        }
    }
}