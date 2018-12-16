
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
public class Main extends Thread {
    Trabajadores personal;
    Requerimientos requerimientos;
    Pacientes pacientes;
    IP listaip;
    String ipMaquina;
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException{
        
        Main main = new Main();
        
        //PROCESA JSON TRABAJADORES Y LOS AGREGA A UNA LISTA personal
        Trabajadores personal = new Trabajadores();
        personal = personal.ProcesarJSON("JSON/Trabajadores.JSON");
        main.personal = personal;
        
        ////PROCESA JSON REQUERIMIENTOS Y LOS AGREGA A UNA LISTA requerimientos
        Requerimientos requerimientos = new Requerimientos();
        requerimientos = requerimientos.ProcesarJSON("JSON/Requerimientos.JSON");
        main.requerimientos = requerimientos;
        
        ////PROCESA JSON Pacientes Y LOS AGREGA A UNA LISTA pacientes
        Pacientes pacientes = new Pacientes();
        pacientes = pacientes.ProcesarJSON("JSON/Pacientes.JSON");
        main.pacientes = pacientes;
        
        //PROCESA JSON IP e instancia una clase IP
        IP listaip = new IP();
        listaip = listaip.ProcesarJSON("JSON/IP.JSON");
        main.listaip = listaip;
        
        //Consulta por IP de maquina
        String ipMaquina = ConsultarIPMaquina();
        main.ipMaquina = ipMaquina;
        
        //Crear Socket Servidor
        Servidor servidor = new Servidor(main);
        
        System.out.print("\nIniciar: ");
        Scanner in = new Scanner(System.in);
        String ip = in.nextLine();
        
        //Crear Socket Cliente
        Cliente cliente = new Cliente();
        List<Socket> listasockets = cliente.CrearSocket(ipMaquina, listaip);

        //El primer coordinador es la maquina con ip 10.4.60.169
        //las demas maquinas envian su mejor candidato para algortimo de bully
        System.out.println("\nAplicando Algortimo Bully...");
        Doctor candidato = personal.getMejorDoctor();
        EnviarCandidato(candidato,listasockets,ipMaquina,cliente);
        //String ipCoordinador = EscogerCoordinador();
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
    
    public static void EnviarCandidato(Doctor candidato,List<Socket> listasockets,String ipmaquina,Cliente cliente) throws IOException{
        if(ipmaquina.equals("10.6.40.169") == false){
            String experiencia = String.valueOf(candidato.getEstudios()+candidato.getExperiencia());
            System.out.println("[Algortimo Bully] Enviando Candidato a Coordinador...");
            for(int i=0;i<listasockets.size();i++){
                if (listasockets.get(i).getInetAddress().getCanonicalHostName().equals("10.6.40.169")){
                    cliente.EnviarIndividual("[Bully];"+experiencia,listasockets.get(i));
                }
            }
        }
    }
    
}
