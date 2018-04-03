
package Servidor.datos;

import java.io.Serializable;



/**
 *
 * @author <<Alonso Guerrero Alvarado, Valeria Hernandez, Adrian Astua, Carlos Madrigal>>
 */
public class Cliente implements Serializable{
    Integer ID;
    String Nombre; /*Nombre del jugador*/
    Pokemon pokemon; /*Pokemon del Cliente*/
    Integer Vida; /*Vida del Jugador*/
    Integer Prioridad; /*Deside quien atacara primero*/
    
    boolean is_Atacando;
    
    String IP; /*IP de la pc*/
    Integer Puerto; /*Puerto en que se recibiran los datos del servidor*/

    public Cliente() {
    }
    
    
     // Crea un nuevo usuario y le asigna los valores como la vida a 100 y su prioridad de ataque.
   
    public void Set_Nuevo(String Nombre, String IP, Integer Puerto)
    {
        this.ID = 0;
        this.Nombre = Nombre;
        this.Vida = 1000;
        this.Prioridad = ((int)(Math.random()*10)+1);
        this.IP = IP;
        this.Puerto = Puerto;
        this.pokemon = new Pokemon();
        is_Atacando = false;
    }

    public Cliente(Integer ID,String Nombre, Integer Vida, Integer Prioridad, String IP, Integer Puerto) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Vida = Vida;
        this.Prioridad = Prioridad;
        this.IP = IP;
        this.Puerto = Puerto;
        is_Atacando = false;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public Integer getPrioridad() {
        return Prioridad;
    }

    public void setPrioridad(Integer Prioridad) {
        this.Prioridad = Prioridad;
    }

    public Integer getPuerto() {
        return Puerto;
    }

    public void setPuerto(Integer Puerto) {
        this.Puerto = Puerto;
    }

    public Integer getVida() {
        return Vida;
    }

    public void setVida(Integer Vida) {
        this.Vida = Vida;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public boolean isIs_Atacando() {
        return is_Atacando;
    }

    public void setIs_Atacando(boolean is_Atacando) {
        this.is_Atacando = is_Atacando;
    }

    @Override
    public String toString() {
        return "Cliente{" + "ID=" + ID + ", Nombre=" + Nombre + ", pokemon=" + pokemon + ", Vida=" + Vida + ", Prioridad=" + Prioridad + ", is_Atacando=" + is_Atacando + ", IP=" + IP + ", Puerto=" + Puerto + '}';
    }



    
    
}
