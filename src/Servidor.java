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
public class Servidor {
    String Ipmaquina;
    int puerto;
    
    public Servidor(String ipmaquina,IP listaip){
        if (listaip.M29.get(0).equals(ipmaquina)){
            this.Ipmaquina = ipmaquina;
            this.puerto = Integer.parseInt(listaip.M29.get(1));
        }
        if (listaip.M30.get(0).equals(ipmaquina)){
            this.Ipmaquina = ipmaquina;
            this.puerto = Integer.parseInt(listaip.M30.get(1));
        }
        if (listaip.M31.get(0).equals(ipmaquina)){
            this.Ipmaquina = ipmaquina;
            this.puerto = Integer.parseInt(listaip.M31.get(1));
            
        }
        if (listaip.M32.get(0).equals(ipmaquina)){
            this.Ipmaquina = ipmaquina;
            this.puerto = Integer.parseInt(listaip.M32.get(1));
        }
    }
    
}
