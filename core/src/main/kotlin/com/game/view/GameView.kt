package com.game.view

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.scene2d.KTable
import ktx.scene2d.label


class GameView (
    skin: Skin,
): Table(skin), KTable {

    init{
        setFillParent(true)

        label("Test HOLA")
    }
}