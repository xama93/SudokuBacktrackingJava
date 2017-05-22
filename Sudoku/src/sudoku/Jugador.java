/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sudoku;
import java.util.*; //PREGUNTAR SI SE PUEDE HACER IMPORTS
/**
 *
 * @author Sistemas Inteligentes
 */
public class Jugador{
    
    int copiamatriz[][];
    int posicionVertices[][];
  
    /**
     * Se llama desde la clase Interfaz para ejecutar BC.
     * Al final de la función la solución del tablero se debe encontrar en tablero
     * @param tablero
     * @return
     */
    public boolean ejecutarBC(Tablero tablero)
    {
        boolean ok = false;
        
        // hago una copia de la matriz original para no
        // substituir por error las casillas ya ocupadas en un backtraking
        copiamatriz = new int[9][9];
        
        for(int i = 0; i < 9; i++) 
        {
            for(int j = 0; j < 9; j++)
            {
                copiamatriz[i][j] = tablero.getCasilla(i, j);
            }
        }
                   
        ok = backtracking(tablero,0,0);
        
        return ok;
    }
   
    /* implementacion base de backtracking y salgo si la fila sale 
    del tablero ya que me muevo con contadores */
    public boolean backtracking(Tablero tab, int fil, int col)
    {
         //comprobar si tablero esta lleno, si lo esta devuelvo true y acabo.
        if(fil == 9)
        {
            return true;
        }
        
        int filsiguiente = fil;
        int colsiguiente = col;

        if(col < 8)
        {
            colsiguiente++;
        }else
        {
            colsiguiente = 0;
            filsiguiente++;
        }
        
        if(tab.getCasilla(fil, col) != 0) //salto si esta ocupada
        {    
            if(backtracking(tab,filsiguiente,colsiguiente) == true)
            {
               return true;
            }
        }
      
        for(int num = 1; num < 10; num++)
        {
            //este if evita la substitucion por backtraking de una casilla
            //que estaba en la matriz original
            if(tab.getCasilla(fil, col) == copiamatriz[fil][col] && tab.getCasilla(fil, col) != 0)
            {
                return false; //hago backtraking
            }
            
            tab.setCasilla(num, fil, col);  //inserto
            
            
            int numactual = tab.getCasilla(fil, col);
            
            //compruebo si es valida la insercion
             if(comprobado(tab,fil,col,numactual) == true)
             {   
                 //compruebo si es valida la siguiente pos
                 if(backtracking(tab,filsiguiente,colsiguiente) == true)
                 {
                     return true;
                 }
             }
        }        
        
        //si no se llega a solucion, asigno 0
        tab.setCasilla(0, fil, col);

        //vuelvo atras. Backtrack.
        return false;        
    }
       
    /* compruebo todas las restricciones para backtrack */
    public boolean comprobado(Tablero tablero, int fil, int col,int num)
    {
        boolean ok = true;
         
        if(comprobarfila(tablero,fil,num,col) == false)
        {
            ok = false;
        }

        if(comprobarcolumna(tablero,col,num,fil) == false)
        {
            ok = false;
        }

        if(revisasubmat(tablero,fil,col) == false)
        {
            ok = false;
        }
                
         return ok;               
    }
    
    /* compruebo si la fila es correcta */
    public boolean comprobarfila(Tablero tablero, int fil,int num, int col)
    {   
        boolean ok = true;
        
        for(int i = 0; i < 9 && ok != false ; i++)
        {
            if(tablero.getCasilla(fil, i) == num && i != col)
            {
                ok = false;
            }
        }
        
        return ok;
    }
    
    /* compruebo si la columna es correcta */
    public boolean comprobarcolumna(Tablero tablero, int col,int num, int fil)
    {   
        boolean ok = true;
        
        for(int i = 0; i < 9 && ok != false ; i++)
        {
            if(tablero.getCasilla(i , col) == num && i != fil)
            {
                ok = false;
            }
        }
        
        return ok;
    }
    
    /* compruebo si la submatriz es correcta */
    public boolean revisasubmat(Tablero tab,int fil,int col)
    {      
        int minimo_fila;
        int maximo_fila;
        int minimo_columna;
        int maximo_columna;
        int num = tab.getCasilla(fil, col);
        boolean ok = true;

        //DETERMINAMOS LAS FILAS DE LA CAJA.
        if ( fil >= 0 && fil <= 2)
        {
         minimo_fila = 0;
         maximo_fila = 2;
         
        }else if ( fil >= 3 && fil <= 5 )
        {
         minimo_fila = 3;
         maximo_fila = 5;
        }else
        {
         minimo_fila = 6;
         maximo_fila = 8;
        }

        //DETERMINAMOS LAS COLUMNAS DE LA CAJA.
        if ( col >= 0 && col <= 2)
        {
         minimo_columna = 0;
         maximo_columna = 2;
        }else if ( col >= 3 && col <= 5 )
        {
         minimo_columna = 3;
         maximo_columna = 5;
        }else
        {
         minimo_columna = 6;
         maximo_columna = 8;
        }
        
        for(int i = minimo_fila; i <= maximo_fila; i++)
        {
            for(int j = minimo_columna; j <= maximo_columna; j++)
            {
                if(tab.getCasilla(i, j) == num && i != fil && j != col)
                {
                    ok = false;
                }
            }
        }
        
        return ok;
    }
    
    
     //////////////////////////////////**********************************//////////////////////////////////////////////
    
    
    //da una lista con todas las restricciones del tablero
    //el orden de la lista solo importa en cuanto a optimizacion
    public Queue damelista(Tablero tablero)
    {
        Queue listarestricciones = new LinkedList();
        
        // nos movemos por toda la matriz
        for(int i = 0; i < 9; i++) 
        {
            for(int j = 0; j < 9; j++)
            {
                int verticeactual = posicionVertices[i][j];
                int segundovertice;
                
                //RESTRICCIONES DE FILA
                for(int fila = 0; fila < 9 ; fila++) 
                {
                    //comprobar que no se hace una <v1,v1>    
                    if(fila != j)
                    {
                        segundovertice = posicionVertices[i][fila];

                        Restriccion restriccion; //llenamos la cola con las restriccciones

                        restriccion = new Restriccion(verticeactual,segundovertice);   

                        listarestricciones.add(restriccion);
                    }
                }

                //RESTRICCIONES DE COLUMNA
                for(int col = 0; col < 9; col++) 
                {
                    //comprobar que no se hace una <v1,v1>
                    if(col != i)
                    {
                        segundovertice = posicionVertices[col][j];

                        Restriccion restriccion; //llenamos la cola con las restriccciones

                        restriccion = new Restriccion(verticeactual,segundovertice);   

                        listarestricciones.add(restriccion);
                    }
                }

                //RESTRICCIONES SUBMATRIZ  
                int minimo_fila;
                int maximo_fila;
                int minimo_columna;
                int maximo_columna;

                //DETERMINAMOS LAS FILAS DE LA CAJA.
                if ( i >= 0 && i <= 2)
                {
                 minimo_fila = 0;
                 maximo_fila = 2;

                }else if ( i >= 3 && i <= 5 )
                {
                 minimo_fila = 3;
                 maximo_fila = 5;
                }else
                {
                 minimo_fila = 6;
                 maximo_fila = 8;
                }

                //DETERMINAMOS LAS COLUMNAS DE LA CAJA.
                if ( j >= 0 && j <= 2)
                {
                 minimo_columna = 0;
                 maximo_columna = 2;
                }else if ( j >= 3 && j <= 5 )
                {
                 minimo_columna = 3;
                 maximo_columna = 5;
                }else
                {
                 minimo_columna = 6;
                 maximo_columna = 8;
                }

                for(int submatx = minimo_fila; submatx <= maximo_fila; submatx++)
                {
                    for(int submaty = minimo_columna; submaty <= maximo_columna; submaty++)
                    {
                        if(submatx != i && submaty != j)
                        {
                            segundovertice = posicionVertices[submatx][submaty];

                            Restriccion restriccion; //llenamos la cola con las restriccciones

                            restriccion = new Restriccion(verticeactual,segundovertice);   

                            listarestricciones.add(restriccion);
                        } 
                    }
                }
            }
        }
        
        return listarestricciones;
    }
    
    
    //da una lista de todos los vertices y sus dominios internos de cada uno de ellos
    public Vertice[]Damelistavertices(Tablero tablero)
    {
        Vertice listavertices[];
        
        listavertices = new Vertice[81]; //una lista de 81 vertices con sus dominios
        
        int cont = 0;
        
        for(int i = 0; i < 9; i++) // nos movemos por toda la matriz
        {
            for(int j = 0; j < 9; j++)
            {
                Vertice vertice;
                int lista[];
                
                if(copiamatriz[i][j] != 0)
                {
                    lista = new int[1];
                    lista[0] = copiamatriz[i][j];
                    vertice = new Vertice(lista,i,j);
                            
                }else
                {
                    lista = new int[9];
                    
                    // si no esta el numero de serie meter 
                    // numeros del 1 al 9 para luego en ac3 podar
                    for(int k = 0; k < 9; k++) 
                    {
                        lista[k] = k + 1;
                    }
                    
                    vertice = new Vertice(lista,i,j);
                }
                
                listavertices[cont] = vertice; //llenamos la lista de vertices con sus dominios
                cont++;
            }
         } 
        
        return listavertices;
    }
    
    //borra un dominio de la lista del vertice creando una nueva lista donde
    //no esta el elemento que se quiere eliminar
    public void borraDominio(Vertice vertice, int numero)
    {
        int nuevalista[];
        //la variable auxiliar solo tomara hasta valor 1 esta variable
        //nos sirve para controlar que no haya casillas con
        //valor por defecto 0 en el vector de dominios del vertice
        int auxiliar = 0;
        
        
        nuevalista = new int[vertice.listadominio.length - 1];
        
        for(int i = 0; i < nuevalista.length; i++)
        {
            if(vertice.listadominio[i] != numero)
            {
                nuevalista[i] = vertice.listadominio[i + auxiliar];
                
            }else
            {
                nuevalista[i] = vertice.listadominio[i + 1];
                auxiliar++;
            }
        }
        vertice.setLista(nuevalista);
    }
    
    //comprueba que el otro vertice tiene opcion a cambio
    //sino la tiene devuelvo false
    public boolean esConsistente(int dominio,Vertice vertice2)
    {
        boolean consistente =  true;
        int cont = 0;
        
        for(int i = 0; i < vertice2.listadominio.length && consistente == true; i++)
        {        
            if(vertice2.listadominio[i] != dominio)
            {
                cont++;
            }
        }
            
        if(cont == 0)
        {
            consistente = false;
        }
        
        return consistente;
    }
    
    //devuelve una lista auxiliar con las nuevas restricciones en cabeza
    public Queue nuevasrestricciones(Queue listarestricciones,Vertice vertice)
    {
        Queue listaaux = listarestricciones;
        
        int coor1 = vertice.posX;
        int coor2 = vertice.posY;
        int minimo_fila;
        int maximo_fila;
        int minimo_columna;
        int maximo_columna;
        
        int verticeactual = posicionVertices[coor1][coor2];
        int segundovertice;
        
        //DETERMINAMOS LAS FILAS DE LA CAJA.
        if ( coor1 >= 0 && coor1 <= 2)
        {
         minimo_fila = 0;
         maximo_fila = 2;
         
        }else if ( coor1 >= 3 && coor1 <= 5 )
        {
         minimo_fila = 3;
         maximo_fila = 5;
        }else
        {
         minimo_fila = 6;
         maximo_fila = 8;
        }

        //DETERMINAMOS LAS COLUMNAS DE LA CAJA.
        if ( coor2 >= 0 && coor2 <= 2)
        {
         minimo_columna = 0;
         maximo_columna = 2;
        }else if ( coor2 >= 3 && coor2 <= 5 )
        {
         minimo_columna = 3;
         maximo_columna = 5;
        }else
        {
         minimo_columna = 6;
         maximo_columna = 8;
        }
        
        //meto restricciones de submatriz
        for(int i = minimo_fila; i <= maximo_fila; i++)
        {
            for(int j = minimo_columna; j <= maximo_columna; j++)
            {
                if(coor1 != i && coor2 != j)//control para evitar <v1,v1>
                {
                    segundovertice = posicionVertices[i][j];

                    Restriccion restriccion; 

                    restriccion = new Restriccion(verticeactual,segundovertice);

                    listaaux.add(restriccion);
                }
            }
        }
                
        //restricciones para la fila
        for(int fil = 0; fil < 9; fil++)
        {
            if(fil != coor2) //control para evitar <v1,v1>
            {
                segundovertice = posicionVertices[coor1][fil];

                Restriccion restriccion; 

                restriccion = new Restriccion(verticeactual,segundovertice);

                listaaux.add(restriccion);
            }
        }
        
        //restricciones para la columna
        for(int col = 0; col < 9; col++)
        {
            if(col != coor1)
            {
                segundovertice = posicionVertices[col][coor2];

                Restriccion restriccion; 

                restriccion = new Restriccion(verticeactual,segundovertice);

                listaaux.add(restriccion);
            }
        }   
        
        return listaaux;
    }
      
    
    //ejecuta el algoritmo ac3     
    public boolean ejecutarAC(Tablero tablero)
    {
        
        //relleno una matriz auxiliar para comprobar las llenas por defecto
        copiamatriz = new int[9][9]; 
        
        for(int i = 0; i < 9; i++) 
        {
            for(int j = 0; j < 9; j++)
            {
                copiamatriz[i][j] = tablero.getCasilla(i, j);
            }
        }
        
        //matriz auxiliar donde solo estan los numeros equivalentes de los vertices
        //si esta matriz se enumenara de 0 a 80(81 vertices totales)
        posicionVertices = new int[9][9];
        int cont = 0;
        
        for(int i = 0; i < 9; i++) 
        {
            for(int j = 0; j < 9; j++)
            {
                posicionVertices[i][j] = cont;
                
                cont++;
            }
        }
        
        Queue listarestricciones;  //esto es Q en el pseudocodigo
        
        listarestricciones = damelista(tablero); //asigno Q
        
        Vertice listavertices[];
        
        listavertices = Damelistavertices(tablero); //me da la lista de vertices y sus restricciones
        
        //mientras que Q no este vacia
        while(listarestricciones.isEmpty() == false)
        {
            Restriccion restriccion;
            restriccion = (Restriccion) listarestricciones.element(); //cojo la 1º restriccion en la lista
            
            int vertice1 = restriccion.x; // me quedo con sus vertices
            int vertice2 = restriccion.y;
                    
            listarestricciones.remove(); //borro la resriccion
            
            boolean cambio = false;
            
            //para cada uno de los dominios del vertice1 de la restriccion
            for(int i = 0; i < listavertices[vertice1].listadominio.length; i++)
            {
                //compruebo si al seleccionar uno de ellos, en el vertice2 hay otra opcion disponible
                if(esConsistente(listavertices[vertice1].listadominio[i],listavertices[vertice2]) == false)
                {
                    //en caso de no haberla borro el dominio del vertice1
                    borraDominio(listavertices[vertice1],listavertices[vertice1].listadominio[i]);
                    cambio = true;
                } 
            }
            
            
            //si el vertice1 se queda sin dominios el problema no tiene solucion
            if(listavertices[vertice1].listadominio.length == 0) 
            {
                return false;
            }
            
            //si el cambio esta a true añadir a Q todos los vertices que 
            //colisionen con vertice1.
            if(cambio == true)
            {
                Queue listaaux;
               
                listaaux = nuevasrestricciones(listarestricciones,listavertices[vertice1]);
                
                listarestricciones = listaaux;
            }               
        }
        
        //imprimimos la lista de restricciones
        for(int posi = 0; posi < listavertices.length ; posi++)
        {
            System.out.print("vertice numero" + posi + ": ");
            System.out.println(Arrays.toString(listavertices[posi].listadominio));            
        }
        
        ejecutarBC(tablero);
        
        return true;
    }
    /*
    Q = {c(e p ) = <V i , V j >|e p E, i ≠ j}
        
        Mientras Q ! Ø hacer
                
            <V k , V m > = seleccionar_y_borrar(Q)
            cambio = falso
            Para todo v k D k hacer
                Si no_consistente (v k , D m ) entonces
                borrar (v k , D k )
                cambio = cierto
            FinSi
            FinPara
            Si Dk = Ø entonces salir_sin_solución FinSi
            Si cambio = cierto entonces
                 Q = Q Ĳ {c(e r ) = <V i , V k > | e r  E, i ≠ k, i ≠ m}
            FinSi
        FinMientras
    */
    public class Vertice
    {
        int listadominio[];
        
        int posX;
        
        int posY;
        
        public Vertice(int lista[], int fil, int col)
        {
            listadominio = new int[lista.length];
                    
            System.arraycopy(lista, 0, listadominio, 0, lista.length);
            
            posX = fil;
            posY = col;
        }
        
        public int[] getLista()
        {
            return listadominio;
        }
        
        public void setLista(int lista[])
        {
            listadominio = new int[lista.length];
                    
            System.arraycopy(lista, 0, listadominio, 0, lista.length);
        }
    }
    
    public class Restriccion
    {
        int x;
        int y;
        
        public Restriccion(int num1,int num2)
        {
            x = num1;
            y = num2;
        }
        
        public int getX()
        {
            return x;
        }
        
        public int getY()
        {
            return y;
        }
        
        public void setX(int num)
        {
            this.x = num;
        }
        
        public void setY(int num)
        {
            this.y = num;
        }
    }
}





  