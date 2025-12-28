package de.obaialbek.chess.data;

/**
 * Anforderung aus Folie 4:
 * "Echtes Spielen mit Schachuhr mit wenigstens 3 Modi" 
 */
public class ChessClock {
    private long whiteTimeLeft;
    private long blackTimeLeft;
    private boolean isRunning;

    // Konstruktor für verschiedene Modi (z.B. Blitz 5min, Rapid 15min)
    public ChessClock(int minutes) {
        this.whiteTimeLeft = minutes * 60 * 1000;
        this.blackTimeLeft = minutes * 60 * 1000;
    }

    public void tick(boolean isWhiteTurn) {
        if (!isRunning) return;
        
        if (isWhiteTurn) {
            whiteTimeLeft -= 1000; // 1 Sekunde abziehen
        } else {
            blackTimeLeft -= 1000;
        }
    }

	public long getWhiteTimeLeft() {
		return whiteTimeLeft;
	}

	public void setWhiteTimeLeft(long whiteTimeLeft) {
		this.whiteTimeLeft = whiteTimeLeft;
	}

	public long getBlackTimeLeft() {
		return blackTimeLeft;
	}

	public void setBlackTimeLeft(long blackTimeLeft) {
		this.blackTimeLeft = blackTimeLeft;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
    
 
}