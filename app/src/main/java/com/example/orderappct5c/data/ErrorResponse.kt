package com.example.orderappct5c.data

import androidx.recyclerview.widget.DiffUtil
import com.example.orderappct5c.data.menu.MenuItem


data class ErrorResponse(
    val message : String,
    val httpStatus : Int
)

