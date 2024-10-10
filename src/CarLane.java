import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CarLane extends Lane {
    private static String imageFileName = "src/images/RoadLane.png";
    private static BufferedImage laneImage;
    private int x,carType,speed,direction,distance;

    /**
     * static block
     * creates road image game sprite
     */
    static {
        try {
            laneImage = ImageIO.read(new File(imageFileName));
        } catch (IOException e) {
            throw new RuntimeException(GrassLane.class + ": Image File " + imageFileName + " Not Found");
        }
    }

    /**
     * CarLane constructor
     * initializes CarLane
     * sets up the cars in the lane
     * @param y
     * @param game
     */
    public CarLane(int y, Game game) {
        super(y, game);
        this.type = Constants.ROAD;
        this.height = Constants.LANE_HEIGHT;
        setupCar();
    }

    /**
     * draws the road image game sprite
     * draws the cars in the lane
     * @param g
     */
    public void draw(Graphics g) {
        g.drawImage(laneImage,0,y,null);
        for (MovingObject e : movingObjects) {
            e.draw(g);
        }
    }

    /**
     * adds cars to the lane
     */
    @Override
    protected void addObjects() {
        if (movingObjects.size() == 0) {
            movingObjects.add(new Car(x,y,carType,speed,direction));
        } else {
            MovingObject lastObject = movingObjects.get(movingObjects.size()-1);
            if (direction == Constants.RIGHT) {
                if (lastObject.x >= distance) {
                    generateDistance();
                    movingObjects.add(new Car(x,y,carType,speed,direction));
                }
            }
            if (direction == Constants.LEFT) {
                if (lastObject.x + lastObject.width <= width-distance) {
                    generateDistance();
                    movingObjects.add(new Car(x,y,carType,speed,direction));
                }
            }
        }
    }

    /**
     * randomly generates up the car's type (short, medium, long), direction, determines the X position based on the direction
     * the speed of the cars, and the distance between each car
     */
    private void setupCar() {
        generateType();
        generateDirection();
        determineX();
        generateSpeed();
        generateDistance();
    }

    private void generateType() {
        carType = getRandomNum(0,2);
    }

    private void generateSpeed() {
        speed = getRandomNum(2,4);
    }

    private void generateDirection() {
        direction = getRandomNum(0,1);
    }

    private void generateDistance() {
        int i = getRandomNum(1,8);
        distance = i*100;
    }

    private void determineX() {
        if (carType == 0 && direction == 1) {
            x = -Constants.CAR_SHORT;
        } else if (carType == 1 && direction == 1) {
            x = -Constants.CAR_MEDIUM;
        } else if (carType == 2 && direction == 1) {
            x = -Constants.CAR_LONG;
        } else if (direction == 0) {
            x = width;
        }
    }
}
