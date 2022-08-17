package arem;

import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.RoomInfo;
//import org.jivesoftware.smackx.si.packet.StreamInitiation.File;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.util.XmppStringUtils;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.Collection;
import java.util.Scanner;
import java.io.File;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaCollector;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.roster.AbstractRosterListener;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;

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

    public static void menuInicio(){
        System.out.println("""
                --------Menu----------
                    1. Crear usuario
                    2. Iniciar sesion
                    3. Opciones
                    4. Salir
                ----------------------
                """); 
    }

    public static void menuPrincipal(){
        System.out.println("""
                --------Menu Principal----------
                    1. Lista de todos los usuarios
                    2. Agregar contacto
                    3. Ver los datos de un usuario
                    4. Chat con usuario
                    5. Unirse a un room
                    6. Cambiar estatus
                    7. Cerrar sesion
                    8. Eliminar cuenta
                    9. Opciones
                --------------------------------
                """); 
    }

    public static void main(String[] args) {

        new Thread(){    
        public void run(){
            try{
                XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                //.setUsernameAndPassword("hola5","hola")
                .setXmppDomain("alumchat.fun")
                .setHost("alumchat.fun")
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setSendPresence(true)
                .build();

                AbstractXMPPConnection connection = new XMPPTCPConnection(config);
                connection.connect(); //Establishes a connection to the server
                System.out.println("Connected");
                
                Scanner conteiner = new Scanner(System.in);
                int op;
                menuInicio();
                while(true){
                    System.out.print("> ");
                    op = Integer.parseInt(conteiner.nextLine());
                    System.out.print(" ");
                    if (op == 1){
                        //#region Pedido de datos crear usuario
                        String user;
                        String pass; 
                        System.out.println("Crear usuario");
                        System.out.println("Ingrese nombre de usuario: ");
                        System.out.print("> ");
                        user = conteiner.nextLine();
                        System.out.println("Ingrese contrasena: ");
                        System.out.print("> "); 
                        pass = conteiner.nextLine();
                        //#endregion
                        //#region Sign Up
                        AccountManager manager = AccountManager.getInstance(connection);
                        Localpart nickname = Localpart.from(user);
                        
                        try {
                            if (manager.supportsAccountCreation()) {
                                manager.sensitiveOperationOverInsecureConnection(true);
                                manager.createAccount(nickname, pass);

                            }
                        } catch (SmackException.NoResponseException e) {
                            e.printStackTrace();
                        } catch (XMPPException.XMPPErrorException e) {
                            e.printStackTrace();
                            System.out.println("Ya existe cuenta");
                        } catch (SmackException.NotConnectedException e) {
                            e.printStackTrace();
                        }
                        //#endregion
                        System.out.println("Usuario Creado1"); 
                        menuInicio();
                    }else if (op == 2){
                        //#region Pedido de datos inicio de sesion
                        String user;
                        String pass; 
                        System.out.println("Iniciar sesion");
                        System.out.println("Ingrese nombre de usuario: ");
                        System.out.print("> ");
                        user = conteiner.nextLine();
                        System.out.println("Ingrese contrasena: ");
                        System.out.print("> "); 
                        pass = conteiner.nextLine();
                        //#endregion
                        
                        connection.login(user, pass); //Logs in
                        System.out.println("Inicio de sesion exitoso");

                        //#region Listeners y declaracion de variables
                        //Account Manager
                        AccountManager manager = AccountManager.getInstance(connection);
                        //Jids
                        EntityBareJid jid;
                        //Presence
                        Presence presence;
                        //#region Chats
                        ChatManager chatManager = ChatManager.getInstanceFor(connection);
                        chatManager.addIncomingListener(new IncomingChatMessageListener() {
                            @Override
                            public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                              System.out.println("New message from " + from + ": " + message.getBody());
                            }
                        });
                        //#endregion
                        //#region Roster
                        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
                        Roster roster = Roster.getInstanceFor(connection);

                        roster.addRosterListener(new RosterListener() {
                            public void entriesAdded(Collection<Jid> addresses) { }
                            public void entriesDeleted(Collection<Jid> addresses) {  }
                            public void entriesUpdated(Collection<Jid> addresses) {  }
                            public void presenceChanged(Presence presence) { 
                                System.out.println("Presence changed: " + presence.getFrom() + " " + presence);
                            }
                        });
                        if (!roster.isLoaded()) 
                            try {
                                roster.reloadAndWait();
                            } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | InterruptedException e) {
                                e.printStackTrace();
                        }
                        //#endregion
                        //#endregion

                        //#region Funciones principales
                        menuPrincipal();
                        String messege = "";
                        while(connection.isConnected()){
                            System.out.print("> ");
                            op = Integer.parseInt(conteiner.nextLine());
                            System.out.print(" ");
                            switch(op){
                                case 1:
                                    //#region Mostrar todo los usuarios
                                    Collection<RosterEntry> entries = roster.getEntries();
                                    System.out.println("------Lista de usuarios -----");
                                    for (RosterEntry entry : entries) {
                                        presence = roster.getPresence(entry.getJid());
                                        System.out.println("JId: " + entry.getJid());
                                        System.out.println("User: " + presence.getType().name());
                                        System.out.println("Status: " + presence.getStatus());
                                        System.out.println("Available: " + presence.isAvailable());
                                    }
                                    System.out.println("-----------------------------");
                                    //#endregion     
                                    break;
                                case 2:
                                    //#region Solicitud de amistad
                                    System.out.println("-----Solicitud de amistas-----");;
                                    System.out.println("Ingrese el nombre del contacto que desea agregar");;
                                    System.out.print(">");
                                    jid = JidCreate.entityBareFrom(conteiner.nextLine() + "@" + connection.getHost());
                                    try {
                                        if (!roster.contains(jid)) {
                                            roster.createItemAndRequestSubscription(jid, conteiner.nextLine(), null);
                                            System.out.println("Se ha enviado exitosamente la solicitud");
                                        } else {
                                            System.out.println("ya es un compa");
                                        }

                                    } catch (SmackException.NotLoggedInException e) {
                                        e.printStackTrace();
                                    } catch (SmackException.NoResponseException e) {
                                        e.printStackTrace();
                                    } catch (SmackException.NotConnectedException e) {
                                        e.printStackTrace();
                                    } catch (XMPPException.XMPPErrorException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //#endregion
                                    break;
                                case 3:
                                    //#region Informacion de un usuario
                                    System.out.println("-----Informacion de un usuario-----");;
                                    System.out.println("Ingrese el nombre del usuario para ver su informacion");;
                                    System.out.print(">");
                                    if (!roster.isLoaded()) 
                                    try {
                                        roster.reloadAndWait();
                                    } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    jid = JidCreate.entityBareFrom(conteiner.nextLine() + "@" + connection.getHost());
                                    presence = roster.getPresence(jid);
                                    System.out.println("----------Informacion----------");;
                                    System.out.println("Jid: " + presence.getFrom()); 
                                    System.out.println("User name: " + presence.getType().name());
                                    System.out.println("Status: " + presence.getStatus());
                                    System.out.println("Root: " + presence.getElementName());
                                    System.out.println("Mode: " + presence.getMode()); 	
                                    System.out.println("Priority: " + presence.getPriority()); 	 	
                                    System.out.println("Available: " + presence.isAvailable()); 	
                                    System.out.println("-------------------------------");;
                                    //#endregion 	
                                    break;
                                case 4:
                                    //#region Chat 1v1
                                    System.out.println("Ingrese con quien quiere chatear");
                                    System.out.print(">");
                                    jid = JidCreate.entityBareFrom(conteiner.nextLine() + "@" + connection.getHost());
                                    Chat chat = chatManager.chatWith(jid);
                                    
                                    System.out.println("-----Chat con " + jid + "-----");;
                                    System.out.println("Para ver la opciones en el menu precione 1: ");
                                    while(!messege.contains("~")){
                                        System.out.print("> (help: 1)");
                                        messege = conteiner.nextLine();
                                        if (messege.contains("1"))
                                                System.out.println("Presione ~ para salir");
                                        else if (!messege.contains("~"))
                                            chat.send(messege);
                                    }
                                    System.out.println("-----Has salido del chat-----");
                                    //#endregion 
                                    break;
                                case 5:
                                    //#region Chat Grupal
                                    System.out.println("Ingrese el room al que quiere ingresar");
                                    System.out.print(">");
                                    jid = JidCreate.entityBareFrom(conteiner.nextLine() + "@conference." + connection.getHost());
                                    MultiUserChatManager managerCum = MultiUserChatManager.getInstanceFor(connection);
                                    MultiUserChat muc = managerCum.getMultiUserChat(jid);
                                    System.out.println("Ingrese su apodo");
                                    System.out.print(">");
                                    Resourcepart room = Resourcepart.from(conteiner.nextLine());
                                    if (!muc.isJoined())
                                        muc.join(room);
                                    
                                    muc.addMessageListener(new MessageListener() {
                                        @Override
                                        public void processMessage(Message message){
                                            System.out.println("Message listener Received message in send message: "
                                            + (message != null ? message.getBody() : "NULL") + "  , Message sender :" + message.getFrom());;
                                        }
                                    });
                                    
                                    System.out.println("-----Chat con " + jid + "-----");;
                                    System.out.println("Para ver la opciones en el menu precione 1: ");
                                    while(!messege.contains("~")){
                                            System.out.print("> (help: 1)");
                                            messege = conteiner.nextLine();
                                            if (messege.contains("1"))
                                                System.out.println("Presione ~ para salir");
                                            else if (!messege.contains("~"))
                                                muc.sendMessage(messege);
                                        }    
                                    muc.leave();
                                    System.out.println("-----Has salido del room-----");
                                    //#endregion 
                                    break;
                                case 6:
                                    //#region Presence
                                    System.out.println("-----Cambio de estado-----");
                                    Stanza presenceSTZ;
                                    System.out.println("""
                                            Cambien su estado:
                                            1. Available.
                                            2. Away.
                                            3. Free to chat.
                                            4. Do not disturb.
                                            5. Away for an extended period of time.
                                            """
                                                );
                                                
                                    while(true){
                                        System.out.print(">");
                                        op = Integer.parseInt(conteiner.nextLine());
                                        System.out.print("Ingrese un status:");
                                        System.out.print(">");
                                        messege = conteiner.nextLine();
                                        switch(op){
                                            case 1:
                                                presenceSTZ = new Presence(Presence.Type.available, messege, 42, Mode.available);
                                                connection.sendStanza(presenceSTZ);       
                                                break;
                                            case 2:
                                                presenceSTZ = new Presence(Presence.Type.available, messege, 42, Mode.away);
                                                connection.sendStanza(presenceSTZ);  
                                                break;
                                            case 3:
                                                presenceSTZ = new Presence(Presence.Type.available, messege, 42, Mode.chat);
                                                connection.sendStanza(presenceSTZ);   
                                                break;
                                            case 4:
                                                presenceSTZ = new Presence(Presence.Type.available, messege, 42, Mode.dnd);
                                                connection.sendStanza(presenceSTZ);  
                                                break;
                                            case 5:
                                                presenceSTZ = new Presence(Presence.Type.available, messege, 42, Mode.xa);
                                                connection.sendStanza(presenceSTZ);  
                                                break;
                                            default:
                                                System.out.println("Elija una de las opciones disponibles");
                                                break;
                                            }
                                        if (op < 5)
                                            System.out.println("Su estado ha sido cambiado");
                                            break;
                                    }
                                    //#endregion
                                    break;
                                case 7:
                                    //#region Cerrar sesion
                                    System.out.println("Sesion cerrada"); 
                                    connection.disconnect();
                                    //#endregion
                                    break;
                                case 8:
                                    //#region Borrar cuenta
                                    System.out.println("Se ha borrado la cuenta"); 
                                    manager.deleteAccount();
                                    connection.disconnect();
                                    //#endregion
                                    break;
                                case 9:
                                    menuPrincipal();
                                    break;
                                default:
                                    System.out.println("Elija una de las opciones disponibles");
                                    break;
                            }
                        }
                        //#endregion
                        connection.connect();// se tiene que volver a conectar al servidor
                        menuInicio();
                    }else if (op == 3){
                        menuInicio();
                    }else if (op == 4){
                        break;
                    }else {
                        System.out.println(" ");
                        System.out.println("Ingrese una opcion correcta");
                        System.out.println(" ");
                    }
                }
                

                //#region Informacion de un usuario
                // Roster roster = Roster.getInstanceFor(connection);
                // if (!roster.isLoaded()) 
                // try {
                //     roster.reloadAndWait();
                // } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | InterruptedException e) {
                //     e.printStackTrace();
                // }
                // EntityBareJid jid = JidCreate.entityBareFrom(conteiner.nextLine() + "@" + connection.getHost());
                // Presence presence = roster.getPresence(jid);
                // System.out.println("Jid: " + presence.getFrom()); 
                // System.out.println("User name: " + presence.getType().name());
                // System.out.println("Status: " + presence.getStatus());
                // System.out.println("Root: " + presence.getElementName());
                // System.out.println("Mode: " + presence.getMode()); 	
                // System.out.println("Priority: " + presence.getPriority()); 	 	
                // System.out.println("Available: " + presence.isAvailable()); 	
                //#endregion 	

                //#region stanza
                // StanzaCollector collector
                //     = connection.createStanzaCollector(StanzaTypeFilter.MESSAGE);
                // Stanza stanza = collector.nextResult();

                // connection.addAsyncStanzaListener(new StanzaListener() {
                //     public void processStanza(Stanza stanza) 
                //       throws SmackException.NotConnectedException,InterruptedException, 
                //         SmackException.NotLoggedInException {
                //             // handle stanza
                //         }
                // }, StanzaTypeFilter.MESSAGE);

                // while(connection.isConnected()){
                //     System.out.println(stanza);
                // }
                //#endregion

                //#region Presence
                // Stanza presence;
                // Scanner conteiner = new Scanner(System.in);
                // int opciones;
                // System.out.println("""
                //         Cambien su estado:
                //         1. Available.
                //         2. Away.
                //         3. Free to chat.
                //         4. Do not disturb.
                //         5. Away for an extended period of time.
                //         """
                //             );
                            
                // while(true){
                //     opciones = conteiner.nextInt(); 
                //     switch(opciones){
                //         case 1:
                //             presence = new Presence(Presence.Type.available, "I am busy", 42, Mode.available);
                //             connection.sendStanza(presence);       
                //             break;
                //         case 2:
                //             presence = new Presence(Presence.Type.available, "I am busy", 42, Mode.away);
                //             connection.sendStanza(presence);  
                //             break;
                //         case 3:
                //             presence = new Presence(Presence.Type.available, "I am busy", 42, Mode.chat);
                //             connection.sendStanza(presence);   
                //             break;
                //         case 4:
                //             presence = new Presence(Presence.Type.available, "I am busy", 42, Mode.dnd);
                //             connection.sendStanza(presence);  
                //             break;
                //         case 5:
                //             presence = new Presence(Presence.Type.available, "I am busy", 42, Mode.xa);
                //             connection.sendStanza(presence);  
                //             break;
                //         default:
                //             System.out.println("Elija una de las opciones disponibles");
                //             break;
                //     }
                //     if (opciones < 5)
                //         break;
                // }
                //#endregion

                //#region send files
                // EntityFullJid jid = JidCreate.entityFullFrom("hola2@alumchat.fun/smack");

                // FileTransferManager manager = FileTransferManager.getInstanceFor(connection);

                // OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(jid);

                // try{
                //     transfer.sendFile(new File("hola.txt"), "You won't believe this!");
                // }catch (SmackException.NoResponseException e) {
                //     e.printStackTrace();
                // }catch (SmackException.NotConnectedException e) {
                //     e.printStackTrace();
                // }
                //#endregion

                //#region Borrar cuenta
                // AccountManager manager = AccountManager.getInstance(connection);
                // manager.deleteAccount();
                //#endregion

                //#region Mostrar todo los usuarios
                // Roster roster = Roster.getInstanceFor(connection);

                // roster.addRosterListener(new RosterListener() {
                //     public void entriesAdded(Collection<Jid> addresses) { }
                //     public void entriesDeleted(Collection<Jid> addresses) {  }
                //     public void entriesUpdated(Collection<Jid> addresses) {  }
                //     public void presenceChanged(Presence presence) { 
                //         System.out.println("Presence changed: " + presence.getFrom() + " " + presence);
                //     }
                // });

                // if (!roster.isLoaded()) 
                // try {
                //     roster.reloadAndWait();
                // } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | InterruptedException e) {
                //     e.printStackTrace();
                // }
                // Collection<RosterEntry> entries = roster.getEntries();
                // Presence presence;
                // for (RosterEntry entry : entries) {
                //     presence = roster.getPresence(entry.getJid());
                //     System.out.println(entry.getJid());
                //     System.out.println(presence.getType().name());
                //     System.out.println(presence.getStatus());
                // }

                //#endregion

                //#region Solicitud de amistad
                // Scanner user = new Scanner(System.in);
                // EntityBareJid jid = JidCreate.entityBareFrom(user.nextLine() + "@" + connection.getHost());

                // try {
                //     Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
                //     Roster roster = Roster.getInstanceFor(connection);
    
                //     if (!roster.contains(jid)) {
                //         roster.createItemAndRequestSubscription(jid, user.nextLine(), null);
                //     } else {
                //         System.out.println("ya es un compa");
                //     }
    
                // } catch (SmackException.NotLoggedInException e) {
                //     e.printStackTrace();
                // } catch (SmackException.NoResponseException e) {
                //     e.printStackTrace();
                // } catch (SmackException.NotConnectedException e) {
                //     e.printStackTrace();
                // } catch (XMPPException.XMPPErrorException e) {
                //     e.printStackTrace();
                // } catch (Exception e) {
                //     e.printStackTrace();
                // }
                //#endregion

                //#region Crear Usuario
                // AccountManager manager = AccountManager.getInstance(connection);
                // Localpart nickname = Localpart.from("Hola5");
                
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
                // EntityBareJid jid = JidCreate.entityBareFrom("are@conference." + connection.getHost());
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
