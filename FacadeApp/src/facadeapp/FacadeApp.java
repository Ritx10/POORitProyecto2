package facadeapp;

import Modelos.Hamburguesa;
import Modelos.MsjServidor;
import Modelos.Orden;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacadeApp {

    public static MsjServidor mensajeServidor;
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        //creamos el servidor
        ServerSocket servidor = null;
        Socket sc = null;
        int PUERTO = 5000;
        try {
            //Creamos el socket del servidor
            servidor = new ServerSocket(PUERTO);
            
            System.out.println("SERVIDOR CONECTADO!!!");
            
        } catch (IOException ex) {
            Logger.getLogger(FacadeApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            sc = servidor.accept();
            //Siempre estara escuchando peticiones
            //Espero a que un cliente se conecte
            //Se obtiene el flujo entrante desde el cliente
            
            //para el salon
//            while(true){
//                ObjectInputStream mensaje = new ObjectInputStream(sc.getInputStream());
//                mensajeServidor = (MsjServidor) mensaje.readObject();
//                if(mensajeServidor.getNueva() != null  ){
//                    if( mensajeServidor.getMensaje().equals("RECIBIR") )
//                    {
//                        Orden nueva = mensajeServidor.getNueva();
//                        
//                        System.out.println("NUEVA ORDEN RECIBIDA PARA LA MESA #"+(nueva.getMesa()+1));
//                    }
//                }else{
//                    System.out.println(mensajeServidor.getMensaje());
//               
//                }          
//            }
            
            //para la cocina
            OutputStream out;
            int agregadas = 0;
            boolean despachada = true;
            while(true){
                //generamos un numero aletorio para ver si agregamos una orden o sale una orden
                Random rand = new Random();
                
                int num = rand.nextInt(50);
                
                if( num < 10 ){
                    out = sc.getOutputStream();
                    ObjectOutputStream mensaje = new ObjectOutputStream(out);
                    Orden o = new Orden();
                    o.setCantidad_mesas(rand.nextInt(5));
                    o.setMesa(rand.nextInt(50));
                    Hamburguesa h = new Hamburguesa();
                    h.setPrecio(rand.nextInt(10000));
                    o.agregarPedido(h);
                    mensaje.writeObject(o);
                    mensaje.flush();
                    agregadas++;
                    despachada = true;
                    System.out.println("SE HA AGREGADO UNA ORDEN PARA LA MESA #"+o.getMesa());
                    
                }else if( num > 40){
                    if( agregadas > 0 ){
                        
                        despachada = false;
                        System.out.println("DESPACHANDO ORDEN....");
                        ObjectInputStream mensajex = new ObjectInputStream(sc.getInputStream());
                        mensajeServidor = (MsjServidor) mensajex.readObject();

                        System.out.println(mensajeServidor.getMensaje());

                        agregadas--;
                    }
                }
            }
            
            
        }catch (IOException  ex) {
            Logger.getLogger(FacadeApp.class.getName()).log(Level.SEVERE, null, ex);
            
            servidor.close();
        }
    }

}
