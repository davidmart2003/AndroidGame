package com.game.model

import com.game.component.ItemCategory

data class ItemModel(
    val itemEntityId: Int,
    val category: ItemCategory,
    val atlasKey: String,
    var slotIdx: Int,
    var equipped: Boolean
)