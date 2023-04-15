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
        Aplicacion.log("  Asignacion "+darReferencia(),false);
    }

    public Entero(int numeroPagina,int  desplazamiento) throws Exception{
        numeroPaginaReferencia = numeroPagina;
        desplazamientoReferencia = desplazamiento;
        MemoriaVirtual.checkPagina(numeroPaginaReferencia);

    }

    public long get(){
        return MemoriaVirtual.get(numeroPaginaReferencia,desplazamientoReferencia);
    }

    public void set(long valor){
        MemoriaVirtual.set(numeroPaginaReferencia,desplazamientoReferencia,valor);
    }

    public String darReferencia(){
        return numeroPaginaReferencia+","+desplazamientoReferencia;
    }
    
}
