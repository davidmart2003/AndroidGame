package com.game.component

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier
import ktx.actors.plusAssign
import ktx.math.vec2

/**
 * Componente que contiene una label y la posicion de la entidad
 *
 */
class FloatingTextComponent {
    /**
     *  Localizacion donde aparecera el texto , segun la posicion de la entidad
     */
    val txtLocation = vec2()

    /**
     * Localizacion del texto de la label
     */
    val txtTarget = vec2()

    /**
     * Duracion del texto
     */
    var lifeSpan = 0f

    /**
     * Contador para el deltaTime
     */
    var time = 0f

    /**
     * Etiqueta de texto
     */
    lateinit var label: Label


    companion object {
        /**
         * Listener que actúa cada vez que se añade el componente
         *
         * @property uiStage Escenario donde se renderiza la interfaz de usuario
         */
        class FloatingTextComponentListener(
            @Qualifier("uiStage") private val uiStage: Stage
        ) : ComponentListener<FloatingTextComponent> {

            /**
             * Cuando una entidad es añadida al componente , se crea la label que se desvanece segun la duracion del texto
             *
             * @param entity Entidad que fue añadida al componente
             * @param  component Tipo de componente que fue añadido
             */
            override fun onComponentAdded(entity: Entity, component: FloatingTextComponent) {
                uiStage.addActor(component.label)
                component.label += fadeOut(component.lifeSpan, Interpolation.pow3OutInverse)
                component.txtTarget.set(
                    component.txtLocation.x + MathUtils.random(-1.5f, 1.5f), component.txtLocation.y
                )
            }

            override fun onComponentRemoved(entity: Entity, component: FloatingTextComponent) {
                return
            }

        }
    }
}