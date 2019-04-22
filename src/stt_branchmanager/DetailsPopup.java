/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stt_branchmanager;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Dido
 */
public class DetailsPopup {
    public static void display(int order_id, double totalPrice, List<String> customerDetails, List<String[]> displayDetails, int prescribed) {
        
        // GridPane Border: http://www.java2s.com/Tutorials/Java/JavaFX_How_to/GridPane/Set_a_CSS_for_the_GridPane.htm
        // Set Font as Bold: t.setFont(Font.font("Verdana", FontWeight.BOLD, 70));
        
        Stage window = new Stage();
//        window.getIcons().add(new Image("File:mapua.png")); 
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Order Details");
        
        String [] saTemp;
        
//        Label content = new Label();
//        content.setText(message);

        
        Label lblOrderInfo = new Label(order_id + ", " + customerDetails.get(0)); // Order_ID and Customer_Name
        lblOrderInfo.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        
        Label lblEmail = new Label("E-mail:");
        TextField tfEmail = new TextField(customerDetails.get(1)); // Customer Email
        
        Label lblCNumber = new Label("Contact Number:");
        TextField tfCNumber = new TextField(customerDetails.get(2)); // Customer Contact Number
        
        Label lblAddress = new Label("Address:");
        TextField tfAddress = new TextField(customerDetails.get(3)); // Customer Address
        
        GridPane gpCustomerInfo = new GridPane();
        gpCustomerInfo.add(lblEmail, 0, 0);
        gpCustomerInfo.add(tfEmail, 1, 0);
        gpCustomerInfo.add(lblCNumber, 0, 1);
        gpCustomerInfo.add(tfCNumber, 1, 1);
        gpCustomerInfo.add(lblAddress, 0, 2);
        gpCustomerInfo.add(tfAddress, 1, 2);
        
        GridPane gpOrderDetails = new GridPane(); //Item Name, Quantity Ordered
        // COLUMNS: Item Name, Original Price, Order Quantity, Final Price

        for (int i = 0; i < displayDetails.size(); i++) {
            saTemp = displayDetails.get(i);
            for (int j = 0; j < saTemp.length; j++) {
                gpOrderDetails.add(new Label(saTemp[j]), j, i);
            }
        }
        gpOrderDetails.setHgap(10);
        gpOrderDetails.setAlignment(Pos.CENTER);
        
        if (prescribed != 0) {
            totalPrice += (double)60;
        }
        
        Label lblTotalPrice = new Label("Total: " + Double.toString((double)Math.round(totalPrice * 100000d) / 100000d)); // Total Price of Items
        lblTotalPrice.setAlignment(Pos.BOTTOM_RIGHT);
        
        Button okBtn = new Button("Close");
        okBtn.setOnAction(e -> {
            window.close();
        });
        okBtn.setAlignment(Pos.CENTER);
        
        VBox vbox = new VBox (10);
        vbox.getChildren().add(lblOrderInfo);
        vbox.getChildren().add(gpCustomerInfo);
        vbox.getChildren().add(gpOrderDetails);
        vbox.getChildren().add(lblTotalPrice);
        vbox.getChildren().add(okBtn);
        vbox.setPadding(new Insets(10,10,10,10));
        
        Scene mainScene = new Scene(vbox);
        window.setScene(mainScene);
        window.showAndWait();
    }
}
