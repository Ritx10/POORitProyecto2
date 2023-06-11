package Modelos;

import java.io.Serializable;
import java.util.ArrayList;

public class Hamburguesa implements Serializable{
    private String nombre;
    private ArrayList<String> ingredientes;
    private int precio;
    
    public Hamburguesa(){
        
        this.ingredientes = new ArrayList<String>();
        this.precio = 0;
        
        //agregamos los ingredientes pre-definidos
        this.ingredientes.add("DOBLE TORTA"); // LA BASE
        this.ingredientes.add("QUESO CHEDDAR");
        this.ingredientes.add("SALSA ESPECIAL DE LA CASA");
        this.ingredientes.add("HONGOS");
        
        this.nombre = "POO";
        
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<String> getIngredientes() {
        return ingredientes;
    }
    
    public void modificarBase(String base){
        this.ingredientes.add(0, base);
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
    
}
