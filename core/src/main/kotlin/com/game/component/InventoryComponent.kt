package com.game.component

import com.github.quillraven.fleks.Entity

class InventoryComponent {
    val items = mutableListOf<Entity>()
    val itemsToAdd = mutableListOf<ItemType>()

    companion object {
        const val INVENTORY_CAPACITY = 18
    }
}