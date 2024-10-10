import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class Menu extends JPanel implements KeyListener, MouseListener {
    private static final Dimension GAME_DIMENSION = Constants.GAME_DIMENSION;
    protected boolean change = false;
    private boolean help = false;
    private BufferedImage startImage, menuImage, backgroundImage, titleImage, controlsImage, helpImage;
    private CrossyRoad crossyRoad;
    protected Music menuMusic = new Music("src/music/MenuMusic.wav");

    //title = 100,200,600,150
    //start = 100,500,400,100
    //start = 200,450,400,30
    //background = 0,0,Constants.GAME_DIMENSION.width,Constants.GAME_DIMENSION.height;
    //image = 700,200,400,400;

    /**
     * Menu constructor
     * calls method createImages
     * sets the size of the JPanel
     * adds KeyListener and MouseListener
     * @param crossyRoad
     */
    public Menu(CrossyRoad crossyRoad) {
        this.crossyRoad = crossyRoad;
        setPreferredSize(new Dimension(GAME_DIMENSION));
        requestFocusInWindow();
        createImages();

        addKeyListener(this);
        addMouseListener(this);
        menuMusic.start();
        menuMusic.loop();
    }

    /**
     * creates the menu sprites
     */
    private void createImages() {
        try {
            startImage = ImageIO.read(new File("src/images/StartImage.png"));
            menuImage = ImageIO.read(new File("src/images/MenuImage.png"));
            backgroundImage = ImageIO.read(new File("src/images/MenuBackground.png"));
            titleImage = ImageIO.read(new File("src/images/GameTitle.png"));
            controlsImage = ImageIO.read(new File("src/images/Controls.png"));
            helpImage = ImageIO.read(new File("src/images/Help.png"));
        } catch (Exception e) {
            System.out.println("Can't find image.");
        }
    }

    /**
     * paints the menu sprites
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, null);
        g.drawImage(helpImage, 1050, 50, null);
        if (help) {
            g.drawImage(controlsImage,200,200,null);
        } else {
            g.drawImage(titleImage, 100, 250, null);
            g.drawImage(menuImage, 700, 200, null);
            g.drawImage(startImage, 200, 500, null);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * switches to game JPanel when spacebar is pressed
     * stops the menu music
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            crossyRoad.showGamePanel();
            menuMusic.stop();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * sets help flag and repaints when the user's mouse presses on the region of the question mark sprite
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() >= 1050 && e.getX() <= 1150 && e.getY() >= 50 && e.getY() <= 150) {
            help = true;
            repaint();
        }
    }

    /**
     * sets help flag and repaints when the user's mouse is released
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        help = false;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}