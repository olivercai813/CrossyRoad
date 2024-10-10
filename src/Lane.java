import java.awt.*;
import java.util.ArrayList;

public abstract class Lane {
    protected int y,type,width,height;
    protected Game game;
    protected ArrayList<MovingObject> movingObjects = new ArrayList<MovingObject>();
    protected Bush[][] bushes = null;
    protected Pad[] pads = null;

    /**
     * Lane constructor, initializes Lane
     * @param y
     * @param game
     */
    public Lane(int y, Game game) {
        this.y = y;
        this.game = game;
        width = game.getPreferredSize().width;
    }

    /**
     * child classes will implement this method
     * @param g
     */
    protected abstract void draw(Graphics g);

    /**
     * child classes will implement this method
     */
    protected abstract void addObjects();

    /**
     * applies the game's scrolling to the lanes
     * calculates the lane's new position by adding the offset
     */
    protected void move() {
        int offset = game.offset;
        y = y + offset;
        if (bushes != null) {
            for (Bush[] bushes1 : bushes) {
                for (Bush bush : bushes1) {
                    if (bush != null) {
                        bush.y = bush.y + offset;
                    }
                }
            }
        }
        if (pads != null) {
            for (Pad pad : pads) {
                if (pad != null) {
                    pad.y = pad.y + offset;
                }
            }
        }
        for (MovingObject e : movingObjects) {
            e.move(offset);
        }
    }

    /**
     * updates the lane
     * calls child class's addObjects method
     */
    protected void updateLane() {
        addObjects();
        move();
        deleteMovingObjects();
    }

    /**
     * checks if a moving object has passed the game dimensions and removes it
     */
    protected void deleteMovingObjects() {
        ArrayList<Integer> deleteIndexes = new ArrayList<Integer>();
        MovingObject movingObject = null;
        for (int i = 0; i < movingObjects.size(); i++) {
            movingObject = movingObjects.get(i);
            if (movingObject.dir == Constants.RIGHT) {
                if (movingObject.x >= width) {
                    deleteIndexes.add(i);
                }
            } else if (movingObject.dir == Constants.LEFT) {
                if (movingObject.x + movingObject.width <= 0) {
                    deleteIndexes.add(i);
                }
            }
        }
        for (int i : deleteIndexes) {
            movingObjects.remove(i);
        }
    }

    /**
     * utility: gets a random number between min and max number (inclusive)
     * @param min
     * @param max
     * @return int
     */
    protected int getRandomNum(int min, int max) {
        return min + (int)(Math.random()*((max-min)+1));
    }

    /**
     * returns the bounds of the lane
     * @return Rectangle
     */
    protected Rectangle getBounds() {
        return new Rectangle(0,y,width,height);
    }
}
