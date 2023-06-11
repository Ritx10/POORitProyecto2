
package Modelos;

import java.io.Serializable;
import java.util.ArrayList;

public class Orden implements Serializable {
    private int mesa;
    private int cantidad_mesas;
    private boolean entregada;
    private boolean enviado;
    
    private ArrayList<Hamburguesa> pedido;
    public Orden(){
        this.pedido = new ArrayList<Hamburguesa>();    
        this.entregada = false;
        this.enviado = false;
    }

    public Orden(int mesa, int cantidad_mesas) {
        this.mesa = mesa;
        this.cantidad_mesas = cantidad_mesas;
        
    }

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public int getCantidad_mesas() {
        return cantidad_mesas;
    }

    public void setCantidad_mesas(int cantidad_mesas) {
        this.cantidad_mesas = cantidad_mesas;
    }
    
    public int generarCuenta(){
        int total = 0;
        //recorremos las ordenes
        for(int i = 0; i < this.pedido.size(); i++){
            total += this.pedido.get(i).getPrecio();
        }
        return total;
    }
    
    public void agregarPedido(Hamburguesa h){
        this.pedido.add(h);
    }

    public boolean isEntregada() {
        return entregada;
    }

    public void setEntregada(boolean entregada) {
        this.entregada = entregada;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    public ArrayList<Hamburguesa> getPedido() {
        return pedido;
    }
    
}
