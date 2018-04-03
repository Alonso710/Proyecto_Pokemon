
package Cliente.Logica;




import Servidor.administrador.OBJComunicacion;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;



/**
 *
 * @author <<Alonso Guerrero Alvarado, Valeria Hernandez, Adrian Astua, Carlos Madrigal>>
 */
public class Envio_Server implements Serializable{

    private final String HOST ="LOCALHOST"; /*IP del servidor al cual se va a conectar*/
    private final int PUERTO= 2002; /*Puerto del servidor al cual el server recibe los datos*/

    private InputStream conexionEntrada;
    private ObjectInputStream flujoLectura;

    private OutputStream conexionSalida;
    private ObjectOutputStream flujoEscritura;

    public Envio_Server() {
    }

    public OBJComunicacion conecteServidor(OBJComunicacion objeto){
        try {
            Socket cliente = new Socket(HOST, PUERTO);
            //Establece mecanismo de comunicacion con el servidor - Lectura..
           
            conexionEntrada = cliente.getInputStream();
            flujoLectura = new ObjectInputStream(conexionEntrada);
            
            conexionSalida = cliente.getOutputStream();
            flujoEscritura = new ObjectOutputStream(conexionSalida);

            // procesar la gestion a solicitar
            flujoEscritura.writeObject(objeto);
            flujoEscritura.flush();
            
            // recupera la respuesta del servidor...
            objeto = (OBJComunicacion) flujoLectura.readObject();

            flujoEscritura.close();
            flujoLectura.close();
            cliente.close();
        } catch (ClassNotFoundException  ex) {
            
            JOptionPane.showMessageDialog(null,"Conectandose a un servidor desconocido.","Error de Conección",JOptionPane.ERROR_MESSAGE);
        }
        catch (UnknownHostException ex) {
         
            JOptionPane.showMessageDialog(null,"Se ha producido un error conectando al servidor.","Error de Conección",JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
           
            JOptionPane.showMessageDialog(null,"Problemas con los flujos de entrada.","Error de Conección",JOptionPane.ERROR_MESSAGE);
        }
        return objeto;
    }


}
