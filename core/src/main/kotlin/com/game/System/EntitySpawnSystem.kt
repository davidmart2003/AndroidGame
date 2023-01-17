package com.game.System

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.game.Component.*
import com.game.Component.PhysicComponent.Companion.physicsComponentFromIMage
import com.game.MyGame.Companion.UNIT_SCALE
import com.game.actor.FlipImage
import com.game.event.MapChangeEvent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.box2d.box
import ktx.math.vec2
import ktx.tiled.layer
import ktx.tiled.x
import ktx.tiled.y
import kotlin.math.roundToInt

@AllOf([SpawnComponent::class])
class EntitySpawnSystem(
    private val physicsWorld: World,
    private val atlas: TextureAtlas,
    private val spawnComponent: ComponentMapper<SpawnComponent>
) : EventListener, IteratingSystem() {

    private val cachedConfigurations = mutableMapOf<String, SpawnConfiguration>()
    private val cachedSizes = mutableMapOf<AnimationType, Vector2>()

    private fun spawnConfiguration(type: String): SpawnConfiguration =
        cachedConfigurations.getOrPut(type) {
            when (type) {
                "Player" -> SpawnConfiguration(
                    AnimationType.char_blue_1,
                    physicScaling = vec2(0.3f, 0.3f),
                    physicOffset = vec2(-1f* UNIT_SCALE, -10f * UNIT_SCALE),
                    bodyType = BodyDef.BodyType.DynamicBody,
                    attackExtraRange = 0.6f,
                    attackScaling = 1.25f
                )
                "FlyingEye" -> SpawnConfiguration(
                    AnimationType.FlyingEye,
                    physicScaling = vec2(0.3f, 0.3f),
                    physicOffset = vec2(0f, -10f * UNIT_SCALE),
                    bodyType = BodyDef.BodyType.DynamicBody
                )
                "Goblin" -> SpawnConfiguration(
                    AnimationType.Goblin,
                    lifeScaling=0.75f,
                    physicScaling = vec2(0.2f, 0.13f),
                    physicOffset = vec2(0f* UNIT_SCALE, -6f * UNIT_SCALE),
                    bodyType = BodyDef.BodyType.DynamicBody
                )
                "Mushroom" -> SpawnConfiguration(
                    AnimationType.Mushroom,
                    physicScaling = vec2(0.3f, 0.3f),
                    physicOffset = vec2(0f, -2f * UNIT_SCALE),
                    bodyType = BodyDef.BodyType.DynamicBody
                )
                "Skeleton" -> SpawnConfiguration(
                    AnimationType.Skeleton,
                    physicScaling = vec2(0.3f, 0.3f),
                    physicOffset = vec2(0f, -2f * UNIT_SCALE),
                    bodyType = BodyDef.BodyType.DynamicBody
                )
                else -> gdxError("$type no tiene configuration")
            }
        }

    override fun handle(event: Event?): Boolean {
        when (event) {
            is MapChangeEvent -> {
                val entityLayer = event.map.layer("Entities")
                entityLayer.objects.forEach { mapObject ->
                    val type = mapObject.name ?: gdxError("MapObject no tiene tipo")
                    world.entity {
                        add<SpawnComponent> {
                            this.type = type
                            this.location.set(mapObject.x * UNIT_SCALE, mapObject.y * UNIT_SCALE)
                        }
                    }
                }
            }
        }
        return true
    }

    override fun onTickEntity(entity: Entity) {
        with(spawnComponent[entity]) {
            val configuration = spawnConfiguration(type)
            val relativeSize = size(configuration.model)
            world.entity {
                val imageComponent = add<ImageComponent> {
                    image = FlipImage().apply {
                        setPosition(location.x, location.y)
                        setSize(relativeSize.x, relativeSize.y)
                        setScaling(Scaling.fill)
                    }
                }
                add<AnimationComponent> {
                    nextAnimation(configuration.model, AnimationState.IDLE)
                }
                physicsComponentFromIMage(
                    physicsWorld,
                    imageComponent.image,
                    BodyDef.BodyType.DynamicBody
                ) { physicComponent, width, height ->
                    val width = width * configuration.physicScaling.x
                    val height = height * configuration.physicScaling.y
                    physicComponent.offset.set((configuration.physicOffset))
                    physicComponent.size.set(width,height)

                    //hitbox
                    box(width, height,configuration.physicOffset) {
                        isSensor = configuration.bodyType != BodyDef.BodyType.StaticBody
                        userData = HIT_BOX_SENSOR
                    }

                    if(configuration.bodyType!=BodyDef.BodyType.StaticBody){
                        // Colision box para los dinamic body
                        val collisionHeight = height*0.6f
                        val collisionOffset = vec2().apply { set(configuration.physicOffset) }
                        collisionOffset.y -= height*1.4f - collisionHeight
                        box(width,collisionHeight,collisionOffset)
                    }
                }
                if (configuration.speedScaling > 0f) {

                    add<MoveComponent> {
                        speed = DEFAULT_SPEED * configuration.speedScaling
                    }

                }

                if(configuration.canAttack){
                    add<AttackComponent>{
                        maxDelay = configuration.attackDelay
                        damage = (DEFAULT_ATTACK_DAMAGE * configuration.attackScaling).roundToInt()
                        extraRange = configuration.attackExtraRange
                    }
                }
                if(configuration.lifeScaling>0f){
                    add<LifeComponent>{
                        maxLife = DEFAULT_LIFE* configuration.lifeScaling
                        life=maxLife
                    }
                }
                if (type == "Player") {
                    add<PlayerComponent>()
                }
            }

        }
        world.remove(entity)
    }

    private fun size(model: AnimationType) = cachedSizes.getOrPut(model) {
        val regions = atlas.findRegions("${model.name}/${AnimationState.IDLE.atlasKey}")
        if (regions.isEmpty) {
            gdxError("No hay regiones para ese tipo")
        }

        val firstFrame = regions.first()

        vec2(firstFrame.originalWidth * UNIT_SCALE, firstFrame.originalHeight * UNIT_SCALE)
    }
    companion object {
        const val  HIT_BOX_SENSOR = "Hitbox"
    }
}
