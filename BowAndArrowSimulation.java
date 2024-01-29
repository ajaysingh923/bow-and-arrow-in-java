import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BowAndArrowSimulation extends JFrame {

    private int bowX = 40;
    private int bowY = 350;
    private double arrowX;
    private double arrowY;
    private double arrowXVelocity;
    private double arrowYVelocity;
    private boolean arrowInFlight = false;
    private double arrowSpeed;
    private double launchAngle; 

    private int targetX = 400;
    private int targetY = 300;

    private long startTime; 
    private long endTime;    
    private long timeOfFlight;  

    public BowAndArrowSimulation() {
        setTitle("Bow and Arrow Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBow(g);
                if (arrowInFlight) {
                    drawArrow(g);
                    checkCollision();
                }
                drawTarget(g);
                displaySpeed(g);
                displayAngle(g);
                displayTimeOfFlight(g);
            }
        };

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!arrowInFlight) {
                    calculateLaunchAngle(e);
                    fireArrow();
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        getContentPane().add(panel);

        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (arrowInFlight) {
                    updateArrow();
                }
                panel.repaint();
            }
        });

        timer.start();
    }

    private void drawBow(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(bowX, bowY, 20, 100);
    }

    private void drawArrow(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval((int) arrowX, (int) arrowY, 50, 50);
    }

    private void drawTarget(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval(targetX, targetY, 100, 100);
    }

    private void calculateLaunchAngle(MouseEvent e) {
        
        double mouseY = e.getY();
        launchAngle = Math.atan2(mouseY - (bowY + 50), bowX + 20);
    }

    private void fireArrow() {
        
        arrowInFlight = true;

       
        startTime = System.currentTimeMillis();

        
        arrowX = bowX + 20;
        arrowY = bowY + 50;

        
        arrowSpeed = 100; 
        arrowXVelocity = arrowSpeed * Math.cos(launchAngle);
        arrowYVelocity = arrowSpeed * Math.sin(launchAngle);
    }

    private void updateArrow() {
        
        arrowX += arrowXVelocity;
        arrowY += arrowYVelocity;

        
        arrowYVelocity += 0.2; 

        
        if (arrowYVelocity > 0) {
            arrowSpeed += 0.1; 
            arrowYVelocity += 0.1; 
        }

        if (arrowY > getHeight() || arrowX > getWidth()) {
            arrowInFlight = false;
            endTime = System.currentTimeMillis();
            timeOfFlight = endTime - startTime;
        }
    }

    private void checkCollision() {
        int arrowCenterX = (int) (arrowX + 5);
        int arrowCenterY = (int) (arrowY + 5);

        if (arrowCenterX >= targetX && arrowCenterX <= targetX + 100 &&
                arrowCenterY >= targetY && arrowCenterY <= targetY + 100) {
            arrowInFlight = false;
            JOptionPane.showMessageDialog(this, "Target hit!");
        }
    }

    private void displaySpeed(Graphics g) {
        g.setColor(Color.BLACK);
        Font boldFont = new Font(g.getFont().getName(), Font.BOLD, 14);
        g.setFont(boldFont);
        g.drawString("Arrow Speed: " + String.format("%.2f", arrowSpeed), 10, 20);
    }

    private void displayAngle(Graphics g) {
        g.setColor(Color.BLACK);
        Font boldFont = new Font(g.getFont().getName(), Font.BOLD, 14);
        g.setFont(boldFont);
        g.drawString("Launch Angle: " + String.format("%.2f", Math.toDegrees(Math.abs(launchAngle))) + " degrees", 10, 40);
    }

    private void displayTimeOfFlight(Graphics g) {
        g.setColor(Color.BLACK);
        Font boldFont = new Font(g.getFont().getName(), Font.BOLD, 14);
        g.setFont(boldFont);
        g.drawString("Time of Flight: " + String.format("%.2f", timeOfFlight / 1000.0) + " seconds", 10, 60);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BowAndArrowSimulation().setVisible(true));
    }
}
