import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Aplicacion {

    public static Matriz A;

    public static Matriz B;

    public static Matriz C;

    public static int numeroFilas;

    public static int numeroColumnas;

    public static String logFile="";

    public static AlgoritmoEnvejecimiento algoritmo;

    public static void main(String[] args) throws Exception {
        inicio();
        numeroFilas = 4;
        numeroColumnas = 4;
        int tamanoEntero = 4;
        int tamanoPagina = 8;
        int marcosPagina = 16;

        MemoriaVirtual.inicializar(tamanoPagina, marcosPagina);

        Matriz.setTamanoEntero(tamanoEntero);

        A = new Matriz(numeroFilas, numeroColumnas);
        B = new Matriz(numeroFilas, numeroColumnas);
        C = new Matriz(numeroFilas, numeroColumnas);

        String imprimir = A.darReferencias("A")+B.darReferencias("B")+C.darReferencias("C");

        System.out.print(imprimir+"\n");
        
        algoritmo = new AlgoritmoEnvejecimiento();
        algoritmo.start();
        recorrido1();
        Aplicacion.log("Termino thread principal");
        Aplicacion.log("Fallos de página "+MemoriaVirtual.cantidadFallosPagina);


    }

    public static void recorrido1(){
        for(int i=0; i<numeroFilas; i++){
            for( int j=0; j<numeroColumnas; j++){
                Aplicacion.log("Realizando Suma C["+i+","+j+"]=A+B");

                C.set(i,j, A.get(i,j) + B.get(i,j));

                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                }
            }
        }
        algoritmo.stopX();
    }


    public static void inicio(){
        String header = "\n";
        header += formatDiv("a----------------------------------------------------------------c\n");
        header += formatRow("|                    CASO 2:  Memoria Virtual                    |\n");
        header += formatRow("|              Infraestructura Computacional 2023-1              |\n");
        header += formatRow("|                    Profesor: Ricardo Gómez                     |\n");
        header += formatDiv("d-------------------------------------b--------------------------f\n");
        header += formatRow("|          NOMBRE INTEGRANTE          |    CóDIGO INTEGRANTE     |\n");
        header += formatDiv("d-------------------------------------e--------------------------f\n");
        header += formatRow("|           Diego Gonzalez            |                          |\n");
        header += formatDiv("d-------------------------------------e--------------------------f\n");
        header += formatRow("|    Daniel Fernando Gomez Barrera    |         201728920        |\n");
        header += formatDiv("d-------------------------------------e--------------------------f\n");
        header += formatRow("|           Juan Maldonado            |                          |\n");
        header += formatDiv("g-------------------------------------h--------------------------i\n"); 
        System.out.println(header);
        
    }



    public void modo1(){

    }

    public void modo2(){

    }

    // IMPRESIÓN

    public static String formatDiv(String str)
    {
        return str.replace('a', '\u250c')
                .replace('b', '\u252c')
                .replace('c', '\u2510')
                .replace('d', '\u251c')
                .replace('e', '\u253c')
                .replace('f', '\u2524')
                .replace('g', '\u2514')
                .replace('h', '\u2534')
                .replace('i', '\u2518')
                .replace('-', '\u2500');
    }

    public static String formatRow(String str)
    {
        return str.replace('|', '\u2502');
    }

    public static synchronized void log(String log){

        
        int year = Year.now().getValue();
        Date date = new Date();   
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date); 
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int second = calendar.get(Calendar.SECOND);
        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);       
        int month =calendar.get(Calendar.MONTH)+1;  
        int milisecond = calendar.get(Calendar.MILLISECOND);
        String prefix = year+"-"+month+"-"+day+"-"+hour+"-"+minute+"-"+second+"-"+milisecond;

        if(logFile.equals("")){
            try {
                try{
                    Files.createDirectories(Paths.get("Logs/"));
                }
                catch(Exception e){
                    // si el directorio ya esta creado
                }
                
                logFile = "Logs/"+prefix+"-log.txt"; 
                File file = new File(logFile);
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(logFile, true));
            output.newLine();
            String toLog = formatDiv("----------------------------------------------------")+"["+prefix+"]\n"+log;
            String toLogW = "----------------------------------------------------"+"["+prefix+"]\n"+log;
            output.write(toLogW);
            System.out.println(toLog);
            output.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
