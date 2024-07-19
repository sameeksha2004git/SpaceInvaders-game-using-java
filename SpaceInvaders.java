import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
    // board
    int tileDimension = 32;
    int numRows = 16;
    int numColumns = 16;

    int boardWidth = tileDimension * numColumns; // 32 * 16
    int boardHeight = tileDimension * numRows; // 32 * 16

    Image spaceshipImage;
    Image alienImage;
    Image alienCyanImage;
    Image alienMagentaImage;
    Image alienYellowImage;
    ArrayList<Image> alienImagesList;

    class Block {
        int posX;
        int posY;
        int blockWidth;
        int blockHeight;
        Image blockImage;
        boolean isAlive = true; // used for aliens
        boolean isUsed = false; // used for bullets

        Block(int posX, int posY, int blockWidth, int blockHeight, Image blockImage) {
            this.posX = posX;
            this.posY = posY;
            this.blockWidth = blockWidth;
            this.blockHeight = blockHeight;
            this.blockImage = blockImage;
        }
    }

    // ship
    int spaceshipWidth = tileDimension * 2;
    int spaceshipHeight = tileDimension;
    int spaceshipX = tileDimension * numColumns / 2 - tileDimension;
    int spaceshipY = tileDimension * numRows - tileDimension * 2;
    int spaceshipVelocityX = tileDimension; // ship moving speed
    Block spaceship;

    // aliens
    ArrayList<Block> aliensList;
    int alienWidth = tileDimension * 2;
    int alienHeight = tileDimension;
    int alienPosX = tileDimension;
    int alienPosY = tileDimension;

    int numAlienRows = 2;
    int numAlienColumns = 3;
    int alienCounter = 0; // number of aliens to defeat
    int alienVelocityX = 1; // alien moving speed

    // bullets
    ArrayList<Block> bulletsList;
    int bulletWidth = tileDimension / 8;
    int bulletHeight = tileDimension / 2;
    int bulletVelocityY = -10; // bullet moving speed

    Timer gameLoopTimer;
    boolean isGameOver = false;
    int playerScore = 0;

    SpaceInvaders() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        // load images
        spaceshipImage = new ImageIcon(getClass().getResource("./ship.png")).getImage();
        alienImage = new ImageIcon(getClass().getResource("./alien.png")).getImage();
        alienCyanImage = new ImageIcon(getClass().getResource("./alien-cyan.png")).getImage();
        alienMagentaImage = new ImageIcon(getClass().getResource("./alien-magenta.png")).getImage();
        alienYellowImage = new ImageIcon(getClass().getResource("./alien-yellow.png")).getImage();

        alienImagesList = new ArrayList<Image>();
        alienImagesList.add(alienImage);
        alienImagesList.add(alienCyanImage);
        alienImagesList.add(alienMagentaImage);
        alienImagesList.add(alienYellowImage);

        spaceship = new Block(spaceshipX, spaceshipY, spaceshipWidth, spaceshipHeight, spaceshipImage);
        aliensList = new ArrayList<Block>();
        bulletsList = new ArrayList<Block>();

        // game timer
        gameLoopTimer = new Timer(1000 / 60, this); // 1000/60 = 16.6
        createAliens();
        gameLoopTimer.start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics) {
        // ship
        graphics.drawImage(spaceship.blockImage, spaceship.posX, spaceship.posY, spaceship.blockWidth,
                spaceship.blockHeight, null);

        // aliens
        for (int i = 0; i < aliensList.size(); i++) {
            Block alien = aliensList.get(i);
            if (alien.isAlive) {
                graphics.drawImage(alien.blockImage, alien.posX, alien.posY, alien.blockWidth, alien.blockHeight, null);
            }
        }

        // bullets
        graphics.setColor(Color.white);
        for (int i = 0; i < bulletsList.size(); i++) {
            Block bullet = bulletsList.get(i);
            if (!bullet.isUsed) {
                graphics.drawRect(bullet.posX, bullet.posY, bullet.blockWidth, bullet.blockHeight);
                // graphics.fillRect(bullet.posX, bullet.posY, bullet.blockWidth,
                // bullet.blockHeight);
            }
        }

        // score
        graphics.setColor(Color.white);
        graphics.setFont(new Font("Arial", Font.PLAIN, 32));
        if (isGameOver) {
            graphics.drawString("Game Over: " + String.valueOf((int) playerScore), 10, 35);
        } else {
            graphics.drawString(String.valueOf((int) playerScore), 10, 35);
        }
    }

    public void move() {
        // alien
        for (int i = 0; i < aliensList.size(); i++) {
            Block alien = aliensList.get(i);
            if (alien.isAlive) {
                alien.posX += alienVelocityX;

                // if alien touches the borders
                if (alien.posX + alien.blockWidth >= boardWidth || alien.posX <= 0) {
                    alienVelocityX *= -1;
                    alien.posX += alienVelocityX * 2;

                    // move all aliens up by one row
                    for (int j = 0; j < aliensList.size(); j++) {
                        aliensList.get(j).posY += alienHeight;
                    }
                }

                if (alien.posY >= spaceship.posY) {
                    isGameOver = true;
                }
            }
        }

        // bullets
        for (int i = 0; i < bulletsList.size(); i++) {
            Block bullet = bulletsList.get(i);
            bullet.posY += bulletVelocityY;

            // bullet collision with aliens
            for (int j = 0; j < aliensList.size(); j++) {
                Block alien = aliensList.get(j);
                if (!bullet.isUsed && alien.isAlive && detectCollision(bullet, alien)) {
                    bullet.isUsed = true;
                    alien.isAlive = false;
                    alienCounter--;
                    playerScore += 100;
                }
            }
        }

        // clear bullets
        while (bulletsList.size() > 0 && (bulletsList.get(0).isUsed || bulletsList.get(0).posY < 0)) {
            bulletsList.remove(0); // removes the first element of the array
        }

        // next level
        if (alienCounter == 0) {
            // increase the number of aliens in columns and rows by 1
            playerScore += numAlienColumns * numAlienRows * 100; // bonus points :)
            numAlienColumns = Math.min(numAlienColumns + 1, numColumns / 2 - 2); // cap at 16/2 -2 = 6
            numAlienRows = Math.min(numAlienRows + 1, numRows - 6); // cap at 16-6 = 10
            aliensList.clear();
            bulletsList.clear();
            createAliens();
        }
    }

    public void createAliens() {
        Random random = new Random();
        for (int c = 0; c < numAlienColumns; c++) {
            for (int r = 0; r < numAlienRows; r++) {
                int randomImageIndex = random.nextInt(alienImagesList.size());
                Block alien = new Block(
                        alienPosX + c * alienWidth,
                        alienPosY + r * alienHeight,
                        alienWidth,
                        alienHeight,
                        alienImagesList.get(randomImageIndex));
                aliensList.add(alien);
            }
        }
        alienCounter = aliensList.size();
    }

    public boolean detectCollision(Block a, Block b) {
        return a.posX < b.posX + b.blockWidth && // a's top left corner doesn't reach b's top right corner
                a.posX + a.blockWidth > b.posX && // a's top right corner passes b's top left corner
                a.posY < b.posY + b.blockHeight && // a's top left corner doesn't reach b's bottom left corner
                a.posY + a.blockHeight > b.posY; // a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (isGameOver) {
            gameLoopTimer.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (isGameOver) { // any key to restart
            spaceship.posX = spaceshipX;
            bulletsList.clear();
            aliensList.clear();
            isGameOver = false;
            playerScore = 0;
            numAlienColumns = 3;
            numAlienRows = 2;
            alienVelocityX = 1;
            createAliens();
            gameLoopTimer.start();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && spaceship.posX - spaceshipVelocityX >= 0) {
            spaceship.posX -= spaceshipVelocityX; // move left one tile
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT
                && spaceship.posX + spaceshipVelocityX + spaceship.blockWidth <= boardWidth) {
            spaceship.posX += spaceshipVelocityX; // move right one tile
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // shoot bullet
            Block bullet = new Block(spaceship.posX + spaceshipWidth * 15 / 32, spaceship.posY, bulletWidth,
                    bulletHeight, null);
            bulletsList.add(bullet);
        }
    }
}
