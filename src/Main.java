
import java.io.IOException;

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
    }
    
}
