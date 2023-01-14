package com.example.tictoctoe.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.radialGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tictoctoe.datamodels.BoxData
import com.example.tictoctoe.utils.BoxStatus
import com.example.tictoctoe.utils.Player

@Composable
fun ClickableBox(
    modifier: Modifier = Modifier,
    data:BoxData,
    onclick: (BoxData) -> Unit
) {

    Box(
       modifier = modifier
           .fillMaxSize()
           .aspectRatio(1f)
           .border(
               brush = radialGradient(
                   listOf(
                       Color(0xFFB2EBF2),
                       Color(0xFFC8E6C9)
                   )
               ),
               width = 2.dp,
               shape = RectangleShape
           )
           .background(
               color = if(data.boxStatus.value == BoxStatus.SPECIAL){
                   Color(0x2DE0F7FA)
               }else{
                   Color.Transparent
               }
           )
           .clickable {
               onclick(data)
           },
        contentAlignment = Alignment.Center
    ){
        data.clickedBy.value?.let { player ->
            Icon(
                modifier = modifier.fillMaxSize(0.7f),
                painter = painterResource(id = player.icon),
                contentDescription = "",
                tint = if(player == Player.Player1)
                            Color(0xFF81D4FA)
                        else
                            Color(0xFFFFCC80)
            )
        }
    }
}