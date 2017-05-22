/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sudoku;

/**
 *
 * @author Sistemas Inteligentes
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Tablero tablero = new Tablero();
        
        //Crea e inicia la interfaz del juego
        Interfaz interfaz = new Interfaz(tablero);
        interfaz.setVisible(true);
    }

}
