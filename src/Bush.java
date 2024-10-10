import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Bush extends BaseObject {
    protected BufferedImage bushImage,lifeBushImage,berryBushImage;
    protected int type;

    public Bush(int x, int y, int type) {
        super(x, y);
        this.width = 25;
        this.height = 25;
        this.type = type;

        createImages();
    }

    private void createImages() {
        try {
            bushImage = ImageIO.read(new File("src/images/Bush.png"));
            lifeBushImage = ImageIO.read(new File("src/images/LifeBush.png"));
            berryBushImage = ImageIO.read(new File("src/images/BerryBush.png"));
        } catch (Exception e) {
            System.out.println("Can't find image.");
        }
    }

    public void draw(Graphics g) {
        if (type == Constants.REGULAR_BUSH) {
            g.drawImage(bushImage, x, y, null);
        } else if (type == Constants.LIFE_BUSH) {
            g.drawImage(lifeBushImage, x, y, null);
        } else {
            g.drawImage(berryBushImage, x, y, null);
        }
    }
}
