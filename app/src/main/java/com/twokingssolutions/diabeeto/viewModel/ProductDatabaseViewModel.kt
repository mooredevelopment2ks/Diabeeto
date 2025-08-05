package com.twokingssolutions.diabeeto.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twokingssolutions.diabeeto.db.entity.AllergyStatements
import com.twokingssolutions.diabeeto.db.entity.Departments
import com.twokingssolutions.diabeeto.db.entity.DietaryStatements
import com.twokingssolutions.diabeeto.db.entity.NutrientValues
import com.twokingssolutions.diabeeto.db.entity.NutritionalInformation
import com.twokingssolutions.diabeeto.db.entity.Product
import com.twokingssolutions.diabeeto.db.relation.FullProductDetails
import com.twokingssolutions.diabeeto.repository.ProductRepository
import com.twokingssolutions.diabeeto.model.SearchSuggestion
import com.twokingssolutions.diabeeto.model.SearchSuggestionType
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class ProductDatabaseViewModel(private val productRepository: ProductRepository) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _productByQuery = MutableStateFlow<List<FullProductDetails>>(emptyList())
    val productByQuery: StateFlow<List<FullProductDetails>> = _productByQuery

    private val _selectedProduct = MutableStateFlow<FullProductDetails?>(null)
    val selectedProduct: StateFlow<FullProductDetails?> = _selectedProduct

    private val _favouriteProductList = MutableStateFlow<List<FullProductDetails>>(emptyList())
    val favouriteProductList: StateFlow<List<FullProductDetails>> = _favouriteProductList

    private val _allAllergyStatements = MutableStateFlow<List<AllergyStatements>>(emptyList())
    val allAllergyStatements: StateFlow<List<AllergyStatements>> = _allAllergyStatements

    private val _allDietaryStatements = MutableStateFlow<List<DietaryStatements>>(emptyList())
    val allDietaryStatements: StateFlow<List<DietaryStatements>> = _allDietaryStatements

    private val _allDepartments = MutableStateFlow<List<Departments>>(emptyList())
    val allDepartments: StateFlow<List<Departments>> = _allDepartments

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _hasMoreResults = MutableStateFlow(true)
    val hasMoreResults = _hasMoreResults.asStateFlow()

    private var _currentOffset = 0
    private val pageSize = 20

    init {
        viewModelScope.launch {
            searchText
                .debounce(300L)
                .distinctUntilChanged()
                .onEach { query ->
                    if (query == "browse_all") {
                        loadAllProductsPaginated()
                    } else if (query.isNotBlank()) {
                        performSearch(query)
                    } else {
                        _productByQuery.value = emptyList()
                        _hasMoreResults.value = false
                    }
                }
                .catch { e ->
                    _isSearching.value = false
                }
                .collect { }
        }

        viewModelScope.launch {
            productRepository.favouriteProducts.collect {
                _favouriteProductList.value = it
            }
        }
    }

    private suspend fun performSearch(query: String) {
        try {
            _isSearching.value = true
            _currentOffset = 0

            val results = when {
                query.startsWith("department:") -> {
                    val departmentName = query.removePrefix("department:")
                    productRepository.getProductsByDepartmentPaginated(departmentName, pageSize, _currentOffset)
                }
                query.startsWith("barcode:") -> {
                    val barcode = query.removePrefix("barcode:")
                    val product = productRepository.getProductByBarcode(barcode)
                    product?.let { listOf(it) } ?: emptyList()
                }
                else -> {
                    productRepository.getProductsByQueryPaginated(query, pageSize, _currentOffset)
                }
            }

            _productByQuery.value = results
            _currentOffset = pageSize
            _hasMoreResults.value = results.size == pageSize
        } catch (e: Exception) {
            _productByQuery.value = emptyList()
            _hasMoreResults.value = false
        } finally {
            _isSearching.value = false
        }
    }

    fun loadAllProductsPaginated() {
        viewModelScope.launch {
            try {
                _isSearching.value = true
                _currentOffset = 0
                _searchText.value = "browse_all"
                val results = productRepository.getAllProductsWithDepartmentsPaginated(pageSize, _currentOffset)
                _productByQuery.value = results
                _currentOffset += results.size
                _hasMoreResults.value = results.size == pageSize
            } catch (e: Exception) {
                _productByQuery.value = emptyList()
                _hasMoreResults.value = false
            } finally {
                _isSearching.value = false
            }
        }
    }

    fun loadMoreResults() {
        if (_isLoadingMore.value || !_hasMoreResults.value) return

        viewModelScope.launch {
            try {
                _isLoadingMore.value = true
                val currentQuery = if (_searchText.value.isBlank() || _searchText.value == "browse_all") "" else _searchText.value

                val results = if (currentQuery.startsWith("department:")) {
                    val departmentName = currentQuery.removePrefix("department:")
                    productRepository.getProductsByDepartmentPaginated(departmentName, pageSize, _currentOffset)
                } else if (currentQuery.isBlank()) {
                    productRepository.getAllProductsWithDepartmentsPaginated(pageSize, _currentOffset)
                } else {
                    productRepository.getProductsByQueryPaginated(currentQuery, pageSize, _currentOffset)
                }

                _productByQuery.value = _productByQuery.value + results
                _currentOffset += results.size
                _hasMoreResults.value = results.size == pageSize
            } catch (e: Exception) {
                _hasMoreResults.value = false
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    fun getSearchSuggestions(query: String, onResult: (List<SearchSuggestion>) -> Unit) {
        viewModelScope.launch {
            val departmentMatches = productRepository.searchDepartments(query)
                .map { SearchSuggestion(it.departmentId, it.departmentName, SearchSuggestionType.DEPARTMENT) }

            val productMatches = productRepository.searchProducts(query)
                .map { SearchSuggestion(it.product.productId, it.product.name, SearchSuggestionType.RECENT_SEARCH) }

            onResult(departmentMatches + productMatches)
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun getProductById(productId: String) {
        viewModelScope.launch {
            val product = productRepository.getProductById(productId)
            _selectedProduct.value = product
        }
    }

    fun getProductByBarcode(barcode: String, onResult: (FullProductDetails?) -> Unit) {
        viewModelScope.launch {
            val product = productRepository.getProductByBarcode(barcode)
            onResult(product)
        }
    }

    fun insertProduct(
        product: Product,
        nutritionalInformation: NutritionalInformation,
        nutrientValues: List<NutrientValues>,
        allergyIds: List<String>,
        dietaryIds: List<String>
    ) {
        viewModelScope.launch {
            productRepository.insertProduct(
                product,
                nutritionalInformation,
                nutrientValues,
                allergyIds,
                dietaryIds
            )
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            productRepository.updateProduct(product)
        }
    }


    suspend fun updateAllProductDetails(
        product: Product,
        nutritionalInformation: NutritionalInformation,
        nutrientValues: List<NutrientValues>,
        allergyIds: List<String>,
        dietaryIds: List<String>
    ) {
        try {
            productRepository.updateAllProductDetails(
                product = product,
                nutritionalInformation = nutritionalInformation,
                nutrientValues = nutrientValues,
                allergyIds = allergyIds,
                dietaryIds = dietaryIds
            )
            getProductById(product.productId)
        } catch (e: Exception) {
            throw e
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productRepository.deleteProduct(product)
        }
    }

    fun loadAllAllergyStatements() {
        viewModelScope.launch {
            _allAllergyStatements.value = productRepository.getAllAllergyStatements()
        }
    }

    fun loadAllDietaryStatements() {
        viewModelScope.launch {
            _allDietaryStatements.value = productRepository.getAllDietaryStatements()
        }
    }

    suspend fun getOrCreateDepartment(departmentName: String): String {
        return productRepository.getOrCreateDepartment(departmentName)
    }

    fun loadAllDepartments() {
        viewModelScope.launch {
            _allDepartments.value = productRepository.getAllDepartments()
        }
    }

    fun updateProductAndRefreshInMemory(product: Product) {
        viewModelScope.launch {
            updateProduct(product)
            _productByQuery.value = _productByQuery.value.map {
                if (it.product.productId == product.productId) {
                    it.copy(product = product)
                } else it
            }
        }
    }
}