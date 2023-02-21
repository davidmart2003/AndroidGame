package com.game.Screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.game.component.InventoryComponent
import com.game.component.ItemCategory
import com.game.component.ItemType
import com.game.component.PlayerComponent
import com.game.model.InventoryModel
import com.game.model.ItemModel
import com.game.ui.view.*
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.scene2d.actors

class InventoryScreen : KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(720f, 480f))
    private val world = world { }
    private val model = InventoryModel(world, stage)
    private val player : Entity
    private lateinit var inventoryVew : InventoryView
    private val inventoryCmps = world.mapper<InventoryComponent>()
    init {
       player = world.entity{
           add<PlayerComponent>()
           add<InventoryComponent>()
       }
    }

    override fun show() {
        stage.clear()
        stage.addListener(model)
        stage.actors {
            inventoryVew = inventoryView(model)

        }
        stage.isDebugAll= false
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width,height)
    }



    override fun render(delta: Float) {
        when {
            Gdx.input.isKeyJustPressed(Keys.R) -> {
                hide()
                show()
            }

            Gdx.input.isKeyJustPressed(Keys.NUM_1) -> {
                inventoryCmps[player].items.clear()
                inventoryVew.clearInventoryAndGear()
            }

            Gdx.input.isKeyJustPressed(Keys.NUM_2) -> {
                inventoryVew.item(ItemModel(-1,ItemCategory.BOOTS,"boots",1,false))
                inventoryCmps[player].itemsToAdd += ItemType.SWORD
            }

            Gdx.input.isKeyJustPressed(Keys.NUM_3) -> {
                inventoryCmps[player].itemsToAdd += ItemType.HELMET
            }

            Gdx.input.isKeyJustPressed(Keys.NUM_4) -> {
                inventoryCmps[player].itemsToAdd += ItemType.ARMOR
            }

            Gdx.input.isKeyJustPressed(Keys.NUM_5) -> {
                inventoryCmps[player].itemsToAdd += ItemType.BOOTS
            }


        }
        stage.act()
        stage.draw()
    }

    override fun dispose() {
        super.dispose()
        stage.dispose()
    }


}