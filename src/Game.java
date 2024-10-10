import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Game extends JPanel {
    private static final int NUM_OF_LANES = 14;
    private static final int SAFE_PERIOD = 1;
    private int y;
    protected ArrayList<Lane> lanes;
    private Player player;
    private boolean start = false;
    private boolean gameOver = false;
    private boolean safe = false;
    protected boolean showMenu = false;
    protected int previousSpeed; //for LogLane
    protected int lives;
    protected int score;
    private BufferedImage heartImage, deadHeartImage, gameOverImage;
    private CrossyRoad crossyRoad;
    protected int offset;
    private int count;
    protected Music gameMusic = new Music("src/music/GameMusic.wav");
    protected Music gameOverMusic = new Music("src/music/GameOverMusic.wav");
    private Music loseLifeMusic = new Music("src/music/LoseLifeMusic.wav");

    /**
     * Game constructor, calls method createImages, sets the size of the JPanel, adds KeyListener to the JPanel
     * @Param crossyRoad
     */
    public Game(CrossyRoad crossyRoad) {
        this.crossyRoad = crossyRoad;
        createImages();
        setPreferredSize(Constants.GAME_DIMENSION);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                player.keyReleased(e);
            }

            /**
             * sets gameOver and showMenu flag when escape and spacebar are pressed
             * @Param e
             */
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    gameOver = true;
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE && gameOver) {
                    showMenu = true;
                }
                player.keyPressed(e);
            }
        });
    }

    /**
     * creates the game sprites
     */
    private void createImages() {
        try {
            heartImage = ImageIO.read(new File("src/images/Heart.png"));
            deadHeartImage = ImageIO.read(new File("src/images/DeadHeart.png"));
            gameOverImage = ImageIO.read(new File("src/images/GameOver.png"));
        } catch (Exception e) {
            System.out.println("Can't find image.");
        }
    }

    /**
     * continuously loops
     * method to start and run the game
     * delays the scrolling of the game before continuously scrolling
     * updates the lanes and the player
     * checks for player collision with enemy objects and the bottom of the screen
     * checks if player lost life, if player lost life, starts a safe period where cannot lose more lives until the period is over
     * checks if player has lost game
     * repaints
     * if player loses game, switches to the menu JPanel using CardLayout
     * sleeps for 10 ms
     */
    public void run() {
        int delayTime = 8;
        int offsetFreq = 3;
        long startTime = 0;
        long currentTime, elapsedTime;
        boolean delayed = false;
        boolean set = false;
        while (true) {
            if (start) {
                if (!set) {
                    startTime = System.currentTimeMillis();
                    set = true;
                } else if (!delayed) {
                    currentTime = System.currentTimeMillis();
                    elapsedTime = (currentTime - startTime) / 1000;
                    if (elapsedTime == delayTime) {
                        count = offsetFreq;
                        delayed = true;
                    }
                }
                if (count == offsetFreq) {
                    offset = 1;
                    count = 0;
                } else if (count < offsetFreq){
                    offset = 0;
                    count++;
                }
                if (gameOver) {
                    start = false;
                }
                update();
                if (player.checkCollision()) {
                    if (!safe) {
                        loseLifeMusic.rewind();
                        loseLifeMusic.start();
                        lives--;
                        if (lives == 0) {
                            gameOver = true;
                        } else {
                            startTime = System.currentTimeMillis();
                            safe = true;
                        }
                    }
                }
                if (player.checkBottomCollision()) {
                    gameOver = true;
                }
                if (safe) {
                    currentTime = System.currentTimeMillis();
                    elapsedTime = (currentTime - startTime) / 1000;
                    if (elapsedTime == SAFE_PERIOD) {
                        safe = false;
                        loseLifeMusic.stop();
                    }
                }
                repaint();
            }
            if (showMenu) {
                showMenu = false;
                delayed = false;
                set = false;
                gameOverMusic.stop();
                crossyRoad.showMenuPanel();
            }
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                System.out.println("sleep exception");
            }
        }
    }

    /**
     * sets and resets the game state
     */
    public void reset() {
        player = new Player(this);
        lives = 3;
        count = 4;
        score = 0;
        y = 800;
        offset = 0;
        generateLanes();
        start = true;
        gameOver = false;
        safe = false;
        showMenu = false;
    }

    /**
     * updates the lanes and player
     */
    private void update() {
        for (Lane e : lanes) {
            e.updateLane();
        }
        generateNewLanes();
        player.setY(player.getY()+offset);
    }

    /**
     * generates the initial lanes when the game starts, each type of lane has a certain chance to be generated
     * starts generating from the bottom, each time it generates a lane the y value decreases for the next lane
     * sets the y value for new lanes to be added in addLanes()
     */
    private void generateLanes() {
        int randomNum;
        lanes = new ArrayList<Lane>();
        y = y - Constants.LANE_HEIGHT;
        lanes.add(new StartLane(y, this));
        for (int i = 0; i < NUM_OF_LANES; i++) {
            randomNum = getRandomNum(1,8);
            if (randomNum == 1 || randomNum == 2 || randomNum == 3) { //Car lane
                y = y - Constants.LANE_HEIGHT;
                lanes.add(new CarLane(y,this));
            } else if (randomNum == 4 || randomNum == 5) {            //Bush lane
                y = y - Constants.LANE_HEIGHT;
                lanes.add(new GrassLane(y,this));
            } else if (randomNum == 6 || randomNum == 7) {            // Water lanes
                randomNum = getRandomNum(1, 2);
                if (randomNum == 1) {                                 // Log lane
                    y = y - Constants.LANE_HEIGHT / 2;
                    lanes.add(new LogLane(y, this));
                    y = y - Constants.LANE_HEIGHT / 2;
                    lanes.add(new LogLane(y, this));
                } else if (randomNum == 2) {                          // Log lane and pad lane
                    y = y - Constants.LANE_HEIGHT / 2;
                    lanes.add(new PadLane(y, this));
                    y = y - Constants.LANE_HEIGHT / 2;
                    lanes.add(new LogLane(y, this));
                }
            } else if (randomNum == 8) {                              // train lane
                y = y - Constants.LANE_HEIGHT;
                lanes.add(new TrainLane(y, this));
            }
        }
        //sets the y value for new lanes to be added in addLanes()
        y = Constants.GAME_TOP_Y - Constants.LANE_HEIGHT;
    }

    /**
     * checks if the bottom-most lane leaves the game and removes the lane
     * adds a new lane at the top of the game
     */
    private void generateNewLanes() {
        Lane lane = lanes.get(0);
        if (lane.type == Constants.WATER && lanes.get(1).y >= Constants.GAME_DIMENSION.height) {
            lanes.remove(1);
            lanes.remove(0);
            addLanes();
        } else if (lane.type != Constants.WATER && lane.y >= Constants.GAME_DIMENSION.height) {
            lanes.remove(0);
            addLanes();
        }
    }

    /**
     * adds a new lane at the top of the game
     * each type of lane has a certain chance to be added
     */
    private void addLanes() {
        int randomNum;
        randomNum = getRandomNum(1,8);
        if (randomNum == 1 || randomNum == 2 || randomNum == 3) { //Car lane
            lanes.add(new CarLane(y,this));
        } else if (randomNum == 4 || randomNum == 5) {            //Bush lane
            lanes.add(new GrassLane(y,this));
        } else if (randomNum == 6 || randomNum == 7) {            // Water lanes
            randomNum = getRandomNum(1, 2);
            if (randomNum == 1) {                                 // Log lane
                lanes.add(new LogLane(y+Constants.LANE_HEIGHT/2, this));
                lanes.add(new LogLane(y, this));
            } else if (randomNum == 2) {                          // Log lane and pad lane
                lanes.add(new PadLane(y+Constants.LANE_HEIGHT/2, this));
                lanes.add(new LogLane(y, this));
            }
        } else if (randomNum == 8) {                              // train lane
            lanes.add(new TrainLane(y, this));
        }
    }

    /**
     * paints the lanes, the player, and the top game banner
     * if the player loses, paints the game over page
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameOver) {
            g.drawImage(gameOverImage,0,0,null);
            gameMusic.stop();
            gameOverMusic.start();
            gameOverMusic.loop();
        } else {
            for (Lane e : lanes) {
                e.draw(g);
            }
            player.draw(g, safe);
            drawBanner(g);
            if (count <= 3) {
                g.setColor(Color.red);
                g.fillRect(0,Constants.GAME_DIMENSION.height-5,Constants.GAME_DIMENSION.width,5);
            }
        }
    }

    /**
     * draws the banner at the top of the game
     * @param g
     */
    private void drawBanner(Graphics g) {
        g.setColor(new Color(0,0,0));
        g.fillRect(0,0,Constants.GAME_DIMENSION.width,Constants.GAME_TOP_Y);

        drawLives(g);

        g.setColor(new Color(255,255,255));
        g.setFont(new Font("Verdana",Font.BOLD,40));
        g.drawString("SCORE " + String.valueOf(score),850,67);
    }

    /**
     * draws the lives in the banner
     * @param g
     */
    private void drawLives(Graphics g) {
        if (lives == 3) {
            g.drawImage(heartImage,20,20,null);
            g.drawImage(heartImage,100,20,null);
            g.drawImage(heartImage,180,20,null);
        } else if (lives == 2) {
            g.drawImage(heartImage,20,20,null);
            g.drawImage(heartImage,100,20,null);
            g.drawImage(deadHeartImage,180,20,null);
        } else if (lives == 1){
            g.drawImage(heartImage,20,20,null);
            g.drawImage(deadHeartImage,100,20,null);
            g.drawImage(deadHeartImage,180,20,null);
        } else {
            g.drawImage(deadHeartImage,20,20,null);
            g.drawImage(deadHeartImage,100,20,null);
            g.drawImage(deadHeartImage,180,20,null);
        }
    }

    /**
     * utility: gets a random number between min and max number (inclusive)
     * @param min
     * @param max
     * @return int
     */
    private int getRandomNum(int min, int max) {
        return min + (int)(Math.random()*((max-min)+1));
    }
}
