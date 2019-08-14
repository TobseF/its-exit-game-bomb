package com.libktx.game.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.libktx.game.Config
import com.libktx.game.Game
import com.libktx.game.assets.*
import com.libktx.game.ecs.network.Network
import ktx.app.KtxScreen
import ktx.graphics.use

class LoadingScreenScreen(private val game: Game,
                          private val batch: Batch,
                          private val font: BitmapFont,
                          private val assets: AssetManager,
                          private val camera: OrthographicCamera) : KtxScreen {
    override fun show() {
        MusicAssets.values().forEach { assets.load(it) }
        SoundAssets.values().forEach { assets.load(it) }
        TextureAtlasAssets.values().forEach { assets.load(it) }
        FontAssets.values().forEach { assets.load(it) }
    }


    override fun render(delta: Float) {
        // continue loading our assets
        assets.update()
        camera.update()
        batch.projectionMatrix = camera.combined

        batch.use {
            font.draw(it, "BombApp ", 100f, 200f)
            font.draw(it, "IP: ${Network.getIpAddress()} Port: ${Config.ServerPort}", 100f, 150f)
            if (assets.isFinished) {
                font.draw(it, "Tap anywhere to begin!", 100f, 100f)
            } else {
                font.draw(it, "Loading assets...", 100f, 100f)
            }
        }

        if (Gdx.input.isTouched && assets.isFinished) {
            hide()
            game.setScreen<LoginPuzzleScreen>()
        }
    }
}