

import java.awt.BorderLayout;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class SudokuServer extends JFrame {

    private JTextArea outputArea;
    private Player[] players;
    private ServerSocket server; // server socket to connect with clients
    private ExecutorService runGame; // will run players

    public SudokuServer() {
        super("Sudoku Server"); // set title of window

        // create ExecutorService with a thread for each player
        runGame = Executors.newFixedThreadPool(100);

        players = new Player[100]; // create array of players

        try {
            server = new ServerSocket(3754, 10); // set up ServerSocket
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.exit(1);
        }

        outputArea = new JTextArea(); // create JTextArea for output
        add(outputArea, BorderLayout.CENTER);
        outputArea.setText("Server waiting sudoku players connections...\n");

        setSize(300, 300); // set size of window
        setVisible(true); // show window
    } // end SudokuServer constructor

    public void execute() {

        // wait for each client to connect
        for (int i = 0; i < players.length; i++) {
            try // wait for connection, create Player, start runnable
            {
                players[i] = new Player(server.accept());
                runGame.execute(players[i]); // execute player runnable
            } // end try
            catch (IOException ioException) {
                ioException.printStackTrace();
                System.exit(1);
            }
        }
    }

    // display message in outputArea
    private void displayMessage(final String messageToDisplay) {
        // display message from event-dispatch thread of execution
        SwingUtilities.invokeLater(
                new Runnable() {

                    public void run() // updates outputArea
                    {
                        outputArea.append(messageToDisplay); // add message
                    }
                });
    }

    // private inner class Player manages each Player as a runnable
    private class Player implements Runnable {

        private Socket connection; // connection to client
        private ObjectInputStream input; // input from server
        private ObjectOutputStream output; // output to server
        private String level;

        // set up Player thread
        public Player(Socket socket) {

            connection = socket; // store socket for client
            try {

                output = new ObjectOutputStream(connection.getOutputStream());
                output.flush();
                input = new ObjectInputStream(connection.getInputStream());
            } catch (IOException ioException) {
                ioException.printStackTrace();
                //System.exit(1);
            }
        }

        public void run() {

//            try {
                displayMessage("New Sudoku player was connected\n");

                do{

                try {
                    level = (String) input.readObject();
                    
                    if (level.equals("Easy")) {
                        try {
                            output.writeObject("596732184871459623324816957917245836458360792263978541730684015685123479142597368");
                            output.flush();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }

                    if (level.equals("Medium")) {
                        try {
                            output.writeObject("090700100001450600320016950907200006050060090200008501039680015005023400002007060");
                            output.flush();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }

                    if (level.equals("Hard")) {
                        try {
                            output.writeObject("090000100001050600300016050907200006050060090200008501039080015005020400002007060");
                            output.flush();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }

                } catch (ClassNotFoundException ce) {
                    System.out.println("ClassNotFoundException");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }


               }while(true);
            
//            }finally {
//                try {
//                    //output.close();
//                    //input.close();
//                    connection.close(); // close connection to client
//                } // end try
//                catch (IOException ioException) {
//                    ioException.printStackTrace();
//                    System.exit(1);
//                }
//            }
                
        }
    }
}
