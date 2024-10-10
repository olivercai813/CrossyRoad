public abstract class MovingObject extends BaseObject {
    protected int speed,dir;

    public MovingObject(int x, int y, int speed, int direction) {
        super(x, y);
        this.dir = direction;

        if (this.dir == Constants.LEFT) {
            this.speed = -speed;
        } else {
            this.speed = speed;
        }
    }

    /**
     * calculates the object's position
     * applies the offset
     * @param offset
     */
    protected void move(int offset) {
        x = x + speed;
        y = y + offset;
    }


}
