package com.game.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.game.event.*
import com.github.quillraven.fleks.IntervalSystem
import ktx.assets.disposeSafely
import ktx.log.logger
import ktx.tiled.propertyOrNull

class AudioSystem : EventListener, IntervalSystem() {
    private val soundCache = mutableMapOf<String, Sound>()
    private val musicCache = mutableMapOf<String, Music>()
    private val soundRequests = mutableMapOf<String, Sound>()
    private var music: Music? = null

    override fun onTick() {
        if (soundRequests.isEmpty()) {
            return
        }

        soundRequests.values.forEach { it.play(1f) }
        soundRequests.clear()
    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is MapChangeEvent -> {
                event.map.propertyOrNull<String>("music")?.let { path ->
                    log.debug { "Changing music to '$path'" }
                    music = musicCache.getOrPut(path) {
                        Gdx.audio.newMusic(Gdx.files.internal("$path")).apply {
                            isLooping = true
                            //volume=1f
                        }
                    }
                    musicCache.forEach{it.value.stop()}
                    music?.play()
                }
            }

            is DeathSound ->{
                when (event.name){
                    "Flying Eye" ->{queueSound("audio/deathFlyingEye.wav")}
                    "Goblin" ->{queueSound("audio/deathGoblin.wav")}
                    "MushRoom" ->{queueSound("audio/deathMushRoom.wav")}
                    "Skeleton" ->{queueSound("audio/deathSkeleton.wav")}
                }

            }

            else -> return false
        }
        return true
    }

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

    override fun onDispose() {
        soundCache.values.forEach { it.disposeSafely() }
        musicCache.values.forEach { it.disposeSafely() }
    }

    companion object {
        private val log = logger<AudioSystem>()
    }
}