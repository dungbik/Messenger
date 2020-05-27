/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import network.Crypto;

/**
 *
 * @author À±Á¤È¯
 */
public class Client {
    public final static AttributeKey<Client> CLIENTKEY = AttributeKey.valueOf("User_netty");
    private Channel session;
    private transient Crypto send, recv;
    private String id, pw, nickname;
    
    public Client(Channel session, Crypto send, Crypto recv) {
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
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setPW(String pw) {
        this.pw = pw;
    }
    
    public String getPW() {
        return this.pw;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getNickname() {
        return this.nickname;
    }
}
