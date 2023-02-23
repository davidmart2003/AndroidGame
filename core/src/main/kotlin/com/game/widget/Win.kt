package com.game.widget

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
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
import ktx.scene2d.*

class Win(
    private val recordPref: Preferences,
    private val skin: Skin
) : WidgetGroup(), KGroup {
    private val background: Image = Image(skin[Drawables.FRAME_BGD])
    private val table: Table
    private var lblTime: Label

    init {
        this += background

        table = table {


            this@Win.lblTime = label(text = "You Win \n", style = Labels.FRAME.skinKey) {
                it.row()
            }

            textButton(text = "Menu", style = Buttons.DEFAULT.skinKey) {
                onClick { stage.fire(MenuScreenEvent()) }

            }

            setPosition(this@Win.background.width * 0.5f, this@Win.background.height * 0.5f)
        }

        this += table
    }

    fun time(time: Int) {
        if (this@Win.recordPref.getInteger("time") > time || this@Win.recordPref.getInteger("time")==0) {
            this@Win.lblTime.setText("New Record!!! Your new time is " + time + "seconds")
            this@Win.recordPref.putInteger("time", time)
        } else {
            this@Win.lblTime.setText("You win was $time seconds")
        }
    }

    override fun getPrefWidth() = background.drawable.minWidth

    override fun getPrefHeight() = background.drawable.minHeight
}


@Scene2dDsl
fun <S> KWidget<S>.winUp(
    recordPref: Preferences,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: Win.(S) -> Unit = {}
): Win = actor(Win(recordPref, skin), init)
