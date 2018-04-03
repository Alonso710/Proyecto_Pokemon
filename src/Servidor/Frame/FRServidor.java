
package Servidor.Frame;

import Servidor.administrador.Servidor;
import javax.swing.JTextArea;

/**
 *
 * @author <<Alonso Guerrero Alvarado, Valeria Hernandez, Adrian Astua, Carlos Madrigal>>
 */
public class FRServidor extends javax.swing.JFrame {
    
    public FRServidor() {
        initComponents();
        setSize(745,420);//Fija el tamaño
        setTitle("Servidor Pokemon");// Titulo del frame
        setResizable(false);  // No deja cambiar el tamaño
        setLocationRelativeTo(null); // Pone la ventana en el centro de la pantalla
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        TxtServidor = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        TxtServidor.setBackground(new java.awt.Color(0, 0, 0));
        TxtServidor.setColumns(20);
        TxtServidor.setEditable(false);
        TxtServidor.setForeground(new java.awt.Color(51, 255, 51));
        TxtServidor.setRows(5);
        jScrollPane2.setViewportView(TxtServidor);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(10, 10, 720, 370);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    /*
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FRServidor().setVisible(true);
            }
        });
    }*/

    public static void main(String[] args) {
        FRServidor f = new FRServidor();
        f.setVisible(true);
        Servidor Pantalla = new Servidor();
        Pantalla.inicialiceServidor(f);
    }

    public JTextArea getTxtServidor() {
        return TxtServidor;
    }

    public void setTxtServidor(JTextArea TxtServidor) {
        this.TxtServidor = TxtServidor;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea TxtServidor;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

}
