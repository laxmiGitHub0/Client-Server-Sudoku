/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.swing.JTextField;

/**
 *
 * @author venetay
 */
public class SudokuCell extends JTextField {

    private int row;
    private int col;

    public SudokuCell(String t, int r, int c) {
        super(t, 1);
        this.setRow(c);
        this.setCol(r);
    }

    public SudokuCell() {

        super("", 1);
        this.row = 0;
        this.col = 0;

    }

    public SudokuCell(SudokuCell sc) {

        super(sc.getText(), 1);
        this.setRow(sc.getRow());
        this.setCol(sc.getCol());

    }

    public int getCol() {
        return col;
    }

    public void setCol(int c) {
        if (c >= 0 && c <= 8) {
            this.col = c;
        } else {
            this.col = 0;
        }
    }

    public int getRow() {
        return row;
    }

    public void setRow(int r) {
        if (r >= 0 && r <= 8) {
            this.row = r;
        } else {
            this.row = 0;
        }
    }
}
