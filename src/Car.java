import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Car extends MovingObject {
    private int type;
    private static final int CAR_HEIGHT = 40;
    private BufferedImage shortCarLeftImage,shortCarRightImage,mediumCarLeftImage,mediumCarRightImage,longCarLeftImage,longCarRightImage;

    public Car(int x, int y, int type, int speed, int direction) {
        super(x,y + Constants.LANE_HEIGHT/2 - CAR_HEIGHT/2,speed,direction);
        this.type = type;
        setDimensions(type);

        createImages();
    }

    /**
     * creates the car game sprites
     */
    private void createImages() {
        try {
            shortCarLeftImage = ImageIO.read(new File("src/images/ShortCarLeft.png"));
            shortCarRightImage = ImageIO.read(new File("src/images/ShortCarRight.png"));
            mediumCarLeftImage = ImageIO.read(new File("src/images/MediumCarLeft.png"));
            mediumCarRightImage = ImageIO.read(new File("src/images/MediumCarRight.png"));
            longCarLeftImage = ImageIO.read(new File("src/images/LongCarLeft.png"));
            longCarRightImage = ImageIO.read(new File("src/images/LongCarRight.png"));
        } catch (Exception e) {
            System.out.println("Can't find image.");
        }
    }

    /**
     * sets the car's dimensions based on it's type (short, medium, long)
     * @param type
     */
    private void setDimensions(int type) {
        super.height = CAR_HEIGHT;
        if (type == Constants.SHORT) {
            super.width = Constants.CAR_SHORT;
        } else if (type == Constants.MEDIUM) {
            super.width = Constants.CAR_MEDIUM;
        } else if (type == Constants.LONG) {
            super.width = Constants.CAR_LONG;
        }
    }

    /**
     * draws the car based on its type and direction
     * @param g
     */
    @Override
    public void draw(Graphics g) {
        if (type == Constants.SHORT && dir == Constants.LEFT) {
            g.drawImage(shortCarLeftImage,x,y,null);
        } else if (type == Constants.SHORT && dir == Constants.RIGHT) {
            g.drawImage(shortCarRightImage,x,y,null);
        } else if (type == Constants.MEDIUM && dir == Constants.LEFT) {
            g.drawImage(mediumCarLeftImage,x,y,null);
        } else if (type == Constants.MEDIUM && dir == Constants.RIGHT) {
            g.drawImage(shortCarRightImage,x,y,null);
        } else if (type == Constants.LONG && dir == Constants.LEFT) {
            g.drawImage(longCarLeftImage,x,y,null);
        } else if (type == Constants.LONG && dir == Constants.RIGHT) {
            g.drawImage(longCarRightImage,x,y,null);
        }
    }
}
