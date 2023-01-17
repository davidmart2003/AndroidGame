package com.game.System

import com.game.Component.DeadComponent
import com.game.Component.LifeComponent
import com.game.Component.PlayerComponent
import com.github.quillraven.fleks.*

@AllOf([LifeComponent::class])
@NoneOf([DeadComponent::class])
class LifeSystem(
    private val lifeComponent: ComponentMapper<LifeComponent>,
    private val deadComponent: ComponentMapper<DeadComponent>,
    private val playerComponent : ComponentMapper<PlayerComponent>
) : IteratingSystem(){
    override fun onTickEntity(entity: Entity) {
        val lifeComponent = lifeComponent[entity]

        lifeComponent.life = (lifeComponent.life + lifeComponent.regeneration * deltaTime).coerceAtMost(lifeComponent.maxLife)

        if(lifeComponent.takeDamage >0f){
            lifeComponent.life -= lifeComponent.takeDamage
            lifeComponent.takeDamage=0f
        }

        if(lifeComponent.isDead){
            configureEntity(entity){
                deadComponent.add(it){
                    if(entity in playerComponent){
                        reviveTime = 7f
                    }
                }
            }
        }
    }
}