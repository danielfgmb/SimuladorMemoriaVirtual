public class Fila {

    public static int tamanoEntero;

    public int numeroColumnas;

    public int numeroPaginaReferencia;

    public int desplazamientoReferencia;

    public static void setTamanoEntero(int pTamanoEntero){
        tamanoEntero = pTamanoEntero;
    }

    public Fila(int pNumeroColumnas) throws Exception{
        numeroColumnas = pNumeroColumnas;
        int[] referencia = MemoriaVirtual.reservarMemoriaDiscoConsecutivamente(numeroColumnas*tamanoEntero);
        numeroPaginaReferencia = referencia[0];
        desplazamientoReferencia = referencia[1];
    }

    public String darReferencias(String nombreMatriz, int fila){
        String res ="";
        for(int j=0; j<numeroColumnas; j++){
            // es lo que se
            res+="["+nombreMatriz+"-"+fila+"-"+j+"],"+numeroPaginaReferencia+","+(desplazamientoReferencia+j*tamanoEntero)+"\n";
        }
        return res;
    }

    public int get(int j){
        int desplazamiento = desplazamientoReferencia + j * tamanoEntero;
        return MemoriaVirtual.get(numeroPaginaReferencia,desplazamiento);
    }

    public void set(int j, int valor){
        int desplazamiento = desplazamientoReferencia + j * tamanoEntero;
        MemoriaVirtual.set(numeroPaginaReferencia,desplazamiento,valor,tamanoEntero);
    }


    
}
