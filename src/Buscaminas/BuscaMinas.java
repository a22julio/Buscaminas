package Buscaminas;

import java.util.Arrays;
import java.util.Random;

public class BuscaMinas {
    private int[][] tableroNoVisible; //Tablero que no será visible por el usuario
    private int[][] tableroVisible; //Tablero que se le muestra al usuario
    private final int MINA; //Caracter asociado a una celda en la que hay una mina
    private final char TERRENO; //Caracter asociado a una celda sin destapar

    /**
     * Método que devuelve el valor del atributo tableroVisible
     *
     * @return el valor de tablero visible
     */
    public int[][] getTableroVisible() {
        return this.tableroVisible;
    }

    /**
     * Método que permite modificar el atributo tableroVisible
     * @param tableroVisible tablero visible por el usuario
     */
    public void setTableroVisible(int[][] tableroVisible) {
        this.tableroVisible = tableroVisible;
    }
//    public int[][] getTableroNoVisible() { return this.tableroNoVisible; }

    /**
     * Método que construye dos tableros de 10x10, uno visible para el usuario y otro que no lo es. Igualmente,
     * este método asigna un caracter para las minas y otro para las celdas no destapadas.
     *
     * @param mina caracter que se asociará a las minas
     * @param terreno caracter que se asociará a una celda no destapada
     */
    public BuscaMinas(int mina, char terreno) {
        this.tableroNoVisible = new int[10][10];
        this.tableroVisible = new int[10][10];
        this.MINA = mina;
        this.TERRENO = terreno;
        this.construirTableroVisible();
        this.construirTableroNoVisible();
    }

    /**
     * Método que crea una copia de un objeto BuscaMinas
     * @param bm objeto BuscaMinas
     */
    public BuscaMinas(BuscaMinas bm) {
        this.tableroNoVisible = bm.tableroNoVisible;
        this.tableroVisible = bm.tableroVisible;
        this.MINA = bm.MINA;
        this.TERRENO = bm.TERRENO;
    }

    private void construirTableroVisible() {
        for (int[] celda : this.tableroVisible) {
            Arrays.fill(celda, this.TERRENO); //Empleo Arrays.fill() en lugar de un segundo bucle por ser más eficiente
        }
    }

    private void construirTableroNoVisible() {
        this.minarTableroNoVisible();
        this.completarTableroNoVisible();
    }

    //Método que sitúa en el tablero 10 minas en posiciones aleatorias
    private void minarTableroNoVisible() {
        Random r = new Random();
        int contador = 0;
        //Incluimos 10 minas por defecto en el tablero
        while (contador < 10) {
            int row = r.nextInt(10);
            int col = r.nextInt(10);
            //Aseguramos que nunca se situarán dos minas en una misma posición
            if (this.tableroNoVisible[row][col] != this.MINA) {
                this.tableroNoVisible[row][col] = this.MINA;
                contador++;
            }
        }
    }

    //Método que permite completar el tablero no visible en función de la posición de las minas
    private void completarTableroNoVisible() {
        for (int i = 0; i < this.tableroNoVisible.length; i++) {
            for (int j = 0; j < this.tableroNoVisible[i].length; j++) {
                if (this.tableroNoVisible[i][j] != this.MINA) {
                    this.contarMinasTableroNoVisible(i, j);
                }
            }
        }
    }

    //Método que cuenta las minas que hay alrededor de una celda para ajustar los valores del tablero no visible
    private void contarMinasTableroNoVisible(int row, int col) {
        int numMinas = 0;
        for (int i = Math.max(0, row - 1); i < Math.min(this.tableroNoVisible.length, row + 2); i++) {
            for (int j = Math.max(0, col - 1); j < Math.min(this.tableroNoVisible[i].length, col + 2); j++) {
                if (this.tableroNoVisible[i][j] == this.MINA) {
                    numMinas++;
                }
            }
        }
        this.tableroNoVisible[row][col] = numMinas;
    }

    /**
     * Método recursivo que se encarga de destapar celdas del tablero visible. Cuando en la posición que se recibe
     * por parámetro hay un 0 (en el tablero no visible), se recorre el tablero en todas las direcciones y se van
     * levantando las celdas del tablero visible hasta llegar a un número distinto de 0.
     *
     * @param row indica la fila del tablero
     * @param col indica la columna del tablero
     */
    public void destaparCeldasTableroVisible(int row, int col) {
        if (row < 0 || row > this.tableroNoVisible.length - 1 || col < 0 || col > this.tableroNoVisible[row].length - 1) {
            return;
        }
        if (this.tableroVisible[row][col] != this.TERRENO) {
            return;
        }

        //Asigno a la celda correspondiente el caracter que le corresponde
        //Sumo + '0' para convertir el valor a caracter y que se visualice en el tablero correctamente
        this.tableroVisible[row][col] = (char) (this.tableroNoVisible[row][col] + '0');

        //Si el caracter es 0, destapo recursivamente todas las celdas adyacentes
        if (this.tableroNoVisible[row][col] == 0) {
            destaparCeldasTableroVisible(row - 1, col);
            destaparCeldasTableroVisible(row, col + 1);
            destaparCeldasTableroVisible(row + 1, col);
            destaparCeldasTableroVisible(row, col - 1);
            destaparCeldasTableroVisible(row + 1, col + 1);
            destaparCeldasTableroVisible(row - 1, col - 1);
            destaparCeldasTableroVisible(row + 1, col - 1);
            destaparCeldasTableroVisible(row - 1, col + 1);
        }
    }

    /**
     * Método que indica si la partido ha finalizado o no
     *
     * @param row indica la fila del tablero
     * @param col indica la columna del tablero
     * @return devuelve true si ha finalizado la partida o false en caso contrario
     */
    public boolean finPartida(int row, int col) {
        return ganaMaquina(row, col) || ganaJugador();
    }

    /**
     * Método que indica si la máquina ha ganado la partida
     *
     * @param row indica la fila del tablero
     * @param col indica la columna del tablero
     * @return true si la máquina gana la partida y false en caso contrario
     */
    public boolean ganaMaquina(int row, int col) {
        return this.tableroNoVisible[row][col] == this.MINA;
    }

    /**
     * Método que indica si el jugador ha ganado la partida
     *
     * @return true si el jugador gana la partida y false en caso contrario
     */
    public boolean ganaJugador() {
        return !quedanCeldasPorLevantar();
    }

    //Método que cuenta el número de celdas tapadas y devuelve true en caso de que sean 10
    private boolean quedanCeldasPorLevantar() {
        int numMinas = 0;
        etiqueta:
        for (int[] row : this.tableroVisible) {
            for (int celda : row) {
                if (celda == this.TERRENO) {
                    numMinas++;
                    if (numMinas > 10) {
                        break etiqueta; //Permite ganar eficiencia al salir de ambos bucles si hay más de 10 bombas
                    }
                }
            }
        }
        return numMinas != 10;
    }
}