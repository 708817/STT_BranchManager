///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package stt_branchmanager;
//
//
///**
// *
// * @author Dido
// */
//class DBAdmin {
//    String username, password;
//    int adminBranch = 0;
//    
//    // PLACEHOLDERS
//    String phAdmin = "", phPass = "";
//    int phBranch = 0;
//    String phEmail = "";
//
//    public boolean DBAdmin(String username, String password) {
//        System.out.println("DBAdmin.java -> DBAdmin");
//
//        if (username.isEmpty() || password.isEmpty()) {
//            return false;
//        }
//        
//        this.username = username;
//        this.password = password;
//
//        return loginAdmin(username, password);
//    }
//    
//    
//
////    private boolean loginAdmin(String user, String pass) {
////        boolean checker;
////        
////        String url = "jdbc:mysql://192.168.137.1/java3_project_stt?useSSL=false&allowPublicKeyRetrieval=true&autoReconnect=true";
////        try {
////
////            Class.forName("com.mysql.cj.jdbc.Driver");
////            Connection con = DriverManager.getConnection(url, "clowie", "12345");//user is case sensitave 
////            
////            String encryptPass = "";
////                try {
////                    String plainPassword = pass;
////
////                    MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
////                    mdAlgorithm.update(plainPassword.getBytes());
////
////                    byte[] digest = mdAlgorithm.digest();
////                    StringBuffer hexString = new StringBuffer();
////
////                    for (int i = 0; i < digest.length; i++) {
////                        plainPassword = Integer.toHexString(0xFF & digest[i]);
////
////                        if (plainPassword.length() < 2) {
////                            plainPassword = "0" + plainPassword;
////                        }
////                        hexString.append(plainPassword);
////                    }
////                    encryptPass = hexString.toString();
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////            
////            
////            Statement st = con.createStatement();
////            ResultSet rs = st.executeQuery("SELECT * from manager_account WHERE admin_id=" + user + " AND authentication=\"" + encryptPass + "\"");
////
////            while (rs.next()) {
////                phAdmin = Integer.toString(rs.getInt("admin_id"));
////                phPass = rs.getString("authentication");
////                phBranch = rs.getInt("branch_id");
////                phEmail = rs.getString("email");
////            }
////            
////            rs.close();
////            st.close();
////            con.close();
////
////        } catch (SQLSyntaxErrorException sqlex) {
//////            sqlex.printStackTrace();
////            return false;
////        } catch (Exception ex)  {
////            ex.printStackTrace(); 
////        }
////
////        // insert db method end
////        if (username.equals(phAdmin)
////                && password.equals(phPass)) {
////            checker = true;
////            adminBranch = phBranch;
////        } else {
////            checker = false;
////        }
////        
////        return checker;
////    }
//    
//    
//}
