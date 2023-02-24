package com.game.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color.BLACK
import com.badlogic.gdx.graphics.Color.WHITE
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import ktx.assets.disposeSafely
import ktx.scene2d.Scene2DSkin
import ktx.style.*

/**
 * Enumerado que guarda imagenes del atlas
 *
 * @property atlasKey Nombre de las imagenes en el atlas
 */
enum class Drawables(
    val atlasKey: String
)  {
    BACKGROUNDINFO("backgroundInfo"),
    PLAYER("idle"),
    FLYINGEYE("flyingeye"),
    GOBLIN("goblin"),
    MUSHROOM("mushroom"),
    SKELETON("skeleton"),
    DEMON("demon"),
    LIFEBAR("lifebar"),
    FRAME_BGD("frame_bgd"),
    FRAME_FGD("background"),
    UP("up"),
    PAUSE("pause"),
    SLIDER("slider"),
    CHECKBOXPR("checkboxpr"),
    CHECKBOXUN("checkboxun"),
    DOWN("down"),
    RIGHT("right"),
    LEFT("left"),
    ATTACK("attack"),
    SHIELD("shield"),
    BUTTON("defaultbutton"),
}

/**
 * Enumerado de las distintas skins de los Sliders
 */
enum class  Sliders{
    DEFAULT;

    /**
     * Nombre de la skin
     */
    val skinKey = this.name.lowercase()
}

/**
 * Enunmerado de las distintas skin de los Checkbox
 */
enum class  CheckBoxs{
    DEFAULT;

    val skinKey = this.name.lowercase()
}
/**
 * Enunmerado de las distintas skin de los Label
 */enum class Labels {
    FRAME,TITLE,LEVEL;

    val skinKey = this.name.lowercase()
}
/**
 * Enunmerado de las distintas skin de los Buttons
 */
enum class Buttons {
    UP,DOWN,LEFT,RIGHT,DEFAULT,ATTACK,SHIELD;

    val skinKey= this.name.lowercase()
}

/**
 * Enunmerado de las distintas skin de las Fonts
 */
enum class Fonts(
    val atlasRegionKey: String,
    val scaling: Float,
) {
    DEFAULT("text", 0.5f),
    TITLE("text", 2f);

    val skinKey = "Font_${this.name.lowercase()}"

    /**
     * Ruta de la fuente en el atlas
     */
    val fontPath = "${this.atlasRegionKey}.fnt"
}

/**
 *Transforma el enumerado de Drawables en en clase Drawables
 */
operator fun Skin.get(drawable: Drawables): Drawable = this.getDrawable(drawable.atlasKey)

/**
 * Transforma el enumerado de Fonts en la clase BitMapfont
 */
operator fun Skin.get(font: Fonts): BitmapFont = this.getFont(font.skinKey)

/**
 * Carga las skins de todos los componentes
 */
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
        slider(Sliders.DEFAULT.skinKey){
            knob=skin[Drawables.BUTTON]
            background= skin[Drawables.SLIDER]
        }

        checkBox(CheckBoxs.DEFAULT.skinKey){
            font = skin[Fonts.DEFAULT]
            fontColor=com.badlogic.gdx.graphics.Color(WHITE)
            checkboxOn=skin[Drawables.CHECKBOXPR]
            checkboxOff=skin[Drawables.CHECKBOXUN]
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
        label(Labels.LEVEL.skinKey) {
            fontColor=com.badlogic.gdx.graphics.Color(WHITE)
            font = skin[Fonts.DEFAULT]


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

/**
 * Quita todas las skin cargadas para eliminar recursos
 */
fun disposeSkin() {
    Scene2DSkin.defaultSkin.disposeSafely()
}


