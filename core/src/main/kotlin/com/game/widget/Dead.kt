package com.game.widget

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.I18NBundle
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

/**
 * Componente que aparece cuando muere el personaje
 *
 * @property recordPref Almacen de los records del juego
 * @property skin Skin de los componentes
 */
class Dead(
    private val recordPref : Preferences,
    private val bundle: I18NBundle,
    private val skin: Skin,
) : WidgetGroup(), KGroup {
    private val background: Image = Image(skin[Drawables.FRAME_BGD])
    private val table: Table
    private var lblTime: Label

    init {
        this += background

        table = table {

            this@Dead.lblTime= label(text = "You are dead \n ", style = Labels.TITLE.skinKey) {
                it.row()

            }

            textButton(text = this@Dead.bundle["Menu"], style = Buttons.DEFAULT.skinKey) {
                onClick {
                    stage.fire(MenuScreenEvent())
                }

            }

            setPosition(this@Dead.background.width * 0.5f, this@Dead.background.height * 0.5f)
        }

        this += table
    }

    override fun getPrefWidth() = background.drawable.minWidth

    override fun getPrefHeight() = background.drawable.minHeight
    /**
     * Actualiza la etiqueta de tiempo y te muestra tu record de mejor tiempo
     */
    fun time(time: Int) {
        this@Dead.lblTime.setText(bundle["Dead"]+", "+bundle["YourTime"]+time+"\n"+bundle["BesTime"]+" "+this@Dead.recordPref.getInteger("time")+" seconds")
    }
    companion object {
        private val log = logger<Dead>()
    }
}


@Scene2dDsl
        /**
         * Extension del constructor para poder añadirla como actor al escenario
         */
fun <S> KWidget<S>.deadUp(
    recordPref: Preferences,
    bundle:I18NBundle,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: Dead.(S) -> Unit = {}
): Dead = actor(Dead(recordPref,bundle,skin), init)
