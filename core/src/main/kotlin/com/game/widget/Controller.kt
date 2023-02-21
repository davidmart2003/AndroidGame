package com.game.widget

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Scaling
import com.game.ai.Action
import com.game.event.ButtonAttackPressed
import com.game.event.ButtonPressedEvent
import com.game.event.ButtonShieldPressed
import com.game.event.fire
import com.game.ui.view.Buttons
import com.game.ui.view.Drawables
import com.game.ui.view.get
import com.github.quillraven.fleks.Qualifier
import ktx.actors.onClick
import ktx.actors.onTouchDown
import ktx.actors.plusAssign
import ktx.log.logger
import ktx.scene2d.*

class Controller(
    charDrawable: Drawables?,
    private val skin: Skin,

    ) : WidgetGroup(), KGroup {
    private val up: Button = button(Buttons.UP.skinKey)
    private val down: Button = button(Buttons.DOWN.skinKey)
    private val right: Button = button(Buttons.RIGHT.skinKey)
    private val left: Button = button(Buttons.LEFT.skinKey)
    private val attack: Button = button(Buttons.ATTACK.skinKey)
    private val shield: Button = button(Buttons.SHIELD.skinKey)


    init {
        this += left.apply {
            setPosition(0f, 100f)
            setSize(100f,100f)
        }
        this += up.apply {
            setPosition(100f, 200f)
            setSize(100f,100f)

        }
        this += down.apply {
            setPosition(100f, 0f)
            setSize(100f,100f)

        }
        this += right.apply {
            setPosition(200f, 100f)
            setSize(100f,100f)

        }
        this += attack.apply {
            setSize(100f,100f)
            setPosition(800f, 100f)

        }
        this += shield.apply {
            setSize(100f,100f)
            setPosition(1000f, 100f)

        }

        left.onTouchDown { stage.fire(ButtonPressedEvent(0f, -1f)) }
        left.onClick {
            stage.fire(ButtonPressedEvent(0f, 0f))
            stage.fire(ButtonShieldPressed(false))}

        right.onTouchDown { stage.fire(ButtonPressedEvent(0f, 1f)) }
        right.onClick {
            stage.fire(ButtonPressedEvent(0f, 0f))
            stage.fire(ButtonShieldPressed(false))}

        up.onTouchDown { stage.fire(ButtonPressedEvent(1f, 0f)) }
        up.onClick {
            stage.fire(ButtonPressedEvent(0f, 0f))
            stage.fire(ButtonShieldPressed(false))}

        down.onTouchDown { stage.fire(ButtonPressedEvent(-1f, 0f)) }
        down.onClick {
            stage.fire(ButtonPressedEvent(0f, 0f))
            stage.fire(ButtonShieldPressed(false))}

        attack.onTouchDown { stage.fire(ButtonAttackPressed(true)) }
        attack.onClick { stage.fire(
            ButtonAttackPressed(false))
            stage.fire(ButtonShieldPressed(false))}

        //shield.onTouchDown { stage.fire(ButtonShieldPressed(true)) }
        shield.onClick { stage.fire(ButtonShieldPressed(true)) }

    }

    companion object {
        private val log = logger<Action>()
    }
}

@Scene2dDsl
fun <S> KWidget<S>.controller(
    charDrawable: Drawables?,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: Controller.(S) -> Unit = {}
): Controller = actor(Controller(charDrawable, skin), init)