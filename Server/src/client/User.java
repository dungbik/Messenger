package client;


import database.DatabaseConnection;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import network.Crypto;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author À±Á¤È¯
 */
public class User {
    
    public final static AttributeKey<User> CLIENTKEY = AttributeKey.valueOf("User_netty");
    private Channel session;
    private transient Crypto send, recv;
    private static String id, pw, nickname = "";
    
    public User(Channel session, Crypto send, Crypto recv) {
        this.session = session;
        this.send = send;
        this.recv = recv;
    }
    
    public final Crypto getRecvCrypto() {
        return recv;
    }

    public final Crypto getSendCrypto() {
        return send;
    }
    
    public Channel getSession() {
        return session;
    }
    
    public static boolean login(String id_, String pw_) {
        String password = "";
        boolean suc = false;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user WHERE account = ?");
            ps.setString(1, id_);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                password = rs.getString("pw");
                suc = password.equals(pw_);
                if (suc) {
                    id = id_;
                    pw = pw_;
                    nickname = rs.getString("nickname");
                }
                rs.close();
                ps.close();
            } else {
                return suc;
            }
        } catch (Exception ex) {
            return suc;
        }
        return suc;
    }
    
    public static String getNickname() {
        return nickname;
    }
    
    public static String getId() {
        return id;
    }
    
    public static String getPW() {
        return pw;
    }
}
