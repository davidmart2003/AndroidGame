package com.game.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Scaling
import com.game.MyGame.Companion.CREATED
import com.game.component.*
import com.game.component.PhysicComponent.Companion.physicsComponentFromIMage
import com.game.MyGame.Companion.UNIT_SCALE
import com.game.actor.FlipImage
import com.game.ai.DefaultGlobalState
import com.game.event.MapChangeEvent
import com.game.event.SpawnPortalEvent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.box2d.box
import ktx.box2d.circle
import ktx.log.logger
import ktx.math.vec2
import ktx.tiled.layer
import ktx.tiled.x
import ktx.tiled.y
import kotlin.math.roundToInt

@AllOf([SpawnComponent::class])
class EntitySpawnSystem(
    private val physicsWorld: World,
    private val atlas: TextureAtlas,
    private val spawnComponent: ComponentMapper<SpawnComponent>,
    private val playerComponents: ComponentMapper<PlayerComponent>,
    private val inventoryCmps: ComponentMapper<InventoryComponent>,
) : EventListener, IteratingSystem() {

    private val cachedConfigurations = mutableMapOf<String, SpawnConfiguration>()
    private val cachedSizes = mutableMapOf<String, Vector2>()
    private var tipo: String = ""
    private var num: Int = 0
    private var locationPortal: Vector2 = vec2(0f, 0f)
    private var created: Boolean = false
    private fun spawnConfiguration(type: String): SpawnConfiguration =
        cachedConfigurations.getOrPut(type) {
            when (type) {
                "Player" -> SpawnConfiguration(
                    "char_blue_1",
                    physicScaling = vec2(0.3f, 0.3f),
                    physicOffset = vec2(-1f * UNIT_SCALE, -10f * UNIT_SCALE),
                    bodyType = BodyDef.BodyType.DynamicBody,
                    attackExtraRange = 0.6f,
                    lifeScaling = 5f,
                    speedScaling = 3f,
                    attackScaling = 3.75f,

                    )

                "Flying Eye" -> SpawnConfiguration(
                    "FlyingEye",
                    physicScaling = vec2(0.2f, 0.13f),
                    physicOffset = vec2(0f * UNIT_SCALE, -6f * UNIT_SCALE),
                    bodyType = BodyDef.BodyType.DynamicBody,
                    aiTreePAth = "ai/FlyingEye.tree",
                    lifeScaling = 1.33f,
                    speedScaling = 1f,
                    attackScaling = 1f,
                    dropExp = 2
                )

                "Goblin" -> SpawnConfiguration(
                    "Goblin",
                    physicScaling = vec2(0.2f, 0.13f),
                    physicOffset = vec2(0f * UNIT_SCALE, -6f * UNIT_SCALE),
                    bodyType = BodyDef.BodyType.DynamicBody,
                    aiTreePAth = "ai/Goblin.tree",
                    lifeScaling = 3f,
                    speedScaling = 1.4f,
                    attackScaling = 2.25f,
                    dropExp = 23

                )

                "MushRoom" -> SpawnConfiguration(
                    "Mushroom",
                    physicScaling = vec2(0.2f, 0.13f),
                    physicOffset = vec2(0f * UNIT_SCALE, -6f * UNIT_SCALE),
                    bodyType = BodyDef.BodyType.DynamicBody,
                    aiTreePAth = "ai/MushRoom.tree",
                    lifeScaling = 5f,
                    speedScaling = 1f,
                    attackScaling = 3.75f,
                    dropExp = 257

                )

                "Skeleton" -> SpawnConfiguration(
                    "Skeleton",
                    physicScaling = vec2(0.2f, 0.13f),
                    physicOffset = vec2(0f * UNIT_SCALE, -6f * UNIT_SCALE),
                    bodyType = BodyDef.BodyType.DynamicBody,
                    aiTreePAth = "ai/Skeleton.tree",
                    lifeScaling = 6.25f,
                    speedScaling = 0.6f,
                    attackScaling = 10.5f,
                    dropExp = 5

                )

                "Portal" -> SpawnConfiguration(
                    "Portal",
                    physicScaling = vec2(0.2f, 0.4f),
                    physicOffset = vec2(0f * UNIT_SCALE, -5f * UNIT_SCALE),
                    bodyType = BodyDef.BodyType.DynamicBody,

                    )


                else -> gdxError("$type no tiene configuration")
            }
        }

    override fun handle(event: Event?): Boolean {
        when (event) {
            is MapChangeEvent -> {
                CREATED = false

                val entityLayer = event.map.layer("Entities")

                entityLayer.objects.forEach { mapObject ->
                    val type = mapObject.name ?: gdxError("MapObject no tiene tipo")

                    world.entity {
                        add<SpawnComponent> {
                            this.type = type
                            this.location.set(mapObject.x * UNIT_SCALE, mapObject.y * UNIT_SCALE)
                        }
                        if (type == "Player" && event.playerComponent != null) {
                            add<PlayerComponent>() {
                                this.actualLife = event.playerComponent.actualLife
                                this.actualStrenght = event.playerComponent.actualStrenght
                             //   log.debug { "$actualLife Fuerza=$actualStrenght" }
                            }
                        }
                    }

                }
            }

            is SpawnPortalEvent -> {
                num++
                world.entity {

                    if (num == 1) {
                        locationPortal = vec2(1600f * UNIT_SCALE, 350f * UNIT_SCALE)
                    }
                    if (num == 2) {
                        locationPortal = vec2(370f * UNIT_SCALE, 172f * UNIT_SCALE)
                    }
                    add<SpawnComponent>() {
                        type = "Portal"
                        location.set(locationPortal.x, locationPortal.y)

                    }

                }

            }

        }

        return true
    }

    override fun onTickEntity(entity: Entity) {
        with(spawnComponent[entity]) {
            val configuration = spawnConfiguration(type)
            tipo = type

            val relativeSize = size(configuration.atlasKey)


            val spawnedEntity= world.entity {
                val imageComponent = add<ImageComponent> {
                    image = FlipImage().apply {
                        setPosition(location.x, location.y)
                        setSize(relativeSize.x, relativeSize.y)
                        setScaling(Scaling.fill)
                    }
                }
                add<AnimationComponent> {
                    nextAnimation(configuration.atlasKey, AnimationState.RUN)
                }

                val physicComponent = physicsComponentFromIMage(
                    physicsWorld,
                    imageComponent.image,
                    BodyDef.BodyType.DynamicBody
                ) { physicComponent, width, height ->
                    val width = width * configuration.physicScaling.x
                    val height = height * configuration.physicScaling.y
                    physicComponent.offset.set((configuration.physicOffset))
                    physicComponent.size.set(width, height)

                    //hitbox

                    box(width, height, configuration.physicOffset) {
                        isSensor = configuration.bodyType != BodyDef.BodyType.StaticBody
                        if (tipo == "Portal") {
                            userData = PORTAL_HIT_BOX_SENSOR
                        } else {
                            if (tipo == "Player") {
                                userData = PLAYER_HIT_BOX_SENSOR
                            } else {
                                userData = HIT_BOX_SENSOR

                            }

                        }
                    }

                    if (configuration.bodyType != BodyDef.BodyType.StaticBody) {
                        // Colision box para los dinamic body
                        val collisionHeight = height * 0.6f
                        val collisionOffset = vec2().apply { set(configuration.physicOffset) }
                        collisionOffset.y -= height * 1.4f - collisionHeight
                        box(width, collisionHeight, collisionOffset)
                    }
                }
                if (configuration.speedScaling > 0f) {

                    add<MoveComponent> {
                        speed = DEFAULT_SPEED * configuration.speedScaling
                    }

                }

                if (configuration.canAttack) {
                    add<AttackComponent> {
                        maxDelay = configuration.attackDelay
                        damage = (DEFAULT_ATTACK_DAMAGE * configuration.attackScaling).roundToInt()
                        extraRange = configuration.attackExtraRange
                        if (entity in playerComponents) {
                            damage=playerComponents[entity].actualStrenght.roundToInt()
                        }
                    }
                }
                if (configuration.lifeScaling > 0f) {
                    add<LifeComponent> {
                        maxLife = DEFAULT_LIFE * configuration.lifeScaling
                        exp = configuration.dropExp
                        if (entity in playerComponents) {
                            life=playerComponents[entity].actualLife
                        }
                    }
                }
                if (type == "Player") {

                    add<PlayerComponent> {
                        if (entity in playerComponents) {
                            log.debug { "$actualLife Fuerza=$actualStrenght" }

                            actualLife = playerComponents[entity].actualLife
                            actualStrenght=playerComponents[entity].actualStrenght
                        }
                    }
                    add<InventoryComponent>()
                    add<LevelComponent>()
                    add<ShieldComponents>()
                    add<StateComponent> {
                        stateMachine.globalState = DefaultGlobalState.CHECK_ALIVE
                    }

                }
                if (type == "MushRoom" || type == "Flying Eye" || type == "Goblin" || type == "Skeleton")
                    add<EnemyComponent>(){
                        name=type
                    }
                add<ShieldComponents>()


                if (configuration.aiTreePAth.isNotBlank()) {
                    add<AiComponent> {
                        treePath = configuration.aiTreePAth;
                    }
                    physicComponent.body.circle(4f) {
                        isSensor = true
                        userData = AI_SENSOR
                    }
                }
            }
            if (spawnedEntity in playerComponents) {
                with(inventoryCmps[spawnedEntity]) {
                    itemsToAdd += ItemType.SWORD
                    itemsToAdd += ItemType.ARMOR
                    itemsToAdd += ItemType.HELMET
                    itemsToAdd += ItemType.BOOTS
                }
            }

        }
        world.remove(entity)
    }

    private fun size(atlasKey: String): Vector2 {
        return cachedSizes.getOrPut(atlasKey) {
            val regions = atlas.findRegions("$atlasKey/${AnimationState.RUN.atlasKey}")
            if (regions.isEmpty) {
                gdxError("There are no texture regions for $atlasKey")
            }
            val firstFrame = regions.first()
            vec2(firstFrame.originalWidth * UNIT_SCALE, firstFrame.originalHeight * UNIT_SCALE)
        }

    }


    companion object {
        const val HIT_BOX_SENSOR = "Hitbox"
        const val PORTAL_HIT_BOX_SENSOR = "Portal"
        const val PLAYER_HIT_BOX_SENSOR = "Player"
        const val AI_SENSOR = "AiSensor"
        private val log = logger<LifeSystem>()

    }
}
