package com.game.ai

import com.badlogic.gdx.graphics.g2d.Animation
import com.game.component.AnimationState
import com.game.event.EntityAggroEvent

/**
 * Enumerado de los distintos estados que puede tener la IAEntity
 */
enum class DefaultState : EntityState {
    IDLE{
        /**
         * Cuando entra en estado idle, cambia la animacion
         */
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationState.IDLE)
        }

        /**
         * CUando actualiza el estado mientras esta en el estado idle, se cambia a al estado con la condicion que quiera hacer la IA
         */
        override fun update(entity: AiEntity) {
            when{
                entity.wantsToAttack -> entity.state(ATTACK)
                entity.wantsToRun -> entity.state(RUN)
                entity.takehit->entity.state(TAKEHIT)
            }
        }
        },
    RUN{
        /**
         * Cuando entra en estado run, cambia la animaciom
         */
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationState.RUN)
        }

        /**
         * CUando actualiza el estado mientras esta en el estado run, se cambia a al estado con la condicion que quiera hacer la IA
         */
        override fun update(entity: AiEntity) {
            when{
                entity.wantsToAttack->entity.state(ATTACK)
                !entity.wantsToRun ->entity.state(IDLE)
                entity.takehit->entity.state(TAKEHIT)
                
            }
        }
       },
    ATTACK{
        /**
         * Cuando entra en estado attack, cambia la animacion y empieza un ataque
         */
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationState.ATTACK, Animation.PlayMode.NORMAL)
            entity.root(true)
            entity.startAttack()
        }

        /**
         * CUando actualiza el estado mientras esta en el estado asttack, si esta atacando cambia al estado anterior, sino empieza otro ataque
         */
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

        /**
         * Cuando sale del estado de attack, la entidad deja de ser root
         */
        override fun exit(entity: AiEntity) {
            entity.root(false)
        }
          },
    DEATH{
        /**
         * Cuando entra en el estado death, la entidad pasa a ser root
         */
        override fun enter(entity: AiEntity) {
            entity.root(true)
        }
         },
    TAKEHIT{
        /**
         * Cuando entra en el estado takehit, cambia la animacion
         */
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationState.TAKEHIT, Animation.PlayMode.NORMAL)

        }

        /**
         * CUando actualiza el estado mientras esta en el estado takehit, se cambia a al estado con la condicion que quiera hacer la IA
         */
        override fun update(entity: AiEntity) {
            when{
                entity.wantsToAttack -> entity.state(ATTACK)
                entity.wantsToRun -> entity.state(RUN)
                !entity.wantsToRun ->entity.state(IDLE)
            }
        }
           },
}

/**
 * Enumerado que contiene los estados globales por default
 */
enum class DefaultGlobalState:EntityState{
    CHECK_ALIVE{
        /**
         * Cuando actualiza este estado , y la entidad esta muerta , pasa a estar en estado de muerta y no tener estado global
         */
        override fun update(entity: AiEntity) {
           if(entity.isDead){

              entity.enableGlobalState(false)
           entity.state(DefaultState.DEATH)
            }
        }
    }
}