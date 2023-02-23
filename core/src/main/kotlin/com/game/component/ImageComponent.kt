package com.game.component

import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.actor.FlipImage
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier


/**
 * Componente que contiene las entidades que requieran una imagen para dibujar en la pantalla
 */
class ImageComponent  : Comparable<ImageComponent>{
    /**
     * Imagen que reprensenta la entidad
     */
    lateinit var image: FlipImage

    companion object {
        /**
         * Listener que actúa cada vez que se añade el componente
         *
         * @property gameStage Escenario donde se va a renderizar
         *
         * @constructor Crea una ImageComponentListener con valores por defecto
         */
        class ImageComponentListener(
            @Qualifier("gameStage") private val gameStage: Stage,
        ) : ComponentListener<ImageComponent> {
            /**
             *Cuando una entidad es añadida al componente se añade al escenario
             *
             * @param entity Entidad que fui añadida al componente
             * @param component tipo de componente que fue añadido
             */
            override fun onComponentAdded(entity: Entity, component: ImageComponent) {
                // Añade la imagen como actor del escenario que va a dibujar
                gameStage.addActor(component.image)
            }

            /**
             * Se ejecuta cuando se elimina la entidad con el componente imagen al sistema
             *
             * @param entity Entidad que fue añadida al componente
             * @param component Tipo de componente que fue añadido
             */
            override fun onComponentRemoved(entity: Entity, component: ImageComponent) {
                // Elimina al actor del escenario
                gameStage.root.removeActor(component.image)
            }

        }
    }

    override fun compareTo(other: ImageComponent): Int {
        val yDiff = other.image.y.compareTo(image.y)
        return if (yDiff != 0) {
            yDiff
        } else {
            other.image.x.compareTo(image.x)
        }
    }
}