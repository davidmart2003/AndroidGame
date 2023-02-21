package com.game.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.utils.Align
import com.game.model.InventoryModel
import com.game.model.ItemModel
import com.game.widget.InventorySlot
import com.game.widget.inventorySlot
import ktx.scene2d.*

class InventoryView(
    private val model: InventoryModel,
    skin: Skin
) : KTable, Table(skin) {

    private val invSlots = mutableListOf<InventorySlot>()
    private val gearSlots = mutableListOf<InventorySlot>()

    init {
        // UI
        setFillParent(true)
        val titlePadding = 15f

        table { outerTableCell ->
            background = skin[Drawables.FRAME_BGD]


            label(text = "Inventory", style = Labels.FRAME.skinKey, skin) {
                this.setAlignment(Align.center)
                it.expandX().fill()
                    .pad(8f, titlePadding, 0f, titlePadding)
                    .top()
                    .row()
            }

            table { invTableCell ->
                for (i in 1..18) {
                    this@InventoryView.invSlots += inventorySlot(skin = skin) { slotCell ->
                        slotCell.padBottom(10f)
                        if (i % 6 == 0) {
                            slotCell.row()
                        } else {
                            slotCell.padRight(10f)
                        }
                    }
                }

                invTableCell.expand().fill()
            }

            outerTableCell.expand().width(300f).height(200f).left().center()
        }

        table { gearTableCell ->
            background = skin[Drawables.FRAME_BGD]

            label(text = "Gear", style = Labels.FRAME.skinKey, skin) {
                this.setAlignment(Align.center)
                it.expandX().fill()
                    .pad(8f, titlePadding, 0f, titlePadding)
                    .top()
                    .row()
            }

            table { invTableCell ->
                this@InventoryView.gearSlots += inventorySlot(Drawables.INVENTORY_SLOT_HELMET, skin) {
                    it.padBottom(2f).colspan(2).row()
                }
                this@InventoryView.gearSlots += inventorySlot(Drawables.INVENTORY_SLOT_WEAPON, skin) {
                    it.padBottom(2f).padRight(2f)
                }
                this@InventoryView.gearSlots += inventorySlot(Drawables.INVENTORY_SLOT_ARMOR, skin) {
                    it.padBottom(2f).padRight(2f).row()
                }
                this@InventoryView.gearSlots += inventorySlot(Drawables.INVENTORY_SLOT_BOOTS, skin) {
                    it.colspan(2).row()
                }
                invTableCell.expand().fill()
            }

            gearTableCell.expand().width(90f).height(200f).left().center()
        }



        // data binding
        model.onPropertyChange(InventoryModel::playerItems) { itemModels ->
            clearInventoryAndGear()
            itemModels.forEach {
                if (it.equipped) {
                    gear(it)
                } else {
                    item(it)
                }
            }
        }
    }

    fun clearInventoryAndGear() {
        invSlots.forEach { it.item(null) }
        gearSlots.forEach { it.item(null) }
    }



    private fun onItemDropped(
        sourceSlot: InventorySlot,
        targetSlot: InventorySlot,
        itemModel: ItemModel
    ) {
        if (sourceSlot == targetSlot) {
            // item dropped on same slot -> do nothing
            return
        }

        // swap slot items in UI
        sourceSlot.item(targetSlot.itemModel)
        targetSlot.item(itemModel)

        // update model
        val sourceItem = sourceSlot.itemModel // this can be null if the target slot was empty
        if (sourceSlot.isGear) {
            model.equip(itemModel, false)
            if (sourceItem != null) {
                model.equip(sourceItem, true)
            }
        } else if (sourceItem != null) {
            model.inventoryItem(invSlots.indexOf(sourceSlot), sourceItem)
        }

        if (targetSlot.isGear) {
            if (sourceItem != null) {
                model.equip(sourceItem, false)
            }
            model.equip(itemModel, true)
        } else {
            model.inventoryItem(invSlots.indexOf(targetSlot), itemModel)
        }
    }

    fun item(itemModel: ItemModel) {
        invSlots[itemModel.slotIdx].item(itemModel)
    }

    private fun gear(itemModel: ItemModel) {
        gearSlots.firstOrNull { it.supportedCategory == itemModel.category }?.item(itemModel)
    }
}

@Scene2dDsl
fun <S> KWidget<S>.inventoryView(
    model: InventoryModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: InventoryView.(S) -> Unit = {}
): InventoryView = actor(InventoryView(model, skin), init)
