package de.obaialbek.chess.domain; 

import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ChessEngine {
    
    private static final Logger logger = LoggerFactory.getLogger(ChessEngine.class);
    private final Board board;

    public ChessEngine() {
        this.board = new Board();
    }

    // Zieht eine Figur, wenn der Zug legal ist
    public boolean attemptMove(String from, String to) {
        try {
            Square sqFrom = Square.fromValue(from.toUpperCase());
            Square sqTo = Square.fromValue(to.toUpperCase());
            Move move = new Move(sqFrom, sqTo);

            List<Move> legalMoves = MoveGenerator.generateLegalMoves(board);
            if (legalMoves.contains(move)) {
                board.doMove(move);
                return true;
            }
        } catch (Exception e) {
            logger.error("Fehler beim Zug", e);
        }
        return false;
    }
    
    public boolean isInCheck() {
        return board.isKingAttacked();
    }
    
    public String getKingSquareCoord() {
        return board.getKingSquare(board.getSideToMove()).value().toLowerCase();
    }
    
    // NEU: Gibt eine Liste von Zielfeldern zurück, wohin die Figur auf 'square' ziehen darf
    public List<String> getLegalMovesForSquare(String squareStr) {
        List<String> validTargets = new ArrayList<>();
        try {
            Square startSquare = Square.fromValue(squareStr.toUpperCase());
            List<Move> legalMoves = MoveGenerator.generateLegalMoves(board);
            
            for (Move m : legalMoves) {
                if (m.getFrom() == startSquare) {
                    validTargets.add(m.getTo().value().toLowerCase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return validTargets;
    }

    // NEU: Status-Abfragen
    public boolean isCheckmate() {
        return board.isMated();
    }
    
    public boolean isDraw() {
        return board.isStaleMate() || board.isDraw();
    }
    
    public boolean isWhiteTurn() {
        return board.getSideToMove() == Side.WHITE;
    }

    public String getFen() {
        return board.getFen();
    }

    public void reset() {
        board.loadFromFen(new Board().getFen());
    }
}