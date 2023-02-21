package com.game.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.game.MyGame
import com.game.component.*
import com.game.event.DeathSound
import com.game.event.EntityAggroEvent
import com.game.event.EntityDamageEvent
import com.game.event.fire
import com.github.quillraven.fleks.*
import ktx.actors.stage
import ktx.assets.disposeSafely
import ktx.log.logger
import ktx.math.vec2

@AllOf([LifeComponent::class])
@NoneOf([DeadComponent::class])
class LifeSystem(
    @Qualifier("gameStage") private val stage:Stage,
    private val lifeComponents: ComponentMapper<LifeComponent>,
    private val deadComponent: ComponentMapper<DeadComponent>,
    private val playerComponent: ComponentMapper<PlayerComponent>,
    private val enemyComponent: ComponentMapper<EnemyComponent>,
    private val physicComponents: ComponentMapper<PhysicComponent>,
    private val animationComponents: ComponentMapper<AnimationComponent>,
    private val shieldComponents: ComponentMapper<ShieldComponents>

) : EventListener,IteratingSystem() {
    private val dmgFont = BitmapFont(Gdx.files.internal("damage.fnt")).apply { data.setScale(2f) }
    private val floatingTextStyle = LabelStyle(dmgFont, Color.RED)
    private var position : Vector2 = vec2(0f,0f)
    override fun onTickEntity(entity: Entity) {
        val lifeComponent = lifeComponents[entity]
        val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))
        lifeComponent.life =
            (lifeComponent.life + lifeComponent.regeneration * deltaTime).coerceAtMost(lifeComponent.maxLife)


        if ( !shieldComponents[entity].holdingShield) {

            if (lifeComponent.takeDamage > 0f) {
                lifeComponent.isTakingDamage = true
                stage.fire(EntityDamageEvent(entity))
                if (lifeComponent.isTakingDamage) {
                    animationComponents.getOrNull(entity)?.let { animationComponent ->
                        animationComponent.nextAnimation(AnimationState.TAKEHIT)
                        animationComponent.mode = Animation.PlayMode.NORMAL
                    }
                }
                val physicComponent = physicComponents[entity]
                position = physicComponent.body.position
                lifeComponent.life -= lifeComponent.takeDamage
                if(entity in playerComponent){

                playerComponent[entity].actualLife = lifeComponent.life
                }
                floatingText(
                    lifeComponent.takeDamage.toInt().toString(),
                    physicComponent.body.position,
                    physicComponent.size
                )
                lifeComponent.takeDamage = 0f

            } else {
                lifeComponent.isTakingDamage = false
            }

        }else {
            lifeComponent.takeDamage=0f
        }
        if (lifeComponent.isDead) {
            if(entity in enemyComponent){
                stage.fire(DeathSound(enemyComponent[entity].name))
            }
            lifeComponent.isTakingDamage = false
            stage.fire(EntityAggroEvent(null))

            playerEntities.forEach { player ->
                lifeComponents[player].exp += lifeComponent.exp
                Gdx.input.vibrate(100)
                //   log.debug {  "Experincia del jugador "+lifeComponents[player].exp.toString()}
            }
            animationComponents.getOrNull(entity)?.let { animationComponent ->
                animationComponent.nextAnimation(AnimationState.DEATH)
                animationComponent.mode = Animation.PlayMode.NORMAL
            }


            configureEntity(entity) {
                deadComponent.add(it) {
                    if (entity in playerComponent) {
                        reviveTime = 7f
                    }
                }
            }
        }

    }

    private fun floatingText(text: String, position: Vector2, size: Vector2) {
        world.entity {
            add<FloatingTextComponent> {
                txtLocation.set(position.x, position.y - size.y * 0.5f)
                lifeSpan = 1.5f
                label = Label(text, floatingTextStyle)
            }
        }
    }

    override fun onDispose() {
        dmgFont.disposeSafely()
    }

    companion object {
        private val log = logger<LifeSystem>()
    }

    override fun handle(event: Event?): Boolean {
        when (event){

        }
        return true
    }
}