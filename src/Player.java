import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class Player {
    private final static int WIDTH = 25;
    private final static int HEIGHT = 25;
    private final static int SPEED = 25;
    private final static int GAME_TOP_Y = Constants.GAME_TOP_Y;
    private int x,y,xStep,yStep,gameWidth,gameHeight;
    private Game game;
    private Lane playerLane = null;
    private Lane attackLane = null;
    private Lane playerLogLane = null;
    private Attack attack = null;
    private BufferedImage playerLeftImage,playerRightImage,deadPlayerLeftImage,deadPlayerRightImage;
    private int dir;
    private boolean attacked = false;

    /**
     * Player constuctor
     * sets the start location of the player
     * creates the player sprite images
     * @param game
     */
    public Player(Game game) {
        this.game = game;
        gameWidth = game.getPreferredSize().width;
        gameHeight = game.getPreferredSize().height;
        setStart();

        try {
            playerLeftImage = ImageIO.read(new File("src/images/ChickenLeft.png"));
            playerRightImage = ImageIO.read(new File("src/images/ChickenRight.png"));
            deadPlayerLeftImage = ImageIO.read(new File("src/images/DeadChickenLeft.png"));
            deadPlayerRightImage = ImageIO.read(new File("src/images/DeadChickenRight.png"));
        } catch (Exception e) {
            System.out.println("Can't find image.");
        }
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    /**
     * draws the player sprites and the attack sprites
     * @param g
     * @param safe
     */
    public void draw(Graphics g, boolean safe) {
        if (safe && dir == Constants.LEFT ) {
            g.drawImage(deadPlayerLeftImage, x, y, null);
        } else if (safe && dir == Constants.RIGHT ) {
            g.drawImage(deadPlayerRightImage,x,y,null);
        } else if (dir == Constants.LEFT) {
            g.drawImage(playerLeftImage,x,y,null);
        } else {
            g.drawImage(playerRightImage,x,y,null);
        }
        if (attack != null) {
            attack.draw(g);
        }
    }

    /**
     * calculates the players position
     * can only move if the player is not about to move outside of the game dimensions or into a barrier
     * makes sure that the player is always locked in a 25x25 grid after getting off a log
     * changes the game's score based on the player's y position changes
     */
    public void move() {
        float temp;
        if ((x + xStep >= 0 && x + xStep <= gameWidth - WIDTH && !checkBarriers())) {
            x = x + xStep;
        }
        if ((y + yStep >= GAME_TOP_Y && y + yStep <= gameHeight - HEIGHT && !checkBarriers())) {
            y = y + yStep;
            game.score = game.score - yStep * 4;
        }
        playerLane = findPlayerLane();
        if (playerLogLane != null && playerLogLane != playerLane) {
            temp = (float)x/WIDTH;
            x = Math.round(temp)*WIDTH;
            playerLogLane = null;
        }
    }

    /**
     * moves the player's position in the corresponding direction when the user presses W,A,S,D
     * creates attacks in the corresponding direction when the user presses the arrow keys
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            xStep = -SPEED;
            move();
            dir = Constants.LEFT;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            xStep = SPEED;
            move();
            dir = Constants.RIGHT;
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            yStep = -SPEED;
            move();
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            yStep = SPEED;
            move();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            attack(Constants.LEFT);
            attackLane = findAttackLane();
            checkAttack();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            attack(Constants.RIGHT);
            attackLane = findAttackLane();
            checkAttack();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            attack(Constants.UP);
            attackLane = findAttackLane();
            checkAttack();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            attack(Constants.DOWN);
            attackLane = findAttackLane();
            checkAttack();
        }
        xStep = 0;
        yStep = 0;
    }

    /**
     * removes the attack and resets the flag when the arrow keys are released
     * @param e
     */
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
            attack = null;
            attackLane = null;
            attacked = false;
        }
    }

    /**
     * sets the player's start position
     */
    private void setStart() {
        x = gameWidth/2 - WIDTH;
        y = 775;
    }

    /**
     * gets the bounds of the player
     * @return Rectangle
     */
    public Rectangle getBounds() {
        return new Rectangle(x,y,WIDTH,HEIGHT);
    }

    /**
     * returns the lane that the player is currently in
     * @return Lane
     */
    public Lane findPlayerLane() {
        for (Lane lane : game.lanes) {
            if (getBounds().intersects(lane.getBounds())) {
                return lane;
            }
        }
        return null;
    }

    /**
     * returns the lane that the attack is currently in
     * @return Lane
     */
    public Lane findAttackLane() {
        for (Lane lane : game.lanes) {
            if (attack.getBounds().intersects(lane.getBounds())) {
                return lane;
            }
        }
        return null;
    }

    /**
     * checks if the player has entered enemy space
     * @return boolean
     */
    public boolean checkCollision() {
        boolean collided = false;
        for (Lane lane : game.lanes) {
            if (lane.type == Constants.WATER) {
                if (getBounds().intersects(lane.getBounds())) {
                    collided = true;                           //collides with enemy
                }
                if (lane instanceof PadLane) {
                    for (Pad pad : lane.pads) {
                        if (pad != null && getBounds().intersects(pad.getBounds())) {
                            collided = false;                  //collides with non-enemy
                        }
                    }
                }
            }
            for (MovingObject object : lane.movingObjects) {
                if (getBounds().intersects(object.getBounds())) {
                    collided = true;                           //collides with enemy
                    if (object instanceof Log) {
                        playerLogLane = lane;
                        collided = false;                      //collides with non-enemy
                        xStep = object.speed;
                        move();
                        break;
                    }
                }
            }
        }
        return collided;
    }

    /**
     * checks if the player has gone past the bottom of the game
     * @return
     */
    public boolean checkBottomCollision() {
        boolean collided = false;
        if (y >= Constants.GAME_DIMENSION.height) {
            collided = true;
        }
        return collided;
    }

    /**
     * checks if the player has collided with a barrier
     * @return boolean
     */
    public boolean checkBarriers() {
        boolean collided = false;
        for (Lane lane : game.lanes) {
            if (lane instanceof GrassLane) {
                for (Bush[] bushes1 : lane.bushes) {
                    for (Bush bush : bushes1) {
                        if (bush != null) {
                            if (new Rectangle(x + xStep, y + yStep, WIDTH, HEIGHT).intersects(bush.getBounds())) {
                                collided = true;
                            }
                        }
                    }
                }
            }
        }
        return collided;
    }

    /**
     * creates an attack in a certain direction
     * @param dir
     */
    public void attack(int dir) {
        if (!attacked) {
            int reach = Constants.ATTACK_REACH;
            if (dir == Constants.LEFT) {
                attack = new Attack(x - reach, y);
            } else if (dir == Constants.RIGHT) {
                attack = new Attack(x + reach, y);
            } else if (dir == Constants.UP) {
                attack = new Attack(x, y - reach);
            } else if (dir == Constants.DOWN) {
                attack = new Attack(x, y + reach);
            }
            attacked = true;
        }
    }

    /**
     * checks if an attack has intersected a bush
     * adds lives if the bush is a LifeBush
     * adds points if the bush is a BerryBush
     * adds points if the player has 3 lives already and attacks a LifeBush
     * removes the bush after being attacked
     */
    public void checkAttack() {
        if (attack != null && attackLane instanceof GrassLane) {
            Bush target = null;
            for (Bush[] bushes : attackLane.bushes) {
                for (int i=0; i<bushes.length; i++) {
                    target = bushes[i];
                    if (target != null && attack.getBounds().intersects(target.getBounds())) {
                        if (target.type == Constants.LIFE_BUSH && game.lives != 3) {
                            game.lives++;
                        } else if (target.type == Constants.LIFE_BUSH && game.lives == 3) {
                            game.score = game.score + 1000;
                        } else if (target.type == Constants.BERRY_BUSH) {
                            game.score = game.score + 100;
                        }
                        bushes[i] = null;
                        return;
                    }
                }
            }
        }
    }
}
