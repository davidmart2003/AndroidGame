package com.game.ai

import com.badlogic.gdx.graphics.g2d.Animation
import com.game.component.AnimationState

enum class DefaultState : EntityState {
    IDLE{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationState.IDLE)
        }

        override fun update(entity: AiEntity) {
            when{
                entity.wantsToAttack -> entity.state(ATTACK)
                entity.wantsToRun -> entity.state(RUN)
            }
        }
        },
    RUN{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationState.RUN)
        }

        override fun update(entity: AiEntity) {
            when{
                entity.wantsToAttack->entity.state(ATTACK)
                !entity.wantsToRun ->entity.state(IDLE)
            }
        }
       },
    ATTACK{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationState.ATTACK, Animation.PlayMode.NORMAL)
            entity.root(true)
            entity.startAttack()
        }

        override fun update(entity: AiEntity) {
            val attackComponent=entity.attackComponent
            if(attackComponent.isReady && !attackComponent.doAttack){
                entity.changeToPreviousState()
            }else if(attackComponent.isReady){
                //Start another attack
                entity.animation(AnimationState.ATTACK,Animation.PlayMode.NORMAL,true)
                entity.startAttack()
            }
        }

        override fun exit(entity: AiEntity) {
            entity.root(false)
        }
          },
    DEATH{
        override fun enter(entity: AiEntity) {
            entity.root(true)
        }
         },
    TAKEHIT{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationState.TAKEHIT, Animation.PlayMode.NORMAL)

        }

        override fun update(entity: AiEntity) {
            if(entity.isAnimationDone){
                entity.state(IDLE)
                entity.root(false)
            }
        }
           },
}

enum class DefaultGlobalState:EntityState{
    CHECK_ALIVE{
        override fun update(entity: AiEntity) {
           if(entity.isDead){

              entity.enableGlobalState(false)
           entity.state(DefaultState.DEATH)
            }
        }
    }
}