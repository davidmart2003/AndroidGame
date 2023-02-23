//package com.game.input
//
//import com.badlogic.gdx.Gdx
//import com.badlogic.gdx.Input.Keys.*
//import com.badlogic.gdx.maps.tiled.TiledMap
//import com.badlogic.gdx.maps.tiled.TmxMapLoader
//import com.badlogic.gdx.scenes.scene2d.Event
//import com.badlogic.gdx.scenes.scene2d.EventListener
//import com.badlogic.gdx.scenes.scene2d.Stage
//import com.game.component.*
//import com.game.event.ButtonPressedEvent
//import com.game.event.MapChangeEvent
//import com.game.event.fire
//import com.github.quillraven.fleks.ComponentMapper
//import com.github.quillraven.fleks.Qualifier
//import ktx.app.KtxInputAdapter
//
//class PlayerKeyboardInputProcessor(
//    private val world: com.github.quillraven.fleks.World,
//    private val moveComponent: ComponentMapper<MoveComponent> = world.mapper(),
//    private val attackComponents: ComponentMapper<AttackComponent> = world.mapper(),
//    private val shieldComponents: ComponentMapper<ShieldComponents> = world.mapper()
//) : KtxInputAdapter, EventListener {
//    private var currentMap: TiledMap? = null
//    private var playerSeno = 0f
//    private var playerCoseno = 0f
//    private val playerEntities =
//        world.family(allOf = arrayOf(PlayerComponent::class), noneOf = arrayOf(SpawnComponent::class))
//
//    init {
//        Gdx.input.inputProcessor = this
//    }
//
//    private fun Int.isMovementKey(): Boolean {
//        return this == UP || this == DOWN || this == RIGHT || this == LEFT
//    }
//
//    private fun updatePlayerMovement() {
//        playerEntities.forEach { player ->
//            with(moveComponent[player]) {
//                cos = playerCoseno
//                sin = playerSeno
//            }
//        }
//    }
//
//
//    override fun keyDown(keycode: Int): Boolean {
//        if (keycode.isMovementKey()) {
//            when (keycode) {
//                UP -> playerSeno = 1f
//                DOWN -> playerSeno = -1f
//                RIGHT -> playerCoseno = 1f
//                LEFT -> playerCoseno = -1f
//            }
//            playerEntities.forEach {
//                with(shieldComponents[it]) {
//                    holdingShield = false
//                }
//            }
//            updatePlayerMovement()
//            return true
//        } else
//            if (keycode == SPACE) {
//
//                playerEntities.forEach {
//                    with(attackComponents[it]) {
//                        doAttack = true
//                    }
//                    with(shieldComponents[it]) {
//                        holdingShield = false
//                    }
//                }
//                return true
//            }
//        if (keycode == G) {
//            playerEntities.forEach {
//                with(shieldComponents[it]) {
//                    doShield = true
//                }
//            }
//        }
//        return false
//    }
//
//    override fun keyUp(keycode: Int): Boolean {
//        if (keycode.isMovementKey()) {
//            when (keycode) {
//                UP -> playerSeno = if (Gdx.input.isKeyPressed(DOWN)) -1f else 0f
//                DOWN -> playerSeno = if (Gdx.input.isKeyPressed(UP)) 1f else 0f
//                RIGHT -> playerCoseno = if (Gdx.input.isKeyPressed(LEFT)) -1f else 0f
//                LEFT -> playerCoseno = if (Gdx.input.isKeyPressed(RIGHT)) 1f else 0f
//            }
//            updatePlayerMovement()
//            return true
//        }
//        if (keycode == G) {
//            playerEntities.forEach {
//                with(shieldComponents[it]) {
//                    doShield = false
//                }
//            }
//        }
//        return false
//    }
//
//    override fun handle(event: Event?): Boolean {
//        when (event) {
//
//
//            else -> return false
//        }
//        return true
//    }
//}