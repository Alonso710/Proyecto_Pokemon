package Servidor.administrador;

import Servidor.datos.Cliente;
import Servidor.datos.Pokemon;
import Servidor.Frame.FRServidor;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author <<Alonso Guerrero Alvarado, Valeria Hernandez, Adrian Astua, Carlos
 * Madrigal>>
 */
public class Administrador implements Serializable {

    Envio_Cliente EnvioCliente = new Envio_Cliente();
    ArrayList<Cliente> ListaCliente = new ArrayList<Cliente>();

    public Administrador() {

    }

    // Ingresa un Cliente a la lista y retorna true si lo logro ingresar y false si tubo algun error.
    boolean Guardar_Cliente_Nuevo(Object Usuario) {
        boolean res = false;
        try {
            Cliente C = (Cliente) Usuario;
            C.setID(ListaCliente.size() + 1);
            if ((ListaCliente.size() + 1) == 1) {
                String[] Skills = new String[]{"Ice Shard", "Ice Beam", "Blizzard", "Roost"};/*Articuno*/

                Pokemon P = new Pokemon(1, Skills);
                C.setPokemon(P);
            } else {
                String[] Skills = new String[]{"Dragon Pulse", "Twister", "Crunch", "Air Slash"};/*Rayquaza*/

                Pokemon P = new Pokemon(2, Skills);
                C.setPokemon(P);
            }
            ListaCliente.add(C);
            res = true;
            if (ListaCliente.size() == 2) {
                Recorrido();
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
        return res;
    }

    public int Cantidad_Jugadores() {
        return ListaCliente.size();
    }

    // Muestra la lista de clientes en el servidor
    public void Mostrar_Clientes(FRServidor ventana) {
        for (int i = 0; i < ListaCliente.size(); i++) {
            Cliente cliente = ListaCliente.get(i);
            ventana.getTxtServidor().append(cliente.toString() + " P: " + cliente.getPokemon().getImagen() + "\n");
        }
    }

    //  Enviar a todos los clientes la lista completa de los jugadores  
    //ListaCliente
    public void Enviar_A_Clientes() {
        try {
            for (int i = 0; i < ListaCliente.size(); i++) {
                Cliente cliente = ListaCliente.get(i);
                OBJComunicacion resp = new OBJComunicacion(ListaCliente);
                resp.setAccion(1);
                resp = EnvioCliente.ConectarClientes(resp, cliente.getIP(), cliente.getPuerto());
                if ((Boolean) resp.getDatoSalida()) {
                    System.out.println("El cliente " + cliente.getNombre() + " ha resivido la información corectamente.");
                }
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }

    /**
     * Recore la lista de jugadores y define quien es el que atacara primero
     */
    private void Recorrido() {
        Cliente j1 = ListaCliente.get(0);
        Cliente j2 = ListaCliente.get(1);
        if (j1.getPrioridad() >= j2.getPrioridad()) {
            ListaCliente.get(0).setIs_Atacando(true);
        } else {
            ListaCliente.get(1).setIs_Atacando(true);
        }
    }

    boolean Calcular_Ataque(Object Local) {
        boolean res = false;
        Cliente j1 = ListaCliente.get(0);
        Cliente j2 = ListaCliente.get(1);
        if (j1.getNombre().contains(((Cliente) Local).getNombre())) {
            if (j1.isIs_Atacando()) {
                ListaCliente.get(1).setVida(ListaCliente.get(1).getVida() - (((int) (Math.random() * 400) + 1)));
                ListaCliente.get(0).setIs_Atacando(false);
                ListaCliente.get(1).setIs_Atacando(true);
                res = true;
            }
        } else if (j2.isIs_Atacando()) {
            ListaCliente.get(0).setVida(ListaCliente.get(0).getVida() - (((int) (Math.random() * 200) + 1)));
            ListaCliente.get(1).setIs_Atacando(false);
            ListaCliente.get(0).setIs_Atacando(true);
            res = true;
        }
        return res;
    }

}
