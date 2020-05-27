package client;


import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author À±Á¤È¯
 */

public class UserResister {
    
    public static boolean getAccountExists(String id) {
        boolean accountExists = false;
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id FROM user WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                accountExists = true;
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
        }
        return accountExists;
    }

    public static void createAccount(String id, String pwd) {
        Connection con;
        try {
            con = DatabaseConnection.getConnection();
        } catch (Exception ex) {
            return;
        }

        try (PreparedStatement ps = con.prepareStatement("INSERT INTO user (account, pw, nickname) VALUES (?, ?, ?)")) {
            ps.setString(1, id);
            ps.setString(2, pwd);
            ps.setString(3, id);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
