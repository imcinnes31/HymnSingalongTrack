import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

import javax.swing.*;

class OutlinedTextLabel extends JLabel {
    private int animX = 0;
    private int animationSpeed = 10;

    public OutlinedTextLabel(String text) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 60));
        setOpaque(true);
        setBackground(Color.GREEN);
        Dimension labelSize = getPreferredSize();
        setSize(labelSize.width + 3, labelSize.height - 18);
//        setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        Timer timer = new Timer(50, e -> {
            animX += animationSpeed;
//            if (hue > textWidth + (animationSpeed - 1)) hue = 0;
            repaint();
        });
        timer.start();

    }

    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Get the glyph vector and shape
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = getFont().createGlyphVector(frc, getText());
        Shape textShape = gv.getOutline(
        		getX() + getInsets().left + 3, 
        		getY() + getInsets().top + g2.getFontMetrics().getAscent() - 7
        );

        // Draw the outline
        g2.setColor(Color.BLACK); // Outline color
        g2.setStroke(new BasicStroke(8.0f)); // Outline thickness
        g2.draw(textShape);

        // Fill the text
        g2.setColor(Color.WHITE); // Fill color
        g2.fill(textShape);
        
        Shape clip = g2.getClip();
        g2.setClip(0, 0, animX, getHeight());
        g2.setColor(new Color(135, 206, 235));
        g2.fill(textShape);
        g2.setClip(clip);

        g2.dispose();
    }
    
    @Override
    public Dimension getPreferredSize() {
        // Correctly calculate the preferred size to account for the outlined text
        String text = getText();
        FontMetrics fm = this.getFontMetrics(getFont());
        int w = fm.stringWidth(text);
        int h = fm.getHeight();
        return new Dimension(w, h);
    }
}