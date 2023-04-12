public class Entero {

    public int numeroPaginaReferencia;

    public int desplazamientoReferencia;

    public static int tamanoEntero;

    public static void setTamanoEntero(int pTamanoEntero){
        tamanoEntero = pTamanoEntero;
    }

    public Entero() throws Exception{
        int[] referencia = MemoriaVirtual.reservarMemoriaDiscoConsecutivamente(tamanoEntero);
        numeroPaginaReferencia = referencia[0];
        desplazamientoReferencia = referencia[1];
    }

    public long get(){
        return MemoriaVirtual.get(numeroPaginaReferencia,desplazamientoReferencia,tamanoEntero);
    }

    public void set(long valor){
        MemoriaVirtual.set(numeroPaginaReferencia,desplazamientoReferencia,valor,tamanoEntero);
    }

    public String darReferencias(String nombreMatriz, int fila, int columna){
        return "["+nombreMatriz+"-"+fila+"-"+columna+"],"+numeroPaginaReferencia+","+desplazamientoReferencia;
    }
    
}