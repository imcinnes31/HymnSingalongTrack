import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.KeyEvent;

public class PanelWithOutlinedLabel {
//    static int animationSpeed = 0;
//    static int animationX = 0;
//    static int animationDelay = 50;
//    private static Timer[] timerArray;
    private static ArrayList<Note> noteList = new ArrayList<Note>();
    private static int currentNoteNumber;
    private static Timer currentTimer;
    private static boolean timerStatus = false;
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Outlined Text Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1500, 600);
            
            JPanel mainContainer = new JPanel();
            
            mainContainer.setFocusable(true); 
            mainContainer.requestFocusInWindow();
            
            Action spacebarAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (timerStatus == false) {
                    	currentTimer.start();
                    	timerStatus = true;
                    } else {
                    	currentTimer.stop();
                    	timerStatus = false;
                    }
                    // Add your custom logic here
                }
            };

            KeyStroke spacebarPressed = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false); // false means on key press

            // 3. Bind the Key Stroke to the Action
            mainContainer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(spacebarPressed, "performSpacebarAction");
            mainContainer.getActionMap().put("performSpacebarAction", spacebarAction);
            
            mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
            JScrollPane scrollPane = new JScrollPane(mainContainer);
            
            String[][] hymn830Data = 
            	{
            			{"PRAISE@","h"},
            			{"GOD@","q"},
            			{"FROM@","q"},	//BAR
            			{"WHOM@","q"},
            			{"ALL@","q"},
            			{"BLESS","h"},	//BAR
            			{"INGS@","h"},
            			{"FLOW@","h"},	//BAR
            			{"....","w"},	//BAR
            			{"PRAISE@","h"},
            			{"HIM@","q"},
            			{"ALL@","q"},	//BAR
            			{"CREA","q"},
            			{"TURES@","q"},
            			{"HERE@","h"},	//BAR
            			{"BE","h"},
            			{"LOW@","h"},	//BAR
            			{"....","w"},	//BAR
            			{"PRAISE@","h"},
            			{"HIM@","q"},
            			{"A","q"},	//BAR
            			{"BOVE@","q"},
            			{"YE@","q"},
            			{"HEAVEN","h"},	//BAR
            			{"LY@","h"},
            			{"HOST@","h"},	//BAR
            			{"....","w"},	//BAR
            			{"PRAISE@","h"},
            			{"FA","q"},
            			{"THER@","q"},	//BAR
            			{"SON@","q"},
            			{"AND@","q"},
            			{"HO","h."},	
            			{"LY@","h."},	//BAR
            			{"GHOST.@","h"},
            			{"....","w"},
            			{"A","ww"},
            			{"MEN.","w."},
            	};
            
            ArrayList<JPanel> hymnLines = new ArrayList<JPanel>();
            JPanel currentLine = addNewLine(hymnLines);
            
            double currentBarLength = 0;
            for (int i = 0; i < hymn830Data.length; i++) {
//            for (int i = 0; i < 7; i++) {
            	if (hymn830Data[i][0].equals("....")) {
            		currentBarLength += 0;
            	} else if (hymn830Data[i][1] == "w.") {
            		currentBarLength += 1.5;
            	} else if (hymn830Data[i][1] == "w") {
            		currentBarLength += 1;
            	} else if (hymn830Data[i][1] == "h.") {
            		currentBarLength += 0.75;
            	} else if (hymn830Data[i][1] == "h") {
            		currentBarLength += 0.5;
            	} else if (hymn830Data[i][1] == "q") {
            		currentBarLength += 0.25;
            	}
            	Note newNote;
            	if (currentBarLength >= 1.5 && i + 1 < hymn830Data.length) {
            		currentBarLength = 0;
            		if (hymn830Data[i][0].equals("....")) {
                		newNote = new Note(hymn830Data[i][0], hymn830Data[i][1]);
            		} else if (hymn830Data[i][0].contains("@")) {
                		newNote = new Note(hymn830Data[i][0].replace("@",""), hymn830Data[i][1]);
                	} else {
                		newNote = new Note(hymn830Data[i][0].replace("@","").concat("-"), hymn830Data[i][1]);
                	}
                	currentLine.add(newNote.getLabel());
                    currentLine = addNewLine(hymnLines);
            	} else {
                	newNote = new Note(hymn830Data[i][0].replace("@",""), hymn830Data[i][1]);
                	currentLine.add(newNote.getLabel());
                	if (hymn830Data[i][0].contains("@")) {
                		JLabel spaceLabel = new JLabel(" ");
                		spaceLabel.setFont(new Font("Arial", Font.BOLD, 60));
                		spaceLabel.setOpaque(true);
                		spaceLabel.setBackground(Color.GREEN);
                		spaceLabel.setPreferredSize(new Dimension(spaceLabel.getPreferredSize().width,newNote.getLabel().getPreferredSize().height));
                		currentLine.add(spaceLabel);
                	}            		
            	}
            	noteList.add(newNote);
            }
            for (int j = 0; j < hymnLines.size(); j++) {
            	mainContainer.add(hymnLines.get(j));
            }
            currentTimer = noteList.get(0).getTimer();
	        frame.add(scrollPane);
	        frame.setVisible(true);
	        
//	        currentTimer.start();

//            JPanel panel = new JPanel() {
//                @Override
//                public Dimension getMaximumSize() {
//                    Dimension preferred = getPreferredSize();
//                    // Allow full width, but limit height
//                    return new Dimension(Integer.MAX_VALUE, preferred.height);
//                }
//            };
////            panel.setBackground(Color.RED);
//            // Use a layout manager, e.g., FlowLayout is default for JPanel
//            panel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
//            
//            JPanel panel2 = new JPanel() {
//                @Override
//                public Dimension getMaximumSize() {
//                    Dimension preferred = getPreferredSize();
//                    // Allow full width, but limit height
//                    return new Dimension(Integer.MAX_VALUE, preferred.height);
//                }
//            };
////            panel2.setBackground(Color.BLUE);
//            // Use a layout manager, e.g., FlowLayout is default for JPanel
//            panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
//            
//            JPanel panel3 = new JPanel() {
//                @Override
//                public Dimension getMaximumSize() {
//                    Dimension preferred = getPreferredSize();
//                    // Allow full width, but limit height
//                    return new Dimension(Integer.MAX_VALUE, preferred.height);
//                }
//            };
//            panel3.setLayout(new FlowLayout(FlowLayout.CENTER));
//
//
//            OutlinedLabel label1 = new OutlinedLabel("LINE 1");
//            label1.setFont(new Font("Arial", Font.BOLD, 60));
//            OutlinedLabel label2 = new OutlinedLabel("LINE 11");
//            label2.setFont(new Font("Arial", Font.BOLD, 60));
//            OutlinedLabel label3 = new OutlinedLabel("LINE 111");
//            label3.setFont(new Font("Arial", Font.BOLD, 60));
//            OutlinedLabel label4 = new OutlinedLabel("LINE 1111");
//            label4.setFont(new Font("Arial", Font.BOLD, 60));
//            OutlinedLabel label5 = new OutlinedLabel("LINE 11111");
//            label5.setFont(new Font("Arial", Font.BOLD, 60));
//            OutlinedLabel label6 = new OutlinedLabel("LINE 111111");
//            label6.setFont(new Font("Arial", Font.BOLD, 60));
//            
//            FlowLayout layout = (FlowLayout) panel.getLayout();
//            layout.setHgap(0);
//            FlowLayout layout2 = (FlowLayout) panel2.getLayout();
//            layout2.setHgap(0);
//            FlowLayout layout3 = (FlowLayout) panel3.getLayout();
//            layout3.setHgap(0);
//
//            panel.add(label1);
//            panel.add(label2);
//            panel2.add(label3);
//            panel2.add(label4);
//            panel3.add(label5);
//            panel3.add(label6);
//            mainContainer.add(panel);
//            mainContainer.add(panel2);
//            mainContainer.add(panel3);
//            mainContainer.setBackground(Color.RED);
//            frame.add(scrollPane);
//            frame.setVisible(true);
//            
//            OutlinedLabel[] labelArray = {label1,label2,label3,label4,label5,label6};
//            timerArray = new Timer[6];
//            int[] widthArray = {
//            		label1.getComponentWidth(),
//            		label2.getComponentWidth(),
//            		label3.getComponentWidth(),
//            		label4.getComponentWidth(),
//            		label5.getComponentWidth(),
//            		label6.getComponentWidth(),
//            };
//            int[] speedArray = {5,2,10,5,2,10};
//            
//            for (int timerCount = 0; timerCount < timerArray.length; timerCount++) {
//            	int currentSpeed = speedArray[timerCount];
//            	OutlinedLabel currentLabel = labelArray[timerCount];
//            	int currentWidth = widthArray[timerCount];
//            	int x = timerCount;
//            	timerArray[timerCount] = new Timer(animationDelay, e -> {
//            		animationX += currentSpeed;
//            		currentLabel.setAnimX(animationX);
//            		currentLabel.repaint();
//            		if (animationX >= currentWidth) {
//            			((Timer) e.getSource()).stop();
//            			if (x != timerArray.length - 1) {
//            				animationX = 0;
//            				timerArray[x + 1].start();
//            			}
//            		}
//            	});
//            }
//            
//            timerArray[0].start();
            
//            int componentWidth2 = label2.getComponentWidth();
//            Timer timer2 = new Timer(animationDelay, e -> {
//                animationSpeed = 2;
//            	animationX += animationSpeed;
//            	label2.setAnimX(animationX);
//                label2.repaint();
//                if (animationX >= componentWidth2) {
//                	((Timer)e.getSource()).stop();
//                }
//            });
//
//            int componentWidth1 = label1.getComponentWidth();
//            Timer timer1 = new Timer(animationDelay, e -> {
//                animationSpeed = 10;
//            	animationX += animationSpeed;
//            	label1.setAnimX(animationX);
//                label1.repaint();
//                if (animationX >= componentWidth1) {
//                	((Timer)e.getSource()).stop();
//                	animationX = 0;
//                	timer2.start();
//                }
//            });
//
//            timer1.start();

//            label1.getTimer().start();
//            label2.getTimer().start();
        });
    }
    
    public static JPanel addNewLine(ArrayList hymnLinesList) {
        JPanel currentLine = new JPanel() {
			{ setLayout(new FlowLayout(FlowLayout.CENTER)); }
			{ FlowLayout layout = (FlowLayout) this.getLayout();
			layout.setHgap(0); }
            @Override
            public Dimension getMaximumSize() {
                Dimension preferred = getPreferredSize();
                // Allow full width, but limit height
                return new Dimension(Integer.MAX_VALUE, preferred.height);
            }  
		};
		hymnLinesList.add(currentLine);
        return currentLine;
    }

	public static void startNextAnim() {
		if (currentNoteNumber < noteList.size()) {
			currentTimer = noteList.get(currentNoteNumber).getTimer();
			currentTimer.start();
			currentNoteNumber++;
		}
	}
}