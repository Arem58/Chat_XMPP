package arem;

import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.Scanner;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        new Thread(){    
        public void run(){
            try{
                XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword("User4","hola")
                .setXmppDomain("alumchat.fun")
                .setHost("alumchat.fun")
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setSendPresence(true)
                .build();

                AbstractXMPPConnection connection = new XMPPTCPConnection(config);
                connection.connect(); //Establishes a connection to the server
                // AccountManager manager = AccountManager.getInstance(connection);
                // Localpart nickname = Localpart.from("User4");
                
                // try {
                //     if (manager.supportsAccountCreation()) {
                //         manager.sensitiveOperationOverInsecureConnection(true);
                //         manager.createAccount(nickname, "hola");

                //     }
                // } catch (SmackException.NoResponseException e) {
                //     e.printStackTrace();
                // } catch (XMPPException.XMPPErrorException e) {
                //     e.printStackTrace();
                //     System.out.println("Ya existe cuenta");
                // } catch (SmackException.NotConnectedException e) {
                //     e.printStackTrace();
                // }

                connection.login(); //Logs in

                ChatManager chatManager = ChatManager.getInstanceFor(connection);
                Scanner user = new Scanner(System.in);
                EntityBareJid jid = JidCreate.entityBareFrom(user.nextLine() + "@alumchat.fun");
                Chat chat = chatManager.chatWith(jid);
                
                chatManager.addIncomingListener(new IncomingChatMessageListener() {
                    @Override
                    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                      System.out.println("New message from " + from + ": " + message.getBody());
                    }
                });

                Scanner messege = new Scanner(System.in);
                        while(connection.isConnected()){
                            chat.send(messege.nextLine());
                        }

                System.out.println("Connected");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        }.start();
    }
}
