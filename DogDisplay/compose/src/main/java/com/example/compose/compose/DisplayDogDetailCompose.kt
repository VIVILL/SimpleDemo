package com.example.compose.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.GlobalApp
import com.example.compose.model.Dog

// mutableStateOf
//会给变量赋予监听数值变化的能力，从而会触发使用该值的View进行重绘
var showConfirmDialog by mutableStateOf(false)
@Composable
fun DisplayDogDetail(dog: Dog,onChanged: (changedDog: Dog) -> Unit) {
   // val stateDog by remember { mutableStateOf(dog) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        //显示Dog图片
        DogAvatar(
            avatar = dog.avatarFilename,
            name = dog.name
        )
        Spacer(
            modifier = Modifier.requiredHeight(26.dp)
        )
        // Adopt 按钮
        AdoptButton(
            adopted = dog.adopted
        )

        Spacer(
            modifier = Modifier.requiredHeight(26.dp)
        )
        DogIntroduction(
            introduction = dog.introduction
        )
    }
    if (showConfirmDialog) {
        AdoptConfirmDialog(dog = dog){
            onChanged(it)
        }
    }
}

//显示Dog图片
@Composable
fun DogAvatar(avatar: String, name: String) {
    val imageIdentity = GlobalApp.context.resources.getIdentifier(
        avatar, "drawable",
        GlobalApp.context.packageName
    )
    val image: Painter = painterResource(imageIdentity)
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = image,
            contentDescription = name,
            modifier = Modifier
                .requiredSize(150.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

//Dog 介绍
@Composable
fun DogIntroduction(introduction: String) {
    Text(
        text = introduction,
        fontSize = 18.sp,
        style = MaterialTheme.typography.body1
    )
}

@Composable
fun AdoptButton(adopted: Boolean) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { showConfirmDialog = true },
            enabled = !adopted
        ) {
            Text(text = if (adopted) "Adopted" else "Adopt")
        }
    }
}

@Composable
fun AdoptConfirmDialog(dog: Dog,onChanged: (changedDog: Dog) -> Unit) {
    AlertDialog(
        onDismissRequest = {
            showConfirmDialog = false
        },
        text = {
            Text(
                text = "Do you want to adopt this lovely dog?",
                style = MaterialTheme.typography.body2
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    showConfirmDialog = false
                    dog.adopted = true
                    // update
                    onChanged(dog)
                }
            ) {
                Text(
                    text = "Yes"
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    showConfirmDialog = false
                }
            ) {
                Text(
                    text = "No"
                )
            }
        }
    )
}
