package arem;

import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.Scanner;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
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
                .setUsernameAndPassword("Hola2","hola")
                .setXmppDomain("alumchat.fun")
                .setHost("alumchat.fun")
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setSendPresence(true)
                .build();

                AbstractXMPPConnection connection = new XMPPTCPConnection(config);
                connection.connect(); //Establishes a connection to the server
                System.out.println("Connected");
                
                connection.login(); //Logs in
                
                //#region Crear Usuario
                // AccountManager manager = AccountManager.getInstance(connection);
                // Localpart nickname = Localpart.from("Hola3");
                
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
                //#endregion

                //#region Chat 1v1
                // Scanner user = new Scanner(System.in);
                //EntityBareJid jid = JidCreate.entityBareFrom("are@conference." + connection.getHost());
                // EntityBareJid jid = JidCreate.entityBareFrom(user.nextLine() + "@" + connection.getHost());
                // ChatManager chatManager = ChatManager.getInstanceFor(connection);
                // Chat chat = chatManager.chatWith(jid);
                
                // chatManager.addIncomingListener(new IncomingChatMessageListener() {
                //     @Override
                //     public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                //       System.out.println("New message from " + from + ": " + message.getBody());
                //     }
                // });

                // Scanner messege = new Scanner(System.in);
                // while(connection.isConnected()){
                //     chat.send(messege.nextLine());
                // }
                //#endregion
                
                //#region Chat Grupal
                // MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
                // MultiUserChat muc = manager.getMultiUserChat(jid);
                // Resourcepart room = Resourcepart.from("arem");
                // if (!muc.isJoined())
                //     muc.join(room);

                // muc.addMessageListener(new MessageListener() {
                //     @Override
                //     public void processMessage(Message message){
                //         System.out.println("Message listener Received message in send message: "
                //         + (message != null ? message.getBody() : "NULL") + "  , Message sender :" + message.getFrom());;
                //     }
                // });
                
                // Scanner conteiner = new Scanner(System.in);
                // String messege = "";
                // System.out.println("Para ver la opciones en el menu precione 1: ");
                // while(!messege.contains("~")){
                    //     messege = conteiner.nextLine();
                    //     if (messege.contains("1"))
                    //         System.out.println("Presione ~ para salir");
                    //     else
                    //         muc.sendMessage(messege);
                    // }
                    
                    // muc.leave();
                    //#endregion
                    
                System.out.println("Disconnected");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        }.start();
    }
}
