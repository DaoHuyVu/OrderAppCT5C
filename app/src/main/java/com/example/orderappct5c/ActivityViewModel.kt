package com.example.orderappct5c

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor() : ViewModel(){
    private val _activityUiState = MutableLiveData(ActivityUiState())
    val activityUiState get() = _activityUiState

    fun toHomeScreen(){
        _activityUiState.value = ActivityUiState(
            navigation = R.navigation.home_nav_graph,
            isShowBottomNavView = true,
            isShowToolbar = true
        )
    }
    fun toAuthScreen(){
        _activityUiState.value = ActivityUiState(
            navigation = R.navigation.auth_nav_graph,
        )
    }
}