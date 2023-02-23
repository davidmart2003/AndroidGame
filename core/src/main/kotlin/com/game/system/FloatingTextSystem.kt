package com.game.system

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.component.FloatingTextComponent
import com.github.quillraven.fleks.*
import ktx.math.vec2


/**
 * Sistema que se encarga de los textos flotantes
 *
 * @property gameStage Escenario donde se renderiza el juego
 * @property uiSTage Escenario donde se renderiza la interfaz de usuario
 * @property txtComponents Conjunto de entidades que contienen FloatingTextComponent
 */
@AllOf([FloatingTextComponent::class])
class FloatingTextSystem(
    @Qualifier("gameStage") private val gameStage: Stage,
    @Qualifier("uiStage") private val uiSTage : Stage,
    private val txtComponents : ComponentMapper<FloatingTextComponent>
) : IteratingSystem() {
    private val uiLocation=vec2()
    private val uiTArget=vec2()
    override fun onTickEntity(entity: Entity) {
        with(txtComponents[entity]){
            if(time>= lifeSpan){
                world.remove(entity)
                return
            }

            time +=deltaTime

            uiLocation.set(txtLocation)
            gameStage.viewport.project(uiLocation)
            uiSTage.viewport.unproject(uiLocation)

            uiTArget.set(txtTarget)
            gameStage.viewport.project(uiTArget)
            uiSTage.viewport.unproject(uiTArget)

            uiLocation.interpolate(uiTArget,(time/lifeSpan).coerceAtMost(1f), Interpolation.pow3OutInverse)
            label.setPosition(uiLocation.x,uiSTage.viewport.worldHeight- uiLocation.y)
        }


    }
}