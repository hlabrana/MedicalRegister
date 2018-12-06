
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hlabrana
 */
public class Paciente {
    public DatosPersonales Datos_Personales;
    public List<String> Procedimientos;
    public List<String> Examenes;
    public List<String> Medicamentos;
    
    public Paciente (){
        this.Procedimientos= new ArrayList();
        this.Examenes= new ArrayList();
        this.Medicamentos= new ArrayList();
    }
    
    public List<String> getProcedimientos(){
        return this.Procedimientos;
    }
    public List<String> getExamenes(){
        return this.Examenes;
    }
    public List<String> getMedicamentos(){
        return this.Medicamentos;
    }
    
    public void SetProcedimientos (String procedure){
        this.Procedimientos.add(procedure);
    }
    public void SetExamenes (String procedure){
        this.Examenes.add(procedure);
    }
    public void SetMedicamentos (String procedure){
        this.Medicamentos.add(procedure);
    }

    public static class DatosPersonales {
        public String Nombre;
        public String Rut;
        public int Edad;
        public List<String> Alergias;
        public List<String> Enfermedades;
        
        public DatosPersonales() {
        }

        public String getNombre(){
            return this.Nombre;
        }
        public String getRut(){
            return this.Rut;
        }
        public int getEdad(){
            return this.Edad;
        }
        public List<String> getAlergias(){
            return this.Alergias;
        }
        public List<String> getEnfermedades(){
            return this.Enfermedades;
        }
      
    }
}
