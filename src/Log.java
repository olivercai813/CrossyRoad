import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Log extends MovingObject {
    private int type;
    private BufferedImage shortLogImage,mediumLogImage,longLogImage;

    public Log(int x, int y, int type, int speed, int direction) {
        super(x,y,speed,direction);
        this.type = type;
        setDimensions(type);

        createImages();
    }

    private void createImages() {
        try {
            shortLogImage = ImageIO.read(new File("src/images/ShortLog.png"));
            mediumLogImage = ImageIO.read(new File("src/images/MediumLog.png"));
            longLogImage = ImageIO.read(new File("src/images/LongLog.png"));
        } catch (Exception e) {
            System.out.println("Can't find image.");
        }
    }

    private void setDimensions(int type) {
        super.height = Constants.LANE_HEIGHT/2;
        if (type == Constants.SHORT) {
            this.width = Constants.LOG_SHORT;
        } else if (type == Constants.MEDIUM) {
            this.width = Constants.LOG_MEDIUM;
        } else if (type == Constants.LONG) {
            this.width = Constants.LOG_LONG;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (type == Constants.SHORT) {
            g.drawImage(shortLogImage,x,y,null);
        } else if (type == Constants.MEDIUM) {
            g.drawImage(mediumLogImage,x,y,null);
        } else if (type == Constants.LONG) {
            g.drawImage(longLogImage,x,y,null);
        }
    }
}

