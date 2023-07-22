package com.live.fixedassets.`interface`

interface OrderListener {

    fun onItemClicked(position: Int )
    fun onEditClick(position: Int)
    fun onDeleteClick(position: Int)

}