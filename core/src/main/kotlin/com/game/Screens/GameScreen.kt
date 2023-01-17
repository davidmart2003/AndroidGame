package com.game.Screens

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.game.Component.ImageComponent
import com.game.Component.ImageComponent.Companion.ImageComponentListener
import com.game.Component.PhysicComponent
import com.game.Component.PhysicComponent.Companion.PhysicComponentListener
import com.game.System.*
import com.game.event.MapChangeEvent
import com.game.event.fire
import com.game.input.PlayerKeyboardInputProcessor
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.math.vec2

class GameScreen : KtxScreen {

    private val textureAtlas: TextureAtlas = TextureAtlas("graphics/gameObjects.atlas")
    private val stage: Stage = Stage(ExtendViewport(16f, 9f))
    private var currentMap: TiledMap? = null
    private val physicsWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false

    }
    private val world = world {
        injectables {
            add(stage)
            add(textureAtlas)
            add(physicsWorld)
        }

        components {
            add<ImageComponentListener>()
            add<PhysicComponentListener>()
        }
        systems {

            add<EntitySpawnSystem>()
            add<CollisionSpawnSystem>()
            add<MoveSystem>()
            add<AttackSystem>()
            add<LifeSystem>()
            add<DeadSystem>()
            add<AnimationSystem>()
            add<PhysicSystem>()
            add<CameraSystem>()
            add<RenderSystem>()
            add<DebugSystem>()
        }
    }

    override fun show() {
        super.show()

        world.systems.forEach { system ->
            if (system is EventListener) {
                stage.addListener(system)
            }
        }

        currentMap = TmxMapLoader().load("map/map1.tmx")
        stage.fire(MapChangeEvent(currentMap!!))

        PlayerKeyboardInputProcessor(world)


    }

    override fun render(delta: Float) {
        world.update(delta.coerceAtMost(0.25f))

    }


    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        stage.disposeSafely()
        world.dispose()
        textureAtlas.dispose()
        currentMap?.disposeSafely()
        physicsWorld.disposeSafely()
    }
}