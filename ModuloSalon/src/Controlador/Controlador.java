
package Controlador;

import Vista.Salon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import Modelos.Hamburguesa;
import Modelos.HamburguesaPersonalizada;
import Modelos.MsjServidor;
import Modelos.Orden;


public class Controlador implements ActionListener{
    //Puerto del servidor
    final int PUERTO = 5000;
    DataInputStream in;
    OutputStream out;
    Socket sc = null;
    
    //para controlar la vista
    private Salon panel;
    private ArrayList<Orden> ordenes; //lista de ordenes
    private ArrayList<String> adicionales; //lista de ordenes
    public Controlador(Salon panel) { //recibe una vista a controlar
        this.panel = panel;
        this.panel.addActionListener(this); //asignamos el escuchador a la vista
        this.ordenes = new ArrayList<Orden>();
        this.adicionales = new ArrayList<String>();
        //nos conectamos al servidor
            try {
                //Creo el socket para conectarme con el cliente
                this.sc = new Socket("localhost", PUERTO);
                
                //envio un mensaje de indicando que el salon se acaba de 
                MsjServidor msj = new MsjServidor();
                
                msj.setNueva(null);
                msj.setMensaje("SALON");
                
                this.out = this.sc.getOutputStream();
                ObjectOutputStream mensaje = new ObjectOutputStream(this.out);
                
                mensaje.writeObject(msj);
                mensaje.flush();
                
                
            } catch (IOException ex) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(null, "Conexion Realizada con el Servidor...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //verificamos cuando se presione el boton 
        if (e.getActionCommand().equals("ADICIONAL")) {
            //verificamos que el texto contenta informacion
            if( this.panel.getjTextField1().length() > 2){
                this.adicionales.add(this.panel.getjTextField1());
                this.panel.setjLabel8("INGREDIENTE AGREGADO!");
            }else{
                this.panel.setjLabel8("DEBE AGREGAR UN INGREDIENTE VALIDO!");
            }
        }
        if (e.getActionCommand().equals("TIPO")) {
            //activamos para agregar adicionales
            if( this.panel.getjComboBox1() == 1)
                this.panel.addAdicionales(true);
            else
                this.panel.addAdicionales(false);
        }
        if (e.getActionCommand().equals("AGREGAR")) {
            //verificamos primero si la mesa seleccionada esta siendo atendida o esta vacia
            if( (this.ocupada(this.panel.getjComboBox4()) && !this.enviada(this.panel.getjComboBox4())) || !this.ocupada(this.panel.getjComboBox4()) ){
                agregarPedido();
                this.adicionales = new ArrayList<String>(); //reiniciamos la lista de adicionales para una nueva orden
            }else{
                this.panel.setjLabel8("NO SE PUEDE AGREGAR MAS A LA ORDEN!");
            }
        }
        
        if (e.getActionCommand().equals("ENVIAR")) {
            try {
                enviarPedido();
            } catch (IOException ex) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void enviarPedido() throws IOException{
        if( this.ocupada(Integer.valueOf(this.panel.getjTextField3())-1)){
            //verificamos si ya esta enviada
            if( !this.enviada(Integer.valueOf(this.panel.getjTextField3())-1) ){
                for(int i = 0; i < this.ordenes.size(); i++){
                    if( this.ordenes.get(i).getMesa() == (Integer.valueOf(this.panel.getjTextField3())-1)){
                        this.ordenes.get(i).setEnviado(true);
                        enviarOrden(this.ordenes.get(i));
                    }
                }   
                this.panel.setjLabel8("LA ORDEN HA SIDO ENVIADA!");
            }else{
                this.panel.setjLabel8("LA ORDEN YA HA SIDO ENVIADA!");
            }
        }else{
            this.panel.setjLabel8("LA MESA NO ESTA OCUPADA!");
        }
    }
    public void enviarOrden(Orden o) throws IOException{
        //enviamos la ordne al servidor
        //la enviamos a la cocina
        OutputStream out;
        try {
        
            this.out = this.sc.getOutputStream();
            ObjectOutputStream msjnuevo = new ObjectOutputStream(this.out);
            //envio un mensaje de indicando que el salon se acaba de 
            MsjServidor msj = new MsjServidor();
                
            msj.setNueva(o);
            msj.setMensaje("RECIBIR");
            msjnuevo.writeObject(msj);
            msjnuevo.flush();
        } catch (IOException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void entregarOrden(int mesa){
        for(int i = 0; i < this.ordenes.size(); i++){
            if( this.ordenes.get(i).getMesa() == mesa ){
                this.ordenes.get(i).setEntregada(true);
            }
       }   
    }
    public void liberarMesa(int mesa){
        for(int i = 0; i < this.ordenes.size(); i++){
            if( this.ordenes.get(i).getMesa() == mesa ){
                this.ordenes.remove(i);
            }
       }  
    }
    public void agregarPedido(){
        //verificamos que se hayan agregado adicinales si se escogio una hamburguesa personalizada
        if( (this.panel.getjComboBox1() == 1 && this.adicionales.size() > 0) || this.panel.getjComboBox1() == 0)
        {
            //verificamos la cantidad de mesas
            if( this.panel.getjTextField2().length() > 0){
                int mesas = Integer.valueOf(this.panel.getjTextField2());
                    
                    //verificamos el tipo de hamburguesa
                    if( this.panel.getjComboBox1() == 1 ){
                        //creamos una hamburguesa
                        HamburguesaPersonalizada h;
                        h = new HamburguesaPersonalizada();
                        //ahora modificamos la base
                        h.modificarBase(this.panel.getjComboBox3Txt());
                        
                        //miramos el precio
                        int tipo = this.panel.getjComboBox2();
                        if( tipo == 0){
                            h.setPrecio(2000+this.adicionales.size()*500);
                        }else if( tipo == 1){
                            h.setPrecio(3000+this.adicionales.size()*800);
                        }else if( tipo == 0){
                            h.setPrecio(4000+this.adicionales.size()*1000);
                        }else{
                            h.setPrecio(5000+this.adicionales.size()*1500);
                        }
                        //ahora verificamos que la mesa no este ocupada
                        if( !this.ocupada(this.panel.getjComboBox4())){
                            //creamos una orden
                            Orden nueva = new Orden();
                            nueva.setCantidad_mesas(mesas);
                            nueva.setMesa(this.panel.getjComboBox4());  
                            nueva.agregarPedido(h);
                            
                            this.ordenes.add(nueva);
                            this.panel.setjLabel8("ORDEN CREADA CON EXITO!");
                        }else{
                            addHamburguesa(h,this.panel.getjComboBox4());
                            this.panel.setjLabel8("SE AGREGO EL PEDIDO A LA ORDEN!");
                        }

                    }else{
                        //creamos una hamburguesa
                        Hamburguesa h;
                        h = new Hamburguesa();
                        //miramos el precio
                        int tipo = this.panel.getjComboBox2();
                        if( tipo == 0){
                            h.setPrecio(1500);
                        }else if( tipo == 1){
                            h.setPrecio(2000);
                        }else if( tipo == 0){
                            h.setPrecio(2500);
                        }else{
                            h.setPrecio(3500);
                        }
                         //ahora verificamos que la mesa no este ocupada
                        if( !this.ocupada(this.panel.getjComboBox4())){
                            //creamos una orden
                            Orden nueva = new Orden();
                            nueva.setCantidad_mesas(mesas);
                            nueva.setMesa(this.panel.getjComboBox4());
                            nueva.agregarPedido(h);
                            
                            this.ordenes.add(nueva);
                            this.panel.setjLabel8("ORDEN CREADA CON EXITO!");
                        }else{
                            addHamburguesa(h,this.panel.getjComboBox4());
                            this.panel.setjLabel8("SE AGREGO EL PEDIDO A LA ORDEN!");
                        }
                    }
                
            }else{
                this.panel.setjLabel8("DEBE AGREGAR LA CANTIDAD DE MESAS!");
            }
        }
        else{
            this.panel.setjLabel8("DEBE AGREGAR ADICIONALES!");
        }
        
        
    }
    
    //metodo para verificar que una mesa no este ocupada
    public boolean ocupada(int mesa){
        
        //recorremos las ordenes
        for(int i = 0; i < this.ordenes.size(); i++){
            if( this.ordenes.get(i).getMesa() == mesa){
                return true;
            }
        }
        
        return false;
    }
    //metodo para verificar que una orden ya se haya enviado
    public boolean enviada(int mesa){
        
        //recorremos las ordenes
        for(int i = 0; i < this.ordenes.size(); i++){
            if( this.ordenes.get(i).getMesa() == mesa){
                return this.ordenes.get(i).isEnviado();
            }
        }
        
        return false;
    }
    //metodo que agrega una hambuguesa a una mesa
    public void addHamburguesa(Hamburguesa h, int mesa){
        //recorremos las ordenes
        for(int i = 0; i < this.ordenes.size(); i++){
            if( this.ordenes.get(i).getMesa() == mesa){
                this.ordenes.get(i).agregarPedido(h);
            }
        }
    }
}
