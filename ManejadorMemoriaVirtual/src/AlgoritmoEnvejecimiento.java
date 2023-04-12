
public class AlgoritmoEnvejecimiento extends Thread{

    public boolean correr = true;

    public void run(){       

        while(correr){
            // sección sincronizada para obligar a que cualquier cambio que se haga en rBits deba esperar a que este termine
            synchronized(MemoriaVirtual.class){
                String log ="Iteración de Algoritmo de Envejecimiento";
                for(int numeroMarco = 0; numeroMarco < MemoriaVirtual.contadoresMarco.size(); numeroMarco++){

                        byte contador = MemoriaVirtual.contadoresMarco.get(numeroMarco);

                        int numeroPagina = MemoriaVirtual.tablaMarcos.get(numeroMarco);

                        // mover una posición a la derecha, pero le dejo a la derecha 1 uno ej. 10000
                        byte res = (byte) (contador >>> 1);

                        // en caso de que rBit sea positivo se colaca
                        if(MemoriaVirtual.bitsR.get(numeroMarco)){
                            res = (byte) (res | (1 << 7)); 
                            // TODO: mejorar o borrar LOG
                            log+="\nPG: "+numeroPagina+" MC: "+numeroMarco+" | 1 >> "+Integer.toBinaryString(contador & 0xFF)+" = "+Integer.toBinaryString(res & 0xFF)+" |||| "+(contador & 0xFF)+" -> "+(res & 0xFF);
                        } else{

                            int val = 1;
                            for(int i = 0; i<6;i++){
                                val= val << 1; 
                                val = val | 1;
                            }
                            
                            res = (byte) (res & val);
            

                            // TODO: mejorar o borrar LOG
                            log+="\nPG: "+numeroPagina+" MC: "+numeroMarco+" | 0 >> "+Integer.toBinaryString(contador & 0xFF)+" = "+Integer.toBinaryString(res & 0xFF)+" |||| "+(contador & 0xFF)+" -> "+(res & 0xFF);

                        }

                        MemoriaVirtual.contadoresMarco.set(numeroMarco,res);
                    

                }
                Aplicacion.log(log);
                    

                MemoriaVirtual.reiniciarRBits();

            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            

        }
    }

    public synchronized void stopX(){
        correr=false;
    }
    
}
