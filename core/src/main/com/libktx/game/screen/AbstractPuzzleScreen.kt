package com.libktx.game.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.SPACE
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Color.RED
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled
import com.libktx.game.Game
import com.libktx.game.Preferences
import com.libktx.game.assets.FontAssets
import com.libktx.game.assets.SoundAssets
import com.libktx.game.assets.get
import com.libktx.game.lib.Countdown
import com.libktx.game.lib.TimeFormatter
import com.libktx.game.lib.drawWithShadow
import com.libktx.game.lib.rect
import com.libktx.game.network.Endpoint
import com.libktx.game.network.NetworkEventManager
import com.libktx.game.network.PuzzleResponse
import com.libktx.game.network.ResponseStatus
import com.libktx.game.puzzle.PuzzleResponseHandler
import ktx.graphics.use

/**
 * Renders a timer.
 * A new puzzle can added by [NetworkEventManager.addEndpoint].
 * To ensure the solving order it's endpoint also needs to bee added to the [BombState.puzzles] list.
 */
abstract class AbstractPuzzleScreen(val endpoint: Endpoint, protected val game: Game,
                                    batch: Batch,
                                    assets: AssetManager,
                                    camera: OrthographicCamera,
                                    shapeRenderer: ShapeRenderer,
                                    val countdown: Countdown) : AbstractScreen(batch, assets, camera, shapeRenderer), PuzzleResponseHandler {

    override fun handlePuzzleResponse(response: PuzzleResponse) {
        if (response.status == ResponseStatus.OK) {
            assets[SoundAssets.MessageRight].play()
            switchToNextScreen()
        } else {
            assets[SoundAssets.MessageWrong].play()
        }
    }

    abstract fun switchToNextScreen()

    override fun render(delta: Float) {
        super.render(delta)

        renderCountdown()
        checkCountdown()

        switchScreenForDebug()
    }

    private fun switchScreenForDebug() {
        if (Preferences.debug && (Gdx.input.isKeyJustPressed(SPACE) || Gdx.input.justTouched())) {
            switchToNextScreen()
        }
    }

    protected open fun checkCountdown() {
        if (countdown.isFinished()) {
            game.setScreen<ExplosionScreen>()
        }
    }

    private fun renderCountdown() {
        clearScreen(Color.LIGHT_GRAY)

        shapeRenderer.use(Filled) {
            it.rect(Color.GRAY, 610f, 0f, 190f, 480f)
        }

        shapeRenderer.use(Filled) {
            it.rect(Color.DARK_GRAY, 610f, 380f, 190f, 100f)
        }

        batch.use {
            val counterFont = assets[FontAssets.Counter]
            counterFont.drawWithShadow(it, RED, getTimeAsString(), 628f, 450f)
        }

    }

    private fun getTimeAsString() = TimeFormatter.getFormattedTimeAsString(countdown.getCountdownTime())

    override fun show() {}
}