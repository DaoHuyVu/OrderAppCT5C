package com.example.orderappct5c.ui

open class Item (
    val id : Long,
){
    override fun equals(other: Any?): Boolean {
        return this.id == (other as Item).id
    }

    override fun hashCode(): Int {
        return this.hashCode();
    }
}