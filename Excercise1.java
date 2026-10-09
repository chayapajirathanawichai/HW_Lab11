/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hw_lab11;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Excercise1 extends JFrame{
    private RaceCar[] cars = new RaceCar[4];
    private JTextField[] jtfSpeeds = new JTextField[4];

    public Excercise1() throws HeadlessException {
        // Panel for input speed
        JPanel panelInputs = new JPanel(new FlowLayout());
        // Panel show car=4 -> row
        JPanel panelCars = new JPanel(new GridLayout(4, 1));
        for (int i = 0; i < 4; i++) {
            cars[i] = new RaceCar();
            // linear for panel
            cars[i].setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
            panelCars.add(cars[i]);

            panelInputs.add(new JLabel("Car " + (i + 1) + ": "));
            jtfSpeeds[i] = new JTextField(5);
            panelInputs.add(jtfSpeeds[i]);

            // catch event to enter TextField for update speed
            final int index = i;
            jtfSpeeds[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        int speed = Integer.parseInt(jtfSpeeds[index].getText());
                        cars[index].setSpeed(speed);
                    } catch (NumberFormatException ex) {
                        // null/0 -> car stop
                        cars[index].setSpeed(0);
                    }
                }
            });
        }

        setLayout(new BorderLayout());
        add(panelInputs, BorderLayout.NORTH);
        add(panelCars, BorderLayout.CENTER);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Excercise1 frame = new Excercise1();
            frame.setTitle("RaceCar Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 350);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    class RaceCar extends JPanel implements Runnable {
        private int xBase = 0;
        private int speed = 1;

        public RaceCar() {
            new Thread(this).start();
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }
        @Override
        public void run() {
            while (true) {
                
                xBase += speed;
                if (xBase > getWidth() && getWidth() > 0) {
                    xBase = -50; 
                }
                SwingUtilities.invokeLater(() -> repaint());

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int yBase = getHeight();

            // car wheel
            g.setColor(Color.BLACK);
            g.fillOval(xBase + 10, yBase - 10, 10, 10);
            g.fillOval(xBase + 30, yBase - 10, 10, 10);

            // car body
            g.setColor(Color.GREEN);
            g.fillRect(xBase, yBase - 20, 50, 10);

            // car roof
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
