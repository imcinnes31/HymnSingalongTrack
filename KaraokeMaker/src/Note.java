import java.awt.Font;

import javax.swing.Timer;

public class Note {
	private OutlinedLabel thisLabel;
	private Timer thisTimer;
	private int thisAnimX = 0;
	private int thisSpeed;
	private int thisWidth;
	private int thisDelay = 0;
	private int maxDelay = 0;
	private static int delayFactor = 48;	// orig 48
	private static int noteFactor = 44;		// orig 44
	private int overallSpeed = 45;	// orig 45
	
	Note (String noteString, String noteLength) {
		thisLabel = new OutlinedLabel(noteString);
    	thisLabel.setFont(new Font("Arial", Font.BOLD, 60));
    	thisWidth = thisLabel.getComponentWidth();
    	    	
    	if (noteString == "....") {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / 4));
    		overallSpeed *= 6;
    	} else if (noteLength == "ww") {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor / 2));
    	} else if (noteLength == "w.") {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor / 1.5));
    	} else if (noteLength == "w") {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor * 1));
    	} else if (noteLength == "h.") {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor * (4 / 3)));
    	} else if (noteLength == "h") {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor * 2));
    	} else if (noteLength == "q") {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor * 4));
    	}
    	
    	if (noteString.equals("....")) {
    		maxDelay = 0;
		} else if (noteLength == "ww") {
    		maxDelay = delayFactor / 3;
    	} else if (noteLength == "w.") {
    		maxDelay = delayFactor / 4;
    	} else if (noteLength == "w") {  
    		maxDelay = delayFactor / 6;
    	} else if (noteLength == "h.") {
    		maxDelay = delayFactor / 8;
    	} else if (noteLength == "h") {
    		maxDelay = delayFactor / 12;
    	} else if (noteLength == "q") {
    		maxDelay = delayFactor / 24;
    	}

    	thisTimer = new Timer(overallSpeed, e -> {
    		if (thisDelay < maxDelay && maxDelay > 0) {
        		thisDelay += 1;
    		} else {
        		thisAnimX += thisSpeed;
        		thisLabel.setAnimX(thisAnimX);
        		thisLabel.repaint();
        		if (thisAnimX >= thisWidth) {
        			((Timer) e.getSource()).stop();
        			PanelWithOutlinedLabel.startNextAnim();
        		}    			
    		}
    	});
	}
	
	public Timer getTimer() {
		return thisTimer;
	}
	
	public OutlinedLabel getLabel() {
		return thisLabel;
	}
}
