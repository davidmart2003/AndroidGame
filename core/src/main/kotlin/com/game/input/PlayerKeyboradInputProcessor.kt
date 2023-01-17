package com.game.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.game.Component.AttackComponent
import com.game.Component.MoveComponent
import com.game.Component.PlayerComponent
import com.github.quillraven.fleks.ComponentMapper
import ktx.app.KtxInputAdapter

class PlayerKeyboardInputProcessor(
    private val world: com.github.quillraven.fleks.World,
    private val moveComponent: ComponentMapper<MoveComponent> = world.mapper(),
    private val attackComponents: ComponentMapper<AttackComponent> = world.mapper()
) : KtxInputAdapter {

    private var playerSeno = 0f
    private var playerCoseno = 0f
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))

    init {
        Gdx.input.inputProcessor = this


    }

    private fun Int.isMovementKey(): Boolean {
        return this == UP || this == DOWN || this == RIGHT || this == LEFT
    }

    private fun updatePlayerMovement() {
        playerEntities.forEach { player ->
            with(moveComponent[player]) {
                cos = playerCoseno
                sin = playerSeno
            }
        }
    }


    override fun keyDown(keycode: Int): Boolean {
        if (keycode.isMovementKey()) {
            when (keycode) {
                UP -> playerSeno = 1f
                DOWN -> playerSeno = -1f
                RIGHT -> playerCoseno = 1f
                LEFT -> playerCoseno = -1f
            }
            updatePlayerMovement()
            return true
        } else
            if (keycode == SPACE) {
                playerEntities.forEach {
                    with(attackComponents[it]) {
                        doAttack = true
                        startAttack()
                    }
                }
                return true
            }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode.isMovementKey()) {
            when (keycode) {
                UP -> playerSeno = if (Gdx.input.isKeyPressed(DOWN)) -1f else 0f
                DOWN -> playerSeno = if (Gdx.input.isKeyPressed(UP)) 1f else 0f
                RIGHT -> playerCoseno = if (Gdx.input.isKeyPressed(LEFT)) -1f else 0f
                LEFT -> playerCoseno = if (Gdx.input.isKeyPressed(RIGHT)) 1f else 0f
            }
            updatePlayerMovement()
            return true
        }
        return false
    }
}