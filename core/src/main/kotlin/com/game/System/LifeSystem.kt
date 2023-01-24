package com.game.System

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.game.component.*
import com.github.quillraven.fleks.*
import ktx.assets.disposeSafely

@AllOf([LifeComponent::class])
@NoneOf([DeadComponent::class])
class LifeSystem(
    private val lifeComponent: ComponentMapper<LifeComponent>,
    private val deadComponent: ComponentMapper<DeadComponent>,
    private val playerComponent : ComponentMapper<PlayerComponent>,
    private val physicComponents: ComponentMapper<PhysicComponent>,
    private val animationComponents : ComponentMapper<AnimationComponent>

) : IteratingSystem(){
    private val dmgFont = BitmapFont(Gdx.files.internal("damage.fnt")).apply { data.setScale(2f) }
    private val floatingTextStyle =LabelStyle(dmgFont, Color.RED)
    override fun onTickEntity(entity: Entity) {
        val lifeComponent = lifeComponent[entity]

        lifeComponent.life = (lifeComponent.life + lifeComponent.regeneration * deltaTime).coerceAtMost(lifeComponent.maxLife)

        if(lifeComponent.takeDamage >0f){
            val physicComponent = physicComponents[entity]
            lifeComponent.life -= lifeComponent.takeDamage
            floatingText(lifeComponent.takeDamage.toInt().toString(),physicComponent.body.position,physicComponent.size)
            lifeComponent.takeDamage=0f
        }

        if(lifeComponent.isDead){
            animationComponents.getOrNull(entity)?.let { animationComponent->
                animationComponent.nextAnimation(AnimationState.DEATH)
                animationComponent.playMode= Animation.PlayMode.NORMAL
            }

            configureEntity(entity){
                deadComponent.add(it){
                    if(entity in playerComponent){
                        reviveTime = 7f
                    }
                }
            }
        }
    }

    private fun floatingText(text:String,position:Vector2,size:Vector2){
        world.entity{
            add<FloatingTextComponent>{
                txtLocation.set(position.x,position.y -size.y *0.5f)
                lifeSpan=1.5f
                label= Label(text,floatingTextStyle)
            }
        }
    }

    override fun onDispose() {
        dmgFont.disposeSafely()
    }
}