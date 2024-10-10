import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PadLane extends Lane {
    private static String imageFileName = "src/images/WaterLane.png";
    private static BufferedImage laneImage;
    private boolean drawn = false;

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
     * PadLane constructor
     * initializes PadLane
     * @param y
     * @param game
     */
    public PadLane(int y, Game game) {
        super(y, game);
        super.type = Constants.WATER;
        super.height = Constants.LANE_HEIGHT/2;

        super.pads = new Pad[48];
    }

    /**
     * draws the water image game sprite
     * draws the pads in the lane
     * @param g
     */
    public void draw(Graphics g) {
        g.drawImage(laneImage,0,y,null);
        for (Pad pad : pads) {
            if (pad != null) {
                pad.draw(g);
            }
        }
    }

    /**
     * adds pads in the lane
     */
    @Override
    public void addObjects() {
        int randomNum;
        if (!drawn) {
            for (int i = 0; i < pads.length; i++) {
                randomNum = getRandomNum(1, 4);
                if (randomNum == 1) {
                    pads[i] = new Pad(i*25,y);
                }
            }
            drawn = true;
        }
    }
}
