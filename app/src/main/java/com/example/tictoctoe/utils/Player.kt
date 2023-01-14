package com.example.tictoctoe.utils

import com.example.tictoctoe.R

sealed class Player(
    val name:String,
    val icon: Int
) {
    object Player1 : Player(
        name="Player 1",
        icon=R.drawable.ic_circle
    )
    object Player2 : Player(
        name="Player 2",
        icon = R.drawable.ic_cross
    )
}