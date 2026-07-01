import java.awt.Image;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ClapIconLabel extends JLabel {
	private int animX = 0;
	private int desiredWidth = 60;
    private int desiredHeight = 60;
    
    URL emptyImageUrl = getClass().getResource("clapIconEmpty.png");
	ImageIcon emptyIcon = new ImageIcon(emptyImageUrl);
	Image emptyImage = emptyIcon.getImage();
	Image scaledEmptyImage = emptyImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
	ImageIcon iconEmpty = new ImageIcon(scaledEmptyImage);
 	
    URL fullImageUrl = getClass().getResource("clapIconFull.png");
	ImageIcon fullIcon = new ImageIcon(fullImageUrl);
	Image fullImage = fullIcon.getImage();
	Image scaledFullImage = fullImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
	ImageIcon iconFull = new ImageIcon(scaledFullImage);

	public ClapIconLabel() {
		this.setIcon(iconEmpty);
		this.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
	}

	public int getComponentWidth() {
    	return getPreferredSize().width;
    }
    
    public void setAnimX(int newX) {
    	if (animX == 0 && newX > 0) {
    		this.setIcon(iconFull);
    	}
    }
}
