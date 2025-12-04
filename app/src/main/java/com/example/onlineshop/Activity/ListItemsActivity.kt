package com.example.onlineshop.Activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.onlineshop.R
import com.example.onlineshop.ViewModel.MainViewModel
import com.example.onlineshop.model.ItemsModel

class ListItemsActivity : BaseActivity() {

    private val viewModel = MainViewModel()
    private var id: String = ""
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_items)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        id = intent.getStringExtra("id") ?: ""
        title = intent.getStringExtra("title") ?: ""

        setContent {
            ListItemScreen(
                title = title,
                onBackClick = { finish() },
                viewModel = viewModel,
                id = id
            )
        }
    }
}

@Composable
fun ListItemScreen(
    title: String,
    onBackClick: () -> Unit,
    viewModel: MainViewModel,
    id: String
) {
    // Observe filteredItems instead of recommended
    val items by viewModel.filteredItems.observeAsState(emptyList())
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(id) {
        viewModel.loadFiltered(id)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.padding(top = 36.dp, start = 16.dp, end = 16.dp)) {
            val (backBtn, cartTxt) = createRefs()

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(cartTxt) { centerTo(parent) },
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                text = title
            )

            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .clickable { onBackClick() }
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )
        }

        // Show content or loading
        if (isLoading && items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (items.isEmpty()) {
            // Show empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.search_icon),
                        contentDescription = "Empty",
                        modifier = Modifier.size(64.dp),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No items found in this category",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            ListItemsFullSize(items)
        }
    }

    // Stop loading when items are loaded
    LaunchedEffect(items) {
        if (items.isNotEmpty()) {
            isLoading = false
        }
    }
}

@Composable
fun ListItemsFullSize(items: List<ItemsModel>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items.size) { row ->
            RecommendedItem(items, row)
        }
    }
}

@Composable
fun RecommendedItem(items: List<ItemsModel>, pos: Int) {
    val item = items[pos]

    Column(
        modifier = Modifier
            .padding(8.dp)
            .height(230.dp)
    ) {
        // Use Base64Image to decode the Base64 string
        Base64Images(
            base64String = item.picUrl.firstOrNull() ?: "",
            modifier = Modifier
                .width(175.dp)
                .height(175.dp)
                .background(colorResource(R.color.LightGrey), RoundedCornerShape(10.dp))
                .padding(8.dp)
                .clickable {},
            contentScale = ContentScale.Inside
        )

        // Title
        Text(
            text = item.title,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )

        // ⭐ Rating + Price Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Rating",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.rating.toString(),
                    color = Color.Black,
                    fontSize = 15.sp
                )
            }

            // Price
            Text(
                text = "Ksh ${item.price}",
                color = colorResource(R.color.purple),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Base64Images(
    base64String: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(base64String) {
        Log.d("Base64Images", "Processing base64 string, length: ${base64String.length}")

        bitmap = try {
            // Check if the string is empty
            if (base64String.isEmpty()) {
                errorMessage = "Base64 string is empty"
                null
            } else {
                val pureBase64 = if (base64String.contains(",")) {
                    base64String.substringAfter(",")
                } else {
                    base64String
                }

                Log.d("Base64Images", "Pure base64 length: ${pureBase64.length}")

                val decoded = Base64.decode(pureBase64, Base64.DEFAULT)
                Log.d("Base64Images", "Decoded byte array size: ${decoded.size}")

                val bitmapResult = BitmapFactory.decodeByteArray(decoded, 0, decoded.size)
                if (bitmapResult == null) {
                    errorMessage = "BitmapFactory returned null"
                    Log.d("Base64Images", "BitmapFactory returned null")
                } else {
                    Log.d("Base64Images", "Bitmap decoded successfully: ${bitmapResult.width}x${bitmapResult.height}")
                }
                bitmapResult
            }
        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
            Log.e("Base64Images", "Error decoding base64", e)
            null
        }
        isLoading = false
        Log.d("Base64Images", "Loading complete, bitmap: ${bitmap != null}, error: $errorMessage")
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp,
                color = colorResource(R.color.purple)
            )
        } else {
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    contentScale = contentScale,
                    modifier = Modifier.fillMaxSize()
                )
            } ?: run {
                // Fallback with debug info
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5F5)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.search_icon),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        colorFilter = ColorFilter.tint(Color(0xFFCCCCCC))
                    )
                    if (errorMessage != null) {
                        Text(
                            text = "Decode failed",
                            color = Color.Red,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}