package com.example.tictoctoe.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictoctoe.datamodels.BoxData
import com.example.tictoctoe.utils.BoxStatus
import com.example.tictoctoe.utils.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardVM @Inject constructor(

): ViewModel() {

    private val _winnerPlayer : MutableStateFlow<Player?> = MutableStateFlow(null)
    val winnerPlayer = _winnerPlayer.asStateFlow()

    private val _list : MutableStateFlow<List<BoxData>> = MutableStateFlow(emptyList())
    val list = _list.asStateFlow()

    private val _currentPlayer : MutableStateFlow<Player> = MutableStateFlow(Player.Player1)
    val currentPlayer = _currentPlayer.asStateFlow()

    private val combinationToWin = listOf(
        listOf(0,1,2),
        listOf(3,4,5),
        listOf(6,7,8),
        listOf(0,3,6),
        listOf(1,4,7),
        listOf(2,5,8),
        listOf(0,4,8),
        listOf(2,4,6),
    )
    init {
        resetBoard()
    }

    fun onItemClick(modelBox:BoxData){
        viewModelScope.launch {
            if(modelBox.boxStatus.value == BoxStatus.IDLE && _winnerPlayer.value == null){
                modelBox.boxStatus.value = BoxStatus.FILLED
                modelBox.clickedBy.value = currentPlayer.value
                checkForWinner()
                _currentPlayer.value = if(_currentPlayer.value == Player.Player1) Player.Player2 else Player.Player1
            }
        }
    }

    private suspend fun checkForWinner(){
        val filledBox = _list.value.filter { it.boxStatus.value == BoxStatus.FILLED }
        if(filledBox.size >= 5){
            combinationToWin.forEach { combination ->
                if(filledBox.map { it.id }.containsAll(combination)){
                    val filteredList = filledBox.filter { combination.contains(it.id) }
                    if(checkForPlayer(filteredList)){
                        filteredList.forEach {
                            it.boxStatus.value = BoxStatus.SPECIAL
                        }
                        _winnerPlayer.emit(_currentPlayer.value)
                        return@forEach
                    }
                }
            }
        }
    }

    private fun checkForPlayer(list:List<BoxData>): Boolean{
        return list.distinctBy { it.clickedBy.value }.size == 1
    }

    fun resetBoard(){
        viewModelScope.launch {
            _list.emit(
                 listOf(
                     BoxData(id =0),
                     BoxData(id =1),
                     BoxData(id =2),
                     BoxData(id =3),
                     BoxData(id =4),
                     BoxData(id =5),
                     BoxData(id =6),
                     BoxData(id =7),
                     BoxData(id =8),
                 )
            )
        }

        viewModelScope.launch {
            _winnerPlayer.emit(null)
            _currentPlayer.emit(Player.Player1)
        }

    }
}