import java.util.ArrayList;

public class AlgoritmoEnvejecimiento extends Thread{

    public void run(){
        while(true){
            ArrayList<Byte> contadores = MemoriaVirtual.darContadores();

            ArrayList<Boolean> RBits = MemoriaVirtual.leerRBits();

            for(int i = 0; i < contadores.size(); i++){

                byte contador = contadores.get(i);
                // mover una posición a la derecha
                byte res = (byte) (contador >>> 1);

                // en caso de que rBit sea positivo se colaca
                if(RBits.get(i)){
                    res = (byte) (res | (1 << 7)); 
                }
            }



            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            MemoriaVirtual.reiniciarRBits();

        }
    }
    
}
