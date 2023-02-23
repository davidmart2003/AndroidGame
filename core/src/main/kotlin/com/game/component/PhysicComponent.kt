package com.game.component

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Shape2D
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.game.MyGame.Companion.UNIT_SCALE
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityCreateCfg
import ktx.app.gdxError
import ktx.box2d.BodyDefinition
import ktx.box2d.body
import ktx.box2d.loop
import ktx.math.vec2

/**
 * Componente de las entidad que requieran de un sistema de físicas
 *
 * @property prevPos Posición anterior respecto a la actual que tenía la entidad
 * @property impulse Impulso de la entidad
 * @property offset Posicion del cuerpo en el mundo de físicas acorde a la pantalla
 * @property size Tamaño del cuerpo
 * @property body Cuerpo de la entidad
 */
class PhysicComponent {
    val prevPos = vec2()
    val impulse = vec2()
    val offset = vec2()
    val size = vec2()
    lateinit var body: Body

    companion object {
        /**
         *Crea las físicas de colisión para cada entidad segun el shape
         *
         * @param world Nuestro mundo de fisicas (box2d)
         * @param x Posición inicial de la colisión en el eje x
         * @param y  Posición inicial de la colisión en el eje y
         * @param shape Tipo de forma que tendra nuestra entidad
         */
        fun EntityCreateCfg.physicComponentFromShape2D(
            world: World,
            x:Int,
            y:Int,
            shape:Shape2D
        ): PhysicComponent{
            when(shape){
                is Rectangle ->{
                    /**
                     * Posicion X del cuerpo
                     */
                    val bodyX = x + shape.x * UNIT_SCALE

                    /**
                     *  Posicion Y del cuerpo
                     */
                    val bodyY = y + shape.y * UNIT_SCALE

                    /**
                     *  Anchura del cuerpo
                     */
                    val bodyWidth = shape.width * UNIT_SCALE

                    /**
                     *  Altura del cuerpo
                     */
                    val bodyHeight = shape.height * UNIT_SCALE


                    return add {
                        body = world.body(BodyDef.BodyType.StaticBody){
                            position.set(bodyX,bodyY)
                            fixedRotation=true
                            allowSleep=true
                            loop(
                                vec2(0f,0f),
                                vec2(bodyWidth,0f),
                                vec2(bodyWidth,bodyHeight),
                                vec2(0f,bodyHeight)
                            )
                        }

                    }
                }
            else -> gdxError("Ese shape no esta controlada en nuestro tilemap")
            }
        }

        /**
         *  Crea las físicas de colision segun una imagen
         *
         *  @param world Mundo de físicas
         *  @param image Imagen de la entidad
         *  @param bodyType Tipo del cuerpo de la entidad
         *  @param fixtureAction Fixture de la entidad
         */
        fun EntityCreateCfg.physicsComponentFromIMage(
            world: World,
            image: Image,
            bodyType: BodyDef.BodyType,
            fixtureAction: BodyDefinition.(PhysicComponent, Float, Float) -> Unit
        ): PhysicComponent {
            val x = image.x
            val y = image.y
            val width = image.width
            val height = image.height

            return add<PhysicComponent> {
                body = world.body(bodyType) {
                    position.set(x + width * 0.5f, y + height * 0.5f)
                    fixedRotation = true
                    allowSleep=false
                    this.fixtureAction(this@add,width,height)
                }
            }
        }

        /**
         * Listener que actúa cada vezx que el componente es añadido
         */
        class PhysicComponentListener : ComponentListener<PhysicComponent>{
            /**
             * Cuando el componente es añadido se guarda el userData segun la entidad
             *
             * @param entity Entidad que fue añadida
             * @param component Tipo de componente
             */
            override fun onComponentAdded(entity: Entity, component: PhysicComponent) {
                component.body.userData=entity
            }

            override fun onComponentRemoved(entity: Entity, component: PhysicComponent) {
                component.body.world.destroyBody(component.body)
                component.body.userData=null
            }

        }
    }
}
