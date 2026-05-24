import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Timer;

public class Note {
	private OutlinedLabel thisLabel;
	private Timer thisTimer;
	private boolean scrollLine;
	private boolean slowScroll;
	private int thisAnimX = 0;
	private int thisSpeed;
	private int thisWidth;
	private int thisDelay = 0;
	private int maxDelay = 0;
	private boolean quickNote = true;
	private static int delayFactor = 48;	// orig 48
	private static int noteFactor = 44;		// orig 44
	private int overallSpeed = 45;	// orig 45
	
	Note (String noteString, String noteLength, boolean scrollLine, int wantedTempo) {
		this.scrollLine = scrollLine;
		this.overallSpeed = wantedTempo;
		thisLabel = new OutlinedLabel(noteString, scrollLine);
    	thisLabel.setFont(new Font("Arial", Font.BOLD, 60));
    	thisWidth = thisLabel.getComponentWidth();
    	 
    	if (noteString == "*") {
    		thisSpeed = 0;
    	} else if (noteString == "") {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / 2));
    	} else if (noteLength.equals("wwww")) {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor / 4));
    		this.quickNote = false;
    	} else if (noteLength.equals("www")) {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor / 3));
    		this.quickNote = false;
    	} else if (noteLength.equals("ww.")) {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor / 2.5));
    		this.quickNote = false;
    	} else if (noteLength.equals("ww")) {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor / 2));
    		this.quickNote = false;
    	} else if (noteLength.equals("w.")) {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor / 1.5));
    		this.quickNote = false;
    	} else if (noteLength.equals("wq")) {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor / 1.25));
    		this.quickNote = false;
    	} else if (noteLength.equals("w")) {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor * 1));
    		this.quickNote = false;
    	} else if (noteLength.equals("h.")) {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor * (4 / 3)));
    		this.quickNote = false;
    	} else if (noteLength.equals("h")) {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor * 2));
    		this.quickNote = false;
    	} else if (noteLength.equals("q.")) {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor * (8 / 3)));
    	} else if (noteLength.equals("q")) {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor * 4));
    	} else if (noteLength.equals("e.")) {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor * (16 / 3)));
    	} else if (noteLength.equals("e")) {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor * 8));
    	} else if (noteLength.equals("s")) {
    		thisSpeed = (int) Math.ceil(Double.valueOf((double) thisWidth / noteFactor * 16));
    	} 
    	
    	if (noteString.equals("*")) {
    		maxDelay = 0;
		} else if (noteString.equals("")) {
    		maxDelay = delayFactor / 2;
		} else if (noteLength.equals("wwww")) {
    		maxDelay = delayFactor / 6;
		} else if (noteLength.equals("www")) {
    		maxDelay = delayFactor / 6;
		} else if (noteLength.equals("ww.")) {
    		maxDelay = delayFactor / 6;
		} else if (noteLength.equals("ww")) {
    		maxDelay = delayFactor / 6;
    	} else if (noteLength.equals("w.")) {
    		maxDelay = delayFactor / 6;
    	} else if (noteLength.equals("wq")) {
    		maxDelay = delayFactor / 6;
    	} else if (noteLength.equals("w")) {  
    		maxDelay = delayFactor / 6;
    	} else if (noteLength.equals("h.")) {
    		maxDelay = delayFactor / 9;
    	} else if (noteLength.equals("h")) {
    		maxDelay = delayFactor / 12;
    	} else if (noteLength.equals("q.")) {
    		maxDelay = delayFactor / 18;
    	} else if (noteLength.equals("q")) {
    		maxDelay = delayFactor / 24;
       	} else if (noteLength.equals("e.")) {
    		maxDelay = delayFactor / 36;
       	} else if (noteLength.equals("e")) {
    		maxDelay = delayFactor / 48;
    	} else if (noteLength.equals("s")) {
    		maxDelay = delayFactor / 96;
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
        			if (this.slowScroll == true && this.scrollLine == true) {
        				this.scrollLine = false;
        				PanelWithOutlinedLabel.scrollScreen();
        			}
        		} 
        		if (thisAnimX >= (thisWidth / 8 * 3) && quickNote == true && slowScroll == false) {		// orig / 2
        			if (this.scrollLine == true) {
        				this.scrollLine = false;
        				PanelWithOutlinedLabel.scrollScreen();
        			}
        		} 
        		if (thisAnimX >= (thisWidth / 8 * 5) && quickNote == false && slowScroll == false) {	//orig  / 4 * 3
        			if (this.scrollLine == true) {
        				this.scrollLine = false;
        				PanelWithOutlinedLabel.scrollScreen();
        			}        			
        		}
    		}
    	});
    	

	}
	
	public boolean checkScroll() {
    	return scrollLine;
	}
	
	public Timer getTimer() {
		return thisTimer;
	}
	
	public OutlinedLabel getLabel() {
		return thisLabel;
	}
	
	public boolean getIsScroll() {
		return scrollLine;
	}
	
	public void setScroll() {
		this.scrollLine = true;
//        System.out.println(this.thisLabel.getText() + " is set to scroll " + quickNote);
	}

	public void setSlowScroll() {
		this.slowScroll = true;
//        System.out.println(this.thisLabel.getText() + " is set to slow scroll");
	}
}
