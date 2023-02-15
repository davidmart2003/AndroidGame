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
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.world
import ktx.math.vec2

abstract class Action(

) : LeafTask<AiEntity>() {
    val entity: AiEntity
        get() = `object`


    override fun copyTo(task: Task<AiEntity>?) = task


}

class IdleTask(
    @JvmField
    @TaskAttribute(required = true)
    var duration: FloatDistribution? = null
) : Action() {
    private var currentDuration = 0f
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

    override fun copyTo(task: Task<AiEntity>?): Task<AiEntity>? {
        (task as IdleTask).duration = duration
        return task
    }
}

class WanderTask(

) : Action() {
    private val startPos = vec2()
    private val targetpos = vec2()
    override fun execute(): Status {
        if (status != Status.RUNNING) {
            entity.animation((AnimationState.RUN))
            if (startPos.isZero) {
                startPos.set(entity.location)
            }
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

class AttackTask : Action() {
    override fun execute(): Status {
        if (status != Status.RUNNING) {
            entity.animation(AnimationState.ATTACK, Animation.PlayMode.NORMAL, true)
            entity.fireEvent(EntityAggroEvent(entity.entity))
            entity.doAndStartAttack()
            return Status.RUNNING
        }

        if (entity.isAnimationDone) {
            entity.animation(AnimationState.IDLE)
            entity.stopMovement()

            return Status.SUCCEEDED
        }

        return Status.RUNNING
    }
}

class FollowTask : Action() {
    private val startPos = vec2()
    private val targetpos = vec2()
    override fun execute(): Status {
        if (status != Status.RUNNING) {
            entity.animation(AnimationState.RUN)
            if (startPos.isZero) {
                startPos.set(entity.location)
            }
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
}

