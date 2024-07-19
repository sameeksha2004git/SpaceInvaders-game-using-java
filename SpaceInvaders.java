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

    int boardWidth = tileDimension * numColumns;
    int boardHeight = tileDimension * numRows;

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
        boolean isAlive = true;
        boolean isUsed = false;

        Block(int posX, int posY, int blockWidth, int blockHeight, Image blockImage) {
            this.posX = posX;
            this.posY = posY;
            this.blockWidth = blockWidth;
            this.blockHeight = blockHeight;
            this.blockImage = blockImage;
        }
    }

     
    int spaceshipWidth = tileDimension * 2;
    int spaceshipHeight = tileDimension;
    int spaceshipX = tileDimension * numColumns / 2 - tileDimension;
    int spaceshipY = tileDimension * numRows - tileDimension * 2;
    int spaceshipVelocityX = tileDimension;  
    Block spaceship;

     
    ArrayList<Block> aliensList;
    int alienWidth = tileDimension * 2;
    int alienHeight = tileDimension;
    int alienPosX = tileDimension;
    int alienPosY = tileDimension;

    int numAlienRows = 2;
    int numAlienColumns = 3;
    int alienCounter = 0;  
    int alienVelocityX = 1;  

     
    ArrayList<Block> bulletsList;
    int bulletWidth = tileDimension / 8;
    int bulletHeight = tileDimension / 2;
    int bulletVelocityY = -10;  

    Timer gameLoopTimer;
    boolean isGameOver = false;
    int playerScore = 0;

    SpaceInvaders() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

         
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

         
        gameLoopTimer = new Timer(1000 / 60, this);  
        createAliens();
        gameLoopTimer.start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics) {
         
        graphics.drawImage(spaceship.blockImage, spaceship.posX, spaceship.posY, spaceship.blockWidth,
                spaceship.blockHeight, null);

         
        for (int i = 0; i < aliensList.size(); i++) {
            Block alien = aliensList.get(i);
            if (alien.isAlive) {
                graphics.drawImage(alien.blockImage, alien.posX, alien.posY, alien.blockWidth, alien.blockHeight, null);
            }
        }

         
        graphics.setColor(Color.white);
        for (int i = 0; i < bulletsList.size(); i++) {
            Block bullet = bulletsList.get(i);
            if (!bullet.isUsed) {
                graphics.drawRect(bullet.posX, bullet.posY, bullet.blockWidth, bullet.blockHeight);
                
                // 
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
     
        for (int i = 0; i < aliensList.size(); i++) {
            Block alien = aliensList.get(i);
            if (alien.isAlive) {
                alien.posX += alienVelocityX;

                 
                if (alien.posX + alien.blockWidth >= boardWidth || alien.posX <= 0) {
                    alienVelocityX *= -1;
                    alien.posX += alienVelocityX * 2;

                     
                    for (int j = 0; j < aliensList.size(); j++) {
                        aliensList.get(j).posY += alienHeight;
                    }
                }

                if (alien.posY >= spaceship.posY) {
                    isGameOver = true;
                }
            }
        }
        for (int i = 0; i < bulletsList.size(); i++) {
             Block bullet = bulletsList.get(i);
            bullet.posY += bulletVelocityY;

            
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

        
        while (bulletsList.size() > 0 && (bulletsList.get(0).isUsed || bulletsList.get(0).posY < 0)) {
             bulletsList.remove(0); 
        }
 
        
        if (alienCounter == 0) {
             
            playerScore += numAlienColumns * numAlienRows * 100; 
            nu mAlienColumns = Math.min(numAlienColumns + 1, numColumns/2 -2); 
            numAlienRows = Math.min(numAlienRows + 1, numRows-6);   
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
                    alienPosX + c*alienWidth, 
                    alienPosY + r*alienH
                        alienWidth,   
                        alienHeight,  
                        alienImages
                        
                        nsList.add(alien);
        }
        alienCounter = aliensList.size();
    }
    


                a.posX + a.blockWidth > b.posX &&  
               a.posY < b.posY + b.blockHeight &  
                a.posY + a.blockHeight > b.posY;   
 
    @Override  
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (isGameOver) {
            gameLoopTimer.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
    

    @Override
    public void keyReleased(KeyEvent e
    ) {
        if (isGameOver) { 
            spaceship.posX = spaceshipX;
            bulletsList.clear();
            aliensList.clear ();
            isGameOver = false;
            playerScore = 0;
            numAlienColumns = 3;
            numAlienRows = 2;
            alienVelocityX = 1;
            createAliens();
            gameLoopTimer.start();
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT  && spaceship.posX - spaceshipVelocityX >= 0) {
            spaceship.posX -= spaceshipVelocityX; 
        } else if (e.getKeyCode() == KeyEvent.VK_RIGH  && spaceship.posX + spaceshipVelocityX + spaceship.blockWidth <= boardWidth) {
            spaceship.posX += spaceshipVelocityX;  
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE
                {
            //shoot bullet 
              bulletsList.add(bullet);
        } 
    }    
                    
}
