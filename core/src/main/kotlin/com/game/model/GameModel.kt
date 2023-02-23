package com.game.model

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.component.AnimationComponent
import com.game.component.LifeComponent
import com.game.component.PlayerComponent
import com.game.event.*
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import ktx.log.logger
import kotlin.math.roundToInt


class GameModel(
    world: World,
    stage: Stage,
) : PropertyChangeSource(), EventListener {

    private val playerComponents: ComponentMapper<PlayerComponent> = world.mapper()
    private val lifeComponents: ComponentMapper<LifeComponent> = world.mapper()
    private val animationComponents: ComponentMapper<AnimationComponent> = world.mapper()
    var level by propertyNotify(1)
    var life by propertyNotify(1)
    var speed by propertyNotify(1)
    var attack by propertyNotify(1)

    var playerLife by propertyNotify(1f)

    private var lastEnemy = Entity(-1)
    var enemyLife = 1f
    var enemyType by propertyNotify("")
    var lootText by propertyNotify("")

    init {
        stage.addListener(this)
    }

    private fun updateEnemy(enemy: Entity?) {
        if (enemy == null) {
            enemyType = enemy.toString()
            return
        }
        val lifeCmp = lifeComponents[enemy]
        enemyLife = lifeCmp.life / lifeCmp.maxLife
       // log.debug { "$enemy " }
        if (lastEnemy != enemy) {
            // update enemy type
            lastEnemy = enemy
            animationComponents.getOrNull(enemy)?.atlasKey?.let { enemyType ->
                this.enemyType = enemyType
            }
        } else {
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


            is SpawnPortalEvent -> {
                lootText="A portal has been spawned, search it"
            }
            is EntityAggroEvent -> {
                updateEnemy(event.entity)
            }

            is LevelUpEvent -> {
                level = event.level
            }

            is MaxLifeEvent ->{
                life = event.life.roundToInt()
            }
            is SpeedEvent ->{
                 speed = event.speed.roundToInt()
            }
            is AttackEvent ->{
                attack = event.attack.roundToInt()
            }
            else -> return false
        }

        return true
    }

    companion object {
        private val log = logger<Action>()
    }
}