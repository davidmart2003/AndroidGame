package com.game.ui.view

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.game.event.HideSettingsGameEvent
import com.game.event.fire
import ktx.actors.onChange

import ktx.actors.onClick
import ktx.actors.onTouchDown
import ktx.log.logger
import ktx.scene2d.*

class SettingsView(
    settingPref: Preferences,
    skin: Skin,
) : KTable, Table(skin) {

    /**
     * Tabla que contiene los componente
     */
    val table: Table

    /**
     * Etiqueta que muesta el nivel de volumen de la musica
     */
    lateinit var labelMusic: Label

    /**
     * Deslizador para cambiar el valor del volumen de la musica
     */
    var sliderMusic: Slider

    /**
     *Etiqueta que muesta el nivel de volumen de los efectos de sonido
     */
    lateinit var labelEffects: Label

    /**
     * Deslizador para cambiar el valor del volumen de los efectos de sonido
     */
    var sliderEffects: Slider

    /**
     * Casilla para activar la vibracion
     */
    var cbVibrate: CheckBox
    var cbAcc: CheckBox


    init {
        setFillParent(true)

        table = table {
            background = skin[Drawables.FRAME_BGD]

            table {
                label(text = "SETTINGS", style = Labels.TITLE.skinKey) {
                    it.padTop(7f).padLeft(7f).center().row()
                }
                //Music volume
                table {
                    label(text = "Music", style = Labels.LEVEL.skinKey) {
                        it.padTop(10f).row()
                    }

                    this@SettingsView.sliderMusic = slider(0f, 100f, 1f, false, Sliders.DEFAULT.skinKey) {
                        value = if (settingPref.contains("music")) settingPref.getInteger("music").toFloat() else 100f

                        onChange {

                            this@SettingsView.labelMusic.setText(value.toInt().toString())
                        }
                        it.width(500f)
                    }

                    this@SettingsView.labelMusic =
                        label(text = settingPref.getInteger("music").toString(), style = Labels.LEVEL.skinKey) {
                            setText(
                                if (settingPref.contains("music")) settingPref.getInteger("music")
                                    .toString() else 100.toString()
                            )

                        }

                    it.padBottom(10f).row()
                }
                //Sound volume
                table {
                    label(text = "Sound", style = Labels.LEVEL.skinKey) {
                        it.row()
                    }

                    this@SettingsView.sliderEffects = slider(0f, 100f, 1f, false, Sliders.DEFAULT.skinKey) {
                        value = if (settingPref.contains("sound")) settingPref.getInteger("sound").toFloat() else 100f

                        onChange {
                            this@SettingsView.labelEffects.setText(value.toInt().toString())
                        }
                        it.width(500f)
                    }

                    this@SettingsView.labelEffects =
                        label(text = settingPref.getInteger("sound").toString(), style = Labels.LEVEL.skinKey) {
                            setText(
                                if (settingPref.contains("music")) settingPref.getInteger("music")
                                    .toString() else 100.toString()
                            )
                        }

                    it.padBottom(10f).row()
                }

                table {
                    this@SettingsView.cbVibrate = checkBox(text = "", style = CheckBoxs.DEFAULT.skinKey) {
                        isChecked = settingPref.getBoolean("vibrate")
                        setText("Vibrate")
                    }

                    this@SettingsView.cbAcc = checkBox(text = "", style = CheckBoxs.DEFAULT.skinKey) {
                        isChecked = settingPref.getBoolean("accelerometer")
                        setText("Accelerometer")

                    }

                    it.padBottom(10f).left().row()
                }





                it.padBottom(10f).left().row()
            }

            textButton(text = "Back", style = Buttons.DEFAULT.skinKey) {

                onClick {
                    onClick {
                        this@SettingsView.labelMusic.setText(settingPref.getInteger("music"))
                        this@SettingsView.sliderMusic.value = settingPref.getInteger("music").toFloat()
                        this@SettingsView.labelMusic.setText(settingPref.getInteger("music"))
                        this@SettingsView.sliderEffects.value = settingPref.getInteger("sound").toFloat()
                        this@SettingsView.labelEffects.setText(settingPref.getInteger("sound"))
                        this@SettingsView.cbVibrate.isChecked = settingPref.getBoolean("vibrate")
                        this@SettingsView.cbAcc.isChecked = settingPref.getBoolean("accelerometer")
                        stage.fire(HideSettingsGameEvent())
                    }
                }

                it.padRight(3f)
            }

        }
    }

    companion object {
        private val log = logger<SettingsView>()
    }
}


@Scene2dDsl
fun <S> KWidget<S>.settingsView(
    settingPref: Preferences,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: SettingsView.(S) -> Unit = {}
): SettingsView = actor(SettingsView(settingPref, skin), init)
