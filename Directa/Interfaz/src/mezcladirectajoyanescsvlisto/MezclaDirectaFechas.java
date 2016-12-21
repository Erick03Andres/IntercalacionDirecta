/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mezcladirectajoyanescsvlisto;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import interfaz.Prueba;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author David
 */
public class MezclaDirectaFechas {
     public MezclaDirectaFechas() throws IOException, ParseException {
        File fich = new File(Prueba.nombreArchivo);
        System.out.println("Ordenando Fechas");
        //escribir(fich);
        mezclaDirecta(fich);
        System.out.println("Archivo Ordenado Fechas");
    }
     
    
     
    public  void mezclaDirecta(File f) throws IOException, ParseException {
        int longSec;
        int numReg = 0;
        //int numReg;
        String cad;
        /// Leer el numero de registros
        CsvReader bf = new CsvReader(new FileReader(f), ',');
        boolean is_end = bf.readRecord();
        while(is_end){
            numReg++;
            is_end = bf.readRecord();
        }
        
        
 
        File f1 = new File("ArchivoAux1.csv");
        File f2= new File("ArchivoAux2.csv");
        /* número de registros se obtiene dividiendo el tamaño
        del archivo por el tamaño del registro: 4.
        */
        //numReg = (int)(f.length()/4)-1;
        System.out.println("Num registros:->"+numReg);
        longSec = 1;
        while (longSec < numReg)
        {
            distribuir(f, f1, f2, longSec, numReg);
            //System.out.println("ENTRANDO..");
            mezclar(f1, f2, f, longSec, numReg);
            longSec *= 2;
            
        }
    }
    public void distribuir(File f, File f1, File f2,int longSec, int numReg) throws IOException{
        int numSec, resto, i;
        
        CsvReader bf = new CsvReader(new FileReader(f), ',');
        CsvWriter pw1 = new CsvWriter(new FileWriter(f1),',');
        CsvWriter pw2 = new CsvWriter(new FileWriter(f2), ',');
        
        numSec = numReg /(2*longSec);
        resto = numReg %(2*longSec);
       
        for (i = 1; i <= numSec; i++)
        {
            subSecuencia(bf, pw1, longSec);
            subSecuencia(bf, pw2, longSec);
        }
        /*
        Se procesa el resto de registros del archivo
        */
        if (resto > longSec)
            resto -= longSec;
        else
        {
            longSec = resto;
            resto = 0;
        }
        subSecuencia(bf, pw1, longSec);
        subSecuencia(bf, pw2, resto);
        bf.close();
        pw1.close();
        pw2.close();
        
    }
    
     public void subSecuencia(CsvReader f, CsvWriter t,int longSec) throws IOException
    {   
        String clave;
      
        for (int j = 1; j <= longSec; j++)
        {
            boolean is_end = f.readRecord();
            //System.out.println("PRIMER VALOR ENTERO:"+clave);
            t.write(f.get(0));
            t.write(f.get(1));
            t.write(f.get(2));
            t.write(f.get(3));
            t.endRecord();
        
        }
    }
     
     
   public void mezclar(File f1, File f2, File f,int lonSec, int numReg) throws IOException, ParseException
  {
        
        int numSec, resto, i, j, k;
        String clave1 ="",clave2 = "";
        String getClave1="",getClave2="",getClave3="";
        numSec = numReg /(2*lonSec); // número de subsecuencias
        resto = numReg %(2*lonSec);
        
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha1;
        Date fecha2;
        
        
        /*FileReader f11= new FileReader(f1);
        FileReader f21= new FileReader(f2);
        BufferedReader bf1 = new BufferedReader(f11);
        BufferedReader bf2 = new BufferedReader(f21);
        FileWriter fichero = new FileWriter(f);
        PrintWriter pw = new PrintWriter(fichero);
        */
        
        CsvReader bf1 = new CsvReader(new FileReader(f1), ',');
        CsvReader bf2 = new CsvReader(new FileReader(f2), ',');
        CsvWriter pw = new CsvWriter(new FileWriter(f), ',');
        
        //claves iniciales
       
            
        boolean is_end =bf1.readRecord();
        
        //System.out.println("Mostrando: "+ primerValor);
        clave1=bf1.get(3);
        fecha1= formateador.parse(clave1);
       
        boolean is_end2 = bf2.readRecord();
        clave2=bf2.get(3);
        fecha2= formateador.parse(clave2);
           
            //bucle para controlar todo el proceso de mezcla
            for (int s = 1; s <= numSec+1; s++)
            {
                int n1, n2;
                n1 = n2 = lonSec;
                if (s == numSec+1)
                { // proceso de subsecuencia incompleta
                    if (resto > lonSec)
                        n2 = resto - lonSec;
                    else
                    {
                        n1 = resto;
                        n2 = 0;
                    }
                }
                i = j = 1;
                while (i <= n1 && j <= n2)
                {
                    String clave;
                    if (fecha1.compareTo(fecha2) <0)
                    {
                        clave = clave1;
                        getClave1=bf1.get(0);
                        getClave2=bf1.get(1);
                        getClave3=bf1.get(2);
                        
                        try {
                            //String clave1aux= bf1.readLine();
                              boolean auxend1=bf1.readRecord();
                              String clave1aux= bf1.get(3);
                              
                            if(auxend1!=false)
                                clave1= clave1aux;
                                fecha1= formateador.parse(clave1);
                        }
                        catch(EOFException e){;}
                        i++;
                    }
                    else
                    {
                        clave = clave2;
                        getClave1=bf2.get(0);
                        getClave2=bf2.get(1);
                        getClave3=bf2.get(2);
                        try {
                            //String clave2aux= bf2.readLine();
                            
                             boolean auxend2=bf2.readRecord();
                             String clave2aux= bf2.get(3);
                             
                            if(auxend2 !=false)
                                clave2= clave2aux;
                                fecha2= formateador.parse(clave2);
                        }
                        catch(EOFException e){;}
                        j++;
                    }
                    //System.out.println("\tordenando1:"+clave);
                    //pw.println(clave);
                    pw.write(getClave1);
                    pw.write(getClave2);
                    pw.write(getClave3);
                    pw.write(clave);
                    pw.endRecord();
                    //flujo.writeInt(clave);

                }
                /*
                Los registros no procesados se escriben directamente
                */
                for (k = i; k <= n1; k++)
                {   
                    //System.out.println("\tordenando2:"+clave1);
                    //pw.println(clave1);
                    
                    getClave1=bf1.get(0);
                    getClave2=bf1.get(1);
                    getClave3=bf1.get(2);
                    pw.write(getClave1);
                    pw.write(getClave2);
                    pw.write(getClave3);
                    pw.write(clave1);
                    pw.endRecord();
                    //flujo.writeInt(clave1);
                    try {
                    //clave1 = flujo1.readInt();}
                        //String clave1aux= bf1.readLine();
                            boolean auxend1=bf1.readRecord();
                            String clave1aux= bf1.get(3);
                            if(auxend1!=false)
                                clave1= clave1aux;
                                fecha1= formateador.parse(clave1);
                    }
                    catch(EOFException e){;}
                }
                for (k = j; k <= n2; k++)
                {
                    //System.out.println("\tordenando3:"+clave2);
                    
                    getClave1=bf2.get(0);
                    getClave2=bf2.get(1);
                    getClave3=bf2.get(2);
                    pw.write(getClave1);
                    pw.write(getClave2);
                    pw.write(getClave3);
                    pw.write(clave2);
                    pw.endRecord();
                    //pw.println(clave2);
                    //flujo.writeInt(clave2);
                    try {
                        //clave2 = flujo2.readInt();
                        //String clave2aux= bf2.readLine();
                        boolean auxend2=bf2.readRecord();
                        String clave2aux= bf2.get(3);
                        if(auxend2!=false)
                           clave2= clave2aux;
                           fecha2= formateador.parse(clave2);
                    }
                    catch(EOFException e){;}
                }
            }
        bf1.close();
        bf2.close();
        pw.close();
        //fichero.close();
        //f11.close();
        //f21.close();
} 
}
