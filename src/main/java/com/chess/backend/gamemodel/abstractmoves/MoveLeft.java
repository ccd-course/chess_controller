package com.chess.backend.gamemodel.abstractmoves;

import com.chess.backend.gamemodel.*;
import com.chess.backend.services.ChessboardService;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the implementation of a move to the left.
 */
public class MoveLeft {

    public MoveLeft() {
    }

    /**
     * Generate concrete possible moves from a given piece and game context.
     * Direction: Left, no limit
     *
     * @param game       The game context.
     * @param fromSquare The originating square.
     * @param attack     Whether the piece may move to an occupied square. This would result in an attack with a captured piece.
     * @param jump       Whether the piece may jump over other pieces (e.g. the knight).
     * @param peaceful   Whether the piece may move to an empty field.
     * @return HashSet of concrete moves
     */
    public static Set<Move> concretise(Game game, Square fromSquare, boolean attack, boolean jump, boolean peaceful) {
        return left(game, fromSquare, attack, jump, peaceful, -1);
    }

    /**
     * Generate concrete possible moves from a given piece and game context.
     * Direction: Left, limit can be set
     *
     * @param game       The game context.
     * @param fromSquare The originating square.
     * @param attack     Whether the piece may move to an occupied square. This would result in an attack with a captured piece.
     * @param jump       Whether the piece may jump over other pieces (e.g. the knight).
     * @param limit      The maximum of steps.
     * @param peaceful   Whether the piece may move to an empty field.
     * @return HashSet of concrete moves
     */
    // TODO: Implement castling, enPassant and piece promotion
    public static Set<Move> left(Game game, Square fromSquare, boolean attack, boolean jump, boolean peaceful, int limit) {
        HashSet<Move> allowedMoves = new HashSet<Move>();
        Chessboard chessboard = game.getChessboard();
        Position toPosition = new Position(fromSquare.getPosX(), fromSquare.getPosY());

        for (int steps = 0;
             toPosition.left(chessboard) != null
                     && (limit == -1 || steps < limit); steps++) {

            toPosition = toPosition.left(chessboard);
            Square toSquare = ChessboardService.getSquare(chessboard, toPosition);
            Piece takenPiece = null;

            if (toSquare.getPiece() != null) {
                if (attack && toSquare.getPiece().getColor() != fromSquare.getPiece().getColor()) {
                    takenPiece = toSquare.getPiece();
                    allowedMoves.add(
                            new Move(fromSquare, toSquare,
                                    fromSquare.getPiece(), takenPiece,
                                    null, false, null
                            ));
                } else if (jump) {
                    continue;
                } else {
                    break;
                }
            } else if (peaceful) {
                allowedMoves.add(
                        new Move(fromSquare, toSquare,
                                fromSquare.getPiece(), takenPiece,
                                null, false, null
                        ));
            }
        }
        return allowedMoves;
    }
}
