import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Aplicacion {

    public static Matriz A;

    public static Matriz B;

    public static Matriz C;

    public static int numeroFilas;

    public static int numeroColumnas;

    public static int numeroMarcos;

    public static String archivoParametros;

    public static String archivoEscritura1;

    public static String archivoLecturaReferencias;

    public static boolean logActivado = false;

    public static String logFile="";

    public static String seleccion = "";

    public static AlgoritmoEnvejecimiento algoritmo;

    public static void main(String[] args) throws Exception {

        inicio();
        
        if(seleccion.equals("2")){ // correr los proceso 1
            procesoUno(archivoParametros,archivoEscritura1);
        }
        else if(seleccion.equals("3")) // correr proceso 2
        {
            procesoDos(archivoLecturaReferencias,numeroMarcos);
        }
        else{ // correr proceso 1 y 2
            procesoUno(archivoParametros,archivoEscritura1);
            procesoDos(archivoLecturaReferencias,numeroMarcos);
        }
    }


    public static void procesoUno(String direccionLectura, String direccionEscritura) throws Exception{
        File archivo = new File(direccionLectura);
		int tamanoPagina = 0;
		int tamanoEntero = 0;

        Aplicacion.log("Inicio PROCESO UNO",true);

        String log="";

        // lectura de parametros
		try (FileReader lector = new FileReader(archivo); BufferedReader br = new BufferedReader(lector);) {
			
			String filasStr = br.readLine();
			numeroFilas = Integer.parseInt(filasStr.substring(3));
            log+="Numero de filas: "+numeroFilas+"\n";
			
			String colsStr = br.readLine();
			numeroColumnas = Integer.parseInt(colsStr.substring(3));
            log+="Numero de columnas: "+numeroColumnas+"\n";
			
			String bytesIntStr = br.readLine();
			tamanoEntero = Integer.parseInt(bytesIntStr.substring(3));
            log+="Tamano de entero: "+tamanoEntero+"\n";
			
			String sizePagStr = br.readLine();
			tamanoPagina = Integer.parseInt(sizePagStr.substring(3));
            log+="Tamano de pagina: "+tamanoPagina+"\n";
			
			String NumMarcosStr = br.readLine();
			numeroMarcos = Integer.parseInt(NumMarcosStr.substring(3));
            log+="Numero de marcos: "+numeroMarcos;

            Aplicacion.log(log, true);
		}
        catch (IOException e) {
            e.printStackTrace();
        }

        MemoriaVirtual.inicializar(tamanoPagina, numeroMarcos);

        // lo debo setear previo a la inicializacion por que de esa manera cuenta cuanto ocupa
        Matriz.setTamanoEntero(tamanoEntero);

        // inicializo las matrices, aqui le asigno todas las referencias
        A = new Matriz(numeroFilas, numeroColumnas,"A");
        B = new Matriz(numeroFilas, numeroColumnas,"B");
        C = new Matriz(numeroFilas, numeroColumnas,"C");

        // archivo para guardar resultado proceso 1
		File archivoEscritura = new File(direccionEscritura);
		FileWriter escritor = new FileWriter(archivoEscritura);

        // guardar los parametros iniciales
        escritor.write("TP="+tamanoPagina +"\n");
		escritor.write("NF="+numeroFilas+"\n");
		escritor.write("NC="+numeroColumnas+"\n");
        int numRefs = numeroFilas*numeroColumnas*3;
		escritor.write("NR="+numRefs);

        String imprimir="";

        // este recorrido se hace así para que queden intercaladas
        for(int i=0; i<numeroFilas; i++)
            for(int j=0;j<numeroColumnas; j++)
                for (Matriz matriz : new Matriz[]{A,B,C}) 
                    imprimir+="\n["+matriz.id+"-"+i+"-"+j+"],"+matriz.filas.get(i).enteros.get(j).darReferencia();

        escritor.write(imprimir);
        escritor.close();
    }

    private static void procesoDos(String nombreArchivo, int marcos) throws Exception{

        Aplicacion.log("Inicio PROCESO DOS", true);
        Aplicacion.log("cPG:"+MemoriaVirtual.numeroPaginas+" "+MemoriaVirtual.tablaPaginas.size(), true);
        lecturaProcesoDos(nombreArchivo, numeroMarcos);
        Aplicacion.log("cPG:"+MemoriaVirtual.numeroPaginas+" "+MemoriaVirtual.tablaPaginas.size(),true);
        algoritmo = new AlgoritmoEnvejecimiento();
        algoritmo.start();

        recorrido1();
        Aplicacion.log("Termino thread principal",true);
        Aplicacion.log("Fallos de página "+MemoriaVirtual.cantidadFallosPagina,true);

    }

    private static void lecturaProcesoDos(String nombreArchivo, int marcos) throws Exception{

        File archivo = new File(nombreArchivo);
        int tamanoPagina = 0;
        int numeroReferencias = 0;

        // leer archivo para referencias
        try (FileReader lector = new FileReader(archivo); BufferedReader br = new BufferedReader(lector);) {
            
            String tamanoPaginaStr = br.readLine();
            tamanoPagina = Integer.parseInt(tamanoPaginaStr.substring(3));
            
            String filasStr = br.readLine();
            numeroFilas = Integer.parseInt(filasStr.substring(3));
            
            String colsStr = br.readLine();
            numeroColumnas = Integer.parseInt(colsStr.substring(3));
            
            String numRefsStr = br.readLine();
            numeroReferencias = Integer.parseInt(numRefsStr.substring(3));
        
            
            //inicializa memoria con el tamaño de pagina y los marcos, el numero de páginas aumentará 
            MemoriaVirtual.inicializar(tamanoPagina, numeroMarcos);
            

            // crea las matrices pero sin el tamaño, se asgigna dinamicamente con las referencias
            A = new Matriz("A");
            B = new Matriz("B");
            C = new Matriz("C");

            for(int i=0; i<numeroReferencias; i++){
                // cada linea es una referencia
                String[] referencias = br.readLine().split(",");

                // la matriz que le corresponde
                String[] datosMatriz = referencias[0].replace("[","").replace("]","").split("-");
                
                String matriz = datosMatriz[0];

                int filaR =Integer.parseInt(datosMatriz[1]);
                int columnaR = Integer.parseInt(datosMatriz[2]);
                int paginaR =Integer.parseInt(referencias[1]);
                int desplazamientoR = Integer.parseInt(referencias[2]);

                if(matriz.equals("A")){
                    A.cargarReferencia(filaR, columnaR, paginaR, desplazamientoR);
                }
                else if(matriz.equals("B")){
                    B.cargarReferencia(filaR, columnaR, paginaR, desplazamientoR);
                }
                else if(matriz.equals("C")){
                    C.cargarReferencia(filaR, columnaR, paginaR, desplazamientoR);
                }
            } 
        }
        catch (IOException e) {
            e.printStackTrace();
        }
            
    }

    public static void recorrido1(){
        for(int i=0; i<numeroFilas; i++){
            for( int j=0; j<numeroColumnas; j++){
                Aplicacion.log("Realizando Suma C["+i+","+j+"]=A+B",false);
                C.set(i,j, A.get(i,j) + B.get(i,j));
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

        String datos = "";

        datos += formatDiv("a----------------------------------------------------------------c\n");
        datos += formatRow("| Ingrese que proceso desea correr                               |\n");
        datos += formatDiv("d----------------------------------------------------------------f\n");
        datos += formatRow("| 1. Proceso uno y dos                                           |\n");
        datos += formatRow("| 2. Proceso uno                                                 |\n");
        datos += formatRow("| 3. Proceso dos                                                 |\n");
        datos += formatDiv("d----------------------------------------------------------------f\n");
        System.out.print(datos);
        System.out.print(formatRow("| RTA:  "));
        Scanner sc = new Scanner(System.in);
        seleccion = sc.nextLine();
        System.out.print(formatDiv("g----------------------------------------------------------------i\n\n"));   


        datos = "";
        datos += formatDiv("a----------------------------------------------------------------c\n");
        datos += formatRow("| Ingrese los datos (deje vacío para configuracion por defecto)  |\n");
        datos += formatDiv("d----------------------------------------------------------------f\n");
        System.out.print(datos);

        String activarLog = "n";

        if(seleccion.equals("1")){
            System.out.print(formatRow("| NOMBRE DE ARCHIVO DE CONFIGURACION (SEGUIR ORDEN FORMATO       |\n"));
            System.out.print(formatRow("| DE EJEMPLO parametros.txt -> NF, NC, TE, TP, MP número marcos) |\n"));
            System.out.print(formatRow("| Rta:  "));
            archivoParametros = sc.nextLine();
            if(archivoParametros.equals("")){
                archivoParametros = "parametros.txt";
            }
            System.out.print(formatDiv("d----------------------------------------------------------------f\n"));
            System.out.print(formatRow("| NOMBRE DE ARCHIVO DE SALIDA ProcesoUno:  "));
            archivoEscritura1 = sc.nextLine();
            if(archivoEscritura1.equals("")){
                archivoEscritura1 = "proceso1.txt";
            }
            archivoLecturaReferencias = archivoEscritura1;
            
        }

        else if(seleccion.equals("2")){
            System.out.print(formatRow("| NOMBRE DE ARCHIVO DE CONFIGURACION (SEGUIR ORDEN FORMATO       |\n"));
            System.out.print(formatRow("| DE EJEMPLO parametros.txt -> NF, NC, TE, TP, MP número marcos) |\n"));
            System.out.print(formatRow("| Rta:  "));
            archivoParametros = sc.nextLine();
            if(archivoParametros.equals("")){
                archivoParametros = "parametros.txt";
            }
            System.out.print(formatDiv("d----------------------------------------------------------------f\n"));
            System.out.print(formatRow("| NOMBRE DE ARCHIVO DE SALIDA ProcesoUno:  "));
            archivoEscritura1 = sc.nextLine();
            if(archivoEscritura1.equals("")){
                archivoEscritura1 = "proceso1.txt";
            }
            archivoLecturaReferencias = archivoEscritura1;
            
        }

        else if(seleccion.equals("3")){
            System.out.print(formatRow("| NOMBRE DE ARCHIVO DE REFERENCIAS (ProcesoUno):  "));
            archivoLecturaReferencias = sc.nextLine();
            if(archivoEscritura1.equals("")){
                archivoEscritura1 = "proceso1.txt";
            }
            archivoLecturaReferencias = archivoEscritura1;
            System.out.print(formatDiv("d----------------------------------------------------------------f\n"));
            System.out.print(formatRow("| NÚMERO DE MARCOS:  "));
            numeroMarcos = Integer.parseInt(sc.nextLine());
            
           
        }
        System.out.print(formatDiv("d----------------------------------------------------------------f\n"));
        System.out.print(formatRow("| Los logs se imprimen en consola y se guardan en la capeta Logs |\n"));
        System.out.print(formatRow("| Debido a la cantidad de mensajes pudiera afectar el tiempo de  |\n"));
        System.out.print(formatRow("| los ciclos de reloj y afectar en el número de fallos de pág    |\n"));
        System.out.print(formatRow("| pero muestran detalladamente el proceso de reemplazo           |\n"));
        System.out.print(formatRow("| ¿DESEA ACTIVAR LOG? (S/N):  "));
        activarLog = sc.nextLine();

        if(activarLog.equals("s")||activarLog.equals("S")){
            logActivado = true;
        }
        System.out.print(formatDiv("g----------------------------------------------------------------i\n\n"));   
        
        sc.close();
        
    }


    // METODOS PARA IMPRESIÓN Y LOG

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

    public static synchronized void log(String log, boolean urgent){
        if(logActivado){
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
        else if(urgent){
            String toLog = formatDiv("----------------------------------------------------")+"[]\n"+log;
            System.out.println(toLog);
        }
    
        
    }

    


    



}
