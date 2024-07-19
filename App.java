import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        // window variables
        int tileDimension = 32;
        int numRows = 16;
        int numColumns = 16;

        int boardWidth = tileDimension * numColumns; // 32 * 16
        int boardHeight = tileDimension * numRows;

        JFrame frame = new JFrame("Space-Invaders");
        // frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SpaceInvaders spaceInvaders = new SpaceInvaders();
        frame.add(spaceInvaders);
        frame.pack();
        spaceInvaders.requestFocus();
        frame.setVisible(true);

    }
}