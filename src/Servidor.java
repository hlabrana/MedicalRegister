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
    String Ipmaquina;
    int puerto;
    
    public Servidor(String ipmaquina,IP listaip){
        if (listaip.M29.get(0).equals(ipmaquina)){
            this.Ipmaquina = ipmaquina;
            this.puerto = Integer.parseInt(listaip.M29.get(1));
            Thread hebra = new Thread(this);
            hebra.start();
        }
        if (listaip.M30.get(0).equals(ipmaquina)){
            this.Ipmaquina = ipmaquina;
            this.puerto = Integer.parseInt(listaip.M30.get(1));
            Thread hebra = new Thread(this);
            hebra.start();
        }
        if (listaip.M31.get(0).equals(ipmaquina)){
            this.Ipmaquina = ipmaquina;
            this.puerto = Integer.parseInt(listaip.M31.get(1));
            Thread hebra = new Thread(this);
            hebra.start();
            
        }
        if (listaip.M32.get(0).equals(ipmaquina)){
            this.Ipmaquina = ipmaquina;
            this.puerto = Integer.parseInt(listaip.M32.get(1));
            Thread hebra = new Thread(this);
            hebra.start();
        }
    }
    
    @Override
    public void run(){
        try {
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
