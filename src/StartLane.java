import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StartLane extends Lane {
    private static String imageFileName = "src/images/GrassLane.png";;
    private static BufferedImage laneImage;

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
     * StartLane constructor
     * initializes StartLane
     * @param y
     * @param game
     */
    public StartLane(int y, Game game) {
        super(y, game);
        this.type = Constants.START;
        this.height = Constants.LANE_HEIGHT;
    }

    /**
     * draws the grass image game sprite
     * @param g
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(laneImage,0,y,null);
    }

    @Override
    protected void addObjects() {

    }


}
