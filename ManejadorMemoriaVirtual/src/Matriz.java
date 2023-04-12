public class Matriz {

    public int numeroFilas;

    public int numeroColumnas;

    public Fila[] filas;

    public static void setTamanoEntero(int pTamanoEntero){
        Entero.setTamanoEntero(pTamanoEntero);
    }

    public Matriz(int pNumeroFilas, int pNumeroColumnas) throws Exception{

        numeroColumnas = pNumeroColumnas;

        numeroFilas = pNumeroFilas;

        filas = new Fila[numeroFilas];
        for(int i=0; i<numeroFilas; i++){
            filas[i] = new Fila(numeroColumnas);
        }
    }

    public String darReferencias(String nombreMatriz){
        String res="";
        for(int i = 0; i<numeroFilas; i++){
            res+= filas[i].darReferencias(nombreMatriz, i);
        }
        return res;
    }

    public long get(int i, int j){
        return filas[i].get(j);
    }

    public void set(int i, int j, long valor){
        filas[i].set(j,valor);
    }


}
