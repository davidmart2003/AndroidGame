package com.game


import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.game.Screens.GameScreen
import com.game.Screens.UiScreen

import ktx.app.KtxGame
import ktx.app.KtxScreen

class MyGame : KtxGame<KtxScreen>() {
    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        addScreen(GameScreen())
        addScreen(UiScreen())
        setScreen<UiScreen>()
    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
        var CREATED = false
    }


}