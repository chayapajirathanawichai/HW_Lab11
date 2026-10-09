/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hw_lab11;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class BalloonShooter extends JPanel implements Runnable, KeyListener{
    // system
    private Random random = new Random();
    
    // Gun
    private double gunAngle = 90; // deg=90
    private int gunLength = 50;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    
    // Balloon
    private int balloonX, balloonY;
    private int balloonRadius = 20;
    
    // Bullets
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private double bulletSpeed = 10;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Balloon Shooter");
            BalloonShooter gamePanel = new BalloonShooter();
            frame.add(gamePanel);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    
    public BalloonShooter() {
        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(600, 400));
        setBackground(Color.WHITE);

        // random 1st balloon
        spawnBalloon();
        new Thread(this).start();
    }
    private void spawnBalloon() {
        balloonY = 40;
        balloonX = random.nextInt(500) + 50;
    }
    @Override
    public void run() {
        while (true) {
            SwingUtilities.invokeLater(() -> {
                updateGame();
                repaint();
            });

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
    private void updateGame() {
        // update gun angle
        if (leftPressed && gunAngle < 170) {
            gunAngle += 3; // turn left
        }
        if (rightPressed && gunAngle > 10) {
            gunAngle -= 3; // turn right
        }

        // update bullet & check collision
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.x += b.dx;
            b.y += b.dy;

            // check collision
            double distance = Math.hypot(b.x - balloonX, b.y - balloonY);
            if (distance < balloonRadius + 4) {
                it.remove(); // del bullet
                spawnBalloon(); // random new balloon
                continue;
            }

            // delete if bullet out of frame
            if (b.x < 0 || b.x > getWidth() || b.y < 0 || b.y > getHeight()) {
                it.remove();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // balloon
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(balloonX - balloonRadius, balloonY - balloonRadius, balloonRadius * 2, balloonRadius * 2);

        // center base
        int baseX = getWidth() / 2;
        int baseY = getHeight();

        double angleRad = Math.toRadians(gunAngle);
        int tipX = baseX + (int) (gunLength * Math.cos(angleRad));
        int tipY = baseY - (int) (gunLength * Math.sin(angleRad));

        // gun
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(baseX, baseY, tipX, tipY);

        // bullet
        for (Bullet b : bullets) {
            g2d.fillOval((int) b.x - 4, (int) b.y - 4, 8, 8);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        } else if (key == KeyEvent.VK_UP) {
            // press to shoot
            double angleRad = Math.toRadians(gunAngle);
            int tipX = getWidth() / 2 + (int) (gunLength * Math.cos(angleRad));
            int tipY = getHeight() - (int) (gunLength * Math.sin(angleRad));

            double dx = bulletSpeed * Math.cos(angleRad);
            double dy = -bulletSpeed * Math.sin(angleRad);

            bullets.add(new Bullet(tipX, tipY, dx, dy));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        } else if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }
    private class Bullet {
        double x, y;
        double dx, dy;

        public Bullet(double x, double y, double dx, double dy) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
        }
    }
}
