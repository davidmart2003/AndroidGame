package com.game.ai

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import com.badlogic.gdx.ai.utils.random.FloatDistribution
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.MathUtils
import com.game.component.AnimationState
import com.game.event.EntityAggroEvent
import com.game.ui.view.GameView
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.world
import ktx.log.logger
import ktx.math.vec2

/**
 * Las diferentes acciones que puede hacer la IA
 */
abstract class Action(

) : LeafTask<AiEntity>() {
    /**
     * IA entidad
     */
    val entity: AiEntity
        get() = `object`

    /**
     * Copia la accion que esta haciendo la IA
     */
    override fun copyTo(task: Task<AiEntity>?) = task


}

/**
 * Accion de estar quieto de la IA
 *
 * @property duration Duración de la accion
 */
class IdleTask(
    @JvmField
    @TaskAttribute(required = true)
    var duration: FloatDistribution? = null
) : Action() {
    /**
     * Duracion actual
     */
    private var currentDuration = 0f

    /**
     * Cuando la entidad esta quieta, empieza la ejecucion y empieza la accion IDLE
     */
    override fun execute(): Status {
        if (status != Status.RUNNING) {
            entity.animation(AnimationState.IDLE)
            currentDuration = duration?.nextFloat() ?: 1f
            return Status.RUNNING
        }

        currentDuration -= GdxAI.getTimepiece().deltaTime
        if (currentDuration <= 0f) {
            return Status.SUCCEEDED
        }

        return Status.SUCCEEDED
    }

    /**
     * Funcion que copia la acción
     *
     * @param task La accion a copiar
     */
    override fun copyTo(task: Task<AiEntity>?): Task<AiEntity>? {
        (task as IdleTask).duration = duration
        return task
    }
}

/**
 * Accion de la IA a moverse aleatoriamente
 */
class WanderTask(

) : Action() {
    /**
     * Posicion inicial
     */
    private val startPos = vec2()

    /**
     *  Posicion deseada a moverse
     */
    private val targetpos = vec2()

    /**
     * La entidad empieza a moverse a la posicion deseada,finaliza cuando llega en un radio a su posicion deseada
     */
    override fun execute(): Status {
        if (status != Status.RUNNING) {
            entity.animation((AnimationState.RUN))
            if (startPos.isZero) {
                startPos.set(entity.location)
            }
            entity.fireEvent(EntityAggroEvent(null))

            targetpos.set(startPos)
            targetpos.x += MathUtils.random(-1f, 1f)
            targetpos.y += MathUtils.random(-1f, 1f)
            entity.moveTo(targetpos)
            return Status.RUNNING
        }

        if (entity.inRange(0.5f, targetpos)) {
            entity.stopMovement()
            return Status.SUCCEEDED
        }
        return Status.RUNNING
    }
}

/**
 * Accion de IA a atacar , cuando no esta atacando , empieza el ataque, cuanod finaliza el ataque finaliza la ejecuxión
 */
class AttackTask : Action() {
    override fun execute(): Status {
        if (status != Status.RUNNING) {
            entity.animation(AnimationState.ATTACK, Animation.PlayMode.NORMAL, true)
            entity.doAndStartAttack()
            return Status.RUNNING
        }
        entity.fireEvent(EntityAggroEvent(entity.entity))

        if (entity.isAnimationDone) {
            entity.animation(AnimationState.IDLE)
            entity.stopMovement()

            return Status.SUCCEEDED
        }

        return Status.RUNNING
    }
}

/**
 * Accion de la Ia para seguir al jugador principal
 */
class FollowTask : Action() {
    /**
     * POsicion inicial
     */
    private val startPos = vec2()

    /**
     * Posicion deseada
     */
    private val targetpos = vec2()

    /**
     * Cuando no lo esta siguiendo, empieza a seguirlo, finaliza cuando llega a la posicion deseada
     */
    override fun execute(): Status {
        if (status != Status.RUNNING) {
            entity.animation(AnimationState.RUN)
            if (startPos.isZero) {
                startPos.set(entity.location)
            }
            entity.fireEvent(EntityAggroEvent(entity.entity))

            targetpos.set(startPos)
            targetpos.x = entity.followPlayerX()
            targetpos.y = entity.followPlayerY()
            entity.moveTo(targetpos)
            return  Status.SUCCEEDED
        }
        if (entity.inRange(0.5f, targetpos)) {
            entity.stopMovement()
            return Status.SUCCEEDED
        }

        return Status.RUNNING
    }

    companion object{
        private val log= logger<Action>()
    }
}

