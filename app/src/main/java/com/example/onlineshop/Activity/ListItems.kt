package com.example.onlineshop.Activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.onlineshop.R
import com.example.onlineshop.model.ItemsModel


@Composable
fun ListItems(items: List<ItemsModel>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .height(500.dp)
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
fun ListItemsFullSizes(items: List<ItemsModel>) {
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
fun RecommendedItems(items: List<ItemsModel>, pos: Int) {
    val item = items[pos]

    Column(
        modifier = Modifier
            .padding(8.dp)
            .height(230.dp)
    ) {
        AsyncImage(
            model = item.picUrl.firstOrNull(),
            contentDescription = item.title,
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
                androidx.compose.foundation.Image(
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
