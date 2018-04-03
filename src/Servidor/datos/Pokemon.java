
package Servidor.datos;

import java.io.Serializable;

/**
 *
 * @author <<Alonso Guerrero Alvarado, Valeria Hernandez, Adrian Astua, Carlos Madrigal>>
 */
public class Pokemon implements Serializable{
    
    Integer Imagen;
    String[] Ataques =new String[4];

    public Pokemon() {
    }

    public Pokemon(Integer Imagen, String[] Ataques) {
        this.Imagen = Imagen;
        this.Ataques = Ataques;
    }

    public String[] getAtaques() {
        return Ataques;
    }

    public void setAtaques(String[] Ataques) {
        this.Ataques = Ataques;
    }

    public Integer getImagen() {
        return Imagen;
    }

    public void setImagen(Integer Imagen) {
        this.Imagen = Imagen;
    }

    @Override
    public String toString() {
        return "Pokemon{" + "Imagen=" + Imagen + ", Ataques=" + Ataques + '}';
    }
    
    
}
