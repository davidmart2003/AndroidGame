package com.game.system

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.component.ImageComponent
import com.game.component.PlayerComponent
import com.game.event.MapChangeEvent
import com.github.quillraven.fleks.*
import ktx.tiled.height
import ktx.tiled.width
import java.lang.Double.max
import java.lang.Double.min
import kotlin.math.max
import kotlin.math.min


/**
 * El sistema de movimiento de la camera , haremos que la camera siga al jugador principal
 * @param stage Escenario donde se renderiza el juego
 * @param imageComponents Conjunto de entidades que tienen ImageComponent
 */
@AllOf([PlayerComponent::class,ImageComponent::class])
class CameraSystem(
    private val imageComponents : ComponentMapper<ImageComponent>,
    @Qualifier("gameStage") stage: Stage
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
        val camMinW = min(viewWidth,maxWidth-viewWidth)
        val camMaxW = max(viewWidth,maxWidth-viewWidth)
        val camMinH = min(viewHeight,maxHeight-viewHeight)
        val camMaxH = max(viewHeight,maxHeight-viewHeight)
        with(imageComponents[entity]){
            camera.position.set(
                image.x.coerceIn(camMinW,camMaxW),
                image.y.coerceIn(camMinH,camMaxH),
                camera.position.z
            )
        }
    }

    /**
     *Se ejecuta cuando se lanza un evento
     *
     * @param event Evento lanzado
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