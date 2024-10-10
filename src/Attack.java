import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Attack extends BaseObject {
    private int attackCount;
    private BufferedImage attack1Image,attack2Image,attack3Image;

    public Attack(int x, int y) {
        super(x, y);
        this.width = Constants.ATTACK_REACH;
        this.height = Constants.ATTACK_REACH;
        attackCount = 0;

        createImages();
    }

    private void createImages() {
        try {
            attack1Image = ImageIO.read(new File("src/images/Attack1.png"));
            attack2Image = ImageIO.read(new File("src/images/Attack2.png"));
            attack3Image = ImageIO.read(new File("src/images/Attack3.png"));
        } catch (Exception e) {
            System.out.println("Can't find image.");
        }
    }

    @Override
    public void draw(Graphics g) {
        if (attackCount <= 3) {
            g.drawImage(attack1Image, x, y, null);
        } else if (attackCount <= 6) {
            g.drawImage(attack2Image, x, y, null);
        } else if (attackCount <= 12) {
            g.drawImage(attack3Image, x, y, null);
        }
        attackCount++;
    }
}
