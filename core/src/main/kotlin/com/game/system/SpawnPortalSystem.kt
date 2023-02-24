package com.game.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.game.MyGame.Companion.CREATED
import com.game.MyGame.Companion.UNIT_SCALE
import com.game.component.*
import com.game.event.MapChangeEvent
import com.game.event.SpawnPortalEvent
import com.game.event.fire
import com.github.quillraven.fleks.*
import ktx.actors.stage
import ktx.log.logger
import ktx.math.vec2

@AllOf([PlayerComponent::class])
/**
 * Sistema que se encarga spanear el portal cuadno no haya enemigos
 *
 * @property stage Escenario donde se renderiza el juego
 */
class SpawnPortalSystem(
    @Qualifier("gameStage") private val stage: Stage,
) : IteratingSystem() {
    /**
     * Entidades que contienen EnemyComponents
     */
    val enemies = world.family(allOf = arrayOf(EnemyComponent::class))

    override fun onTickEntity(entity: Entity) {
        if (enemies.numEntities < 1 && !CREATED) {
            stage.fire(SpawnPortalEvent())

            CREATED=true
        }

     //   log.debug { enemies.numEntities.toString() }
    }

//    private fun floatingText(text: String, position: Vector2, size: Vector2) {
//        world.entity {
//            add<FloatingTextComponent> {
//                txtLocation.set(position.x, position.y - size.y * 0.5f)
//                lifeSpan = 5f
//                label = Label(text, floatingTextStyle)
//            }
//        }
//    }

    companion object {
        private val log = logger<LifeSystem>()
    }
}