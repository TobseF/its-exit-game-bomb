package com.libktx.game.puzzle

import com.libktx.game.Game
import com.libktx.game.lib.Resetable
import com.libktx.game.network.AbstractNetworkEndpoint
import com.libktx.game.network.Endpoint
import com.libktx.game.network.PuzzleResponse

/**
 * Endpoint which resets the game with the command `TURN IT OFF AND ON AGAIN`
 */
class ResetPuzzle(val game: Game, val countdown: Resetable) : AbstractNetworkEndpoint(Endpoint.Reset.path), Resetable {

    override fun reset() {
        game.reset()
        countdown.reset()
    }

    override fun request(data: String): PuzzleResponse {
        return if ("TURN IT OFF AND ON AGAIN" == data.toUpperCase()) {
            reset()
            PuzzleResponse.OK
        } else {
            PuzzleResponse.FALSE
        }
    }

}