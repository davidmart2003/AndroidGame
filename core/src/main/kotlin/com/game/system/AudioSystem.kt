package com.game.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.game.event.*
import com.github.quillraven.fleks.IntervalSystem
import ktx.assets.disposeSafely
import ktx.log.logger
import ktx.tiled.propertyOrNull

/**
 * Sistema que se encarga del audio del juego
 *
 * @property settingsPref Almacen del volumen del audio y efectos
 */
class AudioSystem(
    private val settingsPref: Preferences
) : EventListener, IntervalSystem() {
    /**
     * Almacen de los sonidos cargados
     */
    private val soundCache = mutableMapOf<String, Sound>()

    /**
     * Almacen de la musica cargadas
     */
    private val musicCache = mutableMapOf<String, Music>()

    /**
     * Lista de espera de sonidos para ejecutarlos de manera ordenada
     */
    private val soundRequests = mutableMapOf<String, Sound>()

    /**
     * Musica que se va a reproducir
     */
    private var music: Music? = null

    /**
     * Cada vez que se ejecuta reproduce los sonidos que estan en espera
     */
    override fun onTick() {
        if (soundRequests.isEmpty()) {
            return
        }

        soundRequests.values.forEach {
            if(settingsPref.contains("effects")){
                it.play(settingsPref.getInteger("effects").toFloat()/100)
            }else {
                it.play(1f)
            }
        }
        soundRequests.clear()
    }

    /**
     * Se ejecuta cuando se lanza un evento
     *
     * @param event El evento lanzado
     */
    override fun handle(event: Event): Boolean {
        when (event) {
            is MapChangeEvent -> {
                event.map.propertyOrNull<String>("music")?.let { path ->
                    log.debug { "Changing music to '$path'" }
                    music = musicCache.getOrPut(path) {
                        Gdx.audio.newMusic(Gdx.files.internal("$path")).apply {
                            isLooping = true
                            if (settingsPref.contains("music")){
                                volume = settingsPref.getInteger("music").toFloat()/100
                            }else {
                                volume =1f
                            }
                        }
                    }
                    musicCache.forEach { it.value.stop() }
                    music?.play()
                }
            }

            is DeathSound -> {
                when (event.name) {
                    "Flying Eye" -> {
                        queueSound("audio/deathFlyingEye.wav")
                    }

                    "Goblin" -> {
                        queueSound("audio/deathGoblin.wav")
                    }

                    "MushRoom" -> {
                        queueSound("audio/deathMushRoom.wav")
                    }

                    "Skeleton" -> {
                        queueSound("audio/deathSkeleton.wav")
                    }

                    "Player" -> {
                        queueSound("audio/death.mp3")
                    }

                    "Demon" -> {
                        queueSound("audio/deathSkeleton.wav")
                    }
                }

            }

            is HitEvent -> {
                queueSound("audio/hit.wav")
            }

            is HideSettingsGameEvent -> {
                musicCache.forEach { it ->
                    it.value.volume = settingsPref.getInteger("music").toFloat()/100
                }
            }

            else -> return false
        }
        return true
    }

    /**
     *  Carga los sonidos y los almacena en la lista de espera
     *  @param path Ruta del sonido que se va a reproducir
     */
    private fun queueSound(path: String) {
        log.debug { "Queueing new sound '$path'" }
        if (soundRequests.containsKey(path)) {
            // already requested in this frame -> do not add it again
            return
        }

        val snd = soundCache.computeIfAbsent(path) {
            Gdx.audio.newSound(Gdx.files.internal(it))
        }
        soundRequests[path] = snd
    }

    /**
     * Libera los recursos al cerrar el mundo de entidades
     */
    override fun onDispose() {
        soundCache.values.forEach { it.disposeSafely() }
        musicCache.values.forEach { it.disposeSafely() }
    }

    companion object {
        private val log = logger<AudioSystem>()
    }
}