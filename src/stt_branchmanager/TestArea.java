/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stt_branchmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author Dido
 */
public class TestArea {

    String dbOrder_ID = "",
            dbAccount_ID = "",
            dbBranch_ID = "",
            dbPrescribed = "";
    String dbOrder_Status = "",
            dbOrder_Items = "";
    List<String[]> result;
    
    public List<String[]> getNewOrders(String adminBranch) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java3_project_stt?useSSL=false&autoReconnect=true", "clowie", "12345");
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM orders WHERE status=\"Pending\" AND branch_id=" + adminBranch);
            while (rs.next()) {
                dbOrder_ID = Integer.toString(rs.getInt("order_id"));
                dbAccount_ID = Integer.toString(rs.getInt("account_id"));
                dbBranch_ID = Integer.toString(rs.getInt("branch_id"));
                dbOrder_Items = rs.getString("order_items");
                dbOrder_Status = rs.getString("status");
                dbPrescribed = Integer.toString(rs.getInt("prescribed"));

                String[] details = new String[6];
                details[0] = dbOrder_ID;
                details[1] = dbAccount_ID;
                details[2] = dbBranch_ID;
                details[3] = dbOrder_Items;
                details[4] = dbOrder_Status;
                details[5] = dbPrescribed;
                
                result.add(details);
            }
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
}
