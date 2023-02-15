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

@AllOf([DespawnComponent::class, PlayerComponent::class])
@NoneOf([SpawnComponent::class])
class DespawnSystem(
    @Qualifier("gameStage") private val gameStage: Stage,
    private val playerComponents: ComponentMapper<PlayerComponent>
) : IteratingSystem() {
    private var locationX: Float = 0f;
    private var currentMap: TiledMap? = null
    private var numMap: Int=1
    override fun onTickEntity(entity: Entity) {
        val playerComponent = playerComponents[entity]
        world.removeAll()
        numMap++
        currentMap = TmxMapLoader().load("map/map$numMap.tmx")
        gameStage.fire(MapChangeEvent(currentMap!!))

    }

    companion object {
        private val log = logger<DespawnSystem>()
    }
}