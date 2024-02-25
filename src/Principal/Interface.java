package Principal;

import Buscaminas.BuscaMinas;
import java.util.Scanner;

public class Interface {
    private BuscaMinas bm;

    public Interface(BuscaMinas bm) {
        this.bm = new BuscaMinas(bm);
    }

    public static void main(String[] args) {
        BuscaMinas bm = new BuscaMinas(9, 'T');
        Interface i = new Interface(bm);
//        i.mostrarTableroNoVisible();
//        System.out.println();
        bm.setTableroVisible(new int[10][10]); //No tiene efecto, ocultación OK
        i.mostrarTableroVisible();
        i.turnoUsuario();
    }

    //IMPORTANTE...
    //Método comentado porque se emplea para comprobaciones
    //Si se descomenta puedes ver el tablero final antes de empezar a jugar
    //Para ello, únicamente hay que descomentar este método, lo que aparece comentado en el main y el getter
    //de la clase BuscaMinas
    /*
    public void mostrarTableroNoVisible() {
        for (int[] row : bm.getTableroNoVisible()) {
            System.out.print("\t| ");
            for (int col : row) {
                System.out.print(col + " | ");
            }
            System.out.println();
        }
    }
    */

    public void mostrarTableroVisible() {
        System.out.println("\t=========================================");
        System.out.println("\t================ TABLERO ================");
        System.out.println("\t=========================================");
        for (int[] row : this.bm.getTableroVisible()) {
            System.out.print("\t| ");
            for (int col : row) {
                System.out.print((char) col + " | ");
            }
            System.out.println();
        }
    }

    public void turnoUsuario() {
        Scanner sc = new Scanner(System.in);
        int row, col;
        do {
            System.out.println("\n\tIndica la casilla que quieres destapar");
            System.out.print("\t\tFila[0-9]: ");
            row = sc.nextInt();
            System.out.print("\t\tColumna[0-9]: ");
            col = sc.nextInt();
            System.out.println();
            this.bm.destaparCeldasTableroVisible(row, col);
            mostrarTableroVisible();
        } while (!this.bm.finPartida(row, col));
        mensajeFinal(row, col);
    }

    public void mensajeFinal(int row, int col) {
        if (this.bm.ganaMaquina(row, col)) {
            System.out.println("\nHas tocado una mina... has perdido!");
        }
        if (this.bm.ganaJugador()){
            System.out.println("\nHas conseguido destapar todas las celdas sin bomba... has ganado!");
        }
    }
}