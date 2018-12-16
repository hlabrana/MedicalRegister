import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hlabrana
 */
public class Servidor extends Thread {
    int puerto;
    Main main;
    
    public Servidor(Main main){
        if (main.listaip.M29.get(0).equals(main.ipMaquina)){
            this.puerto = Integer.parseInt(main.listaip.M29.get(1));
            Thread hebra = new Thread();
            hebra.start();

        }
        if (main.listaip.M30.get(0).equals(main.ipMaquina)){
            this.puerto = Integer.parseInt(main.listaip.M30.get(1));
            Thread hebra = new Thread();
            hebra.start();
        }
        if (main.listaip.M31.get(0).equals(main.ipMaquina)){
            this.puerto = Integer.parseInt(main.listaip.M31.get(1));
            Thread hebra = new Thread();
            hebra.start();
            
        }
        if (main.listaip.M32.get(0).equals(main.ipMaquina)){
            this.puerto = Integer.parseInt(main.listaip.M32.get(1));
            Thread hebra = new Thread();
            hebra.start();
        }
    }
    
    @Override
    public void run(){
        try {
            System.out.println(this.puerto);
            ServerSocket servidor = new ServerSocket(this.puerto);
            while(true){
                Socket socket = servidor.accept();
                DataInputStream mensaje = new DataInputStream(socket.getInputStream());
                String data = mensaje.readUTF();
                System.out.println("\n"+data+"\n");
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
