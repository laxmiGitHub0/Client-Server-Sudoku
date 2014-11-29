/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author venetay
 */
public class SudokuBoardPanel extends JPanel {

    private SudokuCell board[][];
    private GridLayout gridLayoutBoard;
    private int countZero;
    private static int countValidEnters;
    private Stack undo;
    private Stack redo;

    private class KeyPressHandler extends KeyAdapter implements KeyListener {

        private SudokuCell baseCell;
        private char chr[] = new char[10];

        private boolean validKey() {

            int intChar = Character.getNumericValue(chr[0]);

            if ((intChar < 1) || (intChar > 9)) {
                return false;
            }


            int row = baseCell.getRow();
            int col = baseCell.getCol();

            for (int i = 0; i < 9; i++) {
                System.out.printf("%s", (board[row][i]).getText());
                if (chr[0] == (((board[row][i]).getText().equals("")) ? '?' :
                    (board[row][i]).getText().toCharArray()[0])) {
                    return false;
                }
            }

            for (int j = 0; j < 9; j++) {
                if (chr[0] == (((board[j][col]).getText().equals("")) ? '?'
                        : (board[j][col]).getText().toCharArray()[0])) {
                    return false;
                }
            }

            int m = 3 * (row / 3);
            int n = 3 * (col / 3);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (chr[0] == (((board[m + i][n + j]).getText().equals("")) ? '?'
                            : (board[m + i][n + j]).getText().toCharArray()[0])) {
                        return false;
                    }
                }
            }


            board[row][col].setText(Character.toString(chr[0]));
            countValidEnters++;
            return true;
        }

        @Override
        public void keyTyped(KeyEvent event) {

            int r = ((SudokuCell) event.getSource()).getRow();
            int c = ((SudokuCell) event.getSource()).getCol();

            baseCell = new SudokuCell("", r, c);

            StringBuilder sb = new StringBuilder();
            sb.append(event.getKeyChar());
            baseCell.setText(sb.toString());
           

            chr[0] = event.getKeyChar();

            if (validKey()) {
                SudokuClient.setStatusBar("Valid! Fill next empty.");
                undo.push((SudokuCell) baseCell);
                
                System.out.printf("countValidEnters=%d, countZero=%d\n", countValidEnters, countZero);
                System.out.printf("%s, x=%d, y=%d\n", ((SudokuCell) undo.peek()).getText(), ((SudokuCell) undo.peek()).getCol(), ((SudokuCell) undo.peek()).getRow());
                result();
            } else {
                SudokuClient.setStatusBar("Sorry. This digit already exist or is not valid character.");
                event.consume();
            }

        }

        private void result() {
            if (countValidEnters == countZero) {
                JOptionPane.showMessageDialog(null, "Congratulations!",
                        "Win", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    public SudokuBoardPanel() {

        countZero = 0;
        countValidEnters = 0;
        undo = new Stack();
        redo = new Stack();

        KeyPressHandler handler = new KeyPressHandler();

        gridLayoutBoard = new GridLayout(9, 9);
        setLayout(gridLayoutBoard);

        board = new SudokuCell[9][9];

        for (int i = 0; i <= 8; i++) {
            for (int j = 0; j <= 8; j++) {
                board[i][j] = new SudokuCell("", i, j);
                board[i][j].addKeyListener(handler);
                add(board[i][j]);
            }
        }

    }

    public void setBoard(char[] b) {

        countZero = 0;
        countValidEnters = 0;
        undo = new Stack();
        redo = new Stack();

        for (int i = 0; i < b.length; i++) {
            if (b[i] == '0') {
                b[i] = ' ';
                countZero++;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(b[i]);
            board[i / 9][i % 9].setText(sb.toString());

        }
    }

    public void undo() {

        if (!undo.empty()) {
            
            int col = ((SudokuCell) undo.peek()).getCol();
            int row = ((SudokuCell) undo.peek()).getRow();

            redo.push((SudokuCell) undo.pop());

            countValidEnters--;
            board[row][col].setText("");

        }
        else {
            JOptionPane.showMessageDialog(null, "No more undo", "Info undo", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void redo() {

        if (!redo.empty()) {

            String cellText = ((SudokuCell) redo.peek()).getText();
            System.out.printf("cellText=%s", cellText);
            int col = ((SudokuCell) redo.peek()).getCol();
            int row = ((SudokuCell) redo.peek()).getRow();

            undo.push((SudokuCell) redo.pop());
            countValidEnters++;
            board[row][col].setText(cellText);

        }
       else {
            JOptionPane.showMessageDialog(null, "No more redo",
                    "Info redo", JOptionPane.PLAIN_MESSAGE);
        }
    }


}
