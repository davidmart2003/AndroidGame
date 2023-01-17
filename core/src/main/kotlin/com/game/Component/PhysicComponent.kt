package com.game.Component

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Shape2D
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Fixture
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

class PhysicComponent {
    val prevPos = vec2()
    val impulse = vec2()
    val offset = vec2()
    val size = vec2()
    lateinit var body: Body

    companion object {
        /**
         *
         * @param world Nuestro mundo de fisicas (box2d)
         * @param x Posicion donde queremos crear nuestra entidad en x
         * @param y Posicion donde queremos crear nuestra entidad en Y
         * @param shape Tipo de forma que tendra nuestra entidad
         * @return Devolvemos la fisica creada a nuestra entidad
         */
        fun EntityCreateCfg.physicComponentFromShape2D(
            world: World,
            x:Int,
            y:Int,
            shape:Shape2D
        ): PhysicComponent{
            //En nuestro tiledMap todas las colisiones son rectangulos
            when(shape){
                is Rectangle ->{
                    //Tamaño de la forma de la colision (trabaja en pixeles , hacemos conversion )+ la posicion inicial donde esta
                    val bodyX = x + shape.x * UNIT_SCALE
                    val bodyY = y + shape.y * UNIT_SCALE
                    val bodyWidth = shape.width * UNIT_SCALE
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
        class PhysicComponentListener : ComponentListener<PhysicComponent>{
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
