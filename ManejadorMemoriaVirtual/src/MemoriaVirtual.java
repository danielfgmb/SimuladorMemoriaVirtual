import java.util.ArrayList;

public class MemoriaVirtual {

    public static ArrayList<Integer> tablaPaginas; // sensible a cambio

    public static ArrayList<Integer> tablaMarcos; // sensible a cambio

    public static boolean[] marcosOcupados; // sensible a cambio

    public static int tamanoPagina;

    public static int numeroMarcosPagina;

    public static int numeroPaginas; // sensible a est

    // no supe como llamarle pero es para aprovechar el tamano de las páginas al máximo
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

    public static int cantidadFallosPagina;


    public static void inicializar(int pTamanoPagina, int pNumeroMarcosPagina){

        // se establece dinámicamente al hacer las referencias, pero inicia en la cantidad de marcos que hay
        numeroPaginas = pNumeroMarcosPagina;

        ultimaPagina = 0;

        ultimoDesplazamiento = -1;

        // al principio no hay 
        //numeroPaginasDisco = 0;

        numeroMarcosPagina = pNumeroMarcosPagina;

        marcosOcupados = new boolean[numeroMarcosPagina];

        tamanoPagina = pTamanoPagina;

        //memoriaReal = new ArrayList<>();

        //memoriaDisco = new ArrayList<>();

        paginaByteOcupado = new ArrayList<>();

        contadoresMarco = new ArrayList<>();

        bitsR = new ArrayList<>();

        tablaPaginas = new ArrayList<>();

        tablaMarcos = new ArrayList<>();

        //paginasMemoria = new ArrayList<>();

        for(int i = 0; i<numeroMarcosPagina; i++){

            // tamaño de la memoria real reservado
            //memoriaReal.add(new byte[tamanoPagina]);

            // al principio es -1 porque no hay lleno nada
            paginaByteOcupado.add(-1);

            contadoresMarco.add((byte) 0);

         
            bitsR.add(false);

            // al principio nada se almacena en memoria real
            tablaPaginas.add(-1);

            tablaMarcos.add(-1);

            //paginasMemoria.add(false);

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

        System.out.println("----------");
        System.out.println("ultPagPrev "+ultimaPagina+"ultimoDesplaPrev "+ultimoDesplazamiento);

        // lo puedo colocar en la ultima pagina llenada 
        // se le suma el 1 porque comparamos en tamanos y por eso tambien el <=
        System.out.println("cond1 "+(tamanoBytes + (ultimoDesplazamiento + 1))+" <= "+tamanoPagina);
        if(tamanoBytes + (ultimoDesplazamiento + 1) <= tamanoPagina){
            System.out.println("entre caso 1");
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
                System.out.println("entre caso 2");
                ultimaPagina +=1;
                
               
                ultimoDesplazamiento = tamanoBytes-1;
                paginaByteOcupado.set(ultimaPagina, ultimoDesplazamiento);
            }
            // segundo caso, no alcanza y me toca hacer una página nueva, más peasado de todos
            else{
                System.out.println("entre caso 3");
                ultimoDesplazamiento = tamanoBytes-1;
                ultimaPagina+=1;
                paginaByteOcupado.add(ultimoDesplazamiento);
                tablaPaginas.add(-1);
                numeroPaginas+=1;

                
            }
            respuesta[0] = ultimaPagina;
            respuesta[1] = 0;
        }
        System.out.println("ultPag "+ultimaPagina+"ultimoDespla "+ultimoDesplazamiento);
        System.out.println("resp0 "+respuesta[0]+"resp1 "+respuesta[1]);

        return respuesta;
    }

    public static long get(int pagina, int desplazamiento, int tamano){
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
        }

        Aplicacion.log("Llamado GET a PG: "+pagina+" DESPLAZ:"+desplazamiento+" TAMANO:"+tamano );

        // hay fallo de pagina
        if(tablaPaginas.get(pagina)==-1){
            cantidadFallosPagina++;
            Aplicacion.log("Fallo de página! " );

            colocarPaginaMemoria(pagina);
            
        }

        // se cambian los bit de referencias
        // lo hago despues por que si hubo un fallo de pagina no se podría colocar el bitR para el marco
        reportarAccesoRBits(pagina);

        return (long) (Math.random() *10) ;
    }

    public static void set(int pagina, int desplazamiento, long valor, int tamano){
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

        Aplicacion.log(log);
    }

    public static synchronized void reportarAccesoRBits(int pagina){
        int marco = tablaPaginas.get(pagina);

        String log = "Actualizando bitR PG: "+pagina+" MC: "+marco+"\n>>> bitsR Antes: "+estadoRBits()+"\n";
        
        
        if(marco != -1){
            bitsR.set(marco, true);
        }
        
        log+=">>> bitsR despues: "+estadoRBits();
        Aplicacion.log(log);
        
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

    
}
