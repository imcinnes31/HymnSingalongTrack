import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

public class AnimatedOutlineText extends JTextField {
    private int hue = 0;
    private int textWidth = 0;
    private int animationSpeed = 20;
    private int fontSize = 60;
    private int verticalMargin = 100; 
    private float strokeSize = 5.0f;
    private final String text = "KARAOKE TEXT M";

    public AnimatedOutlineText() {
        // Timer to change color every 50ms
        Timer timer = new Timer(50, e -> {
            hue += animationSpeed;
//            if (hue > textWidth + (animationSpeed - 1)) hue = 0;
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));

        // Create Shape for the text
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout tl = new TextLayout(text, g2d.getFont(), frc);
        Shape shape = tl.getOutline(null);
        
        FontMetrics metrics = g.getFontMetrics();
        textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();
        int textMargin = (getWidth() - textWidth) / 2;

        // Center the text
        AffineTransform transform = new AffineTransform();
        transform.translate(textMargin, verticalMargin);
        shape = transform.createTransformedShape(shape);

        // 1. Draw Outline
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.draw(shape);
        
        // 2. Fill with Animated Color
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fill(shape);
                
        Shape clip = g2d.getClip();
        g2d.setClip(textMargin, 0, hue, getHeight());
        g2d.setColor(new Color(135, 206, 235));
        g2d.fill(shape);
        g2d.setClip(clip);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Animated Text Frame");
        frame.setSize(750, 200);
        AnimatedOutlineText outlinedText = new AnimatedOutlineText();
        frame.add(outlinedText);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}