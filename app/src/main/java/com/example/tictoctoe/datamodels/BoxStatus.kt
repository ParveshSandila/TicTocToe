package com.example.tictoctoe.datamodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.tictoctoe.utils.BoxStatus
import com.example.tictoctoe.utils.Player

data class BoxData(
   val id:Int,
   var boxStatus: MutableState<BoxStatus> = mutableStateOf(BoxStatus.IDLE),
   var clickedBy: MutableState<Player?> = mutableStateOf(null),
)