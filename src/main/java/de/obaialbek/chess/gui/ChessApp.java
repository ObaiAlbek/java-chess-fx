package de.obaialbek.chess.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image; 
import java.util.Objects;
public class ChessApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main-view.fxml"));
        
        Scene scene = new Scene(fxmlLoader.load(), 900, 650);
        
        stage.setTitle("Chess App");
        stage.setScene(scene);
        
        stage.setResizable(false); 
        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png")));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Icon konnte nicht geladen werden: " + e.getMessage());
        }
        
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}