package de.obaialbek.chess.data;

import com.github.bhlangonijr.chesslib.pgn.PgnHolder;
import java.io.File;

/**
 * Anforderung aus Folie 2 und 7:
 * "gespeicherte Spiele aus PGN-Files laden" 
 */
public class PgnManager {

    public void loadPgnFile(File file) {
        try {
            PgnHolder pgn = new PgnHolder(file.getAbsolutePath());
            pgn.loadPgn();
            // Hier würdest du die Spiele extrahieren und an die Engine übergeben
            System.out.println("Spiele gefunden: " + pgn.getGames().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}