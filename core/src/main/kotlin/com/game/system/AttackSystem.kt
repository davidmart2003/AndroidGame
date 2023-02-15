package com.game.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.World
import com.game.component.*
import com.game.system.EntitySpawnSystem.Companion.HIT_BOX_SENSOR
import com.game.system.EntitySpawnSystem.Companion.PLAYER_HIT_BOX_SENSOR
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.box2d.query
import ktx.math.component1
import ktx.math.component2

@AllOf([AttackComponent::class, PhysicComponent::class, ImageComponent::class])
class AttackSystem(
    private val attackComponents: ComponentMapper<AttackComponent>,
    private val physicComponents: ComponentMapper<PhysicComponent>,
    private val ImageComponents: ComponentMapper<ImageComponent>,
    private val animationComponents: ComponentMapper<AnimationComponent>,
    private val lifeComponent : ComponentMapper<LifeComponent>,
    private val myWorld : World
) : IteratingSystem(){
    override fun onTickEntity(entity: Entity) {
        val attackComponent = attackComponents[entity]

        if(attackComponent.isReady && !attackComponent.doAttack){
            //La entidad esta lista para atacar pero no quiere atacar no hacemos nada
            return
        }

        if(attackComponent.isPrepared && attackComponent.doAttack){
            //LA entidad quiere atacar y esta preparadisimo para atacar
            attackComponent.doAttack=false
            attackComponent.state=AttackState.ATTACKING
            attackComponent.delay = attackComponent.maxDelay
            return
        }

        attackComponent.delay -=deltaTime

        if(attackComponent.delay<=0f && attackComponent.isAttacking){
            //HAcer daño a enemigos cercacos
            attackComponent.state= AttackState.DEAL_DAMAGE

            val image = ImageComponents[entity].image
            val physicComponent = physicComponents[entity]
            val attackLeft = image.flipX
            val (x,y) = physicComponent.body.position
            val (offX,offY) = physicComponent.offset
            val (sizeX,sizeY) = physicComponent.size

            val halfWidth = sizeX * 0.5f
            val halfHeight = sizeY * 0.5f

            if(attackLeft){
                AABB_RECT.set(
                    x + offX - halfWidth - attackComponent.extraRange,
                    y + offY - halfHeight,
                    x + offX + halfWidth,
                    y + offY + halfHeight
                )
            }else {
                AABB_RECT.set(
                    x + offX - halfWidth ,
                    y + offY - halfHeight,
                    x + offX + halfWidth + attackComponent.extraRange,
                    y + offY + halfHeight
                )
            }
            myWorld.query(AABB_RECT.x, AABB_RECT.y, AABB_RECT.width, AABB_RECT.height){ fixture ->
                if(fixture.userData != PLAYER_HIT_BOX_SENSOR && fixture.userData != HIT_BOX_SENSOR){
                    return@query true
                }

                val fixtureEntity = fixture.entity

                if(fixtureEntity == entity){
                    // NO NOS ATACAMOS A NOSOTROS MISMOs
                    return@query true
                }
                //TODO que los enemigos se ataquen a si mismos
                configureEntity(fixtureEntity){
                    lifeComponent.getOrNull(it)?.let { lifeComponent ->
                        lifeComponent.takeDamage += attackComponent.damage * MathUtils.random(0.9f,1.1f)
                    }
                }

                return@query true
            }
        }
        val isDone = animationComponents.getOrNull(entity)?.isAnimationFinished() ?:true
        if(isDone){
            attackComponent.state = AttackState.READY
            AABB_RECT.set(0f,0f,0f,0f)
        }

    }
    companion object {
        val AABB_RECT = Rectangle()
    }

}