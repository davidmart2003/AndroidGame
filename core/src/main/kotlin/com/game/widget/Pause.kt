package com.game.widget

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.I18NBundle
import com.game.event.MenuScreenEvent
import com.game.event.ResumeEvent
import com.game.event.SettingsGameEvent
import com.game.event.fire
import com.game.ui.view.Buttons
import com.game.ui.view.Drawables
import com.game.ui.view.MenuView
import com.game.ui.view.get
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.scene2d.*

/**
 * Componente que pausa el juego y muestra un menu de opciones
 *
 * @property skin Skin de los componentes
 */
class Pause(
    bundle:I18NBundle,
    private val skin: Skin,
) : WidgetGroup(), KGroup {
    private val background: Image = Image(skin[Drawables.FRAME_BGD])
    private val table: Table

    init {
        this += background

        table = table {
            textButton(text = bundle["Resume"], style = Buttons.DEFAULT.skinKey) {

                onClick { stage.fire(ResumeEvent()) }
                it.size(200f, 150f)
                it.padBottom(10f)
                it.row()
            }

            textButton(text = bundle["Settings"], style = Buttons.DEFAULT.skinKey) {
                onClick { stage.fire(SettingsGameEvent()) }
                it.size(200f, 150f)

                it.padBottom(10f)
                it.row()
            }

            textButton(text = bundle["Menu"], style = Buttons.DEFAULT.skinKey) {
                onClick { stage.fire(MenuScreenEvent()) }
                it.size(200f, 150f)

                it.padBottom(10f)
                it.row()
            }

            setPosition(this@Pause.background.width * 0.5f, this@Pause.background.height * 0.5f)
        }

        this += table
    }

    override fun getPrefWidth() = background.drawable.minWidth

    override fun getPrefHeight() = background.drawable.minHeight
}

@Scene2dDsl
        /**
         * Extension del constructor para poder añadirla como actor al escenario
         */
fun <S> KWidget<S>.pauseUp(
    bundle:I18NBundle,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: Pause.(S) -> Unit = {}
): Pause = actor(Pause(bundle,skin), init)