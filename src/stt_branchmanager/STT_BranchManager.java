/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stt_branchmanager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Dido
 */
public class STT_BranchManager extends Application {
    
    Stage window;
    
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
//        window.getIcons().add(new Image("File:IMAGE.jpg"));   // FOR ICONS
        
        // Set Scene
        AdminLogin scenes = new AdminLogin();
        window.setTitle("WHATS THE NAME OF THIS COMPANY?");
        window.setScene(scenes.loginMethod(window));
        window.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
