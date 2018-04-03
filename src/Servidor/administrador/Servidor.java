
package Servidor.administrador;

import Servidor.Frame.FRServidor;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;



/**
 *
 * @author <<Alonso Guerrero Alvarado, Valeria Hernandez, Adrian Astua, Carlos Madrigal>>
 */
public class Servidor {
    private Administrador Adm = new Administrador();
    private ServerSocket servidor;   // objeto que se encarga de atender peticiones externas
    private final int PUERTO = 2002; // numero de puerto por donde va a atender peticiones

   // la conexion de escritura del servidor
    private OutputStream conexionSalida;
    private ObjectOutputStream flujoSalida;

    // la conexion de lectura del servidor
    private InputStream conexionEntrada;
    private ObjectInputStream flujoEntrada;


    // socket que contiene la conexion con el cliente
    private Socket cliente;         // el proceso cliente que esta atendiendo...

    public Servidor() {

    }

    public void inicialiceServidor(FRServidor ventana){
        try {
            servidor = new ServerSocket(PUERTO);
            ventana.getTxtServidor().setForeground(Color.green);
            ventana.getTxtServidor().setBackground(Color.BLACK);
            ventana.getTxtServidor().append("Direccion IP: "+servidor.getInetAddress().getLocalHost().getHostAddress()+"\n");
            ventana.getTxtServidor().append("Nombre del Servidor: "+servidor.getInetAddress().getLocalHost().getHostName()+"\n");
            ventana.getTxtServidor().append("Puerto del Servidor: "+servidor.getLocalPort()+"\n");
            ventana.getTxtServidor().append("Server Timeout: "+servidor.getSoTimeout()+"\n");
            ventana.getTxtServidor().append("Buffer Size del Servidor: "+servidor.getReceiveBufferSize()+"\n");
            while (true){
                ventana.getTxtServidor().append("Esperando una solicitud de un cliente...\n");
                cliente = servidor.accept();  // acepta la solicitud de un cliente

                // se establece DE PRIMERO  el canal de comunicacion-Escritura
                conexionSalida =  cliente.getOutputStream();
                flujoSalida = new ObjectOutputStream(conexionSalida);

               
                // se establece DE SEGUNDO el canal de comunicacion-Lectura
                conexionEntrada = cliente.getInputStream();
                flujoEntrada = new ObjectInputStream(conexionEntrada);

                // atender la peticion...
                procesePeticion(ventana);

                flujoEntrada.close();
                flujoSalida.close();
                cliente.close();
            }
        } catch (IOException ex) {
            ventana.getTxtServidor().append("Problemas creando el servidor en el puerto "+ PUERTO+"\n");
        }
    }

    private void procesePeticion(FRServidor ventana) {
        try {
            OBJComunicacion objeto = (OBJComunicacion) flujoEntrada.readObject();
            
            switch(objeto.getAccion())/*Recibe al accion por parte de los clientes*/
            {
                case 1: /*Recibe los Clientes por este metodo*/
                    boolean resultato = Adm.Guardar_Cliente_Nuevo(objeto.getDatoEntrada());
                    Adm.Mostrar_Clientes(ventana);
                    objeto.setDatoSalida(resultato);
                break;
                    
                case 2: /*Recibe los ataques del cliente*/
                    boolean res = Adm.Calcular_Ataque(objeto.getDatoEntrada());
                    Adm.Mostrar_Clientes(ventana);
                    objeto.setDatoSalida(res);
                break;
            }
            
            flujoSalida.writeObject(objeto);
            
            
            if (Adm.Cantidad_Jugadores() > 1)
                Adm.Enviar_A_Clientes();
            
        } catch (IOException ex) {
            ventana.getTxtServidor().append("Problemas leyendo o escribiendo en el flujo entrada/salida.\n");
        } catch (ClassNotFoundException ex) {
            ventana.getTxtServidor().append("Problemas en la conversion del objeto recibido...\n");
        } catch (IncompatibleClassChangeError ex){
            ventana.getTxtServidor().append("Problemas en transformar datos...\n");
        }
    }




}
