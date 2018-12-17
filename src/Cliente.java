
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
public class Cliente {
    
    public List<Socket> CrearSocket(String ipmaquina, IP listaip){
        //Nomenclatura SCXX: Socket para envio de datos a cliente XX
        if (ipmaquina.equals(listaip.M29.get(0))){
            try {
                Socket SC30 = new Socket(listaip.M30.get(0),Integer.parseInt(listaip.M30.get(1)));
                Socket SC31 = new Socket(listaip.M31.get(0),Integer.parseInt(listaip.M31.get(1)));
                Socket SC32 = new Socket(listaip.M32.get(0),Integer.parseInt(listaip.M32.get(1)));
                List<Socket> sockets = new ArrayList<>();
                sockets.add(SC30);
                sockets.add(SC31);
                sockets.add(SC32);
                return sockets;
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (ipmaquina.equals(listaip.M30.get(0))){
            try {
                Socket SC29 = new Socket(listaip.M29.get(0),Integer.parseInt(listaip.M29.get(1)));
                Socket SC31 = new Socket(listaip.M31.get(0),Integer.parseInt(listaip.M31.get(1)));
                Socket SC32 = new Socket(listaip.M32.get(0),Integer.parseInt(listaip.M32.get(1)));
                List<Socket> sockets = new ArrayList<>();
                sockets.add(SC29);
                sockets.add(SC31);
                sockets.add(SC32);
                return sockets;
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (ipmaquina.equals(listaip.M31.get(0))){
            try {
                Socket SC30 = new Socket(listaip.M30.get(0),Integer.parseInt(listaip.M30.get(1)));
                Socket SC29 = new Socket(listaip.M29.get(0),Integer.parseInt(listaip.M29.get(1)));
                Socket SC32 = new Socket(listaip.M32.get(0),Integer.parseInt(listaip.M32.get(1)));
                List<Socket> sockets = new ArrayList<>();
                sockets.add(SC30);
                sockets.add(SC29);
                sockets.add(SC32);
                return sockets;
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (ipmaquina.equals(listaip.M32.get(0))){
            try {
                Socket SC30 = new Socket(listaip.M30.get(0),Integer.parseInt(listaip.M30.get(1)));
                Socket SC31 = new Socket(listaip.M31.get(0),Integer.parseInt(listaip.M31.get(1)));
                Socket SC29 = new Socket(listaip.M29.get(0),Integer.parseInt(listaip.M29.get(1)));
                List<Socket> sockets = new ArrayList<>();
                sockets.add(SC30);
                sockets.add(SC31);
                sockets.add(SC29);
                return sockets;
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public void EnviarBroadcast(String data,List<Socket> listaSocket) throws IOException{
        //SERIALIZAR DATA EN SOCKET
        DataOutputStream mensaje1 = new DataOutputStream(listaSocket.get(0).getOutputStream());
        DataOutputStream mensaje2 = new DataOutputStream(listaSocket.get(1).getOutputStream());
        DataOutputStream mensaje3 = new DataOutputStream(listaSocket.get(2).getOutputStream());
        //ENVIAR DATA
        mensaje1.writeUTF(data);
        mensaje2.writeUTF(data);
        mensaje3.writeUTF(data);
        //Cerrar Buffer
        //mensaje1.close();
        //mensaje2.close();
        //mensaje3.close();
    }
    
    public void EnviarIndividual(String data,Socket socket) throws IOException{
        DataOutputStream mensaje = new DataOutputStream(socket.getOutputStream());
        mensaje.writeUTF(data);
        //mensaje.close();
    }
    
    
}
