package com.game.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.component.ImageComponent
import com.game.MyGame.Companion.UNIT_SCALE
import com.game.event.MapChangeEvent
import com.github.quillraven.fleks.*
import com.github.quillraven.fleks.collection.compareEntity
import ktx.graphics.use
import ktx.tiled.forEachLayer
/**
 * Sistema que se encarga de renderizar las entidades
 *
 * @property gameStage Escenario donde se renderiza el juego
 * @property uiStage Escenario donde se renderiza la interfaz de usuario
 * @property imgCmps Conjunto de entidades que contiene ImageComponent
 */
@AllOf([ImageComponent::class])
class RenderSystem(
    @Qualifier("gameStage") private  val gameStage:Stage,
    @Qualifier("uiStage") private val uiStage:Stage,
    private val imageComponents : ComponentMapper<ImageComponent>
) : EventListener,IteratingSystem(
    comparator = compareEntity { e1, e2 -> imageComponents[e1].compareTo(imageComponents[e2])  }
) {
    /**
     * Lista de capas del suelo del mapa
     */
    private val backgroundLayers = mutableListOf<TiledMapTileLayer>()

    /**
     * Lista de capas del fondo del mapa
     */
    private val foregroundLayers= mutableListOf<TiledMapTileLayer>()

    /**
     * Renderizado del mapa
     */
    private val maprenderer = OrthogonalTiledMapRenderer(null,UNIT_SCALE,gameStage.batch)

    /**
     * Camara del juego
     */
    private val OrthoCamera = gameStage.camera as OrthographicCamera

    /**
     * Cada vez que se ejecuta dibuja el escenario del mapa sea animado o no
     */
    override fun onTick() {
        super.onTick()

        with(gameStage){
            viewport.apply()

            AnimatedTiledMapTile.updateAnimationBaseTime()
            maprenderer.setView(OrthoCamera)

            if(backgroundLayers.isNotEmpty()){
                gameStage.batch.use(OrthoCamera.combined) {
                    backgroundLayers.forEach{ maprenderer.renderTileLayer(it)}
                }
            }
            act(deltaTime)
            draw()

            if(foregroundLayers.isNotEmpty()){
                gameStage.batch.use(OrthoCamera.combined) {
                    foregroundLayers.forEach { maprenderer.renderTileLayer(it) }
                }
            }
        }


        //render UI
        with(uiStage){
            viewport.apply()

            act(deltaTime)
            draw()
        }
    }
    /**
     *Por cada entidad envia la imagen de la entidad al frente del mapa
     *
     * @param entity Entidad a ejecutar
     */
    override fun onTickEntity(entity: Entity) {
        imageComponents[entity].image.toFront()
    }

    /**
     * Se ejecuta cuando un evento es lanzado
     *
     * @param event Evento lanzado
     */
    override fun handle(event: Event?): Boolean {
        when (event) {
            is MapChangeEvent -> {
                backgroundLayers.clear()
                foregroundLayers.clear()

                event.map.forEachLayer<TiledMapTileLayer> {layer ->
                    if(layer.name.startsWith("foreground_")){
                        foregroundLayers.add(layer)
                    }else {
                        backgroundLayers.add(layer)
                    }
                }
                return true
            }
        }
        return false
    }
}