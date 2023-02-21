package com.game.widget

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.game.event.MenuScreenEvent
import com.game.event.ResumeEvent
import com.game.event.fire
import com.game.ui.view.Buttons
import com.game.ui.view.Drawables
import com.game.ui.view.MenuView
import com.game.ui.view.get
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.scene2d.*

class Pause(
    private val skin: Skin,
) : WidgetGroup(), KGroup {
    private val background: Image =Image(skin[Drawables.FRAME_BGD])
    private val table: Table

    init {
        this += background

        table = table {
            textButton(text ="Resume", style = Buttons.DEFAULT.skinKey) {

                onClick { stage.fire(ResumeEvent()) }

                it.padBottom(10f)
                it.row()
            }

            textButton(text = "Settings", style = Buttons.DEFAULT.skinKey) {
             //   onClick { stage.fire(ShowSettingsEvent()) }

                it.padBottom(10f)
                it.row()
            }

            textButton(text ="Menu", style = Buttons.DEFAULT.skinKey) {
                onClick { stage.fire(MenuScreenEvent()) }

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
fun <S> KWidget<S>.pauseUp(
    skin: Skin = Scene2DSkin.defaultSkin,
    init: Pause.(S) -> Unit = {}
): Pause = actor(Pause(skin), init)