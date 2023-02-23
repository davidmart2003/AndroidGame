package com.game.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.game.event.NewGameEvent
import com.game.event.SettingsGameEvent
import com.game.event.fire
import com.game.model.GameModel
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.log.logger
import ktx.scene2d.*

class MenuView(
    model: GameModel,
    skin: Skin
) : Table(skin), KTable {

    private val btnNewGame: TextButton
    private val btnOptions: TextButton
    private val btnCredits: TextButton
    private val btnExit: TextButton
    private val fondo: Image = Image(skin[Drawables.FONDO])

    init {
        setFillParent(true)

        table {


            label(text = "The Warrior's Dream", style = Labels.TITLE.skinKey) {
                setSize(100f, 100f)
                it.row()
            }

            this@MenuView.btnNewGame =
                textButton(text = "NewGame", style = Buttons.DEFAULT.skinKey) {
                    onClick { stage.fire(NewGameEvent()) }
                    it.padBottom(9f)
                    it.padLeft(100f)
                    it.width(200f).height(50f).row()

                }



            this@MenuView.btnOptions =
                textButton(text = "Options", style = Buttons.DEFAULT.skinKey) {
                    label.y -= 8

                    onClick {
                        stage.fire(SettingsGameEvent())
                    }
                    it.padLeft(100f)
                    it.padBottom(10f)
                    it.width(200f).height(50f).row()
                }

            this@MenuView.btnCredits =
                textButton(text = "Creditos", style = Buttons.DEFAULT.skinKey) {
                    label.y -= 2

                    onClick {
                        // stage.fire(ShowCreditsEvent())
                    }
                    it.padLeft(100f)
                    it.padBottom(10f)
                    it.width(200f).height(50f).row()
                }

            this@MenuView.btnExit =
                textButton(text = "Salir", style = Buttons.DEFAULT.skinKey) {
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
fun <S> KWidget<S>.menuView(
    model: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: MenuView.(S) -> Unit = {}
): MenuView = actor(MenuView(model, skin), init)
