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

/**
 *
 * @author Dido
 */
class DBAdmin {
    String username, password;
    int adminBranch = 0;
    
    // PLACEHOLDERS
    String phAdmin = "", phPass = "";
    int phBranch = 0;

    public boolean DBAdmin(String username, String password) {
        System.out.println("DBAdmin.java -> DBAdmin");

        this.username = username;
        this.password = password;

        return loginAdmin();
    }

    private boolean loginAdmin() {
        System.out.println("DBAdmin.java -> loginAdmin");
        boolean checker;
        // insert db method start
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false", "root", "12345");
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * from manager_account WHERE Admin_ID=" + username + " AND Authentication=\"" + password + "\"");

            while (rs.next()) {
                phAdmin = Integer.toString(rs.getInt("Admin_ID"));
                phPass = rs.getString("Authentication");
                phBranch = rs.getInt("Branch_ID");
            }
            
            rs.close();
            st.close();
            con.close();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        // insert db method end
        if (username.equals(phAdmin)
                && password.equals(phPass)) {
            checker = true;
            adminBranch = phBranch;
        } else {
            checker = false;
        }
        
        return checker;
    }
}
