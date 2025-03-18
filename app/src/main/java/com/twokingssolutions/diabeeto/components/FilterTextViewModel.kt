package com.twokingssolutions.diabeeto.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.model.Food
import com.twokingssolutions.diabeeto.viewmodel.FilterTextViewModel

@Composable
fun FilterTextView(
    paddingValues: PaddingValues = PaddingValues(),
    onFoodItemClick: (Food) -> Unit,
    viewModel: FilterTextViewModel = viewModel()
) {
    val filteredItems by viewModel.filteredItems.collectAsStateWithLifecycle()
    var inputText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 40.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.img),
                contentDescription = "placeholder",
                tint = Color.Unspecified,
                modifier = Modifier
                    .scale(1.5f),
            )
        }
        OutlinedTextField(
            value = inputText,
            onValueChange = {
                inputText = it
                viewModel.filterText(inputText)
            },
            label = { Text("Input Text") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            trailingIcon = {
                if (inputText.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                inputText = ""
                                viewModel.filterText(inputText)
                            }
                    )
                }
            }
        )
        LazyColumn {
            items(
                count = filteredItems.size,
                key = { index -> filteredItems[index] }
            ) {
                // Don't show filteredItems until some input text has been entered
                if (inputText != ""){
                    ListItem(
                        headlineContent = { Text(filteredItems[it]) },
                        modifier = Modifier
                            .fillParentMaxWidth()
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier.padding(15.dp)
        ) {
            items(mySearchResults) { food ->
                FoodItem(food, onFoodItemClick)
            }
        }
    }
}

// sample results data
val mySearchResults = listOf(
    Food(1, "1 Cup Of Rice Uncooked", "120g"),
    Food(2, "1 Cup Of Rice Cooked", "60g")
)


