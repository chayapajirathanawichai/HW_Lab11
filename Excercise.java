/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hw_lab11;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Excercise extends JFrame{

    public Excercise() throws HeadlessException {
        add(new RaceCar());
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Excercise frame = new Excercise();
            frame.setTitle("Exercise - RaceCar");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 150);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    class RaceCar extends JPanel implements Runnable{
        private int xBase = 0;
        private int sleepTime = 10;
        private Thread thread;
        //private Timer timer = new Timer(10, new Listener());
        
        public RaceCar() {
            this.setFocusable(true);
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_EQUALS) { 
                        if (sleepTime > 5) {
                            sleepTime -= 5; 
                        }
                    } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_MINUS) { 
                        sleepTime += 1; 
                    }
                }
            });

            thread = new Thread(this);
            thread.start();
        }

        @Override
        public void run() {
            while (true) {
                SwingUtilities.invokeLater(() -> repaint());
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break; 
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int yBase = getHeight();

            if (xBase > getWidth()) {
                xBase = -50; 
            } else {
                xBase += 1;
            }
            g.setColor(Color.BLACK);
            g.fillOval(xBase + 10, yBase - 10, 10, 10);
            g.fillOval(xBase + 30, yBase - 10, 10, 10);
            g.setColor(Color.GREEN);
            g.fillRect(xBase, yBase - 20, 50, 10);
            g.setColor(Color.RED);
            Polygon polygon = new Polygon();
            polygon.addPoint(xBase + 10, yBase - 20);
            polygon.addPoint(xBase + 20, yBase - 30);
            polygon.addPoint(xBase + 30, yBase - 30);
            polygon.addPoint(xBase + 40, yBase - 20);
            g.fillPolygon(polygon);
        }
    }
}

