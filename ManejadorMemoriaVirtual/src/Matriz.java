import java.util.ArrayList;

public class Matriz {

    public int numeroFilas;

    public int numeroColumnas;

    public ArrayList<Fila> filas;


    public static void setTamanoEntero(int pTamanoEntero){
        Entero.setTamanoEntero(pTamanoEntero);
    }

    public Matriz(int pNumeroFilas, int pNumeroColumnas) throws Exception{

        numeroColumnas = pNumeroColumnas;

        numeroFilas = pNumeroFilas;

        filas = new ArrayList<Fila>(numeroFilas);
        for(int i=0; i<numeroFilas; i++){
            filas.set(i, new Fila(numeroColumnas));
        }
    }

    public Matriz() throws Exception{
        
    }

    public String darReferencias(String nombreMatriz){
        String res="";
        for(int i = 0; i<numeroFilas; i++){
            res+= filas.get(i).darReferencias(nombreMatriz, i);
        }
        return res;
    }

    public long get(int i, int j){
        return filas.get(i).get(j);
    }

    public void set(int i, int j, long valor){
        filas.get(i).set(j,valor);
    }


}
