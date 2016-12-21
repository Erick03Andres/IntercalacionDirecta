/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ordnaturalcsv;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import interfaz.Prueba;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author David
 */
public class OrdenNaturalBooleanos {
     public OrdenNaturalBooleanos() throws Exception{
            
        String nombre_archivo = Prueba.nombreArchivo;
        System.out.println("Ordenando Boooleanos");
        ordenar(nombre_archivo);
    }
    
     public int contarRegistros(int seleccion) {
        int index = 0;
        if (seleccion == 1) {

            index = 0;
            CsvReader archivo = null;
            try {
                archivo = new CsvReader("Auxiliar1.csv");

                while (archivo.readRecord()) {
                    index += 1;
                }
                archivo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }

        if (seleccion == 2) {
            index = 0;
            CsvReader archivo = null;
            try {
                archivo = new CsvReader("Auxiliar2.csv");

                while (archivo.readRecord()) {
                    index += 1;
                }
                archivo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
        return index;
    }

   

    public void ordenar(String nombreArchivo) throws Exception {
       
        int index = 0;
        while (particion(nombreArchivo, "Auxiliar1.csv", "Auxiliar2.csv")) {
            fusion(nombreArchivo, "Auxiliar1.csv", "Auxiliar2.csv");
        }
    }

    //Metodo para generar particiones de secuencias
    public boolean particion(String nombreArchivo, String archivo1, String archivo2) {

        //Se utilizara una logica similar a la del metodo de verificar orden
        //por lo que los indices son declarados de la misma manera
        String actual = null;
        String anterior = null;
        String get1=null, get2=null, get3=null;
        //Variable para controlar el indice del archivo al cual se va a escribir.
        //El archivo en cuestion es declarado dentro de un arreglo de archivos
        int indexOutputStream = 0;

        //Variable que determina si existe un cambio de secuencia en el ordenamiento
        boolean hayCambioDeSecuencia = false;

        //Declaracion de los objetos asociados a los archivos y del arreglo de archivos
        //que sirven para las particiones
        CsvWriter Auxiliares[] = new CsvWriter[2];
        //DataInputStream dis = null;
        CsvReader Archivo = null;
     
        try {
            //Abre o crea los archivos
           
            Auxiliares[0] = new CsvWriter(new FileWriter(archivo1, false), ',');//TRUE
            Auxiliares[1] = new CsvWriter(new FileWriter(archivo2, false), ',');//TRUE
            Archivo = new CsvReader(nombreArchivo);

            //Primero, verifica si existen datos en el archivo que se va a leer
            while (Archivo.readRecord()) {
                //Utiliza la misma logica para las variables que almacenan los datos
                //que en el metodo de la verificacion del orden
                anterior = actual;
                actual = Archivo.get(2);
                get1=Archivo.get(0);
                get2=Archivo.get(1);
                get3=Archivo.get(3);
                
                
                if (anterior == null) {
                    anterior = actual;
                }

                //Cambio de secuencia. Manipulacion del indice del arreglo de archivos
                if (anterior.compareTo(actual) > 0) {
                    indexOutputStream = indexOutputStream == 0 ? 1 : 0;
                    //Actualizacion de la variable booleana, esto indica la existencia
                    //de un cambio de secuencia
                    hayCambioDeSecuencia = true;
                }

                //Imprimir el dato contenido en actual y escribirlo en el archivo correspondiente
                //System.out.println(indexOutputStream + ") "+ actual);
                //System.out.println(get1+"->"+actual+"->"+get2+"->"+get3);
                Auxiliares[indexOutputStream].write(get1);
                Auxiliares[indexOutputStream].write(get2);
                Auxiliares[indexOutputStream].write(actual);
                Auxiliares[indexOutputStream].write(get3);
                Auxiliares[indexOutputStream].endRecord();
               
            }
            Archivo.close();
            Auxiliares[0].close();
            Auxiliares[1].close();
        } catch (FileNotFoundException e) {
            System.out.println("Error lectura/escritura");
        } catch (IOException e) {
            System.out.println("Error en la creacion o apertura del archivo 1");
        }
        //El valor retornado sirve para determinar cuando existe una particion
        return hayCambioDeSecuencia;
    }

    //Metodo de fusion de los datos obtenidos en el metodo de particion
    public void fusion(String nombreArchivo, String archivo1, String archivo2) {
        //Variables para almacenar los datos de los archivos
        //que contienen las particiones
        String[] actual = new String[2];
        String[] anterior = new String[2];
        
        String get11=null,get12=null,get13=null;
        String get21=null,get22=null,get33=null;
        boolean[] finArchivo = new boolean[2];
        int indexArchivo = 0;
        CsvReader Auxiliares[] = new CsvReader[2];
        CsvWriter Archivo = null;

     

        try {
        

            int contAux1 = contarRegistros(1);
            int contAux2 = contarRegistros(2);
          
            Auxiliares[0] = new CsvReader(archivo1);
       
            Auxiliares[1] = new CsvReader(archivo2);
           
            Archivo = new CsvWriter(new FileWriter(nombreArchivo, false), ',');
           
            while (contAux1 > 0 && contAux2 > 0) {

                // 1era vez: inicializar con la primera palabra de cada archivo
                if (anterior[0] == null && anterior[1] == null) {
                    Auxiliares[0].readRecord();
                    anterior[0] = actual[0] = Auxiliares[0].get(2);
                    get11=Auxiliares[0].get(0);
                    get12=Auxiliares[0].get(1);
                    get13=Auxiliares[0].get(3);
                    
                    contAux1--;
                    Auxiliares[1].readRecord();
                    anterior[1] = actual[1] = Auxiliares[1].get(2);
                    get21=Auxiliares[1].get(0);
                    get22=Auxiliares[1].get(1);
                    get33=Auxiliares[1].get(3);
                    contAux2--;
                }

                // al inicio del procesamiento de dos secuencias, anterior y
                // actual apuntan a la primer palabra de cada secuencia.
                anterior[0] = actual[0];
                anterior[1] = actual[1];

                // mezclamos las dos secuencias hasta que una acaba
                while (anterior[0].compareTo(actual[0]) <= 0 &&  anterior[1].compareTo(actual[1]) <= 0)
                {
                    
                    indexArchivo = (actual[0].compareTo(actual[1]) <= 0) ? 0 : 1;
                    
                    if (indexArchivo!=1){
                        Archivo.write(get11);
                        Archivo.write(get12);
                        Archivo.write(actual[0]);
                        Archivo.write(get13);
                        Archivo.endRecord();
                    }
                    else{
                        Archivo.write(get21);
                        Archivo.write(get22);
                        Archivo.write(actual[1]);
                        Archivo.write(get33);
                        Archivo.endRecord(); 
                    }
                   // Archivo.write(actual[indexArchivo]);
                   
                 
                    anterior[indexArchivo] = actual[indexArchivo];
                 //   Archivo.endRecord();
                    // salir del while cuando no haya datos, pero ya procesamos
                    // el ultimo nombre del archivo
                    if(indexArchivo==0){
                        if (contAux1>0) {
                            Auxiliares[0].readRecord();
                            actual[0] = Auxiliares[0].get(2);
                            get11=Auxiliares[0].get(0);
                            get12=Auxiliares[0].get(1);
                            get13=Auxiliares[0].get(3);
                            
                            contAux1--;
                        } else {
                            finArchivo[0] = true;
                            break;
                        }
                    }
                    if(indexArchivo==1){
                        if (contAux2>0) {
                            Auxiliares[1].readRecord();
                            actual[1] = Auxiliares[1].get(2);
                            get21=Auxiliares[1].get(0);
                            get22=Auxiliares[1].get(1);
                            get33=Auxiliares[1].get(3);
                            contAux2--;
                        } else {
                            finArchivo[1] = true;
                            break;
                        }
                    }
                    

                }

                // en este punto indexArchivo nos dice que archivo causo
                // que salieramos del while anterior, por lo que tenemos
                // que purgar el otro archivo
               // indexArchivo = indexArchivo == 0 ? 1 : 0;

                if(indexArchivo == 0 ){
                    while (anterior[1].compareTo(actual[1]) <= 0) {
                        Archivo.write(get21);
                        Archivo.write(get22);
                        Archivo.write(actual[1]);
                        Archivo.write(get33);
                        anterior[1] = actual[1];
                        Archivo.endRecord();
                        if (contAux2>0) {
                            Auxiliares[1].readRecord();
                            actual[1] = Auxiliares[1].get(2);
                            get21=Auxiliares[1].get(0);
                            get22=Auxiliares[1].get(1);
                            get33=Auxiliares[1].get(3);
                            contAux2--;
                        } else {
                            finArchivo[1] = true;
                            break;
                        }
                    }
                }
                if(indexArchivo == 1){
                    while (anterior[0].compareTo(actual[0]) <= 0) {
                        Archivo.write(get11);
                        Archivo.write(get12);
                        Archivo.write(actual[0]);
                        Archivo.write(get13);
                        anterior[0] = actual[0];
                        Archivo.endRecord();
                        if (contAux1>0) {
                            Auxiliares[0].readRecord();
                            actual[0] = Auxiliares[0].get(2);
                            get11=Auxiliares[0].get(0);
                            get12=Auxiliares[0].get(1);
                            get13=Auxiliares[0].get(3);
                            contAux1--;
                        } else {
                            finArchivo[0] = true;
                            break;
                        }
                    }
                }
                
                
            }

            // purgar los dos archivos en caso de que alguna secuencia
            // haya quedado sola al final del archivo.
            // Para salir del while anterior alguno de los 2 archivos
            // debio terminar, por lo que a lo mas uno de los dos whiles
            // siguientes se ejecutara
            if (!finArchivo[0]) {
                Archivo.write(get11);
                Archivo.write(get12);
                Archivo.write(actual[0]);
                Archivo.write(get13);
                Archivo.endRecord();
                while (contAux1>0) {
                    Auxiliares[0].readRecord();
                    Archivo.write(Auxiliares[0].get(0));
                    Archivo.write(Auxiliares[0].get(1));
                    Archivo.write(Auxiliares[0].get(2));
                    Archivo.write(Auxiliares[0].get(3));
                    Archivo.endRecord();
                    contAux1--;
                }
            }

            if (!finArchivo[1]) {
                Archivo.write(get21);
                Archivo.write(get22);
                Archivo.write(actual[1]);
                Archivo.write(get33);
                Archivo.endRecord();
                while (contAux2>0) {
                    Auxiliares[1].readRecord();
                    Archivo.write(Auxiliares[1].get(0));
                    Archivo.write(Auxiliares[1].get(1));
                    Archivo.write(Auxiliares[1].get(2));
                    Archivo.write(Auxiliares[1].get(3));
                    Archivo.endRecord();
                    contAux2--;
                }
            }
            Auxiliares[0].close();
            Auxiliares[1].close();
            Archivo.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
     
     
}
