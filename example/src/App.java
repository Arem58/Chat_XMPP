import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.ConnectionConfiguration;

public class App {
    public static void main(String[] args) throws Exception {
        new Thread(){
        public void run(){
            try{
                ConnectionConfiguration config = new ConnectionConfiguration("alumchat.fun", 5222);
                config.setSecurityMode(co);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        }.start();
    }
}
