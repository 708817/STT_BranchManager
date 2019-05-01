/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stt_branchmanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Dido
 */
public class MessagePopup {
    public static void display(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        
        Label content = new Label(message);
        Button btnOk = new Button("OK");
        btnOk.setOnAction(e -> {
            window.close();
        });
        VBox vbucks = new VBox(10);
        vbucks.getChildren().add(content);
        vbucks.getChildren().add(btnOk);
        vbucks.setPadding(new Insets(10,10,10,10));
        vbucks.setAlignment(Pos.CENTER);
        
        Scene mainScene = new Scene(vbucks);
        window.setScene(mainScene);
        window.showAndWait();
    }
    
}
