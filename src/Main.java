
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hlabrana
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException{
        
        //PROCESA JSON TRABAJADORES Y LOS AGREGA A UNA LISTA personal
        Trabajadores personal = new Trabajadores();
        personal = personal.ProcesarJSON("JSON/Trabajadores.JSON");
        
        ////PROCESA JSON REQUERIMIENTOS Y LOS AGREGA A UNA LISTA requerimientos
        Requerimientos requerimientos = new Requerimientos();
        requerimientos = requerimientos.ProcesarJSON("JSON/Requerimientos.JSON");
        
        ////PROCESA JSON Pacientes Y LOS AGREGA A UNA LISTA pacientes
        Pacientes pacientes = new Pacientes();
        pacientes = pacientes.ProcesarJSON("JSON/Pacientes.JSON");
        
        //PROCESA JSON IP e instancia una clase IP
        IP listaip = new IP();
        listaip = listaip.ProcesarJSON("JSON/IP.JSON");
        
        //Consulta por IP de maquina
        String ipMaquina = ConsultarIPMaquina();
        
        //Crear Socket Cliente
        Cliente cliente = new Cliente();
        List<Socket> listasockets = cliente.CrearSocket(ipMaquina, listaip);
        //Crear Socket Servidor
        Servidor servidor = new Servidor(ipMaquina,listaip);
        
    }
    
    /**
     *
     * @return
     */
    public static String ConsultarIPMaquina(){
        System.out.print("\nIngrese IP de la Maquina: ");
        Scanner in = new Scanner(System.in);
        String ip = in.nextLine();
        return ip;
    }
    
}
