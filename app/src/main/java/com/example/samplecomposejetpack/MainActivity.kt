package com.example.samplecomposejetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.samplecomposejetpack.ui.theme.SampleComposeJetpackTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp

import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleComposeJetpackTheme{
                mainLayout()
            }
        }
    }
}
@Composable
fun mainLayout(){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "SampleCompose")
                },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    }
                }
            )
        }
    ) {
      // listSample(Modifier.padding(it))
        BodyContent()
    }
}
//owl composable

@Composable
fun CustomGrid(
    modifier: Modifier =Modifier,
    rows:Int =3,
    content:@Composable ()->Unit
){
   Layout(
       modifier = modifier,
       content = content
   ){measurables, constraints ->
       val rowWidths = IntArray(rows){0}

       val rowHeights = IntArray(rows) { 0 }

       val placeables =measurables.mapIndexed { index, measurable ->
           val placeable = measurable.measure(constraints)
           val row = index % rows
           rowWidths[row] += placeable.width
           rowHeights[row] = Math.max(rowHeights[row], placeable.height)
           placeable

       }
       // Grid's width is the widest row
       val width = rowWidths.maxOrNull()?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth
       val height = rowHeights.sumOf { it }
           .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

       val rowY = IntArray(rows) { 0 }
       for (i in 1 until rows) {
           rowY[i] = rowY[i-1] + rowHeights[i-1]
       }
       layout(width, height){
           // x cord we have placed up to, per row
           val rowX = IntArray(rows) { 0 }
           placeables.forEachIndexed { index, placeable ->
               val row = index % rows
               placeable.placeRelative(
                   x = rowX[row],
                   y = rowY[row]
               )
               rowX[row] += placeable.width
           }
       }
   }
}
val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)
@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
        CustomGrid(modifier = modifier) {
            for (topic in topics) {
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }

        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}
@Composable
fun listSample(modifier: Modifier){
    val coroutineScope   = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    Column ( modifier= modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally){
        Row() {
           Button(onClick = { coroutineScope.launch {
               scrollState.animateScrollToItem(1)
           } }) {
               Row() {
                   Icon(Icons.Filled.ArrowDropUp, contentDescription = null)
                   Spacer(modifier = modifier.width(10.dp))
                   Text(text = "arriba")
               }
           }
            Button(onClick = { coroutineScope.launch {
                scrollState.animateScrollToItem(99)
            } }) {
                Row() {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                    Spacer(modifier = modifier.width(10.dp))
                    Text(text = "Abajo")
                }
            }
        }
        LazyColumn(modifier= modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally ,state = scrollState) {
            items(100){
                ImageListItem(it)
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotographerCard( modifier: Modifier=Modifier) {
    Row(
        modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .clickable(onClick = { /* Ignoring onClick */ })
            .padding(16.dp)) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {
            // Image goes here
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text("Alfred Sisley", fontWeight = FontWeight.Bold)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotographerCardPreview() {
    SampleComposeJetpackTheme {
        PhotographerCard()
    }
}

@Composable
fun ImageListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Image(
            painter = rememberImagePainter(
                data = "https://developer.android.com/images/brand/Android_Robot.png"
            ),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text("Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}



