package com.game.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Scaling
import com.game.MyGame.Companion.ATTACK
import com.game.MyGame.Companion.CREATED
import com.game.MyGame.Companion.LIFE
import com.game.MyGame.Companion.SPEED
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

/**
 * Sisttema que se encarga del spawneo de entidades
 *
 * @property physicsWorld Mundo de fisicas
 * @property atlas Atlas de texturas
 * @property spawnComponent Conjunto de entidades que contiene SpawnComponente
 * @property playerComponents Conjunto de entidades que contiene PlayerComponent
 */
@AllOf([SpawnComponent::class])
class EntitySpawnSystem(
    private val physicsWorld: World,
    private val atlas: TextureAtlas,
    private val spawnComponent: ComponentMapper<SpawnComponent>,
    private val playerComponents: ComponentMapper<PlayerComponent>,
) : EventListener, IteratingSystem() {

    /**
     * Alamacen de las configuraciones de las entidades
     */
    private val cachedConfigurations = mutableMapOf<String, SpawnConfiguration>()

    /**
     * Almacen del tamaño de las entidades
     */
    private val cachedSizes = mutableMapOf<String, Vector2>()

    /**
     * Nombre de la entidad
     */
    private var tipo: String = ""

    /**
     * Numero del mapa a cargar
     */
    private var num: Int = 0

    /**
     * Localizacion del portal
     */
    private var locationPortal: Vector2 = vec2(0f, 0f)

    /**
     *  Segun la entidad se crea una configuracion distinta
     *
     *  @param type Nombre de la entidad
     */
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
                    dropExp = 3
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
                    dropExp = 5

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
                    dropExp = 7

                )

                "Skeleton" -> SpawnConfiguration(
                    "Skeleton",
                    physicScaling = vec2(0.2f, 0.13f),
                    physicOffset = vec2(0f * UNIT_SCALE, -6f * UNIT_SCALE),
                    bodyType = BodyDef.BodyType.DynamicBody,
                    aiTreePAth = "ai/Skeleton.tree",
                    lifeScaling = 9.75f,
                    speedScaling = 0.6f,
                    attackScaling = 10.5f,
                    dropExp = 11

                )

                "Portal" -> SpawnConfiguration(
                    "Portal",
                    physicScaling = vec2(0.2f, 0.4f),
                    physicOffset = vec2(0f * UNIT_SCALE, -5f * UNIT_SCALE),
                    bodyType = BodyDef.BodyType.DynamicBody,

                    )

                "Demon" -> SpawnConfiguration(
                    "Demon",
                    physicScaling = vec2(0.2f, 0.3f),
                    physicOffset = vec2(0f * UNIT_SCALE, -25f * UNIT_SCALE),
                    bodyType = BodyDef.BodyType.DynamicBody,
                    aiTreePAth = "ai/Demon.tree",
                    lifeScaling = 100f,
                    attackExtraRange = 4f,
                    speedScaling = 1f,
                    attackScaling = 15.5f,
                )


                else -> gdxError("$type no tiene configuration")
            }
        }

    /**
     * Se ejecuta cuando un evento es lanzado
     *
     * @param event Evento lanzado
     */
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
                                this.actualTime = event.playerComponent.actualTime
                                this.actualLevel=event.playerComponent.actualLevel
                                this.actualBasexp=event.playerComponent.actualBasexp
                                this.actualexp=event.playerComponent.actualexp
                                log.debug { "AL cambiar el mapa=======$actualBasexp EXPPPPPPPBASE" }
                                log.debug { "AL cambiar el mapa=======$actualLevel LEVEEEEEEEEEEEEL" }
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

    /**
     * Por cada entidad se les añade los componentes necesarios para que funciones en los sitemas que lo requieran
     *
     * @param entity Entidad a ejecutar
     */
    override fun onTickEntity(entity: Entity) {
        with(spawnComponent[entity]) {
            val configuration = spawnConfiguration(type)
            tipo = type

            val relativeSize = size(configuration.atlasKey)


            val spawnedEntity = world.entity {
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
                            damage = playerComponents[entity].actualStrenght.roundToInt()

                        }
                    }
                }
                if (configuration.lifeScaling > 0f) {
                    add<LifeComponent> {
                        maxLife = DEFAULT_LIFE * configuration.lifeScaling
                        exp = configuration.dropExp
                        life = maxLife
                        if (entity in playerComponents) {
                            exp = playerComponents[entity].actualexp.toInt()
                            life = playerComponents[entity].actualLife
                        }
                    }
                }

                if (type == "Player") {

                    add<PlayerComponent> {
                        if (entity in playerComponents) {
                            actualLevel = playerComponents[entity].actualLevel
                            actualLife = playerComponents[entity].actualLife
                            actualStrenght = playerComponents[entity].actualStrenght
                            actualTime = playerComponents[entity].actualTime
                            log.debug { "AL cambiar el mapa=======$actualBasexp EXPPPPPPPBANTESSSSSSS" }
                            log.debug { "AL cambiar el mapa=======$actualLevel LEVEEEEEEEEEEEEL" }

                            actualBasexp = playerComponents[entity].actualBasexp
                            actualexp = playerComponents[entity].actualexp
                            log.debug { "AL cambiar el mapa=======$actualBasexp EXPPPPPPPDEspuesssssssss" }
                            log.debug { "AL cambiar el mapa=======$actualLevel LEVEEEEEEEEEEEEL" }

                        }
                    }
                    add<TimeComponent>()
                    add<LevelComponent> {
                        if (entity in playerComponents) {
                            baseExp = playerComponents[entity].actualBasexp
                            level = playerComponents[entity].actualLevel.roundToInt()
                        }
                    }
                    add<ShieldComponents>()
                    add<StateComponent> {
                        stateMachine.globalState = DefaultGlobalState.CHECK_ALIVE
                    }

                }
                if (type == "MushRoom" || type == "Flying Eye" || type == "Goblin" || type == "Skeleton" || type == "Demon")
                    add<EnemyComponent>() {
                        name = type
                    }
                add<ShieldComponents>()


                if (configuration.aiTreePAth.isNotBlank()) {
                    add<AiComponent> {
                        treePath = configuration.aiTreePAth;
                    }
                    if (type == "Demon") {
                        physicComponent.body.circle(10f) {
                            isSensor = true
                            userData = AI_SENSOR
                        }
                    } else {

                        physicComponent.body.circle(4f) {
                            isSensor = true
                            userData = AI_SENSOR
                        }
                    }
                }
            }


        }
        world.remove(entity)
    }

    /**
     *  Tamaño de cada entidad segun la resolucion del juego
     *
     *   @param atlasKey Nombre de la entidad en el atlas
     */
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
