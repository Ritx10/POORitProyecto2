
package Modelos;

import java.io.Serializable;

/**
 *
 * @author dell
 */
public class MsjServidor implements Serializable{
    private String mensaje;
    private Orden nueva;
    
    public MsjServidor(){
        
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Orden getNueva() {
        return nueva;
    }

    public void setNueva(Orden nueva) {
        this.nueva = nueva;
    }
    
    
}
