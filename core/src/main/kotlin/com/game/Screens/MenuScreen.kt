package com.game.Screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.game.MyGame
import com.game.S.GameScreen
import com.game.component.ImageComponent

import com.game.component.LifeComponent
import com.game.component.PhysicComponent
import com.game.component.PlayerComponent
import com.game.event.MenuScreenEvent
import com.game.event.NewGameEvent
import com.game.model.GameModel
import com.game.system.*
import com.game.ui.view.*
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.world

import ktx.app.KtxScreen
import ktx.box2d.createWorld
import ktx.math.vec2
import ktx.scene2d.*
import javax.sound.sampled.AudioSystem

class MenuScreen(val game: MyGame) : KtxScreen,EventListener {

    private val stage: Stage = Stage(ExtendViewport(720f, 480f))
    private val world = world { }
    private val model = GameModel(world, stage)

    /**
     * Escenario que representa la UI del juego, se inicia con la uiStage del jueg0
     */
    private val uiStage = game.uiStage

    /**
     * Escenario que representa el juego, se inicia con la gameStage del juego
     */
    private val gameStage = game.gameStage

    /**
     * Vista que contiene el menu
     */
    private var menuView: MenuView


    /**
     * Vista que contiene los ajustes
     */
    // private var settingsView: SettingsView

    /**
     * Vista que contiene los creditos
     */
    // private var creditsView: CreditsView

    /**
     * Vista que contiene el tutorial
     */
    //  private var tutorialView: TutorialView


    /**
     * Datos que se guardan sobre los ajustes
     */
    // private val settingsPrefs: Preferences = game.settingsPrefs

    /**
     * Atlas de texturas que contiene imagenes
     */
    private val textureAtlas = game.textureAtlas

    /**
     * Mapa actual que se muestra de fondo
     */
    // private var currentMap: TiledMap? = null

    /**
     * Mundo de físicas del fondo
     */
    private val physicWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false
    }

    /**
     * Mundo de entidades que representa el fondo del menu principal
     */
    private val eWorld = world {
//        injectables {
//            add("uiStage", uiStage)
//            add("gameStage", gameStage)
//            add(textureAtlas)
//            add(physicWorld)
//          //add(settingsPrefs)
//        }
//
//
//
//        systems {
//
//        //    add<AudioSystem()>
//        }
    }

    init {
        loadSkin()
        Gdx.input.inputProcessor=uiStage
        // Añade al escenario del juego y interfaz los diferentes sistemas que actuan como oyentes de diferentes eventos
        eWorld.systems.forEach { system ->
            if (system is EventListener) {
                gameStage.addListener(system)
                uiStage.addListener(system)
            }
        }

        uiStage.actors {
            menuView = menuView(GameModel(eWorld, uiStage))
            // settingsView = settingsView(bundle = game.bundle, prefs = settingsPrefs)
        }
        //Añade al escenario del juego y UI esta msima clase
        uiStage.addListener(this)
        uiStage.isDebugAll=true
    }


    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
    }



    override fun render(delta: Float) {
        eWorld.update(delta)
        uiStage.act()
        uiStage.draw()
    }

    override fun dispose() {
        super.dispose()
        eWorld.dispose()
    }

    override fun handle(event: Event?): Boolean {
        when (event){
            is NewGameEvent ->{
                gameStage.clear()
                uiStage.clear()

                game.addScreen(GameScreen(game))
                game.setScreen<GameScreen>()

                game.removeScreen<MenuScreen>()
                super.hide()
                this.dispose()
            }

            is MenuScreenEvent ->{

            }
        }
        return true
    }
}