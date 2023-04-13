import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Aplicacion {

    public static Matriz A;

    public static Matriz B;

    public static Matriz C;

    public static int numeroFilas;

    public static int numeroColumnas;

    public static int marcosPagina;


    public static String logFile="";

    public static AlgoritmoEnvejecimiento algoritmo;

    public static void main(String[] args) throws Exception {

        inicio();
        ProcesoUno("parametros.txt");
        lecturaProcesoDos("procesoUno.txt");
        algoritmo = new AlgoritmoEnvejecimiento();
        algoritmo.start();
        recorrido1();
        Aplicacion.log("Termino thread principal");
        Aplicacion.log("Fallos de página "+MemoriaVirtual.cantidadFallosPagina);
        
        
        

    }

    private static void lecturaProcesoDos(String string) throws Exception{
        File archivo = new File(string);
        int tamanoPagina = 0;
        int numeroFilas = 0;
        int numeroColumnas = 0;
        int numeroReferencias = 0;
        try (FileReader lector = new FileReader(archivo); BufferedReader br = new BufferedReader(lector);) {
            
            String tamanoPaginaStr = br.readLine();
            tamanoPagina = Integer.parseInt(tamanoPaginaStr.substring(3));
            
            String filasStr = br.readLine();
            numeroFilas = Integer.parseInt(filasStr.substring(3));
            
            String colsStr = br.readLine();
            numeroColumnas = Integer.parseInt(colsStr.substring(3));
            
            String numRefsStr = br.readLine();
            numeroReferencias = Integer.parseInt(numRefsStr.substring(3));

            String[] referencias = new String[numeroReferencias];
            for(int i=0; i<numeroReferencias; i++){
                referencias[i] = br.readLine();
            }
            MemoriaVirtual.inicializar(tamanoPagina, marcosPagina);
            if(referencias.length>3){
                int tamanioEntero = Integer.parseInt(referencias[3].split(",")[2]);
                Matriz.setTamanoEntero(tamanioEntero);
            }
            A = new Matriz(numeroFilas, numeroColumnas);
            B = new Matriz(numeroFilas, numeroColumnas);
            C = new Matriz(numeroFilas, numeroColumnas);
            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ProcesoUno(String direccion) throws Exception
    {
        File archivo = new File(direccion);
		int tamanoPagina = 0;
		int tamanoEntero = 0;
		try (FileReader lector = new FileReader(archivo); BufferedReader br = new BufferedReader(lector);) {
			
			String filasStr = br.readLine();
			numeroFilas = Integer.parseInt(filasStr);
			
			String colsStr = br.readLine();
			numeroColumnas = Integer.parseInt(colsStr);
			
			String bytesIntStr = br.readLine();
			tamanoEntero = Integer.parseInt(bytesIntStr);
			
			String sizePagStr = br.readLine();
			tamanoPagina = Integer.parseInt(sizePagStr);
			
			String NumMarcosStr = br.readLine();
			marcosPagina = Integer.parseInt(NumMarcosStr);


		}
        catch (IOException e) {
            e.printStackTrace();
        }
        

        MemoriaVirtual.inicializar(tamanoPagina, marcosPagina);

        Matriz.setTamanoEntero(tamanoEntero);

        A = new Matriz(numeroFilas, numeroColumnas);
        B = new Matriz(numeroFilas, numeroColumnas);
        C = new Matriz(numeroFilas, numeroColumnas);

        String imprimir = A.darReferencias("A")+B.darReferencias("B")+C.darReferencias("C");
        String direccionIntermedio = "procesoUno.txt";
		File archivoIntermedio = new File(direccionIntermedio);
		FileWriter escritorIntermedio = new FileWriter(archivoIntermedio);
        escritorIntermedio.write(imprimir);
        escritorIntermedio.close();
        String corregido = metodoCambiarString(direccionIntermedio);
        System.out.print("EYYYYYYY"+imprimir+"EYYYYYY\n");
        String direccionResultado = "procesoUno.txt";
		File archivoResultado = new File(direccionResultado);
		FileWriter escritor = new FileWriter(archivoResultado);
		escritor.write("TP="+tamanoPagina +"\n");
		escritor.write("NF="+numeroFilas+"\n");
		escritor.write("NC="+numeroColumnas+"\n");
        int numRefs = numeroFilas*numeroColumnas*3;
		escritor.write("NR="+numRefs+"\n");
		escritor.write(corregido);
        escritor.close();

        
        

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
        header += formatRow("|           Diego Gonzalez            |         202110240        |\n");
        header += formatDiv("d-------------------------------------e--------------------------f\n");
        header += formatRow("|    Daniel Fernando Gomez Barrera    |         201728920        |\n");
        header += formatDiv("d-------------------------------------e--------------------------f\n");
        header += formatRow("|           Juan Montealegre          |         202012723        |\n");
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

    public static String metodoCambiarString(String direccion){
        File archivo = new File(direccion);
		int tamanoPagina = 0;
		int tamanoEntero = 0;
        String corregido = "";
        ArrayList<String> listaA = new ArrayList<String>();
        ArrayList<String> listaB = new ArrayList<String>();
        ArrayList<String> listaC = new ArrayList<String>();
		try (FileReader lector = new FileReader(archivo); BufferedReader br = new BufferedReader(lector);) {
			String renglon = br.readLine();
            while(renglon != null)
            {
                if(Character.toString(renglon.charAt(1)).equals("A"))
                {
                    listaA.add(renglon);
                }
                else if(Character.toString(renglon.charAt(1)).equals("B"))
                {
                    listaB.add(renglon);
                }
                else if(Character.toString(renglon.charAt(1)).equals("C"))
                {
                    listaC.add(renglon);

                }
                renglon = br.readLine();

            }
            for(int i = 0; i<listaA.size(); i++)
            {
                corregido += listaA.get(i)+"\n";
                corregido += listaB.get(i)+"\n";
                corregido += listaC.get(i)+"\n";
            }
			
			


		}
        catch (IOException e) {
            e.printStackTrace();
        }
        return corregido;

        

    }



}
