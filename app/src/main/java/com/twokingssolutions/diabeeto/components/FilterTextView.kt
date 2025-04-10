package com.twokingssolutions.diabeeto.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.model.NavRoutes
import com.twokingssolutions.diabeeto.viewModel.FoodDatabaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterTextView(
    navController: NavController,
    foodDatabaseViewModel: FoodDatabaseViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val searchText by foodDatabaseViewModel.searchText.collectAsState(initial = "")
    val searchResults by foodDatabaseViewModel.foodList.collectAsState(initial = emptyList())
    val isSearching by foodDatabaseViewModel.isSearching.collectAsState(initial = false)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                foodDatabaseViewModel.onSearchTextChange(it)
                expanded = true
            },
            label = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .focusRequester(focusRequester),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedTextColor = colorResource(R.color.secondary_colour),
                focusedTextColor = colorResource(R.color.secondary_colour)
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    navController.navigate(NavRoutes.SearchResultsRoute(searchResults))
                    expanded = false
                },
                onSearch = {
                    navController.navigate(NavRoutes.SearchResultsRoute(searchResults))
                    expanded = false
                }
            ),
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                foodDatabaseViewModel.onSearchTextChange("")
                                expanded = false
                            }
                    )
                }
            }
        )
        if (isSearching) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else if (searchResults.isNotEmpty() && expanded) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                searchResults.forEach { food ->
                    DropdownMenuItem(
                        text = { Text(food.foodItem) },
                        onClick = {
                            navController.navigate(NavRoutes.SearchResultsRoute(searchResults))
                            expanded = false
                        }
                    )
                }
            }
        } else if (expanded) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("No results found") },
                    onClick = { expanded = false }
                )
            }
        }
    }
}