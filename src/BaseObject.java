import java.awt.*;

public abstract class BaseObject {
    protected int x,y,width,height;

    public BaseObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected abstract void draw(Graphics g);

    public Rectangle getBounds() {
        return new Rectangle(x,y,width,height);
    }
}
