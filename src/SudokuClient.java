/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author venetay
 */
public class SudokuClient extends JFrame implements Runnable {

    private SudokuBoardPanel pnlMain;
    private static JLabel statusBar;
    private Socket client; // client to server
    private ObjectInputStream input; // input from server
    private ObjectOutputStream output; // output to server
    private String sudokuHost;
    private SocketAddress serverAddres;
    private BorderLayout layout;
    private EasyHandler handlerEasy;
    private MediumHandler handlerMedium;
    private HardHandler handlerHard;
    private JMenu fileMenu;
    private JMenuItem menItmMedium;
    private JMenuItem menItmEasy;
    private JMenuItem menItmHard;
    private JMenuItem menItmExit;
    private JMenuBar menuBar;
    private JMenu editMenu;
    private JMenuItem menItmUndo;
    private JMenuItem menItmRedo;
    private JMenu aboutMenu;
    private JMenuItem menItmAbout;

    private class EasyHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            try {

                if(!client.isConnected()) client.connect(serverAddres);

                output.writeObject("Easy");
                output.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            try {
                String level = (String) input.readObject();
                //System.out.printf("Client %s", level);
                setBoard(pnlMain, level);
            } catch (ClassNotFoundException ce) {
                System.out.println("ClassNotFoundException");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
//            finally {
//                try {
//                    output.close();
//                    input.close();
//                    client.close();
//                } catch (IOException ioe) {
//                    ioe.printStackTrace();
//                    System.exit(1);
//                }
//            }

        }
    }

    private class MediumHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {

                output.writeObject("Medium");
                output.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            try {
                String level = (String) input.readObject();
                setBoard(pnlMain, level);
            } catch (ClassNotFoundException ce) {
                System.out.println("ClassNotFoundException");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
//            finally {
//                try {
//                    //output.close();
//                    //input.close();
//                    client.close();
//                } catch (IOException ioe) {
//                    ioe.printStackTrace();
//                    System.exit(1);
//                }
//            }

        }
    }

    private class HardHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
           
            try {

                System.out.println("!!!!!");
                output.writeObject("Hard");
                output.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            try {
                 String level = (String) input.readObject();
                setBoard(pnlMain, level);
            } catch (ClassNotFoundException ce) {
                System.out.println("ClassNotFoundException");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
//            finally {
//                try {
//                    output.close();
//                    input.close();
//                    client.close();
//                } catch (IOException ioe) {
//                    ioe.printStackTrace();
//                    System.exit(1);
//                }
//            }

        }
    }

    public SudokuClient(String host) {

        super("Game Sudoku");

        sudokuHost = host;

        handlerEasy = new EasyHandler();
        handlerMedium = new MediumHandler();
        handlerHard = new HardHandler();

        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        menItmEasy = new JMenuItem("New Easy Game");
        menItmEasy.setMnemonic('N');
        fileMenu.add(menItmEasy);
        menItmEasy.addActionListener(handlerEasy);

        menItmMedium = new JMenuItem("New Medium Game");
        menItmMedium.setMnemonic('M');
        fileMenu.add(menItmMedium);
        menItmMedium.addActionListener(handlerMedium);

        menItmHard = new JMenuItem("New Hard Game");
        menItmHard.setMnemonic('H');
        fileMenu.add(menItmHard);
        menItmHard.addActionListener(handlerHard);

        menItmExit = new JMenuItem("Exit");
        menItmExit.setMnemonic('x');
        fileMenu.add(menItmExit);
        menItmExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(fileMenu);

        editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');

        menItmUndo = new JMenuItem("Undo");
        menItmUndo.setMnemonic('U');
        editMenu.add(menItmUndo);
        menItmUndo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //undo(pnlMain);
                pnlMain.undo();
            }
        });

        menItmRedo = new JMenuItem("Redo");
        menItmRedo.setMnemonic('R');
        editMenu.add(menItmRedo);
        menItmRedo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pnlMain.redo();
                //redo(pnlMain);
            }
        });

        menuBar.add(editMenu);

        aboutMenu = new JMenu("About");
        aboutMenu.setMnemonic('A');

        menItmAbout = new JMenuItem("Game");
        menItmAbout.setMnemonic('G');
        aboutMenu.add(menItmAbout);
        menItmAbout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(pnlMain, "Sudoku Game v0.1",
                        "About", JOptionPane.PLAIN_MESSAGE);
            }
        });

        menuBar.add(aboutMenu);

        layout = new BorderLayout();
        setLayout(layout);

        statusBar = new JLabel("Enter one digit between 1 and 9");

        pnlMain = new SudokuBoardPanel();

        add(pnlMain, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        startClient();

    }

    public void startClient() {
        try {
            client = new Socket(sudokuHost, 3754);
            serverAddres = client.getRemoteSocketAddress();

            // get streams for input and output

            output = new ObjectOutputStream(client.getOutputStream());
            output.flush();

            input = new ObjectInputStream(client.getInputStream());


        } catch (IOException ex) {
            ex.printStackTrace();
        }
//            finally {
//            try {
//                output.close();
//                input.close();
//                client.close();
//            } catch (IOException ioe) {
//                ioe.printStackTrace();
//                System.exit(1);
//            }
//        }

        ExecutorService es = Executors.newFixedThreadPool(100);
        es.execute(this);

    }

    public void run() {

        SwingUtilities.invokeLater(
                new Runnable() {

                    public void run() {

                        setStatusBar("New game start");

                    }
                });

    }

    private void setBoard(final SudokuBoardPanel bp, final String num) {

        SwingUtilities.invokeLater(
                new Runnable() {

                    public void run() {
                        bp.setBoard(num.toCharArray());
                    }
                });
    }

    private void undo(final SudokuBoardPanel bp) {

        SwingUtilities.invokeLater(
                new Runnable() {

                    public void run() {
                        bp.undo();
                    }
                });
    }

    private void redo(final SudokuBoardPanel bp) {

        SwingUtilities.invokeLater(
                new Runnable() {

                    public void run() {
                        bp.redo();
                    }
                });
    }

    public static void setStatusBar(String s) {
        statusBar.setText(s);
    }
}
