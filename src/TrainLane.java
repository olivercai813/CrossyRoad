import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TrainLane extends Lane {
    private static String imageFileName = "src/images/TrackLane.png";
    private static BufferedImage laneImage;
    private int x, direction;
    private static final int TRAIN_SPEED = 30;
    private long startTime, currentTime, elapsedTime;
    private int actionPeriod, alertPeriod;
    private static BufferedImage alertTrackImage;

    /**
     * static block
     * creates track image and alertTrack image game sprite
     */
    static {
        try {
            laneImage = ImageIO.read(new File(imageFileName));
            alertTrackImage = ImageIO.read(new File("src/images/AlertTrackLane.png"));
        } catch (IOException e) {
            throw new RuntimeException(GrassLane.class + ": Image File " + imageFileName + " or src/images/AlertTrackLane.png Not Found");
        }
    }

    /**
     * TrainLane constructor
     * initializes TrainLane
     * randomly generates the time between each train's arrival (3-8 seconds)
     * sets the alertPeriod as half a second before the train's arrival
     * @param y
     * @param game
     */
    public TrainLane(int y, Game game) {
        super(y, game);
        this.type = Constants.TRACK;
        this.height = Constants.LANE_HEIGHT;
        setupTrain();
        startTime = System.currentTimeMillis();
        actionPeriod = getRandomNum(30,80);
        alertPeriod = actionPeriod-5;
    }

    /**
     * draws the track image game sprite
     * draws the train in the lane
     * draws the alert for the train
     * @param g
     */
    public void draw(Graphics g) {
        g.drawImage(laneImage,0,y,null);
        for (MovingObject e : movingObjects) {
            e.draw(g);
        }
        if (elapsedTime == alertPeriod) {
            g.drawImage(alertTrackImage,0,y,null);
        }
    }

    /**
     * adds a train to the lane every 3 to 8 seconds
     * sets the alert for the train as half a second before the train arrives
     */
    @Override
    protected void addObjects() {
        currentTime = System.currentTimeMillis();
        elapsedTime = (currentTime - startTime)/100;

        if (elapsedTime == actionPeriod) {
            if (direction == Constants.RIGHT) {
                movingObjects.add(new Train(x, y, TRAIN_SPEED, direction));
            }
            if (direction == Constants.LEFT) {
                movingObjects.add(new Train(x, y, TRAIN_SPEED, direction));
            }
            startTime = currentTime;
            actionPeriod = getRandomNum(30,80);
            alertPeriod = actionPeriod-5;
        }
    }

    /**
     * sets the train's direction and determines it's starting X value
     */
    private void setupTrain() {
        generateDirection();
        determineX();
    }

    private void generateDirection() {
        direction = getRandomNum(0,1);
    }

    private void determineX() {
        if (direction == 1) {
            x = -Constants.TRAIN_WIDTH;
        } else if (direction == 0) {
            x = width;
        }
    }
}
