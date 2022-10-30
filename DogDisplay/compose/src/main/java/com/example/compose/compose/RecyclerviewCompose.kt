package com.example.compose.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.GlobalApp
import com.example.compose.model.Dog


@Composable
fun DisplayDogListWithOnClick(dogList: List<Dog>,onClick: (position: Int,dog: Dog)-> Unit){
    LazyColumn {
        itemsIndexed(dogList) { position, dog ->
            //显示
            DisplayDogItem(position,dog,onClick)
        }
    }
}

@Composable
fun DisplayDogItem(position: Int, dog: Dog, onClick: (position: Int, dog: Dog) -> Unit) {
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .requiredHeight(220.dp)
            .clickable { onClick(position, dog) }
    ) {
        CardItem(name = dog.name, avatar = dog.avatarFilename, isAdopted = dog.adopted)
    }
}

@Composable
fun CardItem(name: String, avatar: String, isAdopted :Boolean) {
    val imageIdentity = GlobalApp.context.resources.getIdentifier(
        avatar, "drawable",
        GlobalApp.context.packageName
    )
    val image: Painter = painterResource(imageIdentity)
    Image(
        painter = image,
        contentDescription = name,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Surface(
            color = Color(0x99000000),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isAdopted){
                    "$name,Adopted"
                }else{
                    "$name,Not Adopted"
                },
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

