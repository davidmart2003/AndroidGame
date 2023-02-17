package com.game.model

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.component.AnimationComponent
import com.game.component.LifeComponent
import com.game.component.PlayerComponent
import com.game.event.EntityAggroEvent
import com.game.event.EntityDamageEvent
import com.game.event.EntityLootEvent
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import ktx.log.logger


class GameModel(
    world: World,
    stage: Stage,
) : PropertyChangeSource(), EventListener {


    private val playerComponents: ComponentMapper<PlayerComponent> = world.mapper()
    private val lifeComponents: ComponentMapper<LifeComponent> = world.mapper()
    private val animationComponents: ComponentMapper<AnimationComponent> = world.mapper()

    var playerLife by propertyNotify(1f)

    private var lastEnemy = Entity(-1)
    var enemyLife = 1f
    var enemyType by propertyNotify("")
    var lootText = ""
        private set(value) {
            notify(::lootText, value)
            field = value
        }

    init {
        stage.addListener(this)
    }

    private fun updateEnemy(enemy: Entity?) {
        if(enemy==null){
            enemyType= enemy.toString()
            return
        }
        val lifeCmp = lifeComponents[enemy]
        enemyLife = lifeCmp.life / lifeCmp.maxLife
        log.debug { "$enemy "}
        if (lastEnemy != enemy) {
            // update enemy type
            lastEnemy = enemy
            animationComponents.getOrNull(enemy)?.atlasKey?.let { enemyType ->
                this.enemyType = enemyType
            }
        }else {
            animationComponents.getOrNull(enemy)?.atlasKey?.let { enemyType ->
                this.enemyType = enemyType
            }
        }
    }

    override fun handle(event: Event?): Boolean {
        when (event) {
            is EntityDamageEvent -> {
                val isPlayer = event.entity in playerComponents
                val lifeComponent = lifeComponents[event.entity]
                if (isPlayer) {
                    playerLife = lifeComponent.life / lifeComponent.maxLife
                } else {
                    updateEnemy(event.entity)
                    enemyLife = lifeComponent.life / lifeComponent.maxLife
                }
            }

            is EntityLootEvent -> {
                lootText = "You found something useful"
            }

            is EntityAggroEvent -> {
                updateEnemy(event.entity)
            }

            else -> return false
        }

        return true
    }
    companion object{
        private val log= logger<Action>()
    }
}