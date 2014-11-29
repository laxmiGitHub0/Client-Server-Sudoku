/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.swing.JFrame;

/**
 *
 * @author venetay
 */
public class SudokuClientStart {

    public static void main(String[] args) {

        SudokuClient sudokuApp;

        if (args.length == 0) {
            sudokuApp = new SudokuClient("127.0.0.1");
        } else {
            sudokuApp = new SudokuClient(args[0]);
        }

        sudokuApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sudokuApp.setSize(350, 400);
        sudokuApp.setVisible(true);

    }
}
