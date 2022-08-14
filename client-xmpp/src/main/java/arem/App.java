package arem;

import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.parts.Localpart;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.HashMap;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

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
                //.setUsernameAndPassword("test","test")
                .setXmppDomain("alumchat.fun")
                .setHost("alumchat.fun")
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setSendPresence(true)
                .build();

                AbstractXMPPConnection connection = new XMPPTCPConnection(config);
                connection.connect(); //Establishes a connection to the server
                AccountManager manager = AccountManager.getInstance(connection);
                Map<String, String> map = new HashMap<String, String>();
                Localpart nickname = Localpart.from("hola");
                try {
                    if (manager.supportsAccountCreation()) {
                        manager.sensitiveOperationOverInsecureConnection(true);
                        manager.createAccount(nickname, "password");

                    }
                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }

                //connection.login(); //Logs in

                System.out.println("Connected");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        }.start();
    }
}
