package com.game.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.I18NBundle
import com.game.MyGame
import com.game.event.HideCreditsEvent
import com.game.event.HideInventoryEvent
import com.game.event.fire
import com.game.model.GameModel
import ktx.actors.onClick
import ktx.scene2d.*

/**
 * Vista de los creditos del juego
 *
 * @property skin Skin de los componentes
 */
class CreditsView(
    bundle:I18NBundle,
    skin: Skin

) : Table(skin), KTable {

    init {
        // UI
        setFillParent(true)

        table {
            background = skin[Drawables.FRAME_BGD]

            label(text = "ASSETS", style = Labels.LEVEL.skinKey, skin) {
                this.setAlignment(Align.center)
                this.setFontScale(1.5f)

                it.row()
            }
            table {

                label(text = "-Monsters Creatures Fantasy,"+ bundle["CreditosTexto"] +"itch.io", style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).row()
                }
                label(text = "-Cryo's Mini GUI,"+ bundle["CreditosTexto"] +" itch.io", style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).row()
                }
                label(
                    text = "-Generic Character Asset v 0.2, "+ bundle["CreditosTexto"] +" itch.io",
                    style = Labels.LEVEL.skinKey,
                    skin
                ) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).row()
                }
                it.expand().fill().row()
            }

            label(text = "Sound&Music", style = Labels.LEVEL.skinKey, skin) {
                this.setFontScale(1.5f)
                this.setAlignment(Align.center)
                it.padTop(20f).row()
            }
            table {

                label(text = "-Town Theme RPG,"+ bundle["CreditosTexto"] +" opengameart.com", style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).row()
                }
                label(text = "-Battle Theme II , "+ bundle["CreditosTexto"] +" opengameart.com", style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).row()
                }
                label(text = "-Goblin Death, "+ bundle["CreditosTexto"] +" opengameart.com", style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).row()
                }
                label(
                    text = "-15 monster grunt/pain/death sounds, "+ bundle["CreditosTexto"] +" opengameart.com",
                    style = Labels.LEVEL.skinKey,
                    skin
                ) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).row()
                }
                label(text = "-Mutant Death, "+ bundle["CreditosTexto"] +" opengameart.com", style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).row()
                }
                label(
                    text = "Wind, hit, time morph, "+ bundle["CreditosTexto"] +" opengameart.com",
                    style = Labels.LEVEL.skinKey,
                    skin
                ) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).row()
                }
                it.expand().fill().row()
            }

            label(text =  bundle["Agradecimientos"] , style = Labels.LEVEL.skinKey, skin) {
                this.setFontScale(1.5f)
                this.setAlignment(Align.center)
                it.padTop(20f).row()
            }
            table {

                label(text = "-Manuel Ricardo Marín Martinez (Motivation&BetaTester)", style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).row()
                }
                label(text = "-Alberto Gómez Castro, (Motivation&BetaTester)", style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).row()
                }
                label(text = "-Angel León Caamaño, (Motivation)", style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).row()
                }
                label(text = "-Pablo Abilleira Cacheda, (BetaTester)", style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).row()
                }
                label(text = "-Sergio Piñeiro Castro , (BetaTester)", style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).row()
                }
                it.expand().fill().row()
            }

            textButton(text = bundle["Atras"], style = Buttons.DEFAULT.skinKey) {
                onClick { stage.fire(HideCreditsEvent()) }


            }

            it.expand()
        }
    }
}

@Scene2dDsl
        /**
         * Extension del constructor para poder añadirla como actor al escenario
         */
fun <S> KWidget<S>.creditsView(
    bundle:I18NBundle,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: CreditsView.(S) -> Unit = {}
): CreditsView = actor(CreditsView(bundle,skin), init)
