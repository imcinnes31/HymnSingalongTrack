import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class KaraokeMakerWindow {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//		SwingUtilities.invokeLater(() -> {
					JFrame testFrame = new JFrame("Animated Color Text");
					testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					testFrame.setSize(700, 400);

		            JPanel testPanel = new JPanel();
//		            testPanel.setBackground(Color.DARK_GRAY);
		            testPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
		            testPanel.setSize(700,400);

			        OutlinedTextLabel testLabel1 = new OutlinedTextLabel("LINE M");
			        OutlinedTextLabel testLabel2 = new OutlinedTextLabel("LINE N");
			        OutlinedTextLabel testLabel3 = new OutlinedTextLabel("LINE O");

		            testFrame.add(testPanel);
		            testPanel.add(testLabel1);
		            testPanel.add(testLabel2);
		            testPanel.add(testLabel3);
		            
//		            testFrame.add(testLabel1);
//		            testFrame.add(testLabel2);
//		            testFrame.add(testLabel3);

		            testFrame.setVisible(true);
		            
//			        JFrame frame = new JFrame("Animated Color Text");
//			        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			        OutlinedTextLabel testLabel = new OutlinedTextLabel("LINE M");
//			        OutlinedTextLabel testLabel2 = new OutlinedTextLabel("LINE N");
//			        JPanel testPanel = new JPanel();
//			        testPanel.add(testLabel);
//			        testPanel.add(testLabel2);
//			        testPanel.setSize(500,200);
////			        frame.add(testLabel);
//			        frame.add(testPanel);
//			        frame.setSize(1000, 200);
////			        Dimension labelSize = testLabel.getPreferredSize();
//			        frame.setLayout(null); 
////			        testLabel.setBounds(0, 0, labelSize.width + 3, labelSize.height - 18);
//			        frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public KaraokeMakerWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
        JFrame frame = new JFrame("Color Animation");
        AnimatedOutlineText panel = new AnimatedOutlineText();
        frame.add(panel);
        frame.setSize(500, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}

}
