package com.game.system

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.component.*
import com.game.event.MapChangeEvent
import com.game.event.fire
import com.github.quillraven.fleks.*
import ktx.log.debug
import ktx.log.logger
import ktx.math.vec2

/**
 * Sistema que se encarga del despawn de las entidades
 *
 * @property gameStage Escenario que renderiza el juego
 * @property playerComponents Conjunto de entidades que contiene PlayerComponent
 */
@AllOf([DespawnComponent::class, PlayerComponent::class])
@NoneOf([SpawnComponent::class])
class DespawnSystem(
    @Qualifier("gameStage") private val gameStage: Stage,
    private val playerComponents: ComponentMapper<PlayerComponent>
) : IteratingSystem() {
    /**
     * Mapa actual
     */
    private var currentMap: TiledMap? = null

    /**
     * Número del mapa a escoger
     */
    private var numMap: Int=1

    /**
     * Por cada entidad despawnea y cambia el mapa
     */
    override fun onTickEntity(entity: Entity) {
        world.removeAll()
        numMap++
        currentMap = TmxMapLoader().load("map/map$numMap.tmx")
        gameStage.fire(MapChangeEvent(currentMap!!,playerComponents[entity]))

    }

    companion object {
        private val log = logger<DespawnSystem>()
    }
}