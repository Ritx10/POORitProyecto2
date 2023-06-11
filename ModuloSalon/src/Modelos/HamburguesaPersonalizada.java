
package Modelos;

import java.io.Serializable;
import java.util.ArrayList;

public class HamburguesaPersonalizada extends Hamburguesa implements Serializable{
    
    private ArrayList<String> adicionales;
    
    public HamburguesaPersonalizada(){
        super();
        this.adicionales = new ArrayList<String>();
    }
    
    public void agregarAdicional(String add){
        this.adicionales.add(add);
    }
    
}
