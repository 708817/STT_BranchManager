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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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

        Label lblOrderInfo = new Label("Order #" + order_id); // Order_ID and Customer_Name
        lblOrderInfo.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        
        Label lblName = new Label("Name: ");
        HBox hbName = new HBox();
        hbName.getChildren().add(lblName);
        hbName.setAlignment(Pos.CENTER_RIGHT);
        TextField tfName = new TextField(customerDetails.get(0));
        tfName.setEditable(false);
        
        Label lblAddress = new Label("Address:");
        HBox hbAddress = new HBox();
        hbAddress.getChildren().add(lblAddress);
        hbAddress.setAlignment(Pos.CENTER_RIGHT);
        TextField tfAddress = new TextField(customerDetails.get(3)); // Customer Address
        tfAddress.setEditable(false);
        
        Label lblEmail = new Label("E-mail:");
        HBox hbEmail = new HBox();
        hbEmail.getChildren().add(lblEmail);
        hbEmail.setAlignment(Pos.CENTER_RIGHT);
        TextField tfEmail = new TextField(customerDetails.get(2)); // Customer Email
        tfEmail.setEditable(false);
        
        Label lblCNumber = new Label("Contact:");
        HBox hbCNumber = new HBox();
        hbCNumber.getChildren().add(lblCNumber);
        hbCNumber.setAlignment(Pos.CENTER_RIGHT);
        TextField tfCNumber = new TextField(customerDetails.get(1)); // Customer Contact Number
        tfCNumber.setEditable(false);
        
        GridPane gpCustomerInfo = new GridPane();
        gpCustomerInfo.add(hbName, 0, 0);
        gpCustomerInfo.add(tfName, 1, 0);
        gpCustomerInfo.add(hbAddress, 0, 1);
        gpCustomerInfo.add(tfAddress, 1, 1);
        gpCustomerInfo.add(hbEmail, 2, 0);
        gpCustomerInfo.add(tfEmail, 3, 0);
        gpCustomerInfo.add(hbCNumber, 2, 1);
        gpCustomerInfo.add(tfCNumber, 3, 1);
        gpCustomerInfo.setPadding(new Insets(10,10,10,10));
        gpCustomerInfo.setHgap(10);
        
        GridPane gpOrderDetails = new GridPane(); //Item Name, Quantity Ordered
        // COLUMNS: Item Name, Original Price, Order Quantity, Final Price

        for (int i = 0; i < displayDetails.size(); i++) {
            saTemp = displayDetails.get(i);
            for (int j = 0; j < saTemp.length; j++) {
                Label lbltemp = new Label(saTemp[j]);
                HBox hbItem = new HBox();
                hbItem.getChildren().add(lbltemp);
                gpOrderDetails.add(hbItem, j, i);
                if (i == 0) {
                    lbltemp.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                } else {
                    if (j != 0) {
                        GridPane.setHgrow(hbItem, Priority.ALWAYS);
                        hbItem.setAlignment(Pos.CENTER);
                    }
                }
                if (i == 0 && j != 0) {
                    hbItem.setAlignment(Pos.CENTER);
                }
            }
        }
        gpOrderDetails.setHgap(10);
        gpOrderDetails.setAlignment(Pos.CENTER);
        
        Label lblSubTotal = new Label("Sub Total: " + totalPrice);
        lblSubTotal.setFont(Font.font("Verdana", FontWeight.LIGHT, 12));
        
        Label lblDeliveryCharge = new Label("Delivery Charge: 60.00");
        lblDeliveryCharge.setFont(Font.font("Verdana", FontWeight.LIGHT, 12));
        
        totalPrice += (double)60;
        
        Label lblTotalPrice = new Label("Total: " + Double.toString((double)Math.round(totalPrice * 100000d) / 100000d)); // Total Price of Items
        lblTotalPrice.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        
        VBox vbTP = new VBox(5);
        vbTP.getChildren().add(lblSubTotal);
        vbTP.getChildren().add(lblDeliveryCharge);
        vbTP.getChildren().add(lblTotalPrice);
        vbTP.setAlignment(Pos.BOTTOM_RIGHT);
        
        Button okBtn = new Button("Close");
        okBtn.setOnAction(e -> {
            window.close();
        });
        okBtn.getStyleClass().add("blueButton2");
        
        HBox hbOK = new HBox();
        hbOK.getChildren().add(okBtn);
        hbOK.setAlignment(Pos.CENTER);
        
        VBox vbox = new VBox (15);
        vbox.getChildren().add(lblOrderInfo);
        vbox.getChildren().add(gpCustomerInfo);
        vbox.getChildren().add(gpOrderDetails);
        vbox.getChildren().add(vbTP);
        vbox.getChildren().add(hbOK);
        vbox.setPadding(new Insets(15,15,15,15));
        
        Scene mainScene = new Scene(vbox);
        mainScene.getStylesheets().add("css/css_demo.css");
        window.setScene(mainScene);
        window.showAndWait();
    }
    
}
