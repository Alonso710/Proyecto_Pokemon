
package Cliente.Logica;

import Servidor.datos.Cliente;
import Servidor.administrador.OBJComunicacion;
import Cliente.Frame.Ventana_Cliente;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author <<Alonso Guerrero Alvarado, Valeria Hernandez, Adrian Astua, Carlos Madrigal>>
 */
public class Administrador {
    
    private Envio_Server ES = new Envio_Server();
    ArrayList<Cliente> Lista_Jugadores = new ArrayList<Cliente>(); /*Lista de jugadores del server*/
    private Cliente Local = new Cliente(); /*Jugador actual del sistema*/
    private boolean is_Cargado=false; /*Bandera que indica si ya cargamos los datos de la lista del jugadores*/
    
    private boolean is_End=false;/*Indica si el juego termino o no*/

    private ServerSocket servidor;   // objeto que se encarga de atender peticiones externas
    private final int PUERTO = 1000; // numero de puerto por donde va a atender peticiones
    private boolean isRetador_Online = false;
   // la conexion de escritura del servidor
    private OutputStream conexionSalida;
    private ObjectOutputStream flujoSalida;

    // la conexion de lectura del servidor
    private InputStream conexionEntrada;
    private ObjectInputStream flujoEntrada;
    private String IP;

    // socket que contiene la conexion con el cliente
    private Socket cliente;         // el proceso cliente que esta atendiendo...

    public Administrador() {

    }

   
     //Inicia el juego usando al clente como un servidor local el cual escucha por el puerto asignado que es el 2000
     //Intancia el servidor, asigna el puerto y saca la IP
    
    public void Iniciar_Juego(Ventana_Cliente VC) {
        try {
            servidor = new ServerSocket(PUERTO); /*Genera el server y le asina un puerto para el envio y recepción de datos*/
            IP = servidor.getInetAddress().getLocalHost().getHostAddress(); /*Saca la IP de la PC*/
            
            Cargar_Usuario_Local();/*Crea el usuario LOCAL*/
            
            while (true){
              
                System.out.println("Esperando una solicitud de un cliente...\n");
                cliente = servidor.accept();  // acepta la solicitud de un cliente
                System.out.println("Estableciendo canal de escritura...\n");
              
                // se establece DE PRIMERO  el canal de comunicacion-Escritura
                conexionSalida =  cliente.getOutputStream();
                flujoSalida = new ObjectOutputStream(conexionSalida);
                
                System.out.println("Estableciendo canal de lectura ...\n");
              
                // se establece DE SEGUNDO el canal de comunicacion-Lectura
                conexionEntrada = cliente.getInputStream();
                flujoEntrada = new ObjectInputStream(conexionEntrada);

                // atender la peticion...
                procesePeticion(VC);

                flujoEntrada.close();
                flujoSalida.close();
                cliente.close();
                
                
                Juego(VC);
            }
        } catch (IOException ex) {
           
            System.out.println("Problemas creando el servidor en el puerto "+ PUERTO+"\n");
        }
    }

    /**
     * Crea el Nuevo Usuario y se envia los datos al Server
     */
    private void Cargar_Usuario_Local() {
        String Nombre = JOptionPane.showInputDialog(null,"Ingrese su Nombre","Inicio del Juego", JOptionPane.QUESTION_MESSAGE);
        
        Cliente C = new Cliente();
        C.Set_Nuevo(Nombre,IP, PUERTO);
        System.out.println(C.toString());
        Local = C;
        
        OBJComunicacion resp = new OBJComunicacion(C);
        resp.setAccion(1);
        resp = ES.conecteServidor(resp);
        if ((Boolean)resp.getDatoSalida())
            System.out.println("El Cliente se ha registrado al servidor.");
    }

    private void procesePeticion(Ventana_Cliente VC) {
        try {
            OBJComunicacion objeto = (OBJComunicacion) flujoEntrada.readObject();
            switch(objeto.getAccion())/*Recibe al accion por parte del servidor*/
            {
                case 1: /*Recibe a los jugadores y la orden de iniciar el juego, actualiza tambien los datos conforme prociga el juego*/
                    boolean resultado = Guardar_Lista(objeto.getDatoEntrada());
                    objeto.setDatoSalida(resultado);
                break;
            }
            
            flujoSalida.writeObject(objeto);
            
            
            
            
        } catch (IOException ex) {
          
            System.out.println("Problemas leyendo o escribiendo en el flujo entrada/salida.\n");
        } catch (ClassNotFoundException ex) {
           
            System.out.println("Problemas en la conversion del objeto recibido...\n");
        } catch (IncompatibleClassChangeError ex){
           
            System.out.println("Problemas en transformar datos...\n");
        }
    }

    private boolean Guardar_Lista(Object ListaJugadores) {
        boolean res = false;
        try{
            System.out.println("Lista Jugadores Cantidad: "+((ArrayList<Cliente>)ListaJugadores).size()+"\n");
            Lista_Jugadores = new ArrayList<Cliente>(); /*Limpio la lista que tengo de jugadores*/
            for (int i = 0; i < ((ArrayList<Cliente>)ListaJugadores).size(); i++) { /*Voy llenando la lista mia con la  lista del servidor y mostrandolos*/
                Cliente c = ((ArrayList<Cliente>)ListaJugadores).get(i);
                System.out.println(c.toString());
                Lista_Jugadores.add(c);
            }
            isRetador_Online = true;
            res = true;
        }catch(Exception exc){System.out.println(exc.getMessage());}
        return res;
    }

    private void Juego(Ventana_Cliente VC) {
        
        
        if (!isRetador_Online)
                JOptionPane.showMessageDialog(null,"El juego iniciara cuando se conecte un retador.","Espere...",JOptionPane.INFORMATION_MESSAGE);
            else
            {
                VC.setVisible(true);
                
            
                //Si la bandera de is_Cargado es falso, carga los datos de la lista de jugadores
                if (!is_Cargado){
                    Cliente cliente1 = (Cliente) Lista_Jugadores.get(0);
                    Cliente cliente2 = (Cliente) Lista_Jugadores.get(1);
                    
                    Cliente resul = new Cliente();
                    
                    //Identificamos cual de los clientes somos nosotros
                    if (Local.getNombre().contentEquals(cliente1.getNombre()))
                    {    
                        resul = cliente1;
                        VC.getLbl_Jugador().setText(cliente1.getNombre());
                        VC.getLbl_Retador().setText(cliente2.getNombre());
                        
                        if (cliente1.getPokemon().getImagen() == 1)
                        {
                            VC.getImagen_Jugador().setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cliente/Imagenes/Arti_A.gif")));
                            VC.getImagen_Retador().setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cliente/Imagenes/Ray_B.gif")));
                            
                        }else
                        {
                            VC.getImagen_Jugador().setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cliente/Imagenes/Ray_A.gif")));
                            VC.getImagen_Retador().setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cliente/Imagenes/Arti_B.gif")));
                        }
                        
                        String [] Skills = cliente1.getPokemon().getAtaques();
                        VC.getBtn_S1().setText(Skills[0]);
                        VC.getBtn_S2().setText(Skills[1]);
                        VC.getBtn_S3().setText(Skills[2]);
                        VC.getBtn_S4().setText(Skills[3]);
                    }
                    else
                        if (Local.getNombre().contentEquals(cliente2.getNombre()))
                        {
                            resul = cliente2;
                            VC.getLbl_Jugador().setText(cliente2.getNombre());
                            VC.getLbl_Retador().setText(cliente1.getNombre());

                            if (cliente2.getPokemon().getImagen() == 1) {
                                VC.getImagen_Jugador().setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cliente/Imagenes/Arti_A.gif")));
                                VC.getImagen_Retador().setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cliente/Imagenes/Ray_B.gif")));
                            } else {
                                VC.getImagen_Jugador().setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cliente/Imagenes/Ray_A.gif")));
                                VC.getImagen_Retador().setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cliente/Imagenes/Arti_B.gif")));
                            }

                            String[] Skills = cliente2.getPokemon().getAtaques();
                            VC.getBtn_S1().setText(Skills[0]);
                            VC.getBtn_S2().setText(Skills[1]);
                            VC.getBtn_S3().setText(Skills[2]);
                            VC.getBtn_S4().setText(Skills[3]);
                        }
                    
                    
                  
                    
                    is_Cargado = true; //Cambia su estado y evita que se vuelvan a cargar
                }
              
                if (!is_End) {
                    
                    //Busca cual de los 2 jugadores somos nosotros y nos muestra el mensaje de victoria o derrota
                    if (Local.getNombre().contentEquals(Lista_Jugadores.get(0).getNombre())) {
                        if (Lista_Jugadores.get(0).getVida() <= 0)
                        {
                            is_End = true;
                            JOptionPane.showMessageDialog(null,"Has Perdido","Por mamon",JOptionPane.INFORMATION_MESSAGE);
                        }else
                        if (Lista_Jugadores.get(1).getVida() <= 0)
                        {
                            is_End = true;
                            JOptionPane.showMessageDialog(null,"Has Ganado","Mamo el otro hpta",JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        if (Lista_Jugadores.get(1).getVida() <= 0)
                        {
                            is_End = true;
                            JOptionPane.showMessageDialog(null,"Has Perdido","Por mamon",JOptionPane.INFORMATION_MESSAGE);
                        }else
                        if (Lista_Jugadores.get(1).getVida() <= 0)
                        {
                            is_End = true;
                            JOptionPane.showMessageDialog(null,"Has Ganado","Mamo el otro hpta",JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    
                    /*Actualiza la barra de vida de cada jugador*/
                    if (Local.getNombre().contentEquals(Lista_Jugadores.get(0).getNombre())) {
                        VC.getVida_Jugador().setValue(Lista_Jugadores.get(0).getVida());
                        VC.getVida_Retador().setValue(Lista_Jugadores.get(1).getVida());
                        
                        if (Lista_Jugadores.get(0).getVida() > 500) {
                            VC.getVida_Jugador().setForeground(Color.green);
                        } else if (Lista_Jugadores.get(0).getVida() <= 500 && Lista_Jugadores.get(0).getVida() >= 200) {
                            VC.getVida_Jugador().setForeground(Color.yellow);
                        } else {
                            VC.getVida_Jugador().setForeground(Color.red);
                        }

                        if (Lista_Jugadores.get(1).getVida() > 500) {
                            VC.getVida_Retador().setForeground(Color.green);
                        } else if (Lista_Jugadores.get(1).getVida() <= 500 && Lista_Jugadores.get(1).getVida() >= 200) {
                            VC.getVida_Retador().setForeground(Color.yellow);
                        } else {
                            VC.getVida_Retador().setForeground(Color.red);
                        }

                    } else {
                        VC.getVida_Jugador().setValue(Lista_Jugadores.get(1).getVida());
                        VC.getVida_Retador().setValue(Lista_Jugadores.get(0).getVida());
                        
                        if (Lista_Jugadores.get(1).getVida() > 500) {
                            VC.getVida_Jugador().setForeground(Color.green);
                        } else if (Lista_Jugadores.get(1).getVida() <= 500 && Lista_Jugadores.get(1).getVida() >= 200) {
                            VC.getVida_Jugador().setForeground(Color.yellow);
                        } else {
                            VC.getVida_Jugador().setForeground(Color.red);
                        }

                        if (Lista_Jugadores.get(0).getVida() > 500) {
                            VC.getVida_Retador().setForeground(Color.green);
                        } else if (Lista_Jugadores.get(0).getVida() <= 500 && Lista_Jugadores.get(0).getVida() >= 200) {
                            VC.getVida_Retador().setForeground(Color.yellow);
                        } else {
                            VC.getVida_Retador().setForeground(Color.red);
                        }
                    }
                    
                    //Recorremos la lista y decimos quien esta atacando
                    for (int i = 0; i < Lista_Jugadores.size(); i++) {
                        Cliente c = Lista_Jugadores.get(i);
                        if (Local.getNombre().contentEquals(c.getNombre())) {
                            if (c.isIs_Atacando()) {
                                VC.is_Atacando(true);
                                
                                VC.getBtn_Atacar().setVisible(true);
                            
                            } else {
                                VC.is_Atacando(false);
                                
                                VC.getBtn_Atacar().setVisible(false);  
                            }
                        }
                    }
                    
                }else
                {
                    JOptionPane.showMessageDialog(null,"Juego Finalizado","Fin del juego",JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            }
    }

    public void Atacar(Ventana_Cliente VC) {
        for (int i = 0; i < Lista_Jugadores.size(); i++) {
            Cliente c = Lista_Jugadores.get(i);
            if (Local.getNombre().contentEquals(c.getNombre())) {
                if (c.isIs_Atacando()) {
                    
                    VC.getBtn_S1().setVisible(false);
                    VC.getBtn_S2().setVisible(false);
                    VC.getBtn_S3().setVisible(false);
                    VC.getBtn_S4().setVisible(false);
                    
                    OBJComunicacion resp = new OBJComunicacion(Local);
                    resp.setAccion(2);
                    resp = ES.conecteServidor(resp);
                    if ((Boolean) resp.getDatoSalida()) {
                        System.out.println("El Cliente "+Local.getNombre()+" ha atacado.\n");
                    }
                }
            }
        }
    }



    
}
