
package Servidor.administrador;


import java.io.Serializable;

/**
 *
 * @author <<Alonso Guerrero Alvarado, Valeria Hernandez, Adrian Astua, Carlos Madrigal>>
 */
public class OBJComunicacion implements Serializable{
    
    int Accion; 
    Object datoEntrada;
    Object datoSalida;

    public OBJComunicacion(Object datoEntrada) {
        this.datoEntrada = datoEntrada;
    }

    public Object getDatoEntrada() {
        return datoEntrada;
    }

    public void setDatoEntrada(Object datoEntrada) {
        this.datoEntrada = datoEntrada;
    }

    public Object getDatoSalida() {
        return datoSalida;
    }

    public void setDatoSalida(Object datoSalida) {
        this.datoSalida = datoSalida;
    }

    public int getAccion() {
        return Accion;
    }

    public void setAccion(int Accion) {
        this.Accion = Accion;
    }
}
