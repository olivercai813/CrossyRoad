import javax.swing.*;
import java.awt.*;

public class CrossyRoad {
    private static final String GAME_PANEL = "game";
    private static final String MENU_PANEL = "menu";
    private JFrame frame;
    private JPanel homePanel;
    private Game game;
    private Menu menu;
    private CardLayout cl;

    /**
     * creates the JFrame, creates a home JPanel with CardLayout
     * adds the game JPanel and menu JPanel to the home JPanel
     */
    public CrossyRoad() {
        frame = new JFrame("Crossy Road");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cl = new CardLayout();
        homePanel = new JPanel();
        homePanel.setLayout(cl);

        menu = new Menu(this);
        menu.setFocusable(true);
        homePanel.add(menu, MENU_PANEL);

        game = new Game(this);
        game.setFocusable(true);
        homePanel.add(game, GAME_PANEL);

        frame.add(homePanel);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * switches to the game JPanel using CardLayout
     * starts game music
     */
    public void showGamePanel() {
        game.reset();
        game.gameMusic.start();
        game.gameMusic.loop();
        cl.show(homePanel,GAME_PANEL);
        game.requestFocusInWindow();
    }

    /**
     * switches to the menu JPanel using CardLayout
     * starts menu music
     */
    public void showMenuPanel() {
        cl.show(homePanel,MENU_PANEL);
        menu.requestFocusInWindow();
        menu.menuMusic.start();
        menu.menuMusic.loop();
    }

    /**
     * main method
     * creates new CrossyRoad and calls the game's run method
     * @param args
     */
    public static void main(String[] args) {
        CrossyRoad crossyRoad = new CrossyRoad();
        crossyRoad.game.run();
    }
}
