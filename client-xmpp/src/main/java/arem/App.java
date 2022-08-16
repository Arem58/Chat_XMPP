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

                connection.login(); //Logs in

                // Scanner user = new Scanner(System.in);
                EntityBareJid jid = JidCreate.entityBareFrom("are@conference." + connection.getHost());
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
                
                MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
                MultiUserChat muc = manager.getMultiUserChat(jid);
                Resourcepart room = Resourcepart.from("qwer");
                if (!muc.isJoined())
                    muc.join(room);

                muc.addMessageListener(new MessageListener() {
                    @Override
                    public void processMessage(Message message){
                        System.out.println("Message listener Received message in send message: "
                        + (message != null ? message.getBody() : "NULL") + "  , Message sender :" + message.getFrom());;
                    }
                });

                Scanner messege = new Scanner(System.in);
                String hola = "";
                while(!hola.contains("1")){
                    // if (messege.nextLine().contains("1"))
                    //     muc.leave();
                    hola = messege.nextLine();
                    muc.sendMessage(hola);
                }

                muc.leave();

                System.out.println("Connected");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        }.start();
    }
}
