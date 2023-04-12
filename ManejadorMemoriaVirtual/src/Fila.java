public class Fila {

    public int numeroColumnas;

    public Entero[] enteros;

    public Fila(int pNumeroColumnas) throws Exception{

        numeroColumnas = pNumeroColumnas;

        enteros = new Entero[numeroColumnas];

        for(int i=0; i<numeroColumnas; i++){
            enteros[i] = new Entero();
        }
        
    }

    public String darReferencias(String nombreMatriz, int fila){
        String res ="";
        for(int j=0; j<numeroColumnas; j++){
            res+=enteros[j].darReferencias(nombreMatriz, fila, j);
            if(j<numeroColumnas)
                res+="\n";
        }
        return res;
    }

    public long get(int j){
        return enteros[j].get();
    }

    public void set(int j, long valor){
        enteros[j].set(valor);
    }


    
}
