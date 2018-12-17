
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
public class Main implements Runnable {
    Trabajadores personal;
    Requerimientos requerimientos;
    Pacientes pacientes;
    IP listaip;
    String ipMaquina;
    Servidor servidor;
    List<String> candidatos = new ArrayList<>();
    boolean Is_Coordinador;
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, InterruptedException{
        
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
        
        //Crear Socket Servidor
        Servidor servidor = new Servidor(ipMaquina,listaip);
        // Crear HEBRA
        Main main = new Main();
        main.ipMaquina = ipMaquina;
        main.listaip = listaip;
        main.pacientes = pacientes;
        main.personal = personal;
        main.requerimientos = requerimientos;
        main.servidor = servidor;
        Runnable subproceso = main;
        new Thread(subproceso).start();
        
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
        main.candidatos.add(ipMaquina+";"+"Bully;"+String.valueOf(candidato.Estudios+candidato.Experiencia));
        EnviarCandidato(candidato,listasockets,ipMaquina,cliente);
        
        //El coordinador espera el inicio de otras MV
        if(ipMaquina.equals("10.6.40.169")){
            Thread.sleep(5000);
            String mensaje = EscogerCoordinador(main);
            System.out.println("[Algoritmo Bully] Nuevo Coordinador con IP: "+mensaje.split(";")[0]);
            System.out.println("[Algoritmo Bully] Avisando resultado..");
            cliente.EnviarBroadcast(mensaje, listasockets);
        }
        
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
                    cliente.EnviarIndividual(ipmaquina+";"+"Bully;"+experiencia,listasockets.get(i));
                }
            }
        }
    }
    
    public static void ProcesarMensaje(Main main,String mensaje,List<String> candidatos){
        if(mensaje.split(";")[1].equals("Bully")){
            candidatos.add(mensaje);
            String escogido = EscogerCoordinador(main);
        }
        if(mensaje.split(";")[1].equals("R_Bully")){
            System.out.println("[Algoritmo Bully] Resultado: Nuevo Coordinador con IP: "+mensaje.split(";")[0]);
            if(mensaje.split(";")[0].equals(main.ipMaquina)){
                main.Is_Coordinador = true;
                System.out.println("[Algoritmo Bully] Esta maquina es el nuevo Coordinador");
            }
            else{
                main.Is_Coordinador = false;
            }
        }
    }
    
    public static String EscogerCoordinador(Main main){
        if(main.candidatos.size() == 4){
            String ipCoordinador = main.candidatos.get(0).split(";")[0];
            int expCoordinador = Integer.parseInt(main.candidatos.get(0).split(";")[2]);
            for(int i=1;i<main.candidatos.size();i++){
                if(Integer.parseInt(main.candidatos.get(i).split(";")[2]) > expCoordinador){
                    ipCoordinador = main.candidatos.get(i).split(";")[0];
                    expCoordinador = Integer.parseInt(main.candidatos.get(i).split(";")[2]);
                }
            }
        return ipCoordinador+";R_Bully;"+String.valueOf(expCoordinador);
        }
        return null;
    }
    
    
    @Override
    public void run(){
        try {
            ServerSocket servidor = new ServerSocket(this.servidor.puerto);
            while(true){
                Socket socket = servidor.accept();
                DataInputStream mensaje = new DataInputStream(socket.getInputStream());
                String data = mensaje.readUTF();
                ProcesarMensaje(this,data,this.candidatos);
                //System.out.println("\n"+data+"\n");
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
