package com.game.widget

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.game.event.MenuScreenEvent
import com.game.event.fire
import com.game.ui.view.Buttons
import com.game.ui.view.Drawables
import com.game.ui.view.Labels
import com.game.ui.view.get
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.log.logger
import ktx.scene2d.*

class Dead(private val skin: Skin) : WidgetGroup(), KGroup {
    private val background: Image = Image(skin[Drawables.FRAME_BGD])
    private val table: Table

    init {
        this += background

        table = table {

            label(text = "You are dead", style = Labels.TITLE.skinKey) {
                it.row()

            }

            textButton(text = "Menu", style = Buttons.DEFAULT.skinKey) {
                onClick {
                    log.debug { "PULSADO" }
                    stage.fire(MenuScreenEvent())
                }

            }

            setPosition(this@Dead.background.width * 0.5f, this@Dead.background.height * 0.5f)
        }

        this += table
    }

    override fun getPrefWidth() = background.drawable.minWidth

    override fun getPrefHeight() = background.drawable.minHeight

    companion object {
        private val log = logger<Dead>()
    }
}


@Scene2dDsl
fun <S> KWidget<S>.deadUp(
    skin: Skin = Scene2DSkin.defaultSkin,
    init: Dead.(S) -> Unit = {}
): Dead = actor(Dead(skin), init)
