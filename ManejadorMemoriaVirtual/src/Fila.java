import java.util.ArrayList;

public class Fila {

    public int numeroColumnas;

    public ArrayList<Entero> enteros;

    public Fila(int pNumeroColumnas) throws Exception{

        numeroColumnas = pNumeroColumnas;

        enteros = new ArrayList<Entero>(numeroColumnas);

        for(int i=0; i<numeroColumnas; i++){
            Aplicacion.log(" > Asignando Entero/Columna "+i,false);
            enteros.add(new Entero());
        }
        
    }

    public Fila() throws Exception{
        enteros = new ArrayList<Entero>();
    }

    public long get(int j){
        return enteros.get(j).get();
    }

    public void set(int j, long valor){
        enteros.get(j).set(valor);
    }

    public void cargarReferencia(int columnaR, int paginaR, int desplazamientoR) throws Exception {
        if(columnaR+1>enteros.size()){
            for(int i=enteros.size(); i<columnaR+1; i++){
                enteros.add(null);
            }
        }
        enteros.set(columnaR, new Entero(paginaR, desplazamientoR));
    }


    
}
