package com.game.widget

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.I18NBundle
import com.game.event.MenuScreenEvent
import com.game.event.fire
import com.game.ui.view.Buttons
import com.game.ui.view.Drawables
import com.game.ui.view.Labels
import com.game.ui.view.get
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.scene2d.*

/**
 * Componente que salta al ganar la partida
 *
 * @property recordPref Almacen de los records del juego
 * @property skin Skin de los componentes
 */
class Win(
    private val recordPref: Preferences,
    private val bundle : I18NBundle,
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

    /**
     * Actualiza el record si los batido , si no actualiza la etiqueta con tu tiempo de juego
     */
    fun time(time: Int) {
        if (this@Win.recordPref.getInteger("time") > time || this@Win.recordPref.getInteger("time")==0) {
            this@Win.lblTime.setText(bundle["NewRecord"]+" "+ time)
            this@Win.recordPref.putInteger("time", time)
            this@Win.recordPref.flush()
        } else {
            this@Win.lblTime.setText(bundle["Win"]+ " "+time)
        }
    }

    override fun getPrefWidth() = background.drawable.minWidth

    override fun getPrefHeight() = background.drawable.minHeight
}


@Scene2dDsl
        /**
         * Extension del constructor para poder añadirla como actor al escenario
         */
fun <S> KWidget<S>.winUp(
    recordPref: Preferences,
    bundle : I18NBundle,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: Win.(S) -> Unit = {}
): Win = actor(Win(recordPref,bundle, skin), init)
