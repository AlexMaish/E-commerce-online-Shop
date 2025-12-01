package com.example.onlineshop

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.onlineshop.ViewModel.MainViewModel
import com.example.onlineshop.model.CategoryModel
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
    var showBannerLoading by remember { mutableStateOf(true) }
    var showCategoryLoading by remember { mutableStateOf(true) }

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        /** HEADER **/
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Welcome Back", color = Color.Black)
                    Text(
                        "Alex",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {
                    Image(
                        painter = painterResource(R.drawable.fav_icon),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = painterResource(R.drawable.search_icon),
                        contentDescription = null
                    )
                }
            }
        }

        /** BANNERS **/
        item {
            if (showBannerLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Banners(banners)
            }
        }

        /** CATEGORY TITLE **/
        item { SectionTitle("Categories", "See All") }

        /** CATEGORY LIST **/
        item {
            if (showCategoryLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                CategoryList(categories)
            }
        }
    }
}

/**********************************************
CATEGORY LIST
 **********************************************/
@Composable
fun CategoryList(categories: List<CategoryModel>) {
    var selectedIndex by remember { mutableStateOf(-1) }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {
        items(categories.size) { index ->
            val item = categories[index]
            CategoryItem(
                item = item,
                isSelect = selectedIndex == index,
                onItemClick = { selectedIndex = index }
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
    Row(
        modifier = Modifier
            .clickable { onItemClick(item) }
            .background(
                color = if (isSelect) colorResource(R.color.purple_700) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (isSelect) Color.Transparent else colorResource(R.color.LightGrey)
                ),
            contentAlignment = Alignment.Center
        ) {
            Base64Image(
                base64String = item.picUrl ?: "",
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (isSelect) {
            Text(
                item.title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

/**********************************************
BANNERS
 **********************************************/
@Composable
fun Banners(banners: List<SliderModel>) {
    AutoSlidingCarousel(banners = banners)
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
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
                .height(200.dp)
        ) { page ->

            Base64Image(
                base64String = banners[page].url ?: "",
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        DotIndicator(
            totalDots = banners.size,
            selectedIndex = pagerState.currentPage,
            dotSize = 8.dp
        )
    }
}

/**********************************************
BASE64 IMAGE DECODER
 **********************************************/
@Composable
fun Base64Image(
    base64String: String,
    modifier: Modifier = Modifier
) {
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    LaunchedEffect(base64String) {
        bitmap = try {
            val pure = base64String.substringAfter(",")
            val decoded = Base64.decode(pure, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decoded, 0, decoded.size)
        } catch (e: Exception) {
            null
        }
    }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
}

/**********************************************
DOT INDICATOR
 **********************************************/
@Composable
fun DotIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = colorResource(R.color.purple),
    unSelectedColor: Color = colorResource(R.color.grey),
    dotSize: Dp
) {
    Row(
        modifier = modifier.wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            IndicatorDot(
                color = if (index == selectedIndex) selectedColor else unSelectedColor,
                size = dotSize
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

@Composable
fun IndicatorDot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}

/**********************************************
SECTION TITLE
 **********************************************/
@Composable
fun SectionTitle(title: String, actionText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = actionText,
            color = colorResource(R.color.purple)
        )
    }
}
