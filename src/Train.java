import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Train extends MovingObject {
    private static final int TRAIN_HEIGHT = 40;
    private BufferedImage trainLeftImage,trainRightImage;

    public Train(int x, int y, int speed, int direction) {
        super(x, y + Constants.LANE_HEIGHT/2 - TRAIN_HEIGHT/2, speed, direction);
        this.height = TRAIN_HEIGHT;
        this.width = Constants.TRAIN_WIDTH;

        createImages();
    }

    private void createImages() {
        try {
            trainLeftImage = ImageIO.read(new File("src/images/TrainLeft.png"));
            trainRightImage = ImageIO.read(new File("src/images/TrainRight.png"));
        } catch (Exception e) {
            System.out.println("Can't find image.");
        }
    }

    @Override
    public void draw(Graphics g) {
        if (dir == Constants.LEFT) {
            g.drawImage(trainLeftImage,x,y,null);
        } else {
            g.drawImage(trainRightImage,x,y,null);
        }
    }
}
