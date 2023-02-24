package com.game.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.game.event.HideSettingsGameEvent
import com.game.event.fire
import ktx.actors.onChange

import ktx.actors.onClick
import ktx.actors.onTouchDown
import ktx.log.logger
import ktx.scene2d.*

/**
 * Vista de opciones del juego
 * @property Almacen de las opciones del juego
 * @property skin Skin de los componentes
 */
class SettingsView(
    settingPref: Preferences,
    skin: Skin,
) : KTable, Table(skin) {

    /**
     * Tabla
     */
    val table: Table

    /**
     * Etiqueta de texto
     */
    lateinit var labelMusic: Label
    lateinit var labelEffects: Label

    /**
     * Componente deslizador
     */
    var sliderMusic: Slider
    var sliderEffects: Slider

    /**
     * Componente checkbox
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
                table {
                    label(text = "Sound", style = Labels.LEVEL.skinKey) {
                        it.row()
                    }

                    this@SettingsView.sliderEffects = slider(0f, 100f, 1f, false, Sliders.DEFAULT.skinKey) {
                        value =
                            if (settingPref.contains("effects")) settingPref.getInteger("effects").toFloat() else 100f

                        onChange {
                            this@SettingsView.labelEffects.setText(value.toInt().toString())
                        }
                        it.width(500f)
                    }

                    this@SettingsView.labelEffects =
                        label(text = settingPref.getInteger("effects").toString(), style = Labels.LEVEL.skinKey) {
                            setText(
                                if (settingPref.contains("effects")) {
                                    settingPref.getInteger("effects")
                                        .toString()
                                } else {
                                    100.toString()
                                }
                            )
                        }

                    it.padBottom(10f).row()
                }

                table {
                    this@SettingsView.cbVibrate = checkBox(text = "", style = CheckBoxs.DEFAULT.skinKey) {
                        isChecked = settingPref.getBoolean("vibrate")
                        this.onClick {
                            if(settingPref.getBoolean("vibrate")) {
                                Gdx.input.vibrate(200)
                            }
                        }
                        setText("Vibrate")
                        it.padLeft(50f)
                    }

                    this@SettingsView.cbAcc = checkBox(text = "", style = CheckBoxs.DEFAULT.skinKey) {
                        isChecked = settingPref.getBoolean("accelerometer")
                        this.onClick {  if(settingPref.getBoolean("vibrate")) {
                            Gdx.input.vibrate(100)
                        } }

                        setText("Accelerometer")
                        it.padLeft(30f)
                        it.padRight((50f))
                    }

                    it.padBottom(10f).left().row()
                }
                it.padBottom(10f).left().row()
            }

            textButton(text = "Back", style = Buttons.DEFAULT.skinKey) {


                onClick {
                    settingPref.putInteger("music", this@SettingsView.sliderMusic.value.toInt())
                    settingPref.putInteger("effects", this@SettingsView.sliderEffects.value.toInt())
                    settingPref.putBoolean("vibrate", this@SettingsView.cbVibrate.isChecked)
                    settingPref.putBoolean("accelerometer", this@SettingsView.cbAcc.isChecked)

                    settingPref.flush()
                    stage.fire(HideSettingsGameEvent())
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
        /**
         * Extension del constructor para poder añadirla como actor al escenario
         */
fun <S> KWidget<S>.settingsView(
    settingPref: Preferences,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: SettingsView.(S) -> Unit = {}
): SettingsView = actor(SettingsView(settingPref, skin), init)
