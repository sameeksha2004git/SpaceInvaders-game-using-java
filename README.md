# SpaceInvaders-game-using-java
Project Overview:
The Space Invaders game is a classic arcade-style game where the player controls a spaceship and must destroy waves of aliens before they reach the bottom of the screen. The player can move the spaceship left or right and shoot bullets to destroy the aliens. The game ends when an alien reaches the player's ship or the bottom of the screen.

Key Components:

Game Window:

The game is rendered in a window using Java Swing. The game panel is set up with a fixed size, background color, and key event listeners to handle user input.
Spaceship:

The player's spaceship is represented as a movable object at the bottom of the screen. The spaceship can move left or right based on keyboard input and can shoot bullets to destroy aliens.
Aliens:

Aliens are arranged in a grid at the top of the screen. They move horizontally and change direction when they hit the screen edges. The aliens move downward periodically, increasing the difficulty as they approach the player's ship.
Bullets:

The spaceship can shoot bullets upwards. When a bullet collides with an alien, both the bullet and the alien are removed from the game.
Collision Detection:

The game includes logic to detect collisions between bullets and aliens, as well as between aliens and the player's spaceship.
Score and Game Over:

The player earns points for each alien destroyed. The game ends when an alien reaches the bottom of the screen or collides with the player's spaceship. The final score is displayed at the end of the game.
Technical Details:

Graphics Rendering:

The game uses Java's Graphics class to draw the spaceship, aliens, and bullets on the screen. Images for the spaceship and aliens are loaded from resources and drawn at their respective positions.
Event Handling:

Key event listeners are used to handle user input for moving the spaceship and shooting bullets. The game loop periodically updates the positions of the aliens and bullets.
Game Loop:

A javax.swing.Timer is used to create a game loop that updates the game state and repaints the game panel at regular intervals 



