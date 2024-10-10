import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LogLane extends Lane {
    private static String imageFileName = "src/images/WaterLane.png";
    private static BufferedImage laneImage;
    private int x,logType,speed,direction,distance;

    /**
     * static block
     * creates water image game sprite
     */
    static {
        try {
            laneImage = ImageIO.read(new File(imageFileName));
        } catch (IOException e) {
            throw new RuntimeException(GrassLane.class + ": Image File " + imageFileName + " Not Found");
        }
    }

    /**
     * LogLane constructor
     * initializes LogLane
     * sets up the logs in this lane
     * @param y
     * @param game
     */
    public LogLane(int y, Game game) {
        super(y, game);
        this.type = Constants.WATER;
        this.height = Constants.LANE_HEIGHT/2;
        setupLog();
    }

    /**
     * draws the water image game sprite
     * draws the logs in the lane
     * @param g
     */
    public void draw(Graphics g) {
        g.drawImage(laneImage,0,y,null);
        for (MovingObject e : movingObjects) {
            e.draw(g);
        }
    }

    /**
     * adds logs to the lane
     */
    @Override
    protected void addObjects() {
        if (movingObjects.size() == 0) {
            movingObjects.add(new Log(x,y,logType,speed,direction));
        } else {
            MovingObject lastObject = movingObjects.get(movingObjects.size()-1);
            if (direction == Constants.RIGHT) {
                if (lastObject.x >= distance) {
                    generateDistance();
                    movingObjects.add(new Log(x,y,logType,speed,direction));
                }
            }
            if (direction == Constants.LEFT) {
                if (lastObject.x + lastObject.width <= width-distance) {
                    generateDistance();
                    movingObjects.add(new Log(x,y,logType,speed,direction));
                }
            }
        }
    }

    /**
     * randomly generates up the log's type (short, medium, long), direction, determines the X position based on the direction
     * the speed of the logs (cannot be the same as an adjacent lane's logs), and the distance between each log
     */
    private void setupLog() {
        generateType();
        generateDirection();
        determineX();
        generateSpeed();
        generateDistance();
    }

    private void generateType() {
        logType = getRandomNum(0,2);
    }

    private void generateSpeed() {
        speed = getRandomNum(2,3);
        while (game.previousSpeed == speed) {
            speed = getRandomNum(2,3);
        }
        game.previousSpeed = speed;
    }

    private void generateDirection() {
        direction = getRandomNum(0,1);
    }

    private void generateDistance() {
        int i = getRandomNum(1,5);
        distance = i*100;
    }

    private void determineX() {
        if (logType == 0 && direction == 1) {
            x = -Constants.LOG_SHORT;
        } else if (logType == 1 && direction == 1) {
            x = -Constants.LOG_MEDIUM;
        } else if (logType == 2 && direction == 1) {
            x = -Constants.LOG_LONG;
        } else if (direction == 0) {
            x = width;
        }
    }
}
