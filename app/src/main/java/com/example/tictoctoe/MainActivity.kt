package com.example.tictoctoe

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells.Fixed
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tictoctoe.ui.composables.ClickableBox
import com.example.tictoctoe.ui.theme.TicTocToeTheme
import com.example.tictoctoe.viewmodels.BoardVM

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTocToeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Board()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Board(
    modifier: Modifier = Modifier,
    boardVM: BoardVM = hiltViewModel()
){
    val currentPlayer = boardVM.currentPlayer.collectAsState().value
    val list = boardVM.list.collectAsState().value
    val winnerPlayer = boardVM.winnerPlayer.collectAsState().value
    val isGameFinish = boardVM.isGameFinsh.collectAsState().value

    val context = LocalContext.current
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = linearGradient(
                    colors = listOf(
                        Color(0xFF575757),
                        Color(0xFF181818)
                    )
                )
            )
            .padding(
                vertical = 25.dp,
                horizontal = 20.dp
            )
        ,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text(
            color = Color(0xFFE6E6E6),
            text = "TicTocToe",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            cells = Fixed(3)
        ){
            items(list){ item ->
                ClickableBox(
                    data = item,
                    onclick = { modelBox ->
                        boardVM.onItemClick(modelBox){ isValidClick ->
                            val soundEffect = if(isValidClick) AudioManager.FX_KEY_CLICK else AudioManager.FX_KEYPRESS_INVALID
                            audioManager.playSoundEffect(soundEffect,1.0f)
                        }
                    }
                )
            }
        }

        Text(
            color = Color(0xFFE6E6E6),
            text = "Turn of: ${currentPlayer.name}",
            fontSize = 16.sp,
        )
    }

    if(isGameFinish){
        Popup(
            properties = PopupProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
            ),
            alignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = Color(0xFF00796B),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Text(
                    color = Color(0xFFE6E6E6),
                    text = "Game Finish",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(15.dp))

                winnerPlayer?.name.let { winnerPlayerName ->
                    val textToShow = if(winnerPlayerName != null){
                        "Winner Player: $winnerPlayerName"
                    }else{
                        "Game Finish without any winner"
                    }

                    Text(
                        color = Color(0xFFE6E6E6),
                        text = textToShow,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                }

                Button(
                    modifier = Modifier,
                    onClick = {
                        boardVM.resetBoard()
                    }
                ) {
                    Text(
                        text = "Reset Board",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}