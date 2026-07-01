import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

public class OutlinedLabel extends JLabel {

    private Color outlineColor = Color.BLACK;
    private Color fillColor = Color.WHITE;
    private float outlineWidth = 5.0f;
    private Font font = new Font("Arial", Font.BOLD, 60);
    
//    private Timer timer;
    private int animX = 0;

    public OutlinedLabel(String text) {
        super(text);
        setBorder(null);
        // Ensure the label is opaque for proper background painting if needed
//        timer = new Timer(50, e -> {
//            animX += animationSpeed;
//            repaint();
//            if (animX >= getPreferredSize().width) {
//            	((Timer)e.getSource()).stop();
//            }
//        });
//        timer.setRepeats(false); // Ensure it only runs once
//        timer.start();
    }
    
//    public Timer getTimer() {
//    	return timer;
//    }
    
    public int getComponentWidth() {
    	return getPreferredSize().width;
    }
    
    public void setAnimX(int newX) {
    	animX = newX;
    }

    // Setters for colors and width
    public void setOutlineColor(Color color) {
        this.outlineColor = color;
        repaint();
    }

    public void setFillColor(Color color) {
        this.fillColor = color;
        repaint();
    }

    public void setOutlineWidth(float width) {
        this.outlineWidth = width;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
//    	g.setColor(Color.LIGHT_GRAY);
        Color translucentRed = new Color(0, 0, 0, 0); 
        g.setColor(translucentRed);
    	g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Get the metrics and bounds for centering
        String text = getText();
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout tl = new TextLayout(text, font, frc);
        
        // Calculate the position for centering
        // Note: this simple centering might not be perfect, further adjustments may be needed
//        int x = (getWidth() - (int) tl.getBounds().getWidth()) / 2;
//        int y = (int) ((getHeight() + (int) tl.getBounds().getHeight()) / 2 - tl.getAscent());

        // Create the shape of the text
//        Shape shape = tl.getOutline(AffineTransform.getTranslateInstance(x, y + tl.getAscent()));
        Shape shape = tl.getOutline(AffineTransform.getTranslateInstance(2, tl.getAscent() - 8));        
        
        // Draw the outline
        g2.setColor(outlineColor);
        g2.setStroke(new BasicStroke(outlineWidth));
        g2.draw(shape);

        // Draw the fill
        g2.setColor(fillColor);
        g2.fill(shape);
        
        Shape clip = g2.getClip();
        g2.setClip(0, 0, animX, getHeight());
        g2.setColor(new Color(135,206,235));
        g2.fill(shape);
        g2.setClip(clip);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        // Provide a sufficient preferred size to avoid cutting off the outline
        Dimension size = super.getPreferredSize();
        int widthMargin = (int) (outlineWidth * 2 - 5);
        int heightMargin = (int) (outlineWidth * 2 - 20);
        size.width += widthMargin;
        size.height += heightMargin;
        return size;
    }
}