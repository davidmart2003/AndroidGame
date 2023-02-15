package com.game.Screens

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.game.component.AiComponent
import com.game.component.FloatingTextComponent
import com.game.component.ImageComponent.Companion.ImageComponentListener
import com.game.component.PhysicComponent.Companion.PhysicComponentListener
import com.game.component.StateComponent
import com.game.system.*
import com.game.event.MapChangeEvent
import com.game.event.fire
import com.game.input.PlayerKeyboardInputProcessor
import com.game.model.GameModel
import com.game.ui.view.gameView
import com.game.ui.view.loadSkin
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.math.vec2
import ktx.scene2d.actors
import java.util.logging.Level

class GameScreen : KtxScreen {

    private val textureAtlas: TextureAtlas = TextureAtlas("graphics/gameObjects.atlas")
    private val gameStage: Stage = Stage(ExtendViewport(16f, 9f))
    private val uiStage : Stage= Stage(ExtendViewport(1280f,720f))
    private var currentMap: TiledMap? = null
    private val physicsWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false

    }
    private val world = world {
        injectables {
            add("gameStage",gameStage)
            add("uiStage",uiStage)
            add(textureAtlas)
            add(physicsWorld)
        }

        components {
            add<FloatingTextComponent.Companion.FloatingTextComponentListener>()
            add<ImageComponentListener>()
            add<PhysicComponentListener>()
            add<StateComponent.Companion.StateComponentListener>()
            add<AiComponent.Companion.AiComponentListener>()
        }
        systems {

            add<EntitySpawnSystem>()
            add<CollisionSpawnSystem>()
            add<MoveSystem>()
            add<AttackSystem>()
            add<LifeSystem>()
            add<AnimationSystem>()
            add<DeadSystem>()
            add<StateSystem>()
            add<AiSystem>()
            add<PhysicSystem>()
            add<CameraSystem>()
            add<FloatingTextSystem>()
            add<LevelSystem>()
            add<SpawnPortalSystem>()
            add<DespawnSystem>()
            add<ShieldSystem>()
            add<RenderSystem>()
            add<DebugSystem>()
        }
    }

    init {
        loadSkin()
        uiStage.actors {
            gameView(GameModel(world,gameStage))

        }
    }

    override fun show() {
        super.show()

        world.systems.forEach { system ->
            if (system is EventListener) {
                gameStage.addListener(system)
            }
        }

        currentMap = TmxMapLoader().load("map/map1.tmx")
        gameStage.fire(MapChangeEvent(currentMap!!))

        PlayerKeyboardInputProcessor(world)


    }

    override fun render(delta: Float) {
        val dt = delta.coerceAtMost(0.25f)
        GdxAI.getTimepiece().update(dt)
        world.update(delta.coerceAtMost(0.25f))

    }


    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height, true)
        uiStage.viewport.update(width,height,true   )
    }

    override fun dispose() {
        gameStage.disposeSafely()
        uiStage.disposeSafely()
        world.dispose()
        textureAtlas.dispose()
        currentMap?.disposeSafely()
        physicsWorld.disposeSafely()
    }
}