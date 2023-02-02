package com.game.system

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.game.component.PhysicComponent
import com.game.component.PhysicComponent.Companion.physicComponentFromShape2D
import com.game.event.MapChangeEvent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.box2d.body
import ktx.box2d.loop
import ktx.math.vec2

import ktx.tiled.*
import kotlin.math.max

@AllOf([PhysicComponent::class])
class CollisionSpawnSystem(
    private val myWorld: World,
) : EventListener,IteratingSystem() {

    /**
     *Recorre las celdas en un rango para hacer una accion que definiremos
     * cuando la necesitemos ya que es una lambda
     *
     * @param startX Posicion inicial en x de la celda donde se encuentre
     * @param startY Posicion inicial en y de la celda donde se encuentre
     * @param size Tamaño de área que hace una entidad para detecta una colision
     * @param action
     */
    private fun TiledMapTileLayer.forEachCell(
        startX:Int,
        startY:Int,
        size:Int,
        action:(TiledMapTileLayer.Cell,Int,Int)->Unit
    ){
        for (x in startX-size..startX+size){
            for (y in startY-size..startY+size){
                this.getCell(x,y)?.let { action(it,x,y) }
            }
        }
    }


    override fun handle(event: Event?): Boolean {
        when (event){
            is MapChangeEvent ->{
                event.map.forEachLayer<TiledMapTileLayer> { layer->
                    layer.forEachCell(0,0, max(event.map.width,event.map.height)){ cell,x,y ->
                        if(cell.tile.objects.isEmpty()){
                            //Si esta vacía, significa que esta celda no esta
                            //referenciada a un objeto collision, es decir no hacemos nada
                            return@forEachCell
                        }
                        //mapObject son los objetos que contiene el tileMap
                        // En este caso las colisiones
                        //Recorremos todas las celdas que tienen objetos y crearemos
                        //la entidad de la colision necesitando :
                        // El mundo de nuestras físicas
                        //La x de la celda
                        //la y de la celda
                        // El tipo de forma que tiene nuestro objeto
                        cell.tile.objects.forEach { mapObject->
                            world.entity {
                                physicComponentFromShape2D(myWorld,x,y,mapObject.shape)
                            }
                        }
                    }

                }

                //entidad del borde del tile y le agregamos las fisicas de colision
                world.entity{
                    val tileMapWidht= event.map.width.toFloat()
                    val tileMapHeight= event.map.height.toFloat()

                    add<PhysicComponent> {
                        body = myWorld.body(BodyDef.BodyType.StaticBody){
                            position.set(0f,0f)
                            fixedRotation=true
                            allowSleep=true

                            loop(
                                vec2(0f,0f),
                                vec2(tileMapWidht,0f),
                                vec2(tileMapWidht,tileMapHeight),
                                vec2(0f,tileMapHeight)
                            )
                        }

                    }
                }

                return true
            }
        else ->return false
        }
    }

    override fun onTickEntity(entity: Entity) {
        Unit
    }
}
