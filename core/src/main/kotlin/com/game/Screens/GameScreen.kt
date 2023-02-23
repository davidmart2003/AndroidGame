package com.game.S

import com.game.Screens.MenuScreen


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.game.MyGame
import com.game.component.AiComponent
import com.game.component.FloatingTextComponent
import com.game.component.ImageComponent.Companion.ImageComponentListener
import com.game.component.PhysicComponent.Companion.PhysicComponentListener
import com.game.component.StateComponent
import com.game.event.*
import com.game.system.*
import com.game.model.GameModel
import com.game.ui.view.*
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2
import ktx.scene2d.actors

class GameScreen(val game: MyGame) : KtxScreen, EventListener {
    private val settingPref = game.settingPref
    private val recordPref = game.recordPref
    private val textureAtlas: TextureAtlas = game.textureAtlas
    private val gameStage: Stage = game.gameStage
    private val uiStage: Stage = Stage(ExtendViewport(1280f, 720f))
    private var currentMap: TiledMap? = null
    private val physicsWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false

    }
    private var settingsView : SettingsView
    private  var inventory:InventoryView
    private var gameView: GameView
    private val world = world {
        injectables {
            add("gameStage", gameStage)
            add("uiStage", uiStage)
            add(textureAtlas)
            add(physicsWorld)
            add(settingPref)
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
            add<TimeSystem>()
            add<AiSystem>()
            add<PhysicSystem>()
            add<CameraSystem>()
            add<FloatingTextSystem>()
            add<LevelSystem>()
            add<SpawnPortalSystem>()
            add<DespawnSystem>()
            add<AudioSystem>()
            add<ShieldSystem>()
            add<RenderSystem>()
            add<DebugSystem>()
        }
    }

    init {
        loadSkin()
        world.systems.forEach { system ->
            if (system is EventListener) {
                gameStage.addListener(system)
                uiStage.addListener(system)
            }
        }
        uiStage.actors {
            gameView = gameView(GameModel(world, gameStage), recordPref =recordPref )
            inventory =inventoryView(GameModel(world, gameStage)) {
                this.isVisible = false
            }
            settingsView = settingsView(settingPref){
                this.isVisible=false
            }
        }
            uiStage.isDebugAll=true
    }

    override fun show() {
        super.show()
        currentMap = TmxMapLoader().load("map/map1.tmx")
        gameStage.fire(MapChangeEvent(currentMap!!))
        uiStage.addListener(this)
        gameStage.addListener(this)
        Gdx.input.inputProcessor = uiStage

    }

    /**
     * Pausa diferentes sistemas del mundo de entidades para parar el juego
     */
    private fun pauseWorld(pause: Boolean) {
        //Sistemas que van a seguir ejecutandose
        val mandatorySystems = setOf(
            AnimationSystem::class, CameraSystem::class, RenderSystem::class, DebugSystem::class
        )
        //Para el resto de sistemas que no se ha especificado
        world.systems.filter { it::class !in mandatorySystems }.forEach { it.enabled = !pause }
    }

    /**
     * Se ejecuta cuando entra en estado de pausa la ventana, y pausa el juego
     */
    override fun pause() = pauseWorld(true)
    override fun resume() =pauseWorld(false)

    override fun render(delta: Float) {
        val dt = delta.coerceAtMost(0.25f)
        GdxAI.getTimepiece().update(dt)
        world.update(delta.coerceAtMost(0.25f))
    }

    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
    }

    override fun dispose() {
        world.dispose()
        currentMap?.disposeSafely()
        physicsWorld.disposeSafely()
    }

    override fun handle(event: Event?): Boolean {
        when (event) {

            is PauseEvent -> {
                pause()
                gameView.pause()

            }

            is ResumeEvent -> {
                gameView.resume()
                resume()
            }

            is MenuScreenEvent ->{
                gameStage.clear()
                uiStage.clear()

                game.addScreen(MenuScreen(game))
                game.setScreen<MenuScreen>()

                game.removeScreen<GameScreen>()
                super.hide()
                this.dispose()
            }

            is InventoryEvent ->{
                inventory.isVisible=true
            }

            is HideInventoryEvent ->{
                inventory.isVisible=false
            }

            is DeadEvent ->{
                gameView.controller.touchable= Touchable.disabled
                pause()
                gameView.death()
            }
            is WinEvent ->{
                gameView.controller.touchable= Touchable.disabled
                pause()
                gameView.win()
            }


            is SettingsGameEvent -> {
                settingsView.isVisible=true
            }

            is HideSettingsGameEvent -> {
                settingsView.isVisible=false
            }

        }
        return true
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}