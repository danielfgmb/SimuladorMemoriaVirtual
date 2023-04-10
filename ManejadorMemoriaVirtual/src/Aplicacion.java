
public class Aplicacion {

    public static Matriz A;

    public static Matriz B;

    public static Matriz C;

    public static int numeroFilas;

    public static int numeroColumnas;

    public static void main(String[] args) throws Exception {
        inicio();
        numeroFilas = 2;
        numeroColumnas = 2;
        int tamanoEntero = 4;
        int tamanoPagina = 12;
        int marcosPagina = 8;

        MemoriaVirtual.inicializar(tamanoPagina, marcosPagina);

        Matriz.setTamanoEntero(tamanoEntero);

        A = new Matriz(numeroFilas, numeroColumnas);
        B = new Matriz(numeroFilas, numeroColumnas);
        C = new Matriz(numeroFilas, numeroColumnas);

        String imprimir = A.darReferencias("A")+B.darReferencias("B")+C.darReferencias("C");
        System.out.print(imprimir+"xs");
        
        AlgoritmoEnvejecimiento algotitmo = new AlgoritmoEnvejecimiento();
        algotitmo.start();
        recorrido1();
        System.out.println("TERMINE");
        System.out.println(MemoriaVirtual.cantidadFallosPagina);


    }

    public static void recorrido1(){
        for(int i=0; i<numeroFilas; i++){
            for( int j=0; j<numeroColumnas; j++){
                C.set(i,j, A.get(i,j) + B.get(i,j));
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
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


}
