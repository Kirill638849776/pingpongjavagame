import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class pingpong extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 15;
    private static final int PADDLE_HEIGHT = 100;
    private static final int BALL_SIZE = 20;
    
    private Timer timer;
    private boolean gameStarted = false;
    private long animationStartTime = 0;
    
    private int playerY = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int aiY = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int ballX = WIDTH / 2;
    private int ballY = HEIGHT / 2;
    private int ballDX = 4;
    private int ballDY = 4;
    private int playerScore = 0;
    private int aiScore = 0;
    
    private boolean mousePressed = false;
    private int lastMouseY = 0;
    
    public pingpong() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        
        addMouseListener(this);
        addMouseMotionListener(this);
        
        animationStartTime = System.currentTimeMillis();
        timer = new Timer(16, this);
        timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        if (!gameStarted) {
            drawIntroAnimation(g2d);
        } else {
            drawGame(g2d);
        }
    }
    
    private void drawIntroAnimation(Graphics2D g2d) {
        long elapsed = System.currentTimeMillis() - animationStartTime;
        
        if (elapsed < 3000) {
            draw3DPaw(g2d, elapsed);
        } else if (elapsed < 6000) {
            drawCatArsenal(g2d, elapsed - 3000);
        } else {
            gameStarted = true;
        }
    }
    
    private void draw3DPaw(Graphics2D g2d, long elapsed) {
        double progress = elapsed / 3000.0;
        int centerX = WIDTH / 2;
        int centerY = HEIGHT / 2;
        
        g2d.setColor(new Color(255, 165, 0));
        
        for (int i = 0; i < 5; i++) {
            double offset = Math.sin(progress * Math.PI * 2 + i * 0.3) * 20;
            int size = 80 - i * 10;
            g2d.fillOval(centerX - size/2 + (int)offset, centerY - size/2, size, size);
            g2d.setColor(new Color(255, 165 - i*20, 0));
        }
        
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < 4; i++) {
            int fingerX = centerX - 30 + i * 20;
            int fingerY = centerY - 40 + (int)(Math.sin(progress * Math.PI * 2) * 10);
            g2d.fillOval(fingerX, fingerY, 15, 25);
        }
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        String text = "Cat Arsenal Loading...";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(text, (WIDTH - fm.stringWidth(text)) / 2, HEIGHT - 50);
    }
    
    private void drawCatArsenal(Graphics2D g2d, long elapsed) {
        double progress = elapsed / 3000.0;
        
        g2d.setColor(new Color(20, 20, 40));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        
        g2d.setColor(new Color(255, 215, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "CAT ARSENAL";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (WIDTH - fm.stringWidth(title)) / 2;
        int y = HEIGHT / 2 - 50;
        
        double scale = 1.0 + Math.sin(progress * Math.PI * 4) * 0.1;
        g2d.setFont(g2d.getFont().deriveFont((float)(48 * scale)));
        fm = g2d.getFontMetrics();
        x = (WIDTH - fm.stringWidth(title)) / 2;
        g2d.drawString(title, x, y);
        
        g2d.setColor(Color.ORANGE);
        for (int i = 0; i < 5; i++) {
            int catX = 100 + i * 150;
            int catY = HEIGHT / 2 + 50 + (int)(Math.sin(progress * Math.PI * 2 + i) * 20);
            drawSimpleCat(g2d, catX, catY);
        }
        
        g2d.setColor(Color.GRAY);
        g2d.fillRect(100, HEIGHT - 100, WIDTH - 200, 20);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(100, HEIGHT - 100, (int)((WIDTH - 200) * progress), 20);
    }
    
    private void drawSimpleCat(Graphics2D g2d, int x, int y) {
        g2d.fillOval(x, y, 40, 35);
        g2d.fillPolygon(new int[]{x+5, x+15, x+10}, new int[]{y-5, y-5, y}, 3);
        g2d.fillPolygon(new int[]{x+25, x+35, x+30}, new int[]{y-5, y-5, y}, 3);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(x+10, y+10, 6, 6);
        g2d.fillOval(x+24, y+10, 6, 6);
        g2d.setColor(Color.ORANGE);
    }
    
    private void drawGame(Graphics2D g2d) {
        g2d.setColor(new Color(0, 50, 0));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
        g2d.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);
        
        g2d.setColor(Color.BLUE);
        g2d.fillRect(20, playerY, PADDLE_WIDTH, PADDLE_HEIGHT);
        
        g2d.setColor(Color.RED);
        g2d.fillRect(WIDTH - 20 - PADDLE_WIDTH, aiY, PADDLE_WIDTH, PADDLE_HEIGHT);
        
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(ballX - BALL_SIZE/2, ballY - BALL_SIZE/2, BALL_SIZE, BALL_SIZE);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.drawString(String.valueOf(playerScore), WIDTH / 2 - 100, 50);
        g2d.drawString(String.valueOf(aiScore), WIDTH / 2 + 80, 50);
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("Зажми ЛКМ и перетягивай для управления", 10, HEIGHT - 10);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted) {
            updateGame();
        }
        repaint();
    }
    
    private void updateGame() {
        ballX += ballDX;
        ballY += ballDY;
        
        if (ballY <= 0 || ballY >= HEIGHT - BALL_SIZE) {
            ballDY = -ballDY;
        }
        
        if (ballX <= 20 + PADDLE_WIDTH && ballY >= playerY && ballY <= playerY + PADDLE_HEIGHT) {
            ballDX = -ballDX;
            ballX = 20 + PADDLE_WIDTH + 1;
        }
        
        if (ballX >= WIDTH - 20 - PADDLE_WIDTH - BALL_SIZE && ballY >= aiY && ballY <= aiY + PADDLE_HEIGHT) {
            ballDX = -ballDX;
            ballX = WIDTH - 20 - PADDLE_WIDTH - BALL_SIZE - 1;
        }
        
        if (ballX < 0) {
            aiScore++;
            resetBall();
        } else if (ballX > WIDTH) {
            playerScore++;
            resetBall();
        }
        
        int aiCenter = aiY + PADDLE_HEIGHT / 2;
        if (aiCenter < ballY - 10) {
            aiY += 3;
        } else if (aiCenter > ballY + 10) {
            aiY -= 3;
        }
        
        aiY = Math.max(0, Math.min(HEIGHT - PADDLE_HEIGHT, aiY));
    }
    
    private void resetBall() {
        ballX = WIDTH / 2;
        ballY = HEIGHT / 2;
        ballDX = (Math.random() > 0.5 ? 4 : -4);
        ballDY = (Math.random() > 0.5 ? 4 : -4);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
        lastMouseY = e.getY();
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressed = false;
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (mousePressed) {
            int deltaY = e.getY() - lastMouseY;
            playerY += deltaY;
            playerY = Math.max(0, Math.min(HEIGHT - PADDLE_HEIGHT, playerY));
            lastMouseY = e.getY();
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {}
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Ping Pong - Cat Arsenal Edition");
        pingpong game = new pingpong();
        
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}