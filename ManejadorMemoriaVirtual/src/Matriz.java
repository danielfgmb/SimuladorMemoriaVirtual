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
            filas.add(new Fila(numeroColumnas));
        }
    }

    public Matriz() throws Exception{
        filas = new ArrayList<Fila>();
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

    public void cargarReferencia(int filaR, int columnaR, int paginaR, int desplazamientoR) throws Exception{
        if(filaR>=filas.size()){
            for(int i=filas.size(); i<=filaR; i++){
                filas.add(new Fila());
            }
        }
        filas.get(filaR).cargarReferencia(columnaR, paginaR, desplazamientoR);
    }


}
