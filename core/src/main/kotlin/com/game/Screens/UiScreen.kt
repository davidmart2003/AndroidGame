package com.game.Screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.game.view.GameView
import ktx.app.KtxScreen
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.actors

class UiScreen : KtxScreen {

    private val stage: Stage = Stage(ExtendViewport(320f,180f))

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width,height)
    }

    override fun show() {
        stage.clear()
        Scene2DSkin.defaultSkin= Skin()
        stage.actors {
            GameView(Scene2DSkin.defaultSkin)
        }
    }

    override fun render(delta: Float) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            hide()
            show()
        }

        stage.act()
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
    }
}