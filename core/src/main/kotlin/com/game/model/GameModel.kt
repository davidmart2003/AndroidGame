package com.game.model

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.component.AnimationComponent
import com.game.component.LifeComponent
import com.game.component.PlayerComponent
import com.game.component.TimeComponent
import com.game.event.*
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import ktx.log.logger
import kotlin.math.roundToInt


/**
 * MOdelo del jeugo que notifica y actualiza los cambios
 *
 * @param world Mundo de entidades
 * @param stage Escenario donde se renderiza
 */
class GameModel(
    world: World,
    stage: Stage
) : PropertyChangeSource(), EventListener {

    /**
     * Conjunto de entidades que contiene PlayerComponent
     */
    private val playerComponents: ComponentMapper<PlayerComponent> = world.mapper()
    /**
     * Conjunto de entidades que contiene LifeComponent
     */
    private val lifeComponents: ComponentMapper<LifeComponent> = world.mapper()
    /**
     * Conjunto de entidades que contiene AnimationComponent
     */
    private val animationComponents: ComponentMapper<AnimationComponent> = world.mapper()

    /**
     * Nivel del jugador que notifica si hay un cambio
     */
    var level by propertyNotify(1)
    /**
     * Vida del jugador en las estadisticas que notifica si hay un cambio
     */
    var life by propertyNotify(1)
    /**
     * Velocidad del jugador en las estadisticas que notifica si hay un cambio
     */
    var speed by propertyNotify(1)
    /**
     * Ataque del jugador en las estadisticas que notifica si hay un cambio
     */
    var attack by propertyNotify(1)
    /**
     * Tiempo de juego en las estadisticas que notifica si hay un cambio
     */
    var time by propertyNotify(1)

    /**
     * Vida del jugador que notifica si hay un cambio
     */
    var playerLife by propertyNotify(1f)

    /**
     * Ultimo enemigo entidad que notifica si hay un cambio
     */
    private var lastEnemy = Entity(-1)

    /**
     * Vida de los enemigos
     */
    var enemyLife = 1f

    /**
     * Tipo de enemigo que nmotifica si cambia
     */
    var enemyType by propertyNotify("")

    /**
     *
     */
    var lootText by propertyNotify("")

    init {
        stage.addListener(this)
    }

    /**
     * Actualiza la informacion de los enemigos
     *
     * @param enemy Entidad del enemigo
     */
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

    /**
     * Se ejecuta cada que un evento es lanzado
     *
     * @param event Evento lanzado
     */
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

            is ActualLifeEvent ->{
                life = event.life.roundToInt()
            }
            is SpeedEvent ->{
                 speed = event.speed.roundToInt()
            }
            is AttackEvent ->{
                attack = event.attack.roundToInt()
            }
            is TimeEvent -> {

                time=playerComponents[event.entity].actualTime.toInt()
            }
            else -> return false
        }

        return true
    }

    companion object {
        private val log = logger<Action>()
    }
}