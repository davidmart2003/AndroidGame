package com.game.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.game.MyGame.Companion.ATTACK
import com.game.MyGame.Companion.LIFE
import com.game.MyGame.Companion.SPEED
import com.game.event.HideInventoryEvent
import com.game.event.fire
import com.game.model.GameModel
import com.game.widget.PlayerStats
import com.game.widget.playerStats
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.scene2d.*

class InventoryView(
    model: GameModel,
    skin: Skin
) : KTable, Table(skin) {

    var lvl: Label
    var life: Label
    var speed: Label
    var attack: Label
    var  back : TextButton



    init {
        // UI
        setFillParent(true)
        val titlePadding = 15f

        table {
            background = skin[Drawables.FRAME_BGD]


            this@InventoryView.lvl = label(text = "Your Level: 1", style = Labels.FRAME.skinKey, skin) {
                this.setAlignment(Align.center)

            }
            this@InventoryView.life = label(text = "Your Max Life: $LIFE", style = Labels.FRAME.skinKey, skin) {
                this.setAlignment(Align.center)
                it.row()
            }
            this@InventoryView.attack = label(text = "Your total damage: $ATTACK", style = Labels.FRAME.skinKey, skin) {
                this.setAlignment(Align.center)

            }
            this@InventoryView.speed = label(text = "Your actual speed : $SPEED", style = Labels.FRAME.skinKey, skin) {
                this.setAlignment(Align.center)
                it.row()
            }

            this@InventoryView.back = textButton( text="Back", style = Buttons.DEFAULT.skinKey ){
                onClick { stage.fire(HideInventoryEvent()) }
                it.right()

            }

//            table { invTableCell ->
//                for (i in 1..18) {
//                    this@InventoryView.invSlots += inventorySlot(skin = skin) { slotCell ->
//                        slotCell.padBottom(10f)
//                        if (i % 6 == 0) {
//                            slotCell.row()
//                        } else {
//                            slotCell.padRight(10f)
//                        }
//                    }
//                }
//
//                invTableCell.expand().fill()
//            }
//
//            outerTableCell.expand().width(300f).height(200f).left().center()
//        }

//        table { gearTableCell ->
//            background = skin[Drawables.FRAME_BGD]
//
//            label(text = "Gear", style = Labels.FRAME.skinKey, skin) {
//                this.setAlignment(Align.center)
//                it.expandX().fill()
//                    .pad(8f, titlePadding, 0f, titlePadding)
//                    .top()
//                    .row()
//            }
//
//            table { invTableCell ->
//                this@InventoryView.gearSlots += inventorySlot(Drawables.INVENTORY_SLOT_HELMET, skin) {
//                    it.padBottom(2f).colspan(2).row()
//                }
//                this@InventoryView.gearSlots += inventorySlot(Drawables.INVENTORY_SLOT_WEAPON, skin) {
//                    it.padBottom(2f).padRight(2f)
//                }
//                this@InventoryView.gearSlots += inventorySlot(Drawables.INVENTORY_SLOT_ARMOR, skin) {
//                    it.padBottom(2f).padRight(2f).row()
//                }
//                this@InventoryView.gearSlots += inventorySlot(Drawables.INVENTORY_SLOT_BOOTS, skin) {
//                    it.colspan(2).row()
//                }
//                invTableCell.expand().fill()
//            }
//
//            gearTableCell.expand().width(90f).height(200f).left().center()
//        }
            it.expand().fill().left().top()
        }


        // data binding

        model.onPropertyChange(GameModel::level) { level ->
            this@InventoryView.lvl.setText("Your level: $level")
        }
        model.onPropertyChange(GameModel::life) { life ->
            this@InventoryView.life.setText("Actual MaxLife: $life hp")
        }
        model.onPropertyChange(GameModel::speed) { speed ->
            this@InventoryView.speed.setText("Actual speed: $speed")
        }
        model.onPropertyChange(GameModel::attack) { attack ->
            this@InventoryView.attack.setText("Actual damage $attack")
        }

//        model.onPropertyChange(InventoryModel::playerItems) { itemModels ->
//            clearInventoryAndGear()
//            itemModels.forEach {
//                if (it.equipped) {
//                    gear(it)
//                } else {
//                    item(it)
//                }
//            }
    }


//    fun clearInventoryAndGear() {
//        invSlots.forEach { it.item(null) }
//        gearSlots.forEach { it.item(null) }
//    }


//    private fun onItemDropped(
//        sourceSlot: InventorySlot,
//        targetSlot: InventorySlot,
//        itemModel: ItemModel
//    ) {
//        if (sourceSlot == targetSlot) {
//            // item dropped on same slot -> do nothing
//            return
//        }
//
//        // swap slot items in UI
//        sourceSlot.item(targetSlot.itemModel)
//        targetSlot.item(itemModel)
//
//        // update model
//        val sourceItem = sourceSlot.itemModel // this can be null if the target slot was empty
//        if (sourceSlot.isGear) {
//            model.equip(itemModel, false)
//            if (sourceItem != null) {
//                model.equip(sourceItem, true)
//            }
//        } else if (sourceItem != null) {
//            model.inventoryItem(invSlots.indexOf(sourceSlot), sourceItem)
//        }
//
//        if (targetSlot.isGear) {
//            if (sourceItem != null) {
//                model.equip(sourceItem, false)
//            }
//            model.equip(itemModel, true)
//        } else {
//            model.inventoryItem(invSlots.indexOf(targetSlot), itemModel)
//        }
//    }
//
//    fun item(itemModel: ItemModel) {
//        invSlots[itemModel.slotIdx].item(itemModel)
//    }
//
//    private fun gear(itemModel: ItemModel) {
//        gearSlots.firstOrNull { it.supportedCategory == itemModel.category }?.item(itemModel)
//    }
}

@Scene2dDsl
fun <S> KWidget<S>.inventoryView(
    model: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: InventoryView.(S) -> Unit = {}
): InventoryView = actor(InventoryView(model, skin), init)
