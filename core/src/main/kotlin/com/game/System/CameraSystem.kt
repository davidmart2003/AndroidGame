package com.game.System

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.Component.ImageComponent
import com.game.Component.PhysicComponent
import com.game.Component.PlayerComponent
import com.game.event.MapChangeEvent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.tiled.height
import ktx.tiled.width


/**
 * El sistema de movimiento de la camera , haremos que la camera siga al jugador principal
 * @param stage Escenario donde esta nuestra camera a mover
 * @param Imagen del componente para saber donde esta en todo momento , en este caso la imagen del jugador principal
 */
@AllOf([PlayerComponent::class,ImageComponent::class])
class CameraSystem(
    private val imageComponents : ComponentMapper<ImageComponent>,
    stage: Stage
) :EventListener, IteratingSystem() {

    private var maxWidth =0f
    private var maxHeight =0f
    private val camera = stage.camera

    /**
     * Cada actualizacion la camera se refrescará y cogera la posicion de la imagen
     * del jugador pincippal, cogiendo como maximo el ancho y alto del  viewport para qie la camera no se
     * sobresalga de nuestra pantalla.
     * CoerceIn hace que si la imagen en la posicion x llega a un valor mayor maximo de la pantalla(Lo que mide nuestro TileMap)
     * coja el valor maximo que le values en nuestro caso el tamaño del tileMap menos la mitad de lo que del ancho de la camera porque el 0,0
     * de la camera es el centro de ahi coger la mitad y que el minimo valor posible para la camera
     * sea la mitad del viewport especifico
     * @param entity Jugador principal
     */
    override fun onTickEntity(entity: Entity) {

        val viewWidth= camera.viewportWidth*0.5f
        val viewHeight = camera.viewportHeight*0.5f

        with(imageComponents[entity]){
            camera.position.set(
                image.x.coerceIn(viewWidth,maxWidth-viewWidth),
                image.y.coerceIn(viewHeight,maxHeight-viewHeight),
                camera.position.z
            )
        }
    }

    /**
     * Evento de tiledMap que sirve para copger el tamaño maximo del mapa tanto por ancho
     * que por alto
     */
    override fun handle(event: Event?): Boolean {

        if(event is MapChangeEvent){
            maxWidth=event.map.width.toFloat()
            maxHeight = event.map.height.toFloat()
            return true
        }
        return false
    }


}