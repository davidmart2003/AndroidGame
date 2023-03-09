package com.game.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.I18NBundle
import com.game.event.HideCreditsEvent
import com.game.event.HideTutorialEvent
import com.game.event.fire
import ktx.actors.onClick
import ktx.scene2d.*

class TutorialView(
    bundle: I18NBundle,
    skin: Skin
) : Table(skin), KTable {

    init {
        // UI
        setFillParent(true)

        table {
            background = skin[Drawables.FRAME_BGD]
            label(text = bundle["TutorialButtons"], style = Labels.LEVEL.skinKey, skin) {
                this.setAlignment(Align.center)
                this.setFontScale(1.5f)
                it.row()
            }
            table {
                image(drawable = skin[Drawables.LEFT]) {
                    it.padTop(10f).padLeft(10f)
                }
                label(text = bundle["TutorialButtonLeft"], style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).padLeft(10f).row()
                }

                image(drawable = skin[Drawables.RIGHT]) {
                    it.padTop(10f).padLeft(10f)
                }
                label(text = bundle["TutorialButtonRight"], style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).padLeft(10f).row()
                }

                image(drawable = skin[Drawables.UP]) {
                    it.padTop(10f).padLeft(10f)
                }
                label(text = bundle["TutorialButtonUp"], style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).padLeft(10f).row()
                }

                image(drawable = skin[Drawables.DOWN]) {
                    it.padTop(10f).padLeft(10f)
                }
                label(text = bundle["TutorialButtonDown"], style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).padLeft(10f).row()
                }

                image(drawable = skin[Drawables.ATTACK]) {
                    it.padTop(10f).padLeft(10f)
                }
                label(text = bundle["TutorialButtonAttack"], style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).padLeft(10f).row()
                }
                image(drawable = skin[Drawables.SHIELD]) {
                    it.padTop(10f).padLeft(10f)
                }
                label(text = bundle["TutorialButtonShield"], style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).padLeft(10f).row()
                }

                it.row()
            }

            label(text = bundle["TutorialUI"], style = Labels.LEVEL.skinKey, skin) {
                this.setFontScale(1.5f)
                this.setAlignment(Align.center)
                it.padTop(20f).row()
            }
            table {
                image(drawable = skin[Drawables.PAUSE]) {
                    it.padTop(10f).padLeft(10f)
                }
                label(text = bundle["TutorialButtonPause"], style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).padLeft(10f).row()
                }
                image(drawable = skin[Drawables.FRAME_BGD]) {
                    it.padTop(10f).padLeft(10f)
                }
                label(text = bundle["TutorialButtonStats"], style = Labels.LEVEL.skinKey, skin) {
                    this.setAlignment(Align.center)
                    it.padTop(10f).padLeft(10f).row()
                }
                it.row()
            }
            textButton(text = "Back", style = Buttons.DEFAULT.skinKey) {
                onClick { stage.fire(HideTutorialEvent()) }

            }

            it.expand()
        }
    }
}

@Scene2dDsl
        /**
         * Extension del constructor para poder añadirla como actor al escenario
         */
fun <S> KWidget<S>.tutorialView(
    bundle: I18NBundle,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: TutorialView.(S) -> Unit = {}
): TutorialView = actor(TutorialView(bundle, skin), init)
