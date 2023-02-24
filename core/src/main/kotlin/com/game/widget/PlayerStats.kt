package com.game.widget


import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup

import com.game.ui.view.Labels
import ktx.actors.plusAssign
import ktx.scene2d.*
import com.badlogic.gdx.scenes.scene2d.ui.Label

/**
 * Componente que muestra las estadisticas del jugador
 */
class PlayerStats(
    ) : WidgetGroup(), KGroup {
    /**
     * Etiquetas de texto
     */
    val lblLvl : Label
    val lblLife : Label
    val lblSpeed :Label
    val lblAttack : Label

    init {

        lblLvl = label("", Labels.LEVEL.skinKey).apply {
            setPosition(64f,116f)
        }

        this +=lblLvl
        lblLife  = label("", Labels.LEVEL.skinKey).apply {
            setPosition(64f,116f)
        }

        this +=lblLife
        lblAttack = label("", Labels.LEVEL.skinKey).apply {
            setPosition(64f,116f)
        }

        this +=lblAttack

        lblSpeed = label("", Labels.LEVEL.skinKey).apply {
            setPosition(64f,116f)
        }
        this +=lblSpeed
    }

    /**
     * Actualiza la etiqueta de nivel del personaje
     */
    fun level(lvl: Int) = lblLvl.setText(lvl.toString())

}

@Scene2dDsl
        /**
         * Extension del constructor para poder añadirla como actor al escenario
         */
fun <S> KWidget<S>.playerStats(
    init: PlayerStats.(S) -> Unit = {}
): PlayerStats = actor(PlayerStats(), init)