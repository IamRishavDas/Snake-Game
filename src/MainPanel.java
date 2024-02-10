
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;

public class MainPanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = SCREEN_HEIGHT / UNIT_SIZE;
    static final int DELAY = 55;

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 6;

    int score = 0;
    int targetX;
    int targetY;

    char direction = 'R';
    boolean running = false;

    javax.swing.Timer timer;
    Random random;

    MainPanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newTarget();
        running = true;
        timer = new javax.swing.Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // grid
            // for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            // g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            // g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            // }

            // target
            g.setColor(Color.RED);
            g.fillOval(targetX, targetY, UNIT_SIZE, UNIT_SIZE);

            // snake body
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 100, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        } else {
            gameOver(g);
            gameOverSound();
        }
    }

    public void snakeBiteSound() {
        try {
            String sound = "snake-bite-sound.wav";
            File f = new File("./" + sound);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void gameOverSound() {
        try {
            String sound = "game-over-sound.wav";
            File f = new File("./" + sound);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U' -> y[0] -= UNIT_SIZE;
            case 'D' -> y[0] += UNIT_SIZE;
            case 'L' -> x[0] -= UNIT_SIZE;
            case 'R' -> x[0] += UNIT_SIZE;
        }
    }

    public void newTarget() {
        targetX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        targetY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void score() {
        if ((x[0] == targetX) && (y[0] == targetY)) {
            bodyParts++;
            snakeBiteSound();
            score++;
            newTarget();
        }

    }

    public void checkCollision() {
        // head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // head touches border
        if (x[0] < 0)
            running = false;
        if (x[0] > SCREEN_WIDTH)
            running = false;
        if (y[0] < 0)
            running = false;
        if (y[0] > SCREEN_HEIGHT)
            running = false;

        if (!running)
            timer.stop();

    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Algerian", Font.BOLD, 80));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
        g.setFont(new Font("Algerian", Font.PLAIN, 35));
        metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + score, (SCREEN_WIDTH - metrics.stringWidth("Score: ")) / 2, (SCREEN_HEIGHT / 2) - 100);
    }

    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            score();
            checkCollision();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if (direction != 'R')
                        direction = 'L';
                }
                case KeyEvent.VK_RIGHT -> {
                    if (direction != 'L')
                        direction = 'R';
                }
                case KeyEvent.VK_UP -> {
                    if (direction != 'D')
                        direction = 'U';
                }
                case KeyEvent.VK_DOWN -> {
                    if (direction != 'U')
                        direction = 'D';
                }
            }
        }
    }
}
