import java.util.ArrayList;

public class MemoriaVirtual {

    public static ArrayList<Integer> tablaPaginas; // sensible a cambio

    public static ArrayList<Integer> tablaMarcos; // sensible a cambio

    public static boolean[] marcosOcupados; // sensible a cambio

    public static int tamanoPagina;

    public static int numeroMarcosPagina;

    public static int numeroPaginas; // sensible a est

    // indica hasta qye byte está ocupado cada pagina
    // esto significa que aunque en memoria todo cambie lo que nunca cambia es como la pagina en el interior por eso solo necesitamos
    // la info de hasta que byte esta ocupado
    public static ArrayList<Integer> paginaByteOcupado; // sensible a cambios

    // contadores para algoritmo envejecimiento
    public static ArrayList<Byte> contadoresMarco; // sensible a cambios

    // bitsR para algoritmo de envejecimiento
    public static ArrayList<Boolean> bitsR; // sensible a cambios

    // valores para hacer rapido la asignacion de memoria
    public static int ultimaPagina;
    // inicia en 0 así que para compara con tamaños toca +1, el anterior igual
    public static int ultimoDesplazamiento;

    // variable final en donde se guarda el numero de fallos de página por ejecución
    public static int cantidadFallosPagina=0;




    public static void inicializar(int pTamanoPagina, int pNumeroMarcosPagina){

        // se establece dinámicamente al hacer las referencias, pero inicia en la cantidad de marcos que hay
        numeroPaginas = pNumeroMarcosPagina;

        ultimaPagina = 0;

        ultimoDesplazamiento = -1;

        numeroMarcosPagina = pNumeroMarcosPagina;

        marcosOcupados = new boolean[numeroMarcosPagina];

        tamanoPagina = pTamanoPagina;

        paginaByteOcupado = new ArrayList<>();

        contadoresMarco = new ArrayList<>();

        bitsR = new ArrayList<>();

        tablaPaginas = new ArrayList<>();

        tablaMarcos = new ArrayList<>();

        for(int i = 0; i<numeroMarcosPagina; i++){

            // al principio es -1 porque no hay lleno nada
            paginaByteOcupado.add(-1);

            contadoresMarco.add((byte) 0);

         
            bitsR.add(false);

            // al principio nada se almacena en memoria real
            tablaPaginas.add(-1);

            tablaMarcos.add(-1);
        }

    }

    // necesariamente lo mete en memoria disco por requerimiento 
    // TODO: Preguntar!!!
    // pongo consecutivamente porque así es rápido pero no es como lo haría realmente una maquina que le toca buscar el espacio libre
    // pongo disco porque eso decían al principio todo en disco
    public synchronized static int[] reservarMemoriaDiscoConsecutivamente(int tamanoBytes) throws Exception{

        if(tamanoBytes>tamanoPagina){
            throw new Exception("No es posible reservar un tamano en bytes más grande que la página");
        }

        int[] respuesta = new int[2];

        // lo puedo colocar en la ultima pagina llenada 
        // se le suma el 1 porque comparamos en tamanos y por eso tambien el <=
        if(tamanoBytes + (ultimoDesplazamiento + 1) <= tamanoPagina){
            // se le dice que hasta ahí esta ocupada esa pagina
            // si el último desplzamiento es 0 quiere decir que la posición ocupada es 0 por eso no hay que restar 1
            respuesta[0] = ultimaPagina;
            respuesta[1] = ultimoDesplazamiento+1;
            ultimoDesplazamiento = paginaByteOcupado.get(ultimaPagina) + tamanoBytes;
            paginaByteOcupado.set(ultimaPagina, ultimoDesplazamiento); 
        }
        else{
            
            // primer caso, con las paginas que tengo es suficiente
            if(ultimaPagina+1<numeroPaginas){
                ultimaPagina +=1;
                ultimoDesplazamiento = tamanoBytes-1;
                paginaByteOcupado.set(ultimaPagina, ultimoDesplazamiento);
            }
            // segundo caso, no alcanza y me toca hacer una página nueva, más peasado de todos
            else{
                ultimoDesplazamiento = tamanoBytes-1;
                ultimaPagina+=1;
                paginaByteOcupado.add(ultimoDesplazamiento);
                tablaPaginas.add(-1);
                numeroPaginas+=1;                
            }
            respuesta[0] = ultimaPagina;
            respuesta[1] = 0;
        }
        return respuesta;
    }

    public static long get(int pagina, int desplazamiento){

        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
        }

        
        Aplicacion.log("Llamado GET a PG: "+pagina+" DESPLAZ:"+desplazamiento,false);

        // hay fallo de pagina
        if(tablaPaginas.get(pagina)==-1){
            cantidadFallosPagina++;
            Aplicacion.log("Fallo de página! ",false);
            colocarPaginaMemoria(pagina);
        }

        // se cambian los bit de referencias
        // lo hago despues por que si hubo un fallo de pagina no se podría colocar el bitR para el marco
        reportarAccesoRBits(pagina);

        

        return (long) (Math.random() *10) ;
    }

    public static void set(int pagina, int desplazamiento, long valor){

        Aplicacion.log("Llamado GET a PG: "+pagina+" DESPLAZ:"+desplazamiento,false);

        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
        }
    

        if(tablaPaginas.get(pagina)==-1){
            cantidadFallosPagina++;
            colocarPaginaMemoria(pagina);
        }

        // se cambian los bit de referencias
        // lo hago despues por que si hubo un fallo de pagina no se podría colocar el bitR para el marco
        reportarAccesoRBits(pagina);

       
    }

    // es sincronizado por contadoresPaginas que lo accede tanto el algoritmo como aca
    public static synchronized void colocarPaginaMemoria(int pagina){
        // primero mirar si hay marcos libres
        boolean encontreMarcoLibre = false;

        String log = "";
        log+="Inicia búsqueda de Marcos Libres\n";

        for(int i = 0; i<marcosOcupados.length && !encontreMarcoLibre; i++){
            if(!marcosOcupados[i]){
                encontreMarcoLibre=true;
                // a la pagina se le coloca el numero de marco
                log+=">>> Encontré marco libre "+i+" para la pagina "+pagina+" hacemos reemplazo";
                tablaPaginas.set(pagina,i);
                marcosOcupados[i] = true;
                tablaMarcos.set(i, pagina);
            }
        }

        // aqui sucede el reemplazo
        if(!encontreMarcoLibre){
            log+=">>> No encontré marco libre\n";
            log+="Inicia búsqueda en marcos llenos\n";
            // segun NFU seleccionar página con mayor contador
            int min = -1;
            int indice = 0;
            int numeroPaginaCandidata=0;

            for(int i = 0; i < numeroPaginas; i++){
                // primero verifico si es cantidato (debe estar en memoria real)
                int numeroMarco = tablaPaginas.get(i);
                if(numeroMarco!=-1){
                    
                    // calculo valor con los contadores
                    // la operacion & 0xFF es para que qued unsigned es decir solo sea positivo
                    int valor = contadoresMarco.get(numeroMarco) & 0xFF;

                    if(valor<min || min == -1){
                        min=valor;
                        indice = numeroMarco;
                        numeroPaginaCandidata = i;
                    }
                }
            }

            log+=">>> El marco idóneo es el "+indice+" con contador "+min+"\n";

            // aqui ya tenemos el idoneo, hacemos el cambio
            // 0. encontramos el marco en donde esta la pagina candidata
            int marcoDisponible = indice;
            // 1. pasamos la antigua pagina a disco, no modificamos marco dspponible porque nunca va a dejar de estar llena
            tablaPaginas.set(numeroPaginaCandidata, -1);
            // 2. ponemos la nueva pagina en memoria
            tablaPaginas.set(pagina, marcoDisponible);
            tablaMarcos.set(marcoDisponible, pagina);
            // 3. reiniciamos los contadores para el marco en la pagina
            log+=">>> Se reinicia el contador del marco";
            contadoresMarco.set(marcoDisponible, (byte) 0);
        }

        Aplicacion.log(log,false);
    }

    public static synchronized void reportarAccesoRBits(int pagina){
        int marco = tablaPaginas.get(pagina);

        String log = "Actualizando bitR PG: "+pagina+" MC: "+marco+"\n>>> bitsR Antes: "+estadoRBits()+"\n";
        
        if(marco != -1){
            bitsR.set(marco, true);
        }
        
        log+=">>> bitsR despues: "+estadoRBits();
        Aplicacion.log(log,false);
    }

    public static synchronized String estadoRBits(){
        String res = "";
        for(int i=0;i<bitsR.size(); i++){
            if(bitsR.get(i))
                res+=1;
            else
                res+=0;
        }
        return res;
    }

    public static synchronized void reiniciarRBits(){
        for(int i = 0; i<numeroMarcosPagina; i++){
            bitsR.set(i, false);
        }
    }

    public int getTamanioPagina(){
        return tablaPaginas.size();
    }

    public static void checkPagina(int numeroPaginaReferencia) {
        if(numeroPaginaReferencia>=numeroPaginas){
            numeroPaginas=numeroPaginaReferencia+1;
        }
        if(numeroPaginaReferencia>=tablaPaginas.size()){
            for(int i = tablaPaginas.size(); i<=numeroPaginaReferencia; i++){
                tablaPaginas.add(-1);
            }
        }
    }

    
}
