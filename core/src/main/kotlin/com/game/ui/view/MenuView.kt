package com.game.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.I18NBundle
import com.game.event.*
import com.game.model.GameModel
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.log.logger
import ktx.scene2d.*

/**
 * Vista que muestra el menu del juego
 *
 * @property skin Skin de los componentes
 */
class MenuView(
    model: GameModel,
    bundle : I18NBundle,
    skin: Skin
) : Table(skin), KTable {
    /**
     * Componente boton que contiene texto
     */
    private val btnNewGame: TextButton
    private val btnOptions: TextButton
    private val btnCredits: TextButton
    private val btnHelp: TextButton
    private val btnExit: TextButton

    init {
        setFillParent(true)

        table {


            label(text = bundle["MenuTitulo"], style = Labels.TITLE.skinKey) {
                setSize(100f, 100f)
                it.row()
            }

            this@MenuView.btnNewGame =
                textButton(text = bundle["NuevoJuego"], style = Buttons.DEFAULT.skinKey) {
                    onClick { stage.fire(NewGameEvent()) }
                    it.padBottom(9f)
                    it.padLeft(100f)
                    it.width(200f).height(50f).row()

                }



            this@MenuView.btnOptions =
                textButton(text = bundle["Opciones"], style = Buttons.DEFAULT.skinKey) {
                    label.y -= 8

                    onClick {
                        stage.fire(SettingsGameEvent())
                    }
                    it.padLeft(100f)
                    it.padBottom(10f)
                    it.width(200f).height(50f).row()
                }

            this@MenuView.btnCredits =
                textButton(text = bundle["Creditos"], style = Buttons.DEFAULT.skinKey) {
                    label.y -= 2

                    onClick {
                         stage.fire(CreditsGameEvent())
                    }
                    it.padLeft(100f)
                    it.padBottom(10f)
                    it.width(200f).height(50f).row()
                }
            this@MenuView.btnHelp =
                textButton(text = bundle["Ayuda"], style = Buttons.DEFAULT.skinKey) {
                    label.y -= 2

                    onClick {
                        stage.fire(TutorialGameEvent())
                    }
                    it.padLeft(100f)
                    it.padBottom(10f)
                    it.width(200f).height(50f).row()
                }

            this@MenuView.btnExit =
                textButton(text = bundle["Salir"], style = Buttons.DEFAULT.skinKey) {
                    label.y -= 2
                    onClick { Gdx.app.exit() }
                    it.padLeft(100f)
                    it.padBottom(10f)
                    it.width(200f).height(50f).row()
                }

            it.expand().fill().left().top()
        }


    }

    companion object {
        private val log = logger<MenuView>()
    }
}

@Scene2dDsl
        /**
         * Extension del constructor para poder añadirla como actor al escenario
         */
fun <S> KWidget<S>.menuView(
    model: GameModel,
    bundle: I18NBundle,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: MenuView.(S) -> Unit = {}
): MenuView = actor(MenuView(model,bundle, skin), init)
