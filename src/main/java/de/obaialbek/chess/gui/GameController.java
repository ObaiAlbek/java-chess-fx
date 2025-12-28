package de.obaialbek.chess.gui; // Dein Paketname!

import de.obaialbek.chess.domain.ChessEngine;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class GameController {
	private boolean isGameOver = false;
	@FXML
	private GridPane chessBoardGrid;

	@FXML
	private Label statusLabel; // Musst du im FXML noch hinzufügen (siehe unten)

	private ChessEngine engine;
	private String selectedSquare = null;
	private List<String> legalMoveTargets = new ArrayList<>(); // Speichert mögliche Ziele

	@FXML
	public void initialize() {
		engine = new ChessEngine();

		// CSS laden (Styling)
		try {
			chessBoardGrid.getScene().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
		} catch (Exception e) {
			// Scene ist beim Start manchmal noch null, wir laden es später oder ignorieren
			// es hier
		}

		drawBoard();
		updateStatus();
	}

	private void drawBoard() {
		chessBoardGrid.getChildren().clear();

		String fen = engine.getFen();
		String piecePlacement = fen.split(" ")[0];

		int row = 0;
		int col = 0;

		for (int i = 0; i < piecePlacement.length(); i++) {
			char c = piecePlacement.charAt(i);

			if (c == '/') {
				row++;
				col = 0;
			} else if (Character.isDigit(c)) {
				int emptySquares = Character.getNumericValue(c);
				for (int k = 0; k < emptySquares; k++) {
					addSquare(col, row, null);
					col++;
				}
			} else {
				addSquare(col, row, c);
				col++;
			}
		}
	}

	private void addSquare(int col, int row, Character pieceChar) {
        StackPane stack = new StackPane();
        Rectangle square = new Rectangle(60, 60);
        
        // 1. Grundfarben (Holz-Look)
        boolean isWhite = (col + row) % 2 == 0;
        Color baseColor = isWhite ? Color.valueOf("#F0D9B5") : Color.valueOf("#B58863");
        
        String currentCoord = getCoordinate(col, row);

        // 2. Wenn ausgewählt -> Gold
        if (selectedSquare != null && selectedSquare.equals(currentCoord)) {
            baseColor = Color.GOLD;
        }
        
        // 3. NEU: Wenn Schach ist UND das hier das Feld vom König ist -> ROT
        // Das signalisiert: "Du stehst im Schach!"
        if (engine.isInCheck() && currentCoord.equals(engine.getKingSquareCoord())) {
            baseColor = Color.RED;
        }
        
        square.setFill(baseColor);
        stack.getChildren().add(square);

        // --- Logik für mögliche Züge (Grüne Punkte) & Angriffe (Rote Ringe) ---
        if (legalMoveTargets.contains(currentCoord)) {
            
            if (pieceChar != null) {
                // Fall A: Gegner schlagen! -> Roter KREIS (Rahmen)
                Circle captureRing = new Circle(25);
                captureRing.setFill(Color.TRANSPARENT); // Innen durchsichtig
                captureRing.setStroke(Color.RED);       // Rand Rot
                captureRing.setStrokeWidth(4);          // Dicker Rand
                
                stack.getChildren().add(captureRing);
                
            } else {
                // Fall B: Normaler Zug auf leeres Feld -> Grüner Punkt
                Circle dot = new Circle(10);
                dot.setFill(Color.rgb(20, 150, 20, 0.6));
                stack.getChildren().add(dot);
            }
            
            // Hand-Cursor anzeigen
            stack.setStyle("-fx-cursor: hand;");
        }
        // ----------------------------------------------------

        // Figur laden
        if (pieceChar != null) {
            String imageName = getPieceImageName(pieceChar);
            try {
                Image img = new Image(getClass().getResourceAsStream("/images/" + imageName));
                ImageView view = new ImageView(img);
                view.setFitWidth(50);
                view.setFitHeight(50);
                stack.getChildren().add(view);
            } catch (Exception e) {
                // Bild fehlt
            }
        }

        final int c = col;
        final int r = row;
        stack.setOnMouseClicked(e -> handleMouseClick(c, r));

        chessBoardGrid.add(stack, col, row);
    }
	
	private void handleMouseClick(int col, int row) {
		
		
		if (isGameOver) {
            return; 
        }
		
		String clickedCoord = getCoordinate(col, row);
		
		if (selectedSquare == null) {
			// 1. Klick: Auswahl
			selectedSquare = clickedCoord;
			// Hole mögliche Züge für diese Figur
			legalMoveTargets = engine.getLegalMovesForSquare(clickedCoord);
		} else {
			// 2. Klick: Zug versuchen
			if (legalMoveTargets.contains(clickedCoord)) {
				boolean success = engine.attemptMove(selectedSquare, clickedCoord);
				if (success) {
					checkGameOver();
				}
				selectedSquare = null;
				legalMoveTargets.clear();
			} else {
				// Wenn man woanders hinklickt, neue Auswahl starten
				selectedSquare = clickedCoord;
				legalMoveTargets = engine.getLegalMovesForSquare(clickedCoord);
			}
		}
		drawBoard();
		updateStatus();
	}

	private void checkGameOver() {
        if (engine.isCheckmate()) {
            isGameOver = true; // <--- WICHTIG: Brett sperren
            showAlert("Spielende", "Schachmatt! " + (engine.isWhiteTurn() ? "Schwarz" : "Weiß") + " gewinnt.");
        } else if (engine.isDraw()) {
            isGameOver = true; // <--- WICHTIG: Brett sperren
            showAlert("Spielende", "Remis! Unentschieden.");
        }
    }
	
	private void updateStatus() {
		if (statusLabel != null) {
			String side = engine.isWhiteTurn() ? "Weiß" : "Schwarz";
			statusLabel.setText(side + " ist am Zug");
		}
	}

	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private String getCoordinate(int col, int row) {
		char file = (char) ('a' + col);
		int rank = 8 - row;
		return "" + file + rank;
	}

	private String getPieceImageName(char pieceChar) {
		String color = Character.isUpperCase(pieceChar) ? "W" : "B";
		String type = String.valueOf(Character.toUpperCase(pieceChar));
		return color + "_" + type + ".png";
	}

	@FXML
    protected void onNewGameClick() {
        engine.reset();
        isGameOver = false;
        selectedSquare = null;
        legalMoveTargets.clear();
        drawBoard();
        updateStatus();
    }
}