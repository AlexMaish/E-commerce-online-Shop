package com.example.onlineshop.Activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.onlineshop.R
import com.example.onlineshop.ViewModel.MainViewModel
import com.example.onlineshop.model.CategoryModel
import com.example.onlineshop.model.ItemsModel
import com.example.onlineshop.model.SliderModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityScreen() }
    }
}

@Composable
fun MainActivityScreen() {

    val viewModel = remember { MainViewModel() }

    var banners by remember { mutableStateOf(emptyList<SliderModel>()) }
    var categories by remember { mutableStateOf(emptyList<CategoryModel>()) }
    var recommended by remember { mutableStateOf(emptyList<ItemsModel>()) }

    var showBannerLoading by remember { mutableStateOf(true) }
    var showCategoryLoading by remember { mutableStateOf(true) }
    var showRecommendedLoading by remember { mutableStateOf(true) }

    /** Load Banners **/
    LaunchedEffect(Unit) {
        viewModel.loadBanner()
        viewModel.banners.observeForever {
            banners = it
            showBannerLoading = false
        }
    }

    /** Load Categories **/
    LaunchedEffect(Unit) {
        viewModel.loadCategory()
        viewModel.categories.observeForever {
            categories = it
            showCategoryLoading = false
        }
    }

    LaunchedEffect(Unit){
        viewModel.loadRecommended()
        viewModel.recommended.observeForever {
            recommended = it
            showRecommendedLoading = false
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            /**  HEADER **/
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Welcome Back,",
                            color = Color(0xFF666666),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Alex",
                            color = colorResource(R.color.purple_700),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Text(
                            "Let's find something amazing!",
                            color = Color(0xFF888888),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    // Bell icon with notification
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF5F5F5))
                                .clickable { /* Handle notification click */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.bell_icon),
                                contentDescription = "Notifications",
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Notification badge
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "3",
                                color = Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            /** BANNERS **/
            item {
                if (showBannerLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .padding(top = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(R.color.purple),
                            strokeWidth = 2.dp
                        )
                    }
                } else {
                    Banners(banners)
                }
            }

            /** CATEGORY SECTION **/
            item {
                SectionTitle(
                    title = "Categories",
                    actionText = "See All",
                    gradientColorStart = colorResource(R.color.purple),
                    gradientColorEnd = colorResource(R.color.purple_700)
                )
            }

            /** CATEGORY LIST **/
            item {
                if (showCategoryLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(R.color.purple),
                            strokeWidth = 2.dp
                        )
                    }
                } else {
                    CategoryList(categories)
                }
            }

            /** RECOMMENDATION SECTION **/
            item {
                SectionTitle(
                    title = "Recommendation",
                    actionText = "See All",
                    gradientColorStart = Color(0xFFFF6B6B),
                    gradientColorEnd = Color(0xFFFF8E53)
                )
            }

            /** RECOMMENDATION LIST **/
            item {
                if (showRecommendedLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = colorResource(R.color.purple),
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Loading Recommendations...",
                                color = Color(0xFF666666),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.3.sp
                            )
                        }
                    }
                } else {
                    RecommendedGrid(items = recommended)
                }
            }

            item{
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Bottom Menu positioned at the bottom
        BottomMenu(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}


@Composable
fun SectionTitle(
    title: String,
    actionText: String,
    gradientColorStart: Color = colorResource(R.color.purple),
    gradientColorEnd: Color = colorResource(R.color.purple_700)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Title with gradient
        Text(
            text = title,
            style = TextStyle(
                brush = Brush.horizontalGradient(
                    colors = listOf(gradientColorStart, gradientColorEnd)
                ),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            ),
            modifier = Modifier
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(4.dp),
                    clip = false
                )
        )

        // See All button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            gradientColorStart.copy(alpha = 0.1f),
                            gradientColorEnd.copy(alpha = 0.1f)
                        )
                    )
                )
                .clickable { /*  see all */ }
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = actionText,
                    color = gradientColorStart,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    painter = painterResource(R.drawable.search_icon), // Arrow icon
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    colorFilter = ColorFilter.tint(gradientColorStart)
                )
            }
        }
    }
}


@Composable
fun BottomMenu(
    modifier: Modifier = Modifier,
    onExplorerClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onOrdersClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 32.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                clip = true
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(R.color.purple_700),
                        colorResource(R.color.purple)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                ),
                shape = RoundedCornerShape(24.dp)
            ),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomMenuItem(
            icon = painterResource(R.drawable.btn_1),
            text = "Explorer",
            onItemClick = onExplorerClick
        )
        BottomMenuItem(
            icon = painterResource(R.drawable.btn_2),
            text = "Cart",
            onItemClick = onCartClick
        )
        BottomMenuItem(
            icon = painterResource(R.drawable.btn_3),
            text = "Favorite",
            onItemClick = onFavoriteClick
        )
        BottomMenuItem(
            icon = painterResource(R.drawable.btn_4),
            text = "Orders",
            onItemClick = onOrdersClick
        )
        BottomMenuItem(
            icon = painterResource(R.drawable.btn_5),
            text = "Profile",
            onItemClick = onProfileClick
        )
    }
}

@Composable
fun BottomMenuItem(
    icon: Painter,
    text: String,
    onItemClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .height(64.dp)
            .clickable { onItemClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.2.sp
        )
    }
}


@Composable
fun CategoryList(categories: List<CategoryModel>) {
    var selectedIndex by remember { mutableStateOf(-1) }
val context = LocalContext.current
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        items(categories.size) { index ->
            val item = categories[index]
            CategoryItem(
                item = item,
                isSelect = selectedIndex == index,
                onItemClick = {
                    selectedIndex = index
                    Handler(Looper.getMainLooper()).postDelayed({
                        val  intent= Intent(context, ListItemsActivity::class.java).apply {
                            putExtra("id",categories[index].id.toString())
                            putExtra("title",categories[index].title)
                        }
                        startActivity(context, intent,null)
                    }, 1000)
                }

            )
        }
    }
}

@Composable
fun CategoryItem(
    item: CategoryModel,
    isSelect: Boolean,
    onItemClick: (CategoryModel) -> Unit
) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable { onItemClick(item) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon container with gradient when selected
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(
                    if (isSelect) {
                        Brush.radialGradient(
                            colors = listOf(
                                colorResource(R.color.purple),
                                colorResource(R.color.purple_700)
                            ),
                            center = Offset(0.3f, 0.3f)
                        )
                    } else {
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFF0F0F0),
                                Color(0xFFE0E0E0)
                            )
                        )
                    }
                )
                .shadow(
                    elevation = if (isSelect) 8.dp else 2.dp,
                    shape = CircleShape,
                    clip = true
                ),
            contentAlignment = Alignment.Center
        ) {
            Base64Image(
                base64String = item.picUrl ?: "",
                modifier = Modifier.size(32.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Category title
        Text(
            text = item.title,
            color = if (isSelect) colorResource(R.color.purple) else Color(0xFF666666),
            fontSize = 11.sp,
            fontWeight = if (isSelect) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}


//BANNERS with dot design

@Composable
fun Banners(banners: List<SliderModel>) {
    if (banners.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color(0xFFF8F8F8)),
            contentAlignment = Alignment.Center
        ) {
            Text("No banners available", color = Color.Gray)
        }
    } else {
        AutoSlidingCarousel(banners = banners)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoSlidingCarousel(
    modifier: Modifier = Modifier,
    banners: List<SliderModel>
) {
    val pagerState = rememberPagerState(pageCount = { banners.size })
    val coroutine = rememberCoroutineScope()

    /** Auto-slide **/
    LaunchedEffect(pagerState.currentPage) {
        delay(3000)
        val next = (pagerState.currentPage + 1) % banners.size
        coroutine.launch { pagerState.animateScrollToPage(next) }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(top = 8.dp, start = 20.dp, end = 20.dp)
        ) { page ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(20.dp),
                        clip = true
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Base64Image(
                    base64String = banners[page].url ?: "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        DotIndicator(
            totalDots = banners.size,
            selectedIndex = pagerState.currentPage,
            dotSize = 10.dp,
            selectedColor = colorResource(R.color.purple),
            unSelectedColor = Color(0xFFDDDDDD)
        )
    }
}


//RECOMMENDED ITEMS GRID

@Composable
fun RecommendedGrid(items: List<ItemsModel>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            RecommendedItemCard(item = item)
        }
    }
}

@Composable
fun RecommendedItemCard(item: ItemsModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                if (item.picUrl.isNotEmpty()) {
                    Base64Image(
                        base64String = item.picUrl[0],
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                        contentScale = ContentScale.Fit
                    )

                } else {
                    // Placeholder if no image
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFF5F5F5),
                                        Color(0xFFEEEEEE)
                                    )
                                )
                            )
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Image", color = Color.Gray)
                    }
                }

                // Favorite button overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .clickable { /* Add to favorites */ },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.fav_icon),
                        contentDescription = "Favorite",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Product Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                // Title
                Text(
                    text = item.title,
                    color = Color(0xFF333333),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    letterSpacing = (-0.2).sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Rating and Price Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating with stylish background
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFFF8E1))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = "Rating",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = item.rating.toString(),
                            color = Color(0xFFE65100),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Price with gradient
                    Text(
                        text = "Ksh ${item.price}",
                        style = TextStyle(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    colorResource(R.color.purple),
                                    colorResource(R.color.purple_700)
                                )
                            ),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.3).sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                //  Show description if available
                if (item.description.isNotEmpty()) {
                    Text(
                        text = item.description,
                        color = Color(0xFF777777),
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


//BASE64 IMAGE DECODER

@Composable
fun Base64Image(
    base64String: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(base64String) {
        bitmap = try {
            val pureBase64 = if (base64String.contains(",")) {
                base64String.substringAfter(",")
            } else {
                base64String
            }
            val decoded = Base64.decode(pureBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decoded, 0, decoded.size)
        } catch (e: Exception) {
            null
        }
        isLoading = false
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
                // Fallback image if Base64 decoding fails
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.search_icon),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        colorFilter = ColorFilter.tint(Color(0xFFCCCCCC))
                    )
                }
            }
        }
    }
}

@Composable
fun DotIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = colorResource(R.color.purple),
    unSelectedColor: Color = Color(0xFFDDDDDD),
    dotSize: Dp
) {
    Row(
        modifier = modifier.wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            // Animated dot indicator
            val animatedSize by animateDpAsState(
                targetValue = if (index == selectedIndex) dotSize * 1.5f else dotSize,
                animationSpec = tween(durationMillis = 200)
            )

            IndicatorDot(
                color = if (index == selectedIndex) selectedColor else unSelectedColor,
                size = animatedSize,
                isSelected = index == selectedIndex
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun IndicatorDot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color,
    isSelected: Boolean = false
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
            .shadow(
                elevation = if (isSelected) 4.dp else 1.dp,
                shape = CircleShape,
                clip = true
            )
    )
}