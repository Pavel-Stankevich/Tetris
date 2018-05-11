package by.azzibom.tetris.view.swing;

import by.azzibom.tetris.model.TetrisGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * окно игры
 *
 * @author Ihar Misevich
 * @version 1.0
 */
public class GameFrame extends JFrame {

    private JPanel nextShapeField;
    private JPanel gameField;

    private JPanel contentField;
    private JLabel removeLineValueLabel;
    private JLabel scoreValueLabel;
    private JLabel speedValueLabel;
    private JLabel statusLabelValue;
    private JPanel gameFieldContainer;

    private TetrisGame game;
    private DrawSquareStyleStrategy drawSquareStyleStrategy;

    public GameFrame(TetrisGame game, DrawSquareStyleStrategy drawSquareStyleStrategy) {
        this.game = game;
        this.drawSquareStyleStrategy = drawSquareStyleStrategy;

        try {
            super.setIconImage(ImageIO.read(getClass().getResourceAsStream("/tetris_icon.png")));
        } catch (IOException e) {
            System.err.println("load icon exception");
        }

        super.setTitle(game.getName());
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        super.setContentPane(contentField);

        super.pack();
        super.setResizable(false);

        super.setLocationRelativeTo(null);
//        super.setVisible(true);

        this.game.addObserver(new GameObserver());

        removeLineValueLabel.setText(String.valueOf(game.getRemovedLines()));
        scoreValueLabel.setText(String.valueOf(game.getScore()));
        speedValueLabel.setText(String.valueOf(game.getSpeed()));

    }

    // метод для кастомного создания компонентов
    // компоненты создаются после до конструктора
    private void createUIComponents() {


        this.gameField = new TertisGameField(game, drawSquareStyleStrategy);
        this.nextShapeField = new NextShapeField(game, drawSquareStyleStrategy);

        JMenuBar menuBar = new JMenuBar();

        JMenu menu = menuBar.add(new JMenu("File"));

        JMenuItem menuItem = menu.add("Start");
        menuItem.addActionListener(e -> game.start());
        menu.addSeparator();
        menuItem = menu.add("Exit");
        menuItem.addActionListener(e -> System.exit(0));

        super.setJMenuBar(menuBar);
    }

    /**
     * внутрений класс подпищика(наблюдателя)
     *
     * @author Ihar Misevich
     * @version 1.0
     */
    private class GameObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            if (arg != null) {
                if (arg.equals("removedLines"))
                    removeLineValueLabel.setText(String.valueOf(game.getRemovedLines()));
                if (arg.equals("score"))
                    scoreValueLabel.setText(String.valueOf(game.getScore()));
                if (arg.equals("speed"))
                    speedValueLabel.setText(String.valueOf(game.getSpeed()));
                if (arg.equals("pause") && game.isPause())
                    statusLabelValue.setEnabled(true);
                else
                    statusLabelValue.setEnabled(false);

                if (arg.equals("gameOver") && game.isGameOver()) {
                    statusLabelValue.setEnabled(true);
                    statusLabelValue.setForeground(Color.RED);
                    statusLabelValue.setText("Game Over!");
                }
            }
            repaint();
        }
    }
}
