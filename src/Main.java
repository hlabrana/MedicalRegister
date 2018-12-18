
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    String ipCoordinador;
    boolean permiso;
    FileWriter escribir;
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

        //El primer coordinador es la maquina con ip 10.4.60.169
        //las demas maquinas envian su mejor candidato para algortimo de bully
        System.out.println("\nAplicando Algortimo Bully...");
        Doctor candidato = personal.getMejorDoctor();
        main.candidatos.add(ipMaquina+";"+"Bully;"+String.valueOf(candidato.Estudios+candidato.Experiencia));
        EnviarCandidato(candidato,main,ipMaquina,cliente);
        
        //El coordinador espera el inicio de otras MV
        if(ipMaquina.equals("10.6.40.169")){
            Thread.sleep(10000);
            String mensaje = EscogerCoordinador(main);
            System.out.println("[Algoritmo Bully] Nuevo Coordinador con IP: "+mensaje.split(";")[0]);
            System.out.println("[Algoritmo Bully] Avisando resultado..");
            cliente.EnviarBroadcastN(mensaje,main.ipMaquina,main.listaip);
            if(mensaje.split(";")[0].equals(main.ipMaquina)){
                main.Is_Coordinador = true;
                main.ipCoordinador = mensaje.split(";")[0];
                System.out.println("[Algoritmo Bully] Esta maquina es el nuevo Coordinador");
            }
            else{
                main.Is_Coordinador = false;
                main.ipCoordinador = mensaje.split(";")[0];
            }
        }
        else{
            Thread.sleep(15000);
        }
        
        //PROCESAMIENTO ARCHIVOS
        
        
        //CREAR ARCHIVO LOG
        try{
        FileWriter archivolog = new FileWriter("Operaciones.log",false);
        archivolog.write("Operaciones Relativas a Maquina IP: "+main.ipMaquina+"\n\n");
        archivolog.close();
        }
        catch (IOException e){}

        //SI ES EL COORDINADOR ACTUAL
        if(main.Is_Coordinador){
            int turno = 0;
            while(true){
                if(turno == 0){ //Turno Coordinador
                    String operacion = ProcesarRequerimiento(main);
                    System.out.println("[Requerimiento] Enviando Broadcast con Requerimiento");
                    cliente.EnviarBroadcastN(ipMaquina+";LOG;"+operacion,main.ipMaquina,main.listaip);
                    System.out.println("[Requerimiento] LOCAL: Escribiendo en LOG");
                    EscribirLog(main,operacion);
                    turno++;
                }
                else{ //Turno maquinas No coordinadoras
                    System.out.println("[Requerimiento] Enviando Permiso turno: "+turno);
                    EnviarPermiso(ipMaquina+";PERMISO;",main.ipMaquina,turno-1,cliente,main.listaip);
                    Thread.sleep(10000); //Espera por log de maquina cliente
                    if(turno == 3){ //RESET DE CONTADOR DE TURNOS
                        turno = 0;
                    }
                    else{
                        turno++;
                    }
                }
            }
        }
        
        
        //SI NO ES EL COORDINADOR
        if(main.Is_Coordinador == false){
            while(true){
                while(main.permiso == false){ //Cuando llegue el permiso puede ejecutarse
                    System.out.println("[Requerimiento] Esperando Aviso Coordinador");
                    Thread.sleep(1000); //Espera por turno un segundo
                }
                System.out.println("[Requerimiento] Permiso Obtenido");
                String operacion = ProcesarRequerimiento(main);
                System.out.println("[Requerimiento] Enviando Requerimiento al Coordinador");
                EnviarACoordinador(main,main.ipMaquina+";R_LOG;"+operacion,cliente);
                main.permiso = false;
            }
        }
        
        
    }
    
    /**
     *
     * @return
     */
    
    public static void ProcesarLog(Main main,String mensaje) throws IOException{
        String cargo = mensaje.split(";")[2];
        String nombre = mensaje.split(";")[3];
        String ficha = mensaje.split(";")[4];
        String texto = mensaje.split(";")[5];
        
        for (int j=0;j<main.pacientes.Pacientes.size();j++){

                if(cargo.equals("Doctor")){
                    if(main.pacientes.Pacientes.get(j).Datos_Personales.Nombre.equals(nombre)){
                        if(ficha.equals("Procedimientos")){
                            main.pacientes.Pacientes.get(j).Procedimientos.Asignados.add(texto);
                        }
                        if(ficha.equals("Medicamentos")){
                            main.pacientes.Pacientes.get(j).Medicamentos.Recetados.add(texto);
                        }
                        if(ficha.equals("Examenes")){
                            main.pacientes.Pacientes.get(j).Examenes.No_Realizados.add(texto);
                        }
                    }
                }

                if(cargo.equals("Enfermero")){
                    if(main.pacientes.Pacientes.get(j).Datos_Personales.Nombre.equals(nombre)){
                        if(ficha.equals("Procedimientos")){
                            main.pacientes.Pacientes.get(j).Procedimientos.Completados.add(texto);
                        }
                        if(ficha.equals("Medicamentos")){
                            main.pacientes.Pacientes.get(j).Medicamentos.Suministrados.add(texto);
                        }
                    }
                }

                if(cargo.equals("Paramedico")){
                    if(main.pacientes.Pacientes.get(j).Datos_Personales.Nombre.equals(nombre)){
                        if(ficha.equals("Examenes")){
                            main.pacientes.Pacientes.get(j).Examenes.Realizados.add(texto);
                        }
                    }
                }
        }
        EscribirLog(main,cargo+";"+nombre+";"+ficha+";"+texto);
    }
    
    public static void EnviarACoordinador(Main main,String operacion,Cliente cliente) throws IOException{
        if(main.ipCoordinador.equals(main.listaip.M29.get(0))){
            cliente.EnviarIndividualN(operacion,main.ipCoordinador,Integer.parseInt(main.listaip.M29.get(1)));
        }
        if(main.ipCoordinador.equals(main.listaip.M30.get(0))){
            cliente.EnviarIndividualN(operacion,main.ipCoordinador,Integer.parseInt(main.listaip.M30.get(1)));
        }
        if(main.ipCoordinador.equals(main.listaip.M31.get(0))){
            cliente.EnviarIndividualN(operacion,main.ipCoordinador,Integer.parseInt(main.listaip.M31.get(1)));
        }
        if(main.ipCoordinador.equals(main.listaip.M32.get(0))){
            cliente.EnviarIndividualN(operacion,main.ipCoordinador,Integer.parseInt(main.listaip.M32.get(1)));
        }
    }
    
    public static void EscribirLog(Main main,String operacion) throws IOException{
        Date date = new Date();
        DateFormat hourformat = new SimpleDateFormat("EEEEE dd MMMMM yyyy HH:mm:ss");
        String fecha = hourformat.format(date);
        try (FileWriter escribir = new FileWriter("Operaciones.log",true)) {
            escribir.write("["+fecha+"] "+operacion+"\n");
        }
    }
    
    public static void EnviarPermiso(String mensaje,String ipmaquina,int turno,Cliente cliente,IP listaip) throws IOException{
        List<Socket> listasockets = cliente.CrearSocket(ipmaquina, listaip);
        cliente.EnviarIndividual(mensaje,listasockets.get(turno));
    }
    
    public static String ConsultarIPMaquina(){
        System.out.print("\nIngrese IP de la Maquina: ");
        Scanner in = new Scanner(System.in);
        String ip = in.nextLine();
        return ip;
    }
    
    public static void EnviarCandidato(Doctor candidato,Main main,String ipmaquina,Cliente cliente) throws IOException{
        if(ipmaquina.equals("10.6.40.169") == false){
            String experiencia = String.valueOf(candidato.getEstudios()+candidato.getExperiencia());
            System.out.println("[Algortimo Bully] Enviando Candidato a Coordinador...");
            cliente.EnviarIndividualN(ipmaquina+";"+"Bully;"+experiencia,"10.6.40.169",2900);
        }
    }
    
    public static void ProcesarMensaje(Main main,String mensaje,List<String> candidatos) throws IOException{
        if(mensaje.split(";")[1].equals("Bully")){
            candidatos.add(mensaje);
            String escogido = EscogerCoordinador(main);
        }
        if(mensaje.split(";")[1].equals("R_Bully")){
            System.out.println("[Algoritmo Bully] Resultado: Nuevo Coordinador con IP: "+mensaje.split(";")[0]);
            if(mensaje.split(";")[0].equals(main.ipMaquina)){
                main.Is_Coordinador = true;
                main.ipCoordinador = mensaje.split(";")[0];
                System.out.println("[Algoritmo Bully] Esta maquina es el nuevo Coordinador");
            }
            else{
                main.Is_Coordinador = false;
                main.ipCoordinador = mensaje.split(";")[0];
            }
        }
        if(mensaje.split(";")[1].equals("LOG")){
            ProcesarLog(main,mensaje);
        }
        if(mensaje.split(";")[1].equals("PERMISO")){
            main.permiso = true;
        }
        if(mensaje.split(";")[1].equals("R_LOG")){
            Cliente cliente = new Cliente();
            ProcesarLog(main,mensaje);
            String operacion = mensaje.split(";")[2]+";"+mensaje.split(";")[3]+";"+mensaje.split(";")[4]+";"+mensaje.split(";")[5];
            cliente.EnviarBroadcastN(main.ipMaquina+";LOG;"+operacion,main.ipMaquina,main.listaip);
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
    
    public static String ProcesarRequerimiento(Main main){
        Requerimiento requerimiento = null;
        Requerimiento.PacienteReq operacion = null;
        for(int i=0;i<main.requerimientos.Requerimientos.size();i++){
            if(main.requerimientos.Requerimientos.get(i).ListaPacientes.size() != 0){
                requerimiento = main.requerimientos.Requerimientos.get(i);
                operacion = main.requerimientos.Requerimientos.get(i).ListaPacientes.get(0);
                break;
            }
        }
        
        if(operacion == null){
            return null;
        }
        else{
        
            for (int j=0;j<main.pacientes.Pacientes.size();j++){

                if(requerimiento.Cargo.equals("Doctor")){
                    if(main.pacientes.Pacientes.get(j).Datos_Personales.Nombre.equals(operacion.Nombre)){
                        if(operacion.Ficha.equals("Procedimientos")){
                            main.pacientes.Pacientes.get(j).Procedimientos.Asignados.add(operacion.Texto);
                        }
                        if(operacion.Ficha.equals("Medicamentos")){
                            main.pacientes.Pacientes.get(j).Medicamentos.Recetados.add(operacion.Texto);
                        }
                        if(operacion.Ficha.equals("Examenes")){
                            main.pacientes.Pacientes.get(j).Examenes.No_Realizados.add(operacion.Texto);
                        }
                    }
                    main.requerimientos.Requerimientos.get(j).ListaPacientes.remove(0);
                }

                if(requerimiento.Cargo.equals("Enfermero")){
                    if(main.pacientes.Pacientes.get(j).Datos_Personales.Nombre.equals(operacion.Nombre)){
                        if(operacion.Ficha.equals("Procedimientos")){
                            main.pacientes.Pacientes.get(j).Procedimientos.Completados.add(operacion.Texto);
                        }
                        if(operacion.Ficha.equals("Medicamentos")){
                            main.pacientes.Pacientes.get(j).Medicamentos.Suministrados.add(operacion.Texto);
                        }
                    }
                    main.requerimientos.Requerimientos.get(j).ListaPacientes.remove(0);
                }

                if(requerimiento.Cargo.equals("Paramedico")){
                    if(main.pacientes.Pacientes.get(j).Datos_Personales.Nombre.equals(operacion.Nombre)){
                        if(operacion.Ficha.equals("Examenes")){
                            main.pacientes.Pacientes.get(j).Examenes.Realizados.add(operacion.Texto);
                        }
                    }
                    main.requerimientos.Requerimientos.get(j).ListaPacientes.remove(0);
                }

            }
            
            return requerimiento.Cargo+";"+operacion.Nombre+";"+operacion.Ficha+";"+operacion.Texto;
        }
        
    }
    
    
    @Override
    public void run(){
        try {
            ServerSocket servidor = new ServerSocket(this.servidor.puerto);
            while(true){
                Socket socket = servidor.accept();
                DataInputStream mensaje = new DataInputStream(socket.getInputStream());
                String data = mensaje.readUTF();
                System.out.println("\nSERVIDOR: "+data+"\n");
                ProcesarMensaje(this,data,this.candidatos);
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
