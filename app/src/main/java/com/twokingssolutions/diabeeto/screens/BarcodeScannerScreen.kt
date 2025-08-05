package com.twokingssolutions.diabeeto.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.view.PreviewView.ScaleType.FILL_CENTER
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.util.BarcodeAnalyser
import com.twokingssolutions.diabeeto.db.relation.FullProductDetails
import com.twokingssolutions.diabeeto.model.NavRoutes
import com.twokingssolutions.diabeeto.viewModel.ProductDatabaseViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.Executors
import androidx.compose.material.icons.Icons
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcodeScannerScreen(
    navController: NavController,
    productDatabaseViewModel: ProductDatabaseViewModel = koinViewModel ()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    var cameraProvider: ProcessCameraProvider? by remember { mutableStateOf(null) }
    var isScanning by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    LaunchedEffect(hasCameraPermission) {
        if (hasCameraPermission) {
            isScanning = true
        } else {
            isScanning = false
            showPermissionDialog = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Barcode Scanner") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        content = { Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )}
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showPermissionDialog) {
                AlertDialog(
                    onDismissRequest = { showPermissionDialog = false },
                    title = { Text("Camera Permission Required") },
                    text = { Text("Please grant camera permission to use the barcode scanner.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showPermissionDialog = false
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }) {
                            Text("Grant Permission")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showPermissionDialog = false
                            navController.popBackStack()
                        }) {
                            Text("Cancel")
                        }
                    }
                )
            } else if (hasCameraPermission) {
                if (isScanning) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AndroidView(
                            factory = { context ->
                                val previewView = PreviewView(context).apply {
                                    this.scaleType = FILL_CENTER
                                }
                                val cameraSelector = CameraSelector.Builder()
                                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                    .build()
                                val resolutionSelector = ResolutionSelector.Builder()
                                    .setResolutionStrategy(
                                        ResolutionStrategy(
                                            Size(1280, 720),
                                            ResolutionStrategy.FALLBACK_RULE_CLOSEST_LOWER_THEN_HIGHER
                                        )
                                    )
                                    .build()
                                val preview = Preview.Builder()
                                    .setResolutionSelector(resolutionSelector)
                                    .build()
                                    .also {
                                        it.surfaceProvider = previewView.surfaceProvider
                                    }
                                val imageAnalysis = ImageAnalysis.Builder()
                                    .setResolutionSelector(resolutionSelector)
                                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                    .build()
                                    .also {
                                        it.setAnalyzer(
                                            Executors.newSingleThreadExecutor(),
                                            BarcodeAnalyser { barcode ->
                                                isScanning = false
                                                productDatabaseViewModel.getProductByBarcode(barcode) { product: FullProductDetails? ->
                                                    if (product != null) {
                                                        navController.navigate(NavRoutes.SearchResultsRoute(queryString = "barcode:$barcode"))
                                                    } else {
                                                        coroutineScope.launch {
                                                            val snackbarResult = snackbarHostState.showSnackbar(
                                                                message = "Barcode not found in the database.",
                                                                actionLabel = "Add Product",
                                                                withDismissAction = true
                                                            )
                                                            if (snackbarResult == SnackbarResult.ActionPerformed) {
                                                                navController.navigate(NavRoutes.AddProductItemRoute)
                                                            } else {
                                                                isScanning = true
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        )
                                    }

                                coroutineScope.launch {
                                    try {
                                        cameraProvider = ProcessCameraProvider.getInstance(context).get()
                                        cameraProvider?.unbindAll()
                                        cameraProvider?.bindToLifecycle(
                                            lifecycleOwner,
                                            cameraSelector,
                                            preview,
                                            imageAnalysis
                                        )
                                    } catch (e: Exception) {
                                        println("DEBUG: ${e.message}")
                                    }
                                }
                                previewView
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                Text(
                    text = "Camera permission not granted. Please enable it in settings.",
                    modifier = Modifier.padding(16.dp)
                )
                Button(onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }) {
                    Text("Request Permission")
                }
            }
        }
    }
}