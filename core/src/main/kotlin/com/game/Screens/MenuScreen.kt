package com.game.Screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.game.MyGame
import com.game.S.GameScreen
import com.game.component.ImageComponent

import com.game.component.LifeComponent
import com.game.component.PhysicComponent
import com.game.component.PlayerComponent
import com.game.event.*
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

/**
 *  Pantalla del menu principal
 */
class MenuScreen(val game: MyGame) : KtxScreen, EventListener {

    private val settingPref = game.settingPref
    /**
     * Escenario donde se renderiza la interfaz de usuario
     */
    private val uiStage = game.uiStage
    /**
     * Escenario donde se renderiza el juegio
     */
    private val gameStage = game.gameStage
    /**
     * Vista que contiene el Menu Principal
     */
    private var menuView: MenuView
    /**
     * Vista que contiene las Opciones
     */
    private var settingsView: SettingsView

    /**
     * Vista que contiene los creditos
     */
    private var creditsView: CreditsView

    /**
     * Vista que contiene el tutorial
     */
    //  private var tutorialView: TutorialView


    /**
     * Mundo de entidades
     */
    private val eWorld = world {}


    init {
        loadSkin()
        Gdx.input.inputProcessor = uiStage

        eWorld.systems.forEach { system ->
            if (system is EventListener) {
                gameStage.addListener(system)
                uiStage.addListener(system)
            }
        }

        uiStage.actors {
            menuView = menuView(GameModel(eWorld, uiStage))
            settingsView = settingsView(settingPref) {
                isVisible = false
            }
            creditsView = creditsView() {
                isVisible = false
            }
        }
        uiStage.addListener(this)
    }

    /**
     * Se ejecuta cuando la ventana cambia de tamaño
     */
    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
    }

    /**
     * Se ejecutada cada frame para actualizar la ventana
     */
    override fun render(delta: Float) {
        eWorld.update(delta)
        uiStage.act()
        uiStage.draw()
    }

    /**
     * Se ejecuta cuando al cerrar la ventana
     */
    override fun dispose() {
        super.dispose()
        eWorld.dispose()
    }

    /**
     * Se ejecuta cuando un evento es lanzado
     *
     * @param event Evento lanzado
     */
    override fun handle(event: Event?): Boolean {
        when (event) {
            is NewGameEvent -> {
                gameStage.clear()
                uiStage.clear()

                game.addScreen(GameScreen(game))
                game.setScreen<GameScreen>()

                game.removeScreen<MenuScreen>()
                super.hide()
                this.dispose()
            }

            is SettingsGameEvent -> {
                menuView.touchable = Touchable.disabled
                settingsView.isVisible = true
            }

            is HideSettingsGameEvent -> {
                menuView.touchable = Touchable.enabled

                settingsView.isVisible = false
            }

            is CreditsGameEvent -> {
                creditsView.isVisible = true
            }

            is HideCreditsEvent -> {
                creditsView.isVisible = false
            }
        }
        return true
    }
}