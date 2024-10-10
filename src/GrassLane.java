import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GrassLane extends Lane {
    private static String imageFileName = "src/images/GrassLane.png";
    private static BufferedImage laneImage;
    private boolean drawn = false;

    /**
     * static block
     * creates grass image game sprite
     */
    static {
        try {
            laneImage = ImageIO.read(new File(imageFileName));
        } catch (IOException e) {
            throw new RuntimeException(GrassLane.class + ": Image File " + imageFileName + " Not Found");
        }
    }

    /**
     * GrassLane constructor
     * initializes GrassLane
     * @param y
     * @param game
     */
    public GrassLane(int y, Game game) {
        super(y, game);
        this.type = Constants.GRASS;
        this.height = Constants.LANE_HEIGHT;

        this.bushes = new Bush[2][48];
    }

    /**
     * draws the grass image game sprite
     * draws the bushes in the lane
     * @param g
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(laneImage,0,y,null);
        for (Bush[] bushes1 : bushes) {
            for (Bush bush : bushes1) {
                if (bush != null) {
                    bush.draw(g);
                }
            }
        }
    }

    /**
     * adds bushes to the lane
     */
    @Override
    public void addObjects() {
        int randomNum;
        if (!drawn) {
            for (int i = 0; i < bushes.length; i++) {
                for (int j = 0; j < bushes[i].length; j++) {
                    randomNum = getRandomNum(1,400);
                    if (randomNum<=150) {
                        bushes[i][j] = new Bush(j * 25, y + i * 25, Constants.REGULAR_BUSH);
                    } else if (randomNum <= 160) {
                        bushes[i][j] = new Bush(j * 25, y + i * 25, Constants.BERRY_BUSH);
                    } else if (randomNum == 400) {
                        bushes[i][j] = new Bush(j * 25, y + i * 25, Constants.LIFE_BUSH);
                    }
                }
            }
            drawn = true;
        }
    }
}
