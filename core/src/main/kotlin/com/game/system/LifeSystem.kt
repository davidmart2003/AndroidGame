package com.game.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.game.component.*
import com.game.event.*
import com.github.quillraven.fleks.*
import ktx.assets.disposeSafely
import ktx.log.logger
import ktx.math.vec2

@AllOf([LifeComponent::class])
@NoneOf([DeadComponent::class])
/**
 * Sistema que se encarga la vida, recibir daño y matar a las entidades
 *
 * @property stage Escenario donde se renderiza el juego
 * @property settingsPref Almacen de las opciones guardadas
 * @property lifeComponents Conjunto de entidades que contienen LifeComponent
 * @property deadComponents Conjunto de entidades que contienen DeadComponent
 * @property playerComponent Conjunto de entidades que contienen PlayerComponent
 * @property enemyComponent Conjunto de entidades que contienen EnemyComponent
 * @property physicComponents Conjunto de entidades que contienen PhysicComponent
 * @property animationComponents Conjunto de entidades que contienen AnimationComponent
 * @property shieldComponents Conjunto de entidades que contienen ShieldComponent
 */
class LifeSystem(
    @Qualifier("gameStage") private val stage: Stage,
    private val settingsPref: Preferences,
    private val lifeComponents: ComponentMapper<LifeComponent>,
    private val deadComponent: ComponentMapper<DeadComponent>,
    private val playerComponent: ComponentMapper<PlayerComponent>,
    private val enemyComponent: ComponentMapper<EnemyComponent>,
    private val physicComponents: ComponentMapper<PhysicComponent>,
    private val animationComponents: ComponentMapper<AnimationComponent>,
    private val shieldComponents: ComponentMapper<ShieldComponents>

) :  IteratingSystem() {
    private val dmgFont = BitmapFont(Gdx.files.internal("damage.fnt")).apply { data.setScale(2f) }
    private val floatingTextStyle = LabelStyle(dmgFont, Color.RED)
    private val floatingTextStylePlayer = LabelStyle(dmgFont, Color.BLACK)
    private var position: Vector2 = vec2(0f, 0f)
    private var isDone: Boolean = false

    /**
     * Cada que vez que se ejecuta po cada entidad , mira si la entidad esta defendiendose y esta recibiendo daño,
     * entonces le resta la vida segun la cantidad de daño que lke hayan hecho
     * Si la vida es menor que 0 la entidad muere
     */
    override fun onTickEntity(entity: Entity) {
        val lifeComponent = lifeComponents[entity]
        val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))
        lifeComponent.life =
            (lifeComponent.life + lifeComponent.regeneration * deltaTime).coerceAtMost(lifeComponent.maxLife)
        if (entity in playerComponent) {

            //    log.debug { "VIDA ACTUAL ${lifeComponent.life} vida maxima ${lifeComponent.maxLife} " }
            stage.fire(ActualLifeEvent(lifeComponent.life))
        }

        if (!shieldComponents[entity].holdingShield) {

            if (lifeComponent.takeDamage > 0f) {
                lifeComponent.isTakingDamage = true
                stage.fire(EntityDamageEvent(entity))
                if (lifeComponent.isTakingDamage) {
                    animationComponents.getOrNull(entity)?.let { animationComponent ->
                        animationComponent.nextAnimation(AnimationState.TAKEHIT)
                        animationComponent.mode = Animation.PlayMode.NORMAL
                    }
                    if (settingsPref.getBoolean("vibrate")) {
                        Gdx.input.vibrate(100)
                    }


                }
                val physicComponent = physicComponents[entity]
                position = physicComponent.body.position
                lifeComponent.life -= lifeComponent.takeDamage
                if (entity in playerComponent) {
                    log.debug { "DFSADADAD DFUSNUOKSFNFNSSFIOU " + playerComponent[entity].actualLife }
                    playerComponent[entity].actualLife = lifeComponent.life
                    floatingText(
                        lifeComponent.takeDamage.toInt().toString(),
                        physicComponent.body.position,
                        physicComponent.size,
                        true
                    )
                } else {

                    floatingText(
                        lifeComponent.takeDamage.toInt().toString(),
                        physicComponent.body.position,
                        physicComponent.size,
                        false
                    )
                }
                lifeComponent.takeDamage = 0f

            } else {
                lifeComponent.isTakingDamage = false
            }

        } else {
            lifeComponent.takeDamage = 0f
        }
        if (lifeComponent.isDead) {
            if (entity in enemyComponent) {
                stage.fire(DeathSound(enemyComponent[entity].name))
            }

            lifeComponent.isTakingDamage = false
            stage.fire(EntityAggroEvent(null))

            playerEntities.forEach { player ->
                lifeComponents[player].exp += lifeComponent.exp
            }
            animationComponents.getOrNull(entity)?.let { animationComponent ->
                animationComponent.nextAnimation(AnimationState.DEATH)
                animationComponent.mode = Animation.PlayMode.NORMAL
                isDone = true
            }
            if (entity in playerComponent) {
                stage.fire(DeathSound("Player"))
            }
            configureEntity(entity) {
                deadComponent.add(it) {

                    reviveTime = 7f

                }
            }


        }

    }

    /**
     * Texto flotante que aparece y se desvanace segun el daño causado
     */
    private fun floatingText(text: String, position: Vector2, size: Vector2, player: Boolean) {
        world.entity {
            add<FloatingTextComponent> {
                txtLocation.set(position.x, position.y - size.y * 0.5f)
                lifeSpan = 1.5f
                if (player) {

                    label = Label(text, floatingTextStylePlayer)
                } else {
                    label = Label(text, floatingTextStyle)
                }
            }
        }
    }

    /**
     * Cerrar el tipo de fuente para ahorrar recursos
     */
    override fun onDispose() {
        dmgFont.disposeSafely()
    }

    companion object {
        private val log = logger<LifeSystem>()
    }



}