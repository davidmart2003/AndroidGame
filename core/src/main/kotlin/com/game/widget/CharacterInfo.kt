package com.game.widget

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.game.ui.view.get
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Scaling
import com.game.ui.view.Drawables
import com.game.ui.view.Labels
import jdk.jfr.Label
import jdk.jfr.Percentage
import ktx.actors.plusAssign
import ktx.scene2d.*

/**
 * Componente creado para mostrar la informacion de los personajes
 *
 * @property charDrawable Modelo del personaje a mostrar
 * @property skin Skin de los componentes
 */
class CharacterInfo(
    val charDrawable: Drawables?,
    private val skin: Skin,

) : WidgetGroup(), KGroup {
    /**
     * Imagen del fondo que coge del enumerado de DRAWABLES
     */
    private val background: Image = Image(skin[Drawables.BACKGROUNDINFO])

    /**
     * Imagen del personaje
     */
    private val character: Image = Image(if (charDrawable == null) null else skin[charDrawable])

    /**
     * Imagen de la barra de vida
     */
    private val lifeBar: Image = Image(skin[Drawables.LIFEBAR])

    /**
     * Imagen de fondo diferente a la anterior
     */
    private val fondo : Image = Image(skin[Drawables.FRAME_FGD])

    /**
     * Etiqueta de texto del nivel del personaje
     */
    val lblLvl : com.badlogic.gdx.scenes.scene2d.ui.Label

    init {
        this +=fondo.apply {
            setPosition(32f,72f)
        }
        this += background
        this += character.apply {
            setPosition(32f, 70f)
            setSize(64f, 64f)
            setScaling(Scaling.contain)
        }
        this += lifeBar.apply {
            setPosition(21f, 16f)
            setSize(128f, 16f)
        }
        lblLvl = label("", Labels.LEVEL.skinKey).apply {
            setPosition(64f,116f)
        }
        this +=lblLvl
    }

    /**
     * Altura minima del componente
     */
    override fun getPrefHeight() = background.drawable.minHeight

    /**
     * Anchura minima del componente
     */
    override fun getPrefWidth() = background.drawable.minWidth

    /**
     * Funcion que difenrecia la informacion de si el personaje es enemigo o es el jugador
     */
    fun character(charDrawable: Drawables?) {
        if (charDrawable == null) {
            character.drawable = null
        } else {
        character.drawable=skin[charDrawable]
        }
    }

    /**
     * Actualiza la etiqueta del nivel del personaje
     */
    fun level(lvl: Int) = lblLvl.setText(lvl.toString())

    /**
     * Actualiza la barra de la vida del personaje
     */
    fun life(percentage: Float,duration:Float=0.75f){
        lifeBar.clearActions()
        lifeBar += Actions.scaleTo(MathUtils.clamp(percentage,0f,1f),1f,duration)
    }
}

@Scene2dDsl
        /**
         * Extension del constructor para poder añadirla como actor al escenario
         */
fun <S> KWidget<S>.characterInfo(
    charDrawable: Drawables?,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: CharacterInfo.(S) -> Unit = {}
): CharacterInfo = actor(CharacterInfo(charDrawable, skin), init)