import java.util.ArrayList;

public class Matriz {

    public int numeroFilas;

    public int numeroColumnas;

    public ArrayList<Fila> filas;

    public String id;


    public static void setTamanoEntero(int pTamanoEntero){
        Entero.setTamanoEntero(pTamanoEntero);
    }

    public Matriz(int pNumeroFilas, int pNumeroColumnas, String pId) throws Exception{

        Aplicacion.log("Asignando Matriz "+pId,false);

        numeroColumnas = pNumeroColumnas;

        numeroFilas = pNumeroFilas;

        filas = new ArrayList<Fila>(numeroFilas);
        for(int i=0; i<numeroFilas; i++){
            Aplicacion.log("> Asignando Fila "+i,false);
            filas.add(new Fila(numeroColumnas));
        }

        id = pId;
    }

    public Matriz(String pId) throws Exception{
        filas = new ArrayList<Fila>();
        id = pId;
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
