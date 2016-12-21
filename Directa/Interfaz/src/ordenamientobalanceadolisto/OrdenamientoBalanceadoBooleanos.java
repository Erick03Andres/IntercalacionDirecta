/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ordenamientobalanceadolisto;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import interfaz.Prueba;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author David
 */
public class OrdenamientoBalanceadoBooleanos {
    static final int N=6;           //Archivos Auxiliares
    static final int N2=N/2;        //Constante de Entrada 
    
    static File f0;
    static File []f=new File[N];    //Archivos Auxiliares
    
    static final int NumReg=20;
    static final String tope="Zzzzzzzzzzzzzzzzzzzzzzz";

    
    public OrdenamientoBalanceadoBooleanos() throws FileNotFoundException {
         String [] nombreF={"ar1.csv", "ar2.csv", "ar3.csv", "ar4.csv", "ar5.csv", "ar6.csv"};
        
        f0=new File(Prueba.nombreArchivo);
        CsvReader flujo = new CsvReader(new FileReader(f0), ',');
        /// crea N archivosAux
        for(int i=0; i<N; i++)
        {
            f[i]=new File(nombreF[i]);
        }
       
        
        try
        {
          
          System.out.println("Archivo Original...");
            //escribir(f0);
            mezclaEqMple();
        }
        catch(IOException e)
        {
            System.out.println("Error de entrada/salida en la ordenacion");
            e.printStackTrace();
        }
    }
    
     public static void mezclaEqMple() throws FileNotFoundException, IOException
    {
        int i, j, k, k1, t;
        String anterior="";
        int [] c=new int[N];
        int []cd=new int[N];
        String [] r=new String[N2];
        
        Object [] flujos=new Object[N];
         
        //DataInputStream flujoEntradaActual=null;
        CsvReader flujoEntradaActual =null;
        //DataOutputStream flujoSalidaActual=null;
        CsvWriter flujoSalidaActual =null ;
        boolean[] actvs=new boolean[N2];
        String get1="";
        String get2="";
        String get3="";
        
         //t=distribuir();  
        try
        {
            t=distribuir();
            for(i=0; i<N; i++)
            {
                c[i]=i;
            }
            do
            {
                //System.out.println("\t\tCUANTAS VECES\n");
                k1=(t<N2)?t:N2;
                for(i=0; i<k1; i++)
                {
                    //flujos[c[i]]=new DataInputStream(new BufferedInputStream(new FileInputStream(f[c[i]])));
                    flujos[c[i]] = new CsvReader(new FileReader(f[c[i]]), ',');
                    cd[i]=c[i];
                }
                
                j=N2;
                t=0;
                //archivos de salida
                for(i=j; i<N; i++)
                {
                    
                    flujos[c[i]] = new CsvWriter(new FileWriter(f[c[i]]), ',');
                }
                
                for(int n=0; n<k1; n++)
                {
                   
                    flujoEntradaActual=(CsvReader)flujos[cd[n]];
                    if(flujoEntradaActual.readRecord()){
                        
                        r[n]=(flujoEntradaActual.get(2));
                        get1=flujoEntradaActual.get(0);
                        get2=flujoEntradaActual.get(1);
                        get3=flujoEntradaActual.get(3);
                        //System.out.println("DATOS AUXILIARES:"+r[n]);
                    }
                  
                }
                
                while(k1>0)
                {
                    t++;            //mezcla de otro tramo
                    for(i=0; i<k1; i++)
                    {
                        actvs[i]=true;
                    }
                    //flujoSalidaActual=(DataOutputStream)flujos[c[j]];
                    flujoSalidaActual=(CsvWriter)flujos[c[j]];
                    while(!finDeTramos(actvs, k1))
                    {
                        //System.out.println("Infinito");
                        int n;
                        n=minimo(r, actvs, k1);
                        //flujoEntradaActual=(DataInputStream)flujos[cd[n]];
                        
                        flujoEntradaActual=(CsvReader)flujos[cd[n]];
                        get1=flujoEntradaActual.get(0);
                        get2=flujoEntradaActual.get(1);
                        get3=flujoEntradaActual.get(3);
                       
                        //flujoSalidaActual.writeInt(r[n]);
                        
                        flujoSalidaActual.write(get1);
                        flujoSalidaActual.write(get2);
                        flujoSalidaActual.write(String.valueOf(r[n]));
                        flujoSalidaActual.write(get3);
                        flujoSalidaActual.endRecord();
                        //System.out.println("ORDEN:"+r[n]);
                        anterior = r[n];
                        
                       
                            //r[n]=flujoEntradaActual.readInt();
                            if(flujoEntradaActual.readRecord()){
                                r[n]=(flujoEntradaActual.get(2));
                                get1=flujoEntradaActual.get(0);
                                get2=flujoEntradaActual.get(1);
                                get3=flujoEntradaActual.get(3);
                               
                             
                                if(anterior.compareTo(r[n])>0){
                                    actvs[n]=false;
                                }
                            }
                            
                            else{
                               k1--;
                                flujoEntradaActual.close();
                                cd[n]=cd[k1];
                                r[n]=r[k1];
                                actvs[n]=actvs[k1];
                                actvs[k1]=false; 
                            }
                    }
                    j=(j<N-1)? j+1: N2;
                }
                for(i=N2; i<N; i++)
                {
                    
                    flujoSalidaActual=(CsvWriter)flujos[c[i]];
                    flujoSalidaActual.close();
                }
                for(i=0; i<N2; i++)
                {
                    int a;
                    a=c[i];
                    c[i]=c[i+N2];
                    c[i+N2]=a;
                }
            }
            while(t>1);
            System.out.println("Archivo Ordenado de Booleanos...");
            escribir(f[c[0]]);
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static int distribuir() throws IOException
    {
        int  j, nt;
        //int clave;
        String anterior="";
        String clave="";
        String get1="";
        String get2="";
        String get3="";
       
        CsvReader flujo = new CsvReader(new FileReader(f0), ',');
 
        CsvWriter[] flujoSalida= new CsvWriter[N2];
        
        for(j=0; j<N2; j++)
        {
            //flujoSalida[j]=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f[j])));
            flujoSalida[j]=new CsvWriter(new FileWriter(f[j]), ',');
        }
        
        anterior="a";
        clave=anterior+1;
        j=0;
        nt=0;
        //boolean is_end = flujo.readRecord();
        //try
        //{   
            while(flujo.readRecord())
            {
                //clave=flujo.readInt();
                
                clave=(flujo.get(2));
                get1=flujo.get(0);
                get2=flujo.get(1);
                get3=flujo.get(3);
                
                while(anterior.compareTo(clave)<=0 && flujo.readRecord()==true )
                { 
                    //flujoSalida[j].writeInt(clave);
                    
                    flujoSalida[j].write(get1);
                    flujoSalida[j].write(get2);
                    flujoSalida[j].write(clave);
                    flujoSalida[j].write(get3);
                    flujoSalida[j].endRecord();
                    anterior=clave;
                    //clave=flujo.readInt();
                    clave=(flujo.get(2));
                    get1=flujo.get(0);
                    get2=flujo.get(1);
                    get3=flujo.get(3);
                    //System.out.println("CLAVE dentro:"+clave);
                    
                   // is_end=flujo.readRecord();
                    //System.out.println("Infinito");
                   
                }
                nt++;
                j=(j<N2-1)?j+1:0;
                //flujoSalida[j].writeInt(clave);
                
                flujoSalida[j].write(get1);
                flujoSalida[j].write(get2);
                flujoSalida[j].write(clave);
                flujoSalida[j].write(get3);
                
                flujoSalida[j].endRecord();
                
                anterior=clave;
                
            }
        //}catch(EOFException e)
        //{
            nt++;
            System.out.println("Numero de tramos: " + nt);
            flujo.close();
            
            for(j=0; j<N2; j++)
            {
                flujoSalida[j].close();
            }
            return nt;
        //}
    }
    
    private static int minimo(String []r, boolean[]activo, int n)
    {
        int i, indice;
        String m;
        
        i=indice=0;
        //m=tope+1;
        m=tope;
        
        for(;i<n; i++)
        {
            if(activo[i]&&r[i].compareTo(m)<0)
            {
                m=r[i];
                indice=i;
            }
            /*if(r[i]<m)
            {
                m=r[i];
                indice=i;
            }*/
        }
        return indice;
    }
    
    
    private static boolean finDeTramos(boolean []activo, int n)
    {
        boolean s=true;
        
        for(int k=0; k<n; k++)
        {
            if(activo[k]) s=false;
        }
        return s;
    }
    
    static void escribir(File f)
    {
        int clave, k;
        boolean mas=true;
        //DataInputStream flujo=null;
        CsvReader flujo=null;
        try
        {
            //flujo=new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
            flujo= new CsvReader(new FileReader(f),',');
            k=0;
            boolean is_end = flujo.readRecord();
            
            while(is_end)
            {   
                k++;
                //System.out.println(flujo.readInt() + " ");
                System.out.println(flujo.get(2) + " ");
               // if(k%19==0)
                   // System.out.println();
                is_end = flujo.readRecord();
                
            }
            System.out.println("Hay "+k+" Elementos");
        }catch(IOException e)
        {
            System.out.println("Fin del Archivo");
            if(e instanceof EOFException) flujo.close();
        }
    }
}
