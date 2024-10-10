import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Pad extends BaseObject {
    private BufferedImage padImage;

    public Pad(int x, int y) {
        super(x, y);
        width = 25;
        height = 25;

        try {
            padImage = ImageIO.read(new File("src/images/Pad.png"));
        } catch (Exception e) {
            System.out.println("Can't find image.");
        }
    }

    public void draw(Graphics g) {
        g.drawImage(padImage,x,y,null);
    }
}
