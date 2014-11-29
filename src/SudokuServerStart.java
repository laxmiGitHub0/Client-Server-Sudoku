

import javax.swing.JFrame;

public class SudokuServerStart {

    public static void main(String args[]) {
        SudokuServer application = new SudokuServer();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.execute();
    }
}
