package com.game.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color.*
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.game.model.GameModel
import ktx.assets.disposeSafely
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.style.*
import java.awt.Color

enum class Drawables(
    val atlasKey: String
)  {
    BACKGROUNDINFO("backgroundInfo"),
    PLAYER("idle"),
    FLYINGEYE("flyingeye"),
    LIFEBAR("lifebar"),
    FRAME_BGD("frame_bgd"),
    FRAME_FGD("background"),
    PAUSE("pause"),
    UP("up"),
    DOWN("down"),
    RIGHT("right"),
    LEFT("left"),
    FONDO("fondo"),
    ATTACK("attack"),
    SHIELD("shield"),
    BUTTON("defaultbutton"),
    INVENTORY_SLOT("inv_slot"),
    INVENTORY_SLOT_HELMET("inv_slot_helmet"),
    INVENTORY_SLOT_ARMOR("inv_slot_armor"),
    INVENTORY_SLOT_WEAPON("inv_slot_weapon"),
    INVENTORY_SLOT_BOOTS("inv_slot_boots"),
}

enum class Labels {
    FRAME,TITLE;

    val skinKey = this.name.lowercase()
}

enum class Buttons {
    UP,DOWN,LEFT,RIGHT,DEFAULT,ATTACK,SHIELD;

    val skinKey= this.name.lowercase()
}

enum class Fonts(
    val atlasRegionKey: String,
    val scaling: Float,
) {
    DEFAULT("text", 0.5f),
    TITLE("text", 2f);

    val skinKey = "Font_${this.name.lowercase()}"
    val fontPath = "${this.atlasRegionKey}.fnt"
}

operator fun Skin.get(drawable: Drawables): Drawable = this.getDrawable(drawable.atlasKey)
operator fun Skin.get(font: Fonts): BitmapFont = this.getFont(font.skinKey)

fun loadSkin() {
    Scene2DSkin.defaultSkin = skin(TextureAtlas("ui/ui.atlas")) { skin ->
        Fonts.values().forEach { fnt ->
            skin[fnt.skinKey] = BitmapFont(
                Gdx.files.internal(fnt.fontPath),
                skin.getRegion(fnt.atlasRegionKey)
            ).apply {
                data.setScale(fnt.scaling)
                data.markupEnabled = true
                color= WHITE
            }
        }

        label(Labels.FRAME.skinKey) {
            fontColor=com.badlogic.gdx.graphics.Color(WHITE)
            font = skin[Fonts.DEFAULT]
            background = skin[Drawables.FRAME_BGD].apply {
                leftWidth = 100f
                rightWidth = 100f
                topHeight = 100f
                bottomHeight=100f
            }

        }
        label(Labels.TITLE.skinKey) {
            fontColor=com.badlogic.gdx.graphics.Color(WHITE)
            font = skin[Fonts.TITLE]
            background = skin[Drawables.FRAME_BGD].apply {
                leftWidth = 100f
                rightWidth = 100f
                topHeight = 100f
                bottomHeight=100f
            }

        }
        textButton (Buttons.DEFAULT.skinKey){
            font=skin[Fonts.DEFAULT]
            down=skin[Drawables.BUTTON]
            up=skin[Drawables.BUTTON]
            fontColor=com.badlogic.gdx.graphics.Color(BLACK)

        }
        button(Buttons.UP.skinKey){
            up=skin[Drawables.UP]
            down=skin[Drawables.UP]
        }
        button(Buttons.DOWN.skinKey){
            up=skin[Drawables.DOWN]
            down=skin[Drawables.DOWN]
        }
        button(Buttons.LEFT.skinKey) {
            up = skin[Drawables.LEFT]
            down = skin[Drawables.LEFT]
        }
        button(Buttons.RIGHT.skinKey){
            up=skin[Drawables.RIGHT]
            down=skin[Drawables.RIGHT]
        }
        button(Buttons.ATTACK.skinKey){
            up=skin[Drawables.ATTACK]
            down=skin[Drawables.ATTACK]
        }
        button(Buttons.SHIELD.skinKey){
            up=skin[Drawables.SHIELD]
            down=skin[Drawables.SHIELD]
        }
    }
}

fun disposeSkin() {
    Scene2DSkin.defaultSkin.disposeSafely()
}


