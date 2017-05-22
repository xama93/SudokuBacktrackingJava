/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sudoku;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Sistemas Inteligentes
 */
public class Tablero {


    //Fichero del que se va a crear el tablero
    private File archivo;

    //Datos del tablero
    private int m_tablero[][];

    //Constructor del tablero
    public Tablero()
    {
        archivo = null;

        //Crea el tablero
        m_tablero = new int[9][9];

        //Inicializa el tablero
        for (int i=0; i<9; i++)
        {
            for(int j=0; j<9; j++)
                m_tablero[i][j] = 0;
        }

    }

     /**
     * Constsructor de Copia del tablero
     * @param original Tablero del cual realizar la copia
     */
    public Tablero (Tablero original)
    {
        archivo = original.archivo;

        //Crea el tablero
        m_tablero = new int[9][9];

        //Inicializa el tablero
        for (int i=0; i<9; i++)
        {
            for(int j=0; j<9; j++)
                m_tablero[i][j] = original.m_tablero[i][j];
        }
    }

    /**
     * Devuelve el valor de una casilla
     * @param i
     * @param j
     * @return
     */
    public int getCasilla(int i, int j)
    {
        return m_tablero[i][j];
    }

    /**
     * Indica el valor de una casilla
     */
    public void setCasilla(int valor, int fila, int columna)
    {
        m_tablero[fila][columna] = valor;
    }

    /**
     * Indicar el nombre de un fichero
     */
    public void setFichero(File fichero)
    {
        archivo = fichero;
    }

    /**
     * Devuelve el fichero
     */
    public File getFichero()
    {
        return archivo;
    }

    /**
     * Carga en m_tablero el sudoku leído desde un fichero
     */
    public void CargarTablero()
    {
        FileReader fr = null;
        String sCadena;
        String delimitador = " ";
        int i;

        try
        {
            //Abre el fichero
            fr = new FileReader(archivo.getPath());
            BufferedReader bf = new BufferedReader(fr);

            try
            {
                i = 0;
                //Mientras queden líneas en el fichero las lee
                while ((sCadena = bf.readLine())!=null)
                {
                    //Separa los diferentes números de la cadena que ha leído
                    String[] numeros = sCadena.split(delimitador);
                    
                    for (int j = 0; j < numeros.length; j++) {
                        m_tablero[i][j] = Integer.parseInt(numeros[j]);
                    }
                    
                    i++;
                }
              //Si falla la lectura del fichero
            } catch (IOException e1)
              {
                System.out.println("Error en la lectura del fichero:"+archivo.getName());
            }
          //Si falla la apertura del fichero
        } catch (FileNotFoundException e2)
        {
			System.out.println("Error al abrir el fichero: "+archivo.getName());
	}finally{
         // Cerramos el fichero en finally porque así nos aseguramos que se cierra tanto si todo ha ido bien, como
         // si ha saltado alguna excepción
            try{
                if( null != fr )
                {
                    fr.close();
                }
                }catch (Exception e3){
                    System.out.println("Error al cerrar el fichero: "+archivo.getName());
                }
        }
    }

    /**
     * Deja todo el tablero con valor 0
     */
    public void LimpiarTablero()
    {
        //Recorre el tablero
        for (int i=0; i<9; i++)
        {
            for(int j=0; j<9; j++)
                m_tablero[i][j] = 0;
        }
    }

    /**
     * Comprueba si el tablero está vacío
     */
    public boolean TableroVacio()
    {
        boolean vacio;
        vacio = true;

        //Recorre el tablero
        for (int i=0; i<9; i++)
        {
            for(int j=0; j<9; j++)
                if(m_tablero[i][j] != 0)
                    vacio = false;
        }

        return vacio;
    }
    
    /**
     * Comprueba si en una submatriz hay elemento repetidos
     */
    public boolean revisaSubmatriz (int fila, int col){
        int i,j, central;
        boolean distinto;
        
        distinto= true;
        central=m_tablero[fila][col];
        i=fila-1;
        while (i<=fila+1 && distinto){
            j=col-1;
            while (j<=col+1 && distinto){
                if (i!=fila && j!=col && central==m_tablero[i][j])
                    distinto=false;            
                j++;
            }
            i++;
        }
        return distinto;               
    }

    /**
     * Comprueba si la solución dada es correcta. Cada cuadrícula, cada fila y cada columna debe contener los números del 1 a 9
     */
    public boolean TableroCorrecto()
    {
        int casilla;
        boolean correcto;
        correcto = true;

        //Recorre todo el tablero
        for(int i=0; i<9; i++)
            for(int j=0;j<9; j++)
            {
                casilla = m_tablero[i][j];

                if (casilla == 0)
                    return false;

                //Comprueba que ese número no esté repetido en la fila
                for(int z=0; z<9; z++)
                {
                    if( j!=z)
                        if(casilla == m_tablero[i][z])
                        {
                            correcto = false;
                            return correcto;
                        }
                }

                //Comprueba que ese número no esté repetido en la columna
                for(int z=0; z<9; z++)
                {
                    if( i!=z)
                        if(casilla == m_tablero[z][j])
                        {
                            correcto = false;
                            return correcto;
                        }
                }
                
            } 
        //revisar submatrices
        for (int i=1; i<=7; i+=3)
            for (int j=1; j<=7; j+=3)
                if (revisaSubmatriz (i, j)==false){
                    correcto = false;
                    return correcto;
                }
        
        return correcto;
    }
}
