import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import static javax.swing.ScrollPaneConstants.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PanelWithOutlinedLabel {
//    static int animationSpeed = 0;
//    static int animationX = 0;
//    static int animationDelay = 50;
//    private static Timer[] timerArray;
	private static double allowedLength;
	private static int hymnTempo;
	private static String hymnName = "";
	private static int hymnNumber = 0;
	
	private static boolean isTest = false;
	private static int testHymnID = 2;	//	1:03:21,15:48,57:18
	// 619, 940
	
    private static ArrayList<Note> noteList = new ArrayList<Note>();
    private static int currentNoteNumber;
	private static boolean addedNewLine = false;
	private static int lineWordCount = 0;
    private static Timer currentTimer;
    private static boolean timerStatus = false;
    private static boolean isScrollLine = false;
    private static int newLineCount = 0;
	private static JScrollPane scrollPane;
	private static JPanel menuPane;
	private static JPanel largePanel;
	private static JPanel smallPanel;
	private static JPanel mainContainer;
	private static JFrame menuFrame;
	private static JFrame singFrame;
	private static JLabel titleLabel;
	private static JLabel numberLabel;
	private static JComboBox<Hymn> comboBox;
	private static JButton startButton;
	private static ArrayList<Image> icons;
	private static int scrollNumber = 0;
	private static float scrollAlpha = 0.0f;
	private static float infoAlpha = 0.0f;
	private static boolean lyricsUp = false;
	private static boolean infoUp = false;
	private static boolean songStarted = false;
	private static boolean songEnded = false;
	private static Timer infoFadeOutTimer;
	private static Timer infoFadeInTimer;
	private static int currentHymnID = 0;
	private static int musician = 0;
	private static boolean usePauses = true;
	private static Hymn selectedMenuItem = null;
	private static String[][] currentHymnData;
	
    private static final String DB_HOST = "co28d739i4m2sb7j.cbetxkdyhwsb.us-east-1.rds.amazonaws.com";
    private static final String DB_NAME = "raw5l6mnqtlyokfw";
    private static final String USER = "q6b7fx1pny2hkold";
    private static final String PASS = "i84r6te0qju8rb7w";
    private static final String PORT = "3306";
	
    public static void main(String[] args) {     
        UIManager.put("ComboBox.selectionBackground", new ColorUIResource(Color.decode("#008800")));
        UIManager.put("ComboBox.selectionForeground", new ColorUIResource(Color.decode("#004400")));
        UIManager.put("Button.select", Color.decode("#008800"));
        UIManager.put("Button.focus", Color.decode("#00DD00"));
        
        SwingUtilities.invokeLater(() -> {        	
        	menuPane = new JPanel();
        	Dimension menuPaneDim = new Dimension(700,225);
        	menuPane.setPreferredSize(menuPaneDim);
        	menuPane.setMinimumSize(menuPaneDim);
        	menuPane.setMaximumSize(menuPaneDim);
        	menuPane.setLayout(new BoxLayout(menuPane, BoxLayout.Y_AXIS));
//        	menuPane.setFocusable(true);
        	
        	JPanel appHeader = new JPanel(new GridBagLayout()) {
        	    @Override
        	    public Dimension getMaximumSize() {
        	        return new Dimension(700, getPreferredSize().width);
        	    }
        	};
            GridBagConstraints gbc = new GridBagConstraints();
            
        	Border line = BorderFactory.createLineBorder(Color.decode("#004400"), 2);
        	Border padding = BorderFactory.createEmptyBorder(3, 13, 3, 13);
        	Border compound = BorderFactory.createCompoundBorder(line, padding);

        	JButton quitButton = new JButton("Quit");
        	Font quitButtonFont = new Font("Times New Roman", Font.BOLD, 18);
        	quitButton.setFont(quitButtonFont);
        	quitButton.setBackground(Color.decode("#00DD00"));
        	quitButton.setForeground(Color.decode("#004400"));
//        	quitButton.setContentAreaFilled(false); 
        	quitButton.setOpaque(true);
        	
//        	quitButton.setFocusPainted(false);
        	quitButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Default padding

        	quitButton.addMouseListener(new java.awt.event.MouseAdapter() {
        	    @Override
        	    public void mouseEntered(java.awt.event.MouseEvent evt) {
        	        // Change to your desired hover line color
        	    	quitButton.setBorder(compound); 
        	    }

        	    @Override
        	    public void mouseExited(java.awt.event.MouseEvent evt) {
        	        // Revert to original border when not hovering
        	    	quitButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); 
        	    }
        	});

        	gbc.gridx = 1; // Second column
            gbc.gridy = 0; // First row
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.anchor = GridBagConstraints.FIRST_LINE_END; // Align to top-right
            gbc.insets = new Insets(10, 10, 10, 10); // Optional padding
            appHeader.add(quitButton, gbc);

            JLabel menuHeaderLabelChurch = new JLabel("St. John's Presbyterian Church");
            Font headerFont = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 24);
            menuHeaderLabelChurch.setFont(headerFont);
            menuHeaderLabelChurch.setForeground(Color.decode("#004400"));
            menuHeaderLabelChurch.setHorizontalAlignment(SwingConstants.CENTER);
            menuHeaderLabelChurch.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        	
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2; // Span both columns to ensure true center
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.CENTER;
            appHeader.add(menuHeaderLabelChurch, gbc);
            
        	appHeader.setBackground(Color.decode("#00EE00"));
            menuPane.add(appHeader);
        	
        	JLabel menuHeaderLabelApp = new JLabel("Hymn Singalong");
        	menuHeaderLabelApp.setAlignmentX(Component.CENTER_ALIGNMENT);
            Font menuFont = new Font("Times New Roman", Font.ITALIC, 18);
            menuHeaderLabelApp.setFont(menuFont);
            menuHeaderLabelApp.setForeground(Color.decode("#004400"));
            
            menuHeaderLabelApp.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        	menuPane.add(menuHeaderLabelApp);
        	
        	JLabel musicianLabel = new JLabel("Musician:");
        	JRadioButton musician0Option = new JRadioButton("Kenneth Tham");
            JRadioButton musician1Option = new JRadioButton("Silas and Peter Jo");
            JRadioButton musician2Option = new JRadioButton("Kenneth Tham (No Pauses)");
            
            musician0Option.setSelected(true);

            ButtonGroup musicianGroup = new ButtonGroup();
            
            musicianGroup.add(musician0Option);
            musicianGroup.add(musician1Option);
            musicianGroup.add(musician2Option);
            musician0Option.setActionCommand("TAG_01");
            musician1Option.setActionCommand("TAG_10");
            musician2Option.setActionCommand("TAG_00");
            
            ActionListener musicianListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String radioTag = e.getActionCommand().replace("TAG_","");
                    musician = Character.getNumericValue(radioTag.charAt(0));
                    usePauses = Character.getNumericValue(radioTag.charAt(1)) == 1 ? true : false;
                }
            };

            musician0Option.addActionListener(musicianListener);
            musician1Option.addActionListener(musicianListener);
            musician2Option.addActionListener(musicianListener);

            Color circleColor = Color.decode("#004400");
            Icon unselectedIcon = new CustomRadioIcon(circleColor, false);
            Icon selectedIcon = new CustomRadioIcon(circleColor, true);
            
            musician0Option.setFocusPainted(false);
            musician1Option.setFocusPainted(false);
            musician2Option.setFocusPainted(false);
            musician0Option.setIcon(unselectedIcon);
            musician0Option.setSelectedIcon(selectedIcon);
            musician1Option.setIcon(unselectedIcon);
            musician1Option.setSelectedIcon(selectedIcon);
            musician2Option.setIcon(unselectedIcon);
            musician2Option.setSelectedIcon(selectedIcon);
            
            JPanel musicianPanel = new JPanel();
            musicianPanel.setBackground(Color.decode("#00EE00"));
        	Dimension musicianPaneDim = new Dimension(650,40);
        	musicianPanel.setPreferredSize(musicianPaneDim);
        	musicianPanel.setMinimumSize(musicianPaneDim);
        	musicianPanel.setMaximumSize(musicianPaneDim);
            musicianPanel.setLayout(new FlowLayout());
            musicianPanel.add(musicianLabel);
            musicianPanel.add(musician0Option);
            musicianPanel.add(musician1Option);
            musicianPanel.add(musician2Option);
            Font musicianFont = new Font("Times New Roman", Font.BOLD, 16);
            Component[] musicianComponents = musicianPanel.getComponents();
            
            for (Component comp : musicianComponents) {
                comp.setFont(musicianFont);
                comp.setForeground(Color.decode("#004400"));
                comp.setBackground(Color.decode("#00EE00"));
            }
        	
            menuPane.add(musicianPanel);
            
            Connection conn = null;
            String connectionUrl = "jdbc:mysql://" + DB_HOST + ":" + PORT + "/" + DB_NAME + 
                                   "?verifyServerCertificate=false&useSSL=true";
                        
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                conn = DriverManager.getConnection(connectionUrl, USER, PASS);

                Statement stmtAllHymns = conn.createStatement();
                String queryAllHymns = "SELECT * FROM hymns ORDER BY hymnID";
                ResultSet rsAllHymns = stmtAllHymns.executeQuery(queryAllHymns);
                
                ArrayList<Hymn> hymnList = new ArrayList<Hymn>();

                while (rsAllHymns.next()) {
                	hymnList.add(new Hymn(rsAllHymns.getInt("hymnID"),rsAllHymns.getInt("hymnNumber"),rsAllHymns.getString("hymnTitle")));
                }
                
                hymnList.sort(Comparator.comparing(Hymn::getHymnNumber));

                Hymn[] items = hymnList.toArray(new Hymn[hymnList.size()]);
                
                comboBox = new JComboBox<Hymn>(items);  
                                
                String targetValue = "830 - Praise God From Whom All Blessings Flow";
                for (int i = 0; i < comboBox.getItemCount(); i++) {
                    // Get item and compare its specific property
                    if (comboBox.getItemAt(i).toString().equals(targetValue)) {
                        comboBox.setSelectedIndex(i);
                        selectedMenuItem = comboBox.getItemAt(i);
                        currentHymnID = selectedMenuItem.getHymnID();
                        break;
                    }
                }
                
                Dimension comboBoxSize = comboBox.getPreferredSize();

                comboBoxSize.height = 25; 
                comboBoxSize.width = 650;
                comboBox.setMinimumSize(comboBoxSize);                
                comboBox.setMaximumSize(comboBoxSize);                
                comboBox.setPreferredSize(comboBoxSize);   
                
                comboBox.addActionListener(e -> {
             		Object selected = comboBox.getSelectedItem();
             		if (selected instanceof Hymn) {
             			selectedMenuItem = (Hymn) selected;
                    	comboBox.setEditable(false);
             		    currentHymnID = selectedMenuItem.getHymnID();
             		}
                });
                   
                comboBox.setBackground(Color.decode("#00DD00"));
                comboBox.setForeground(Color.decode("#004400"));

                comboBox.setUI(new BasicComboBoxUI() {
                    @Override
                    protected JButton createArrowButton() {
                        // Parameters: direction, background, shadow, darkShadow, highlight
                        BasicArrowButton arrow = new BasicArrowButton(
                            BasicArrowButton.SOUTH,
                            Color.decode("#00DD00"),  // Box Background
                            Color.decode("#004400"),      // Shadow
                            Color.decode("#004400"),      // Dark Shadow (Arrow Triangle Color)
                            Color.decode("#004400")        // Highlight
                        );
                        return arrow;
                    }

                    @Override
                    protected void installDefaults() {
                        super.installDefaults();
                        // Uninstalls the Look and Feel default border completely
                        LookAndFeel.uninstallBorder(comboBox); 
                    }

//                    @Override
//                    protected ComboPopup createPopup() {
//                        // Instantiate the default popup component
//                        BasicComboPopup basicComboPopup = new BasicComboPopup(comboBox);
//                        
//                        // Apply your custom line border color and thickness
//                        basicComboPopup.setBorder(new LineBorder(Color.decode("#004400"), 1)); 
//                        
//                        return basicComboPopup;
//                    }
                    
                    @Override
                    protected ComboPopup createPopup() { 
                        BasicComboPopup popup =  new BasicComboPopup(comboBox) {
                            @Override
                            protected JScrollPane createScroller() {
                                JScrollPane scroller = super.createScroller();
                                
                                // Customize the vertical scrollbar's UI colors
                                scroller.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                                    @Override
                                    protected void configureScrollBarColors() {
                                        this.thumbColor = Color.decode("#004400");      // Color of the draggable thumb
                                        this.trackColor = Color.decode("#008800");     // Color of the background track
                                        this.thumbDarkShadowColor = Color.decode("#004400");
                                        this.thumbHighlightColor = Color.decode("#004400");
                                    }
                                    
                                    @Override
                                    protected JButton createDecreaseButton(int orientation) {
                                    	return new BasicArrowButton(orientation, 
                                                Color.decode("#00DD00"), Color.decode("#004400"), Color.decode("#004400"), Color.decode("#004400"));
                                    }
                                    @Override
                                    protected JButton createIncreaseButton(int orientation) {
                                    	return new BasicArrowButton(orientation, 
                                                Color.decode("#00DD00"), Color.decode("#004400"), Color.decode("#004400"), Color.decode("#004400"));                                    }
                                });
                                return scroller;
                            }
                        };
                        popup.setBorder(new LineBorder(Color.decode("#004400"), 1)); 
                        
                        return popup;
                    }
                });
                
                JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();
                editor.setCaretColor(Color.decode("#00DD00"));
                editor.setForeground(Color.decode("#004400"));
                editor.setBackground(Color.decode("#00DD00"));
                editor.setBorder(BorderFactory.createEmptyBorder());
                
//                comboBox.setBorder(new EmptyBorder(0, 0, 0, 0));
//                
//                for (int i = 0; i < comboBox.getComponentCount(); i++) {
//                    Component comp = comboBox.getComponent(i);
//                    if (comp instanceof JComponent) {
//                        ((JComponent) comp).setBorder(new EmptyBorder(0, 10, 0, 10));
//                    }
//                    if (comp instanceof AbstractButton) {
//                        ((AbstractButton) comp).setBorderPainted(false);
//                    }
//                }
//                
//                if (comboBox.getEditor() != null && comboBox.getEditor().getEditorComponent() instanceof JComponent) {
//                    JComponent editorBox = (JComponent) comboBox.getEditor().getEditorComponent();
//                    editorBox.setBorder(new EmptyBorder(0, 0, 0, 0));
//                }
                                                
                menuPane.add(comboBox);
                
        		comboBox.addPopupMenuListener(new PopupMenuListener() {
        			@Override
        			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            			// Popup is opening
        			}

        			@Override
        			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

        			}

        			@Override
        			public void popupMenuCanceled(PopupMenuEvent e) {
        				comboBox.setEditable(true);
//        				editor.setText(selectedMenuItem.toString());
        				comboBox.setSelectedItem(selectedMenuItem);
        				comboBox.setEditable(false);
        			}
        		});
               
                comboBox.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        comboBox.setEditable(true);
                    }
                });
                                
                editor.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        editor.setText("");
                        comboBox.showPopup();
                    }
                    
                    @Override
                    public void focusLost(FocusEvent e) {
        				comboBox.setEditable(true);
//        				editor.setText(selectedMenuItem.toString());
                        DefaultComboBoxModel<Hymn> refreshModel = new DefaultComboBoxModel<>(items);
                        comboBox.setModel(refreshModel);
        				comboBox.setSelectedItem(selectedMenuItem);
        				comboBox.setEditable(false);                    	
                    }
                });

                editor.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        String text = editor.getText();
                        String[] keywords = text.toLowerCase().split(" ");
                        
                        // Filter items that contain all keywords
                        Vector<Hymn> filteredItems = new Vector<>();
                        for (Hymn item : items) {
                            boolean match = true;
                            for (String keyword : keywords) {
                                if (!item.toString().toLowerCase().contains(keyword)) {
                                    match = false;
                                    break;
                                }
                            }
                            if (match) filteredItems.add(item);
                        }

                        // Update Model
                        DefaultComboBoxModel<Hymn> newModel = new DefaultComboBoxModel<>(filteredItems);
                        comboBox.setModel(newModel);
                        comboBox.setSelectedItem(text); // Keep text
                        comboBox.showPopup();
                    }
                });
                
            } catch (SQLException se) {
            	JLabel failureMessage = new JLabel("Error. Cannot connect to hymn database.");
            	failureMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
            	failureMessage.setForeground(Color.RED);
            	menuPane.add(failureMessage);
                se.printStackTrace();
            } catch (Exception sqlE) {
                // Handle errors for Class.forName
            	sqlE.printStackTrace();
            } finally {
                // Finally block used to close resources
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }            	

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        	startButton = new JButton("Start Hymn Text");
        	Font buttonFont = new Font("Times New Roman", Font.ITALIC | Font.BOLD, 18);
        	startButton.setFont(buttonFont);
        	startButton.setBackground(Color.decode("#00DD00"));
        	startButton.setForeground(Color.decode("#004400"));
//        	startButton.setContentAreaFilled(false); 
        	startButton.setOpaque(true);
        	startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        	
//        	startButton.setFocusPainted(false);
        	startButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Default padding
        	
        	startButton.addMouseListener(new java.awt.event.MouseAdapter() {
        	    @Override
        	    public void mouseEntered(java.awt.event.MouseEvent evt) {
        	        // Change to your desired hover line color
        	        startButton.setBorder(compound); 
        	    }

        	    @Override
        	    public void mouseExited(java.awt.event.MouseEvent evt) {
        	        // Revert to original border when not hovering
        	        startButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); 
        	    }
        	});

        	buttonPanel.add(startButton);
        	buttonPanel.setBackground(Color.decode("#00EE00"));
        	menuPane.add(buttonPanel);
        	
        	menuPane.setBackground(Color.decode("#00EE00"));
        	menuPane.setBorder(BorderFactory.createLineBorder(Color.decode("#004400")));
        	        	
            singFrame = new JFrame();
            singFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            singFrame.setLocationRelativeTo(null); 
            singFrame.setUndecorated(true);
            singFrame.setBackground(Color.GREEN);
            
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                .getDefaultScreenDevice();

            if (gd.isFullScreenSupported()) {
                gd.setFullScreenWindow(singFrame); // This covers the taskbar
            } else {
                // Fallback if full-screen mode isn't supported
            	singFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            	singFrame.setVisible(true);
            }
                                    
            mainContainer = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    
                    Graphics2D g2d = (Graphics2D) g.create();

                    g2d.setComposite(AlphaComposite.Src); // Use Src to overwrite
//                    int backgroundAlpha = (int) Math.ceil(255 * scrollAlpha);
                    g2d.setColor(Color.GREEN);
//                    g2d.setColor(new Color(0,0,0,0)); // or new Color(0,0,0,0) for transparent
                    g2d.fillRect(0, 0, getWidth(), getHeight());

//                    g2d.setComposite(AlphaComposite.SrcOver.derive(scrollAlpha));

                    g2d.dispose();
                }
                
                @Override
                protected void paintChildren(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, scrollAlpha));
                    super.paintChildren(g2d);
                    g2d.dispose();
                }
            };
            
            mainContainer.setBorder(BorderFactory.createEmptyBorder());
//            mainContainer.setFocusable(true);
            mainContainer.requestFocusInWindow();
            
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent e) {
                    // Check if the event is a key press (KEY_PRESSED) and the key is the spacebar (VK_SPACE)
                    if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    	if (currentTimer != null) {
    	                    if (timerStatus == false && lyricsUp == true) {
    	                    	currentTimer.start();
    	                    	timerStatus = true;
    	                    } else {
    	                    	currentTimer.stop();
    	                    	timerStatus = false;
    	                    }                    		
                    	}
                    } else if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    	if (songStarted == true) {
                        	if (infoUp == true) {
                            	infoFadeOutTimer.start();
                            } else {
        	                    smallPanel.setVisible(true);
                            	infoFadeInTimer.start();
                            }                    		
//                    	} else if (menuPane.isVisible() == true) {
//                    		if (comboBox.isPopupVisible() == true) {
//                    			comboBox.setSelectedIndex(0);
//                    		} else {
//                    			startButton.doClick();
//                    		}
                    	}	// VERSION-A
                    	if (infoUp == true) {
                        	infoFadeOutTimer.start();
                        } // VERSION-B             		
                    } else if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    	if (isTest == true) {
                    		System.exit(0);
//                    	} else if (menuPane.isVisible() == true) {	// VERSION-A
                    		
                    	} else if (infoAlpha > 0.0f && infoAlpha < 1.0f) {
                    		infoFadeInTimer.stop();
                    		infoFadeOutTimer.stop();
                        	songStarted = false;
                        	infoAlpha = 0.0f;
                        	scrollAlpha = 0.0f;
                        	scrollPane.repaint();                   	
                        	smallPanel.repaint(); 
                        	scrollNumber = 0;
                			scrollPane.getVerticalScrollBar().setValue(scrollNumber);
                			newLineCount = 0;
                			lineWordCount = 0;
//                        	menuFrame.toFront();
                        	infoUp = false;
                        	lyricsUp = false;
                        	timerStatus = false;
                        	if (currentTimer != null) {
                        		currentTimer.stop();
                        	}
                        	currentTimer = null;
                        	currentNoteNumber = 0;
                        	if (isTest == true) {
                        		System.exit(0);
                        	}   
                        	songEnded = true;
                	    	startButton.setEnabled(true);
//                	    	startButton.setText("Start Hymn Text");	// VERSION-B
                    	} else if (songEnded == false) {
                        	songStarted = false;
                        	infoAlpha = 0.0f;
                        	scrollAlpha = 0.0f;
                        	scrollPane.repaint();                   	
                        	smallPanel.repaint(); 
                        	scrollNumber = 0;
                			scrollPane.getVerticalScrollBar().setValue(scrollNumber);
                			newLineCount = 0;
                			lineWordCount = 0;
//                        	menuFrame.toFront();
                        	infoUp = false;
                        	lyricsUp = false;
                        	timerStatus = false;
                        	if (currentTimer != null) {
                        		currentTimer.stop();
                        	}
                        	currentTimer = null;
                        	currentNoteNumber = 0;
                        	if (isTest == true) {
                        		System.exit(0);
                        	}   
                        	songEnded = true;
                	    	startButton.setEnabled(true);
//                	    	startButton.setText("Start Hymn Text");	// VERSION-B
                    	} 
                    	else {
                        	songStarted = false;
                        	scrollAlpha = 0.0f;
                        	scrollPane.repaint();                   	
                        	smallPanel.repaint(); 
                        	scrollNumber = 0;
                			scrollPane.getVerticalScrollBar().setValue(scrollNumber);
                			newLineCount = 0;
                			lineWordCount = 0;
//                        	menuFrame.toFront();
                        	infoUp = false;
                        	lyricsUp = false;
                        	timerStatus = false;
                        	if (currentTimer != null) {
                        		currentTimer.stop();
                        	}
                        	currentTimer = null;
                        	currentNoteNumber = 0;
                        	if (isTest == true) {
                        		System.exit(0);
                        	}   
                        	songEnded = true;
                	    	startButton.setEnabled(true);
                	    	startButton.setText("Start Hymn Text");
                    	}	// VERSION-B
//                    	else {
//                    		songEnded = false;
//                    		menuPane.setVisible(true);
//                    	}	// VERSION-A
                    } else if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_OPEN_BRACKET) {
                    	changeTrackSpeed(1);
                    } else if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_CLOSE_BRACKET) {
                    	changeTrackSpeed(-1);
                    }
                    return false;
                }
            });
            
            mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
            
            scrollPane = new JScrollPane(mainContainer,
                    VERTICAL_SCROLLBAR_NEVER,
                    HORIZONTAL_SCROLLBAR_NEVER);
            
            scrollPane.getViewport().setBackground(new Color(0,0,0,0));
            scrollPane.setBorder(null);
            
            InputMap im = mainContainer.getInputMap(JComponent.WHEN_FOCUSED);

            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "none");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "none");

            mainContainer.getActionMap().put("none", new AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {}
            });
            
            currentHymnData = null;

            if (isTest == true) {
                String[][] testHymnNames = 
                	{
                			{"Praise God From Whom All Blessings Flow","830","34","1.5"},	// 1:03:21
                			{"Blest Are They","624","18","3"},	// 15:48
                			{"Take Time To Be Holy","638","16","1.5"},	// 35:11
                	};
                String[][][] testHymnData = 
                	{
                			{
    	            			{"PRAISE@","h"},
    	            			{"GOD@","q"},
    	            			{"FROM@","q"},
    	            			{"WHOM@","q"},
    	            			{"ALL@","q"},
    	            			{"BLESS","h"},
    	            			{"INGS@","h"},
    	            			{"FLOW@","h"},
//    	            			{"","w"},
    	            			{"PRAISE@","h"},
    	            			{"HIM@","q"},
    	            			{"ALL@","q"},
    	            			{"CREA","q"},
    	            			{"TURES@","q"},
    	            			{"HERE@","h"},
    	            			{"BE","h"},
    	            			{"LOW@","h"},
    	            			{"","w"},
    	            			{"PRAISE@","h"},
    	            			{"HIM@","q"},
    	            			{"A","q"},
    	            			{"BOVE@","q"},
    	            			{"YE@","q"},
    	            			{"HEAVEN","h"},
    	            			{"LY@","h"},
    	            			{"HOST@","h"},
//    	            			{"","w"},
    	            			{"PRAISE@","h"},
    	            			{"FA","q"},
    	            			{"THER@","q"},
    	            			{"SON@","q"},
    	            			{"AND@","q"},
    	            			{"HO","h."},	
    	            			{"LY@","h"},
    	            			{"GHOST@","h."},
//    	            			{"","w"},
    	            			{"A","ww"},
    	            			{"MEN@","ww"},
    	            		},
                			{
    	            			{"BLEST@","h"},
    	            			{"ARE@","q"},
    	            			{"THEY@","h"},
    	            			{"THE@","q"},
    	            			{"POOR@","h"},
    	            			{"IN@","q"},
    	            			{"SPIR","q"},
    	            			{"IT@","h"},
    	            			{"THEIRS@","q"},
    	            			{"IS@","q"},
    	            			{"THE@","q"},
    	            			{"KING","q"},
    	            			{"DOM@","q"},
    	            			{"OF@","q"},
    	            			{"GOD@","w."},
    	            			{"","w"},
    	            			{"BLEST@","h"},
    	            			{"ARE@","q"},
    	            			{"THEY@","h."},
    	            			{"FULL@","h"},
    	            			{"OF@","q"},
    	            			{"SOR","q"},
    	            			{"ROW@","h"},
    	            			{"THEY@","h"},
    	            			{"SHALL@","q"},
    	            			{"BE@","h"},
    	            			{"CON","q"},
    	            			{"SOLED@","w."},
    	            			{"","w"},
    	            			{"RE","q"},
    	            			{"JOICE@","w"},
    	            			{"AND@","q"},
    	            			{"BE@","q"},
    	            			{"GLAD@","w."},
    	            			{"BLESS","q"},
    	            			{"ED@","q"},
    	            			{"ARE@","q"},
    	            			{"YOU@","h."},
    	            			{"HO","q"},
    	            			{"LY@","q"},
    	            			{"ARE@","q"},
    	            			{"YOU@","h"},
    	            			{"RE","q"},
    	            			{"JOICE@","w"},
    	            			{"AND@","q"},
    	            			{"BE@","q"},
    	            			{"GLAD@","w."},
    	            			{"","w"},
    	            			{"YOURS@","q"},
    	            			{"IS@","q"},
    	            			{"THE@","q"},
    	            			{"KING","q"},
    	            			{"DOM@","q"},
    	            			{"OF@","q"},
    	            			{"GOD@","w."},
    	            			{"","w"},
    	            			{"BLEST@","h"},
    	            			{"ARE@","q"},
    	            			{"THEY@","h"},
    	            			{"THE@","q"},
    	            			{"LOW","h"},
    	            			{"LY@","q"},
    	            			{"ONES@","h."},
    	            			{"THEY@","q"},
    	            			{"SHALL@","q"},
    	            			{"IN","q"},
    	            			{"HER","q"},
    	            			{"IT@","q"},
    	            			{"THE@","q"},
    	            			{"EARTH@","w."},
    	            			{"","w"},
    	            			{"BLEST@","h"},
    	            			{"ARE@","q"},
    	            			{"THEY@","h"},
    	            			{"WHO@","q"},
    	            			{"HUN","q"},
    	            			{"GER@","q"},
    	            			{"AND@","q"},
    	            			{"THIRST@","h."},
    	            			{"THEY@","h"},
    	            			{"SHALL@","q"},
    	            			{"HAVE@","h"},
    	            			{"THEIR@","q"},
    	            			{"FILL@","w."},
    	            			{"","w"},
    	            			{"RE","q"},
    	            			{"JOICE@","w"},
    	            			{"AND@","q"},
    	            			{"BE@","q"},
    	            			{"GLAD@","w."},
    	            			{"BLESS","q"},
    	            			{"ED@","q"},
    	            			{"ARE@","q"},
    	            			{"YOU@","h."},
    	            			{"HO","q"},
    	            			{"LY@","q"},
    	            			{"ARE@","q"},
    	            			{"YOU@","h"},
    	            			{"RE","q"},
    	            			{"JOICE@","w"},
    	            			{"AND@","q"},
    	            			{"BE@","q"},
    	            			{"GLAD@","w."},
    	            			{"","w"},
    	            			{"YOURS@","q"},
    	            			{"IS@","q"},
    	            			{"THE@","q"},
    	            			{"KING","q"},
    	            			{"DOM@","q"},
    	            			{"OF@","q"},
    	            			{"GOD@","w."},
    	            			{"","w"},
    	            			{"BLEST@","h"},
    	            			{"ARE@","q"},
    	            			{"THEY@","h."},
    	            			{"WHO@","h"},
    	            			{"SHOW@","q"},
    	            			{"MER","q"},
    	            			{"CY@","h"},
    	            			{"MER","h"},
    	            			{"CY@","q"},
    	            			{"SHALL@","h"},
    	            			{"BE@","q"},
    	            			{"THEIRS@","w."},
//    	            			{"","w"},
    	            			{"BLEST@","h"},
    	            			{"ARE@","q"},
    	            			{"THEY@","h"},
    	            			{"THE@","q"},
    	            			{"PURE@","h"},
    	            			{"OF@","q"},
    	            			{"HEART@","h."},
    	            			{"THEY@","h."},
    	            			{"SHALL@","h"},
    	            			{"SEE@","q"},
    	            			{"GOD@","w."},
    	            			{"","w"},
    	            			{"RE","q"},
    	            			{"JOICE@","w"},
    	            			{"AND@","q"},
    	            			{"BE@","q"},
    	            			{"GLAD@","w."},
    	            			{"BLESS","q"},
    	            			{"ED@","q"},
    	            			{"ARE@","q"},
    	            			{"YOU@","h."},
    	            			{"HO","q"},
    	            			{"LY@","q"},
    	            			{"ARE@","q"},
    	            			{"YOU@","h"},
    	            			{"RE","q"},
    	            			{"JOICE@","w"},
    	            			{"AND@","q"},
    	            			{"BE@","q"},
    	            			{"GLAD@","w."},
    	            			{"","w"},
    	            			{"YOURS@","q"},
    	            			{"IS@","q"},
    	            			{"THE@","q"},
    	            			{"KING","q"},
    	            			{"DOM@","q"},
    	            			{"OF@","q"},
    	            			{"GOD@","w."},
    	            			{"","w"},
    	            			{"BLEST@","h"},
    	            			{"ARE@","q"},
    	            			{"THEY@","h."},
    	            			{"WHO@","h"},
    	            			{"SEEK@","q"},
    	            			{"PEACE@","h."},
    	            			{"THEY@","q"},
    	            			{"ARE@","q"},
    	            			{"THE@","q"},
    	            			{"CHIL","q"},
    	            			{"DREN@","q"},
    	            			{"OF@","q"},
    	            			{"GOD@","w."},
//    	            			{"","w"},
    	            			{"BLEST@","h"},
    	            			{"ARE@","q"},
    	            			{"THEY@","h"},
    	            			{"WHO@","q"},
    	            			{"SUF","q"},
    	            			{"FER@","q"},
    	            			{"IN@","q"},
    	            			{"FAITH@","h"},
    	            			{"THE@","q"},
    	            			{"GLO","q"},
    	            			{"RY@","q"},
    	            			{"OF@","q"},
    	            			{"GOD@","h"},
    	            			{"IS@","q"},
    	            			{"THEIRS@","w."},
    	            			{"","w"},
    	            			{"RE","q"},
    	            			{"JOICE@","w"},
    	            			{"AND@","q"},
    	            			{"BE@","q"},
    	            			{"GLAD@","w."},
    	            			{"BLESS","q"},
    	            			{"ED@","q"},
    	            			{"ARE@","q"},
    	            			{"YOU@","h."},
    	            			{"HO","q"},
    	            			{"LY@","q"},
    	            			{"ARE@","q"},
    	            			{"YOU@","h"},
    	            			{"RE","q"},
    	            			{"JOICE@","w"},
    	            			{"AND@","q"},
    	            			{"BE@","q"},
    	            			{"GLAD@","w."},
    	            			{"","w"},
    	            			{"YOURS@","q"},
    	            			{"IS@","q"},
    	            			{"THE@","q"},
    	            			{"KING","q"},
    	            			{"DOM@","q"},
    	            			{"OF@","q"},
    	            			{"GOD@","w."},
    	            			{"","w"},
    	            			{"BLEST@","h"},
    	            			{"ARE@","q"},
    	            			{"YOU@","h"},
    	            			{"WHO@","q"},
    	            			{"SUF","h"},
    	            			{"FER@","q"},
    	            			{"HATE@","h."},
    	            			{"ALL@","h"},
    	            			{"BE","q"},
    	            			{"CAUSE@","h"},
    	            			{"OF@","q"},
    	            			{"ME@","wq"},
//    	            			{"","w"},
    	            			{"RE","q"},
    	            			{"JOICE@","q"},
    	            			{"AND@","q"},
    	            			{"BE@","q"},
    	            			{"GLAD@","h."},
    	            			{"YOURS@","q"},
    	            			{"IS@","q"},
    	            			{"THE@","q"},
    	            			{"KING","h"},
    	            			{"DOM@","q"},
    	            			{"SHINE@","h"},
    	            			{"FOR@","q"},
    	            			{"ALL@","h"},
    	            			{"TO@","q"},
    	            			{"SEE@","w."},
    	            			{"","w"},
    	            			{"RE","q"},
    	            			{"JOICE@","w"},
    	            			{"AND@","q"},
    	            			{"BE@","q"},
    	            			{"GLAD@","w."},
    	            			{"BLESS","q"},
    	            			{"ED@","q"},
    	            			{"ARE@","q"},
    	            			{"YOU@","h."},
    	            			{"HO","q"},
    	            			{"LY@","q"},
    	            			{"ARE@","q"},
    	            			{"YOU@","h"},
    	            			{"RE","q"},
    	            			{"JOICE@","w"},
    	            			{"AND@","q"},
    	            			{"BE@","q"},
    	            			{"GLAD@","w."},
//    	            			{"","w"},
    	            			{"YOURS@","q"},
    	            			{"IS@","q"},
    	            			{"THE@","q"},
    	            			{"KING","q"},
    	            			{"DOM@","q"},
    	            			{"OF@","q"},
    	            			{"GOD@","w."},
                			},
                			{
                				{"TAKE@","q."},
                				{"TIME@","e"},
                				{"TO@","e"},
                				{"BE@","e"},
                				{"HO","q."},
                				{"LY@","q."},
                				{"SPEAK@","q."},
                				{"OFT@","e"},
                				{"WITH@","e"},
                				{"THY@","e"},
                				{"LORD@","h."},
                				{"A","q."},
                				{"BIDE@","e"},
                				{"IN@","e"},
                				{"HIM@","e"},
                				{"AL","q."},
                				{"WAYS@","q."},
                				{"AND@","q."},
                				{"FEED@","e"},
                				{"ON@","e"},
                				{"HIS@","e"},
                				{"WORD@","h."},
                				{"MAKE@","q."},
                				{"FRIENDS@","e"},
                				{"OF@","e"},
                				{"GOD'S@","e"},
                				{"CHIL","q."},
                				{"DREN@","q."},
                				{"HELP@","q."},
                				{"THOSE@","e"},
                				{"WHO@","e"},
                				{"ARE@","e"},
                				{"WEAK@","h."},
                				{"FOR","q."},
                				{"GET","e"},
                				{"TING@","e"},
                				{"IN@","e"},
                				{"NOTH","q."},
                				{"ING@","q."},
                				{"HIS@","q."},
                				{"BLESS","e"},
                				{"ING@","e"},
                				{"TO@","e"},
                				{"SEEK@","h."},
                				{"","w"},
                				{"TAKE@","q."},
                				{"TIME@","e"},
                				{"TO@","e"},
                				{"BE@","e"},
                				{"HO","q."},
                				{"LY@","q."},
                				{"THE@","q."},
                				{"WORLD@","e"},
                				{"RUSH","e"},
                				{"ES@","e"},
                				{"ON@","h."},
                				{"SPEND@","q."},
                				{"MUCH@","e"},
                				{"TIME@","e"},
                				{"IN@","e"},
                				{"SE","q."},
                				{"CRET@","q."},
                				{"WITH@","q."},
                				{"JE","e"},
                				{"SUS@","e"},
                				{"A","e"},
                				{"LONE@","h."},
                				{"BY@","q."},
                				{"LOOK","e"},
                				{"ING@","e"},
                				{"TO@","e"},
                				{"JE","q."},
                				{"SUS@","q."},
                				{"LIKE@","q."},
                				{"HIM@","e"},
                				{"THOU@","e"},
                				{"SHALT@","e"},
                				{"BE@","h."},
                				{"THY@","q."},
                				{"FRIENDS@","e"},
                				{"IN@","e"},
                				{"THY@","e"},
                				{"CON","q."},
                				{"DUCT@","q."},
                				{"HIS@","q."},
                				{"LIKE","e"},
                				{"NESS@","e"},
                				{"SHALL@","e"},
                				{"SEE@","h."},
                				{"","w"},
                				{"TAKE@","q."},
                				{"TIME@","e"},
                				{"TO@","e"},
                				{"BE@","e"},
                				{"HO","q."},
                				{"LY@","q."},
                				{"LET@","q."},
                				{"HIM@","e"},
                				{"BE@","e"},
                				{"THY@","e"},
                				{"GUIDE@","h."},
                				{"AND@","q."},
                				{"RUN@","e"},
                				{"NOT@","e"},
                				{"BE","e"},
                				{"FORE@","q."},
                				{"HIM@","q."},
                				{"WHAT","q."},
                				{"EV","e"},
                				{"ER@","e"},
                				{"BE","e"},
                				{"TIDE@","h."},
                				{"IN@","q."},
                				{"JOY@","e"},
                				{"OR@","e"},
                				{"IN@","e"},
                				{"SOR","q."},
                				{"ROW@","q."},
                				{"STILL@","q."},
                				{"FOL","e"},
                				{"LOW@","e"},
                				{"THY@","e"},
                				{"LORD@","h."},
                				{"AND@","q."},
                				{"LOOK","e"},
                				{"ING@","e"},
                				{"TO@","e"},
                				{"JE","q."},
                				{"SUS@","q."},
                				{"STILL@","q."},
                				{"TRUST@","e"},
                				{"IN@","e"},
                				{"HIS@","e"},
                				{"WORD@","h."},
                				{"","w"},
                				{"TAKE@","q."},
                				{"TIME@","e"},
                				{"TO@","e"},
                				{"BE@","e"},
                				{"HO","q."},
                				{"LY@","q."},
                				{"BE@","q."},
                				{"CALM@","e"},
                				{"IN@","e"},
                				{"THY@","e"},
                				{"SOUL@","h."},
                				{"EACH@","q."},
                				{"THOUGHT@","e"},
                				{"AND@","e"},
                				{"EACH@","e"},
                				{"MO","q."},
                				{"TIVE@","q."},
                				{"BE","q."},
                				{"NEATH@","e"},
                				{"HIS@","e"},
                				{"CON","e"},
                				{"TROL@","h."},
                				{"THUS@","q."},
                				{"LED@","e"},
                				{"BY@","e"},
                				{"HIS@","e"},
                				{"SPIR","q."},
                				{"T@","q."},
                				{"TO@","q."},
                				{"FOUN","e"},
                				{"TAINS@","e"},
                				{"OF@","e"},
                				{"LOVE@","h."},
                				{"THOU@","q."},
                				{"SOON@","e"},
                				{"SHALT@","e"},
                				{"BE@","e"},
                				{"FIT","q."},
                				{"TED@","q."},
                				{"FOR@","q."},
                				{"SERV","e"},
                				{"ICE@","e"},
                				{"A","q"},
                				{"BOVE@","w."},
                			},
                	};

                currentHymnData = testHymnData[testHymnID];
                String [] hymnInfo = testHymnNames[testHymnID];
                hymnTempo = Integer.parseInt(hymnInfo[2]);
                allowedLength = Double.parseDouble(hymnInfo[3]);
                
            	hymnName = hymnInfo[0];
            	hymnNumber = Integer.parseInt(hymnInfo[1]);
            	            	    
            } else {
            }   
            
            JPanel fullPanel = new JPanel(new GridBagLayout());
//            fullPanel.setLayout(new OverlayLayout(fullPanel));
            fullPanel.setBorder(null);
            fullPanel.setBackground(Color.GREEN);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new OverlayLayout(mainPanel));
            mainPanel.setBorder(null);
            Dimension mainPanelSize = new Dimension(1450,280);
            mainPanel.setPreferredSize(mainPanelSize);
            mainPanel.setMinimumSize(mainPanelSize);
            mainPanel.setMaximumSize(mainPanelSize);
            
            GridBagConstraints gbcText = new GridBagConstraints();
            gbcText.gridx = 0;
            gbcText.gridy = 0;
            gbcText.weightx = 1.0; // Horizontal centering
            gbcText.weighty = 1.0; // Pushes component down
            gbcText.anchor = GridBagConstraints.SOUTH; // Pins to bottom
            gbcText.insets = new Insets(0, 0, 100, 0); 
                        
            largePanel = new JPanel(new GridBagLayout()) {};
            largePanel.setBackground(new Color(0, 0, 0, 0));
//            largePanel.setBackground(Color.GREEN);
            largePanel.setBorder(null);
//            largePanel.setOpaque(false);
            
            smallPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    
                    Graphics2D g2d = (Graphics2D) g.create();

                    g2d.setComposite(AlphaComposite.Src); // Use Src to overwrite
                    int backgroundAlpha = (int) Math.ceil(255 * infoAlpha);
                    g2d.setColor(new Color(backgroundAlpha,255,backgroundAlpha,backgroundAlpha)); // or new Color(0,0,0,0) for transparent
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    this.setBorder(BorderFactory.createLineBorder(new Color(0,0,0,backgroundAlpha)));

                    g2d.setComposite(AlphaComposite.SrcOver.derive(infoAlpha));

                    g2d.dispose();
                }
                
                @Override
                protected void paintChildren(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, infoAlpha));
                    super.paintChildren(g2d);
                    g2d.dispose();
                }
                
                
            };
            smallPanel.setOpaque(false);
            smallPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            smallPanel.setPreferredSize(new Dimension(1000,200));
            smallPanel.setLayout(new BoxLayout(smallPanel, BoxLayout.Y_AXIS));
            
//            JPanel titlePanel = new JPanel();
            JPanel titlePanel = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    
                    Graphics2D g2d = (Graphics2D) g.create();

                    g2d.setComposite(AlphaComposite.Src); // Use Src to overwrite
                    int backgroundAlpha = (int) Math.ceil(255 * infoAlpha);
                    g2d.setColor(new Color(backgroundAlpha,255,backgroundAlpha,backgroundAlpha)); // or new Color(0,0,0,0) for transparent
                    g2d.fillRect(0, 0, getWidth(), getHeight());

                    g2d.setComposite(AlphaComposite.SrcOver.derive(infoAlpha));

                    g2d.dispose();
                }
                
                @Override
                protected void paintChildren(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, infoAlpha));
                    super.paintChildren(g2d);
                    g2d.dispose();
                }
                
                
            };
            titlePanel.setPreferredSize(new Dimension(950,150));
            titlePanel.setMinimumSize(new Dimension(950,150));
            titlePanel.setMaximumSize(new Dimension(950,150));

//            titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
            titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        
            titleLabel = new JLabel();
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER); 
            titleLabel.setFont(new Font("Lucida Calligraphy", Font.PLAIN, 48));
//            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 100, 0, 100));
            
            titleLabel.setHorizontalAlignment(JLabel.CENTER); 

            titlePanel.add(titleLabel, BorderLayout.CENTER);
            
            numberLabel = new JLabel();
            numberLabel.setHorizontalAlignment(SwingConstants.CENTER); 
            numberLabel.setFont(new Font("Serif", Font.BOLD, 24));
            numberLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            numberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            if (isTest == true) {
                titleLabel.setText("<html><center>\"" + hymnName + "\"</center></html>");
                numberLabel.setText("<html><center>Hymn #" + hymnNumber + "</center></html>");
            }
//            
//            smallPanel.setBackground(Color.WHITE);
            smallPanel.add(titlePanel);
//            titlePanel.setBackground(Color.WHITE);
            titlePanel.setOpaque(true);
            titlePanel.add(titleLabel);
//            smallPanel.add(titleLabel);
            smallPanel.add(numberLabel);

            largePanel.add(smallPanel);
            
            mainPanel.add(largePanel);
            mainPanel.add(scrollPane);
            
            scrollPane.setOpaque(false);
            
            GridBagConstraints gbcMenu = new GridBagConstraints();
            gbcMenu.gridx = 0;
            gbcMenu.gridy = 0;
            gbcMenu.insets = new Insets(100, 0, 0, 0); 
            gbcMenu.anchor = GridBagConstraints.NORTH;
            gbcMenu.weighty = 1.0;
            
            fullPanel.add(mainPanel, gbcText);
            fullPanel.add(menuPane, gbcMenu);
            singFrame.setVisible(true);
            singFrame.add(fullPanel);
            
//            if (isTest == false) {
//            	menuFrame.setVisible(true);
//            }
            
	        Timer scrollFirstFade = new Timer(15, new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	scrollAlpha += 0.05f;
	                if (scrollAlpha >= 1.0f) {
	                	scrollAlpha = 1.0f;
	                    ((Timer) e.getSource()).stop();
	                    lyricsUp = true;
	                }
	                scrollPane.repaint();
	            }
	        });
	        infoFadeOutTimer = new Timer(50, new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	infoAlpha -= hymnNumber == 830 ? 0.10f : 0.05f;
	                if (infoAlpha <= 0.0f) {
	                	infoAlpha = 0.0f;
		                smallPanel.repaint();
	                    ((Timer) e.getSource()).stop();
	                    smallPanel.setVisible(false);
	                    scrollFirstFade.start();
	                }
//	                largePanel.repaint();
	                smallPanel.repaint();
	            }
	        });
	        infoFadeInTimer = new Timer(50, new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	infoAlpha += hymnNumber == 830 ? 0.10f : 0.05f;
	                if (infoAlpha >= 1.0f) {
	                	infoAlpha = 1.0f;
	                    ((Timer) e.getSource()).stop();
	                    infoUp = true;
	                }
//	                largePanel.repaint();
	                smallPanel.repaint();
	            }
	        });
	        
	        Action blankAction = new AbstractAction() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // Do nothing
	            }
	        };

	        if (isTest == true) {
	        	if (songStarted == false) {
	                titleLabel.setText("<html><center>\"" + hymnName + "\"</center></html>");
	                numberLabel.setText("<html><center>" + (hymnNumber == 0 ? "Found In Insert" : "Hymn #" + hymnNumber) + "</center></html>");
	                                                                                                                                                            
	                setupHymnLyrics(currentHymnData);
	    	    	infoAlpha = 0.0f;
	    	    	scrollAlpha = 0.0f;
	    	    	infoFadeInTimer.start();      
	    	    	songStarted = true;	
	        	}	        	
	        } else {
		        if (startButton != null && quitButton != null) {
			        ActionMap amQuitButton = quitButton.getActionMap();
			        InputMap imQuitButton = quitButton.getInputMap(JComponent.WHEN_FOCUSED);

			        imQuitButton.put(KeyStroke.getKeyStroke("SPACE"), "none"); // Mapping to the special name "none" works as well
			        amQuitButton.put("none", blankAction);

			        ActionMap amButton = startButton.getActionMap();
			        InputMap imButton = startButton.getInputMap(JComponent.WHEN_FOCUSED);

			        imButton.put(KeyStroke.getKeyStroke("SPACE"), "none"); // Mapping to the special name "none" works as well
			        amButton.put("none", blankAction);
			        
			        ActionMap amCombo = comboBox.getActionMap();
			        InputMap imCombo = comboBox.getInputMap(JComponent.WHEN_FOCUSED);

			        imCombo.put(KeyStroke.getKeyStroke("SPACE"), "none"); // Mapping to the special name "none" works as well
			        amCombo.put("none", blankAction);
			        
		        	quitButton.addActionListener(new ActionListener() {
		        	    @Override
		        	    public void actionPerformed(ActionEvent e) {
		        	    	System.exit(0);
		        	    }
		        	});
		        	
		        	startButton.addActionListener(new ActionListener() {
		        	    @Override
		        	    public void actionPerformed(ActionEvent e) {
		        	    	if (songStarted == false) {
		        	    		if (isTest == true) {                                
		                            titleLabel.setText("<html><center>\"" + hymnName + "\"</center></html>");
		                            numberLabel.setText("<html><center>Hymn #" + (hymnNumber == 0 ? "Found In Insert" : hymnNumber) + "</center></html>");
		                                                     
		                            setupHymnLyrics(currentHymnData);
		                	    	infoAlpha = 0.0f;
		                	    	scrollAlpha = 0.0f;
		                	    	infoFadeInTimer.start();      
		                	    	songStarted = true;

		        	    		} else {
		        	    			startButton.setEnabled(false);
	        	                    smallPanel.setVisible(true);	// VERSION-B
	                            	infoFadeInTimer.start();	// VERSION-B

		            	            Connection conn = null;
		            	            String connectionUrl = "jdbc:mysql://" + DB_HOST + ":" + PORT + "/" + DB_NAME + 
		            	                                   "?verifyServerCertificate=false&useSSL=true";
		            	                                    
		                            try {
		                                Class.forName("com.mysql.cj.jdbc.Driver");

		                                conn = DriverManager.getConnection(connectionUrl, USER, PASS);
		                                Statement stmtHymnInfo = conn.createStatement();
//		                                System.out.println("CurrentHymnID: " + currentHymnID);
		                                String queryHymnInfo = "SELECT * FROM hymns WHERE hymnID = " + currentHymnID;
		                                ResultSet rsHymnInfo = stmtHymnInfo.executeQuery(queryHymnInfo);
		                                int size = 0;
		                                while (rsHymnInfo.next()) {
		                                	size++;
		                                	hymnNumber = rsHymnInfo.getInt("hymnNumber");
		                                	hymnName = rsHymnInfo.getString("hymnTitle");
		                                	hymnTempo = rsHymnInfo.getInt("hymnTempo" + musician);
		                                	allowedLength = rsHymnInfo.getDouble("hymnMaxBarLength");
		                                }
		                                if (size == 0) {
		                                	throw new ArrayIndexOutOfBoundsException();
		                                }
		                                
		                                titleLabel.setText("<html><center>\"" + hymnName + "\"</center></html>");
		                                numberLabel.setText("<html><center>" + (hymnNumber == 0 ? "Found In Insert" : "Hymn #" + hymnNumber) + "</center></html>");
		                                                                                
		                                ArrayList<ArrayList<String>> hymnDataLists = new ArrayList<>();
		                                
		                                Statement stmtHymnData = conn.createStatement();
		                                String queryHymnData = "SELECT * FROM notes WHERE noteHymn = " + currentHymnID + " ORDER BY noteNumber";
		                                ResultSet rsHymnData = stmtHymnData.executeQuery(queryHymnData);
		                                while (rsHymnData.next()) {
		                                	ArrayList<String> currentDataList = new ArrayList<>();
		                                	
		                                	if (rsHymnData.getString("noteLyric") == null) {
		                                		currentDataList.add("");
		                                	} else {
		                                    	currentDataList.add(rsHymnData.getString("noteLyric"));
		                                	}
		                                	currentDataList.add(rsHymnData.getString("noteType"));
		                                	
		                                	hymnDataLists.add(currentDataList);
		                                }     
		                                
		                                currentHymnData = hymnDataLists.stream()
		                                        .map(l -> l.toArray(new String[0]))
		                                        .toArray(String[][]::new);
		                                                                                            
		                                setupHymnLyrics(currentHymnData);
		                    	    	infoAlpha = 0.0f;
		                    	    	scrollAlpha = 0.0f;
//		                    	    	menuPane.setVisible(false);	// VERSION-A


//		                    	    	firstFadeInTimer.start();    
		                    	    	songStarted = true;

		                            } catch (SQLException se) {
		                            	JLabel failureMessage = new JLabel("Error. Cannot connect to hymn database.");
		                            	failureMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		                            	failureMessage.setForeground(Color.decode("#004400"));
		                            	menuPane.add(failureMessage);
		                            	menuPane.revalidate(); // Recompute the layout
		                            	menuPane.repaint();
		                            	
		                                se.printStackTrace();
		                            } catch (ArrayIndexOutOfBoundsException ae) {
		                            	JLabel failureMessage = new JLabel("No data for this hymn yet.");
		                            	failureMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		                            	failureMessage.setForeground(Color.decode("#004400"));
		                            	menuPane.add(failureMessage);
		                            	menuPane.revalidate(); // Recompute the layout
		                            	menuPane.repaint();
		                            	
		                                ae.printStackTrace();
		                            } catch (Exception sqlE) {
		                                // Handle errors for Class.forName
		                            	sqlE.printStackTrace();
		                            } finally {
		                                // Finally block used to close resources
		                                try {
		                                    if (conn != null) {
		                                        conn.close();
		                                    }
		                                } catch (SQLException se) {
		                                    se.printStackTrace();
		                                } finally {
			                    	    	startButton.setEnabled(true);
//			                    	    	startButton.setText("Start Hymn Text");	// VERSION-B
		                                }
		                            }            	        	    			
		        	    		}
		        	                        
		        	    	}
		        	    }
		        	});
		        	
		        }
	        	
	        }
	        
	        
            
        });
    }
    
    protected static void changeTrackSpeed(int speedChange) {
    	if (!(speedChange == -1 && hymnTempo == 1)) {
    		hymnTempo += speedChange;
    		if (currentTimer != null) {
    			currentTimer.setDelay(hymnTempo);
    		}
    		System.out.println(hymnTempo);
    	}
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
        currentLine.setOpaque(false);
        lineWordCount = 0;
		hymnLinesList.add(currentLine);
        return currentLine;
    }

	public static void startNextAnim() {
		if (currentNoteNumber < noteList.size()) {
			currentTimer = noteList.get(currentNoteNumber).getTimer();
			currentTimer.setDelay(hymnTempo);
			currentTimer.start();

			currentNoteNumber++;
		} else {
			Timer exitPause = new Timer(1000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
                	songStarted = false;
                	infoAlpha = 0.0f;
                	scrollAlpha = 0.0f;
                	scrollPane.repaint();                   	
                	smallPanel.repaint(); 
                	scrollNumber = 0;
        			scrollPane.getVerticalScrollBar().setValue(scrollNumber);
        			newLineCount = 0;
        			lineWordCount = 0;
//                	menuFrame.toFront();
                	infoUp = false;
                	lyricsUp = false;
                	timerStatus = false;
                	if (currentTimer != null) {
                		currentTimer.stop();
                	}
                	currentTimer = null;
                	currentNoteNumber = 0;
                	if (isTest == true) {
                		System.exit(0);
                	}
                	songEnded = true;
				}
			});
			exitPause.setRepeats(false);
			Timer lastFade = new Timer(50, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					scrollAlpha -= 0.05f;
	                if (scrollAlpha <= 0.0f) {
	                	scrollAlpha = 0.0f;
	                    ((Timer) e.getSource()).stop();
	                    exitPause.start();
	                }
	                scrollPane.repaint();
				}
			});
			Timer lastFadePause = new Timer(1000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					lastFade.start();
				}
			});
			lastFadePause.setRepeats(false);
			lastFadePause.start();
		}
	}
	
	static void scrollScreen() {
		scrollNumber += 280;
//		scrollPane.getVerticalScrollBar().setValue(scrollNumber);

        Timer fadeInTimer = new Timer(hymnTempo / 6 * 2, new ActionListener() {	// orig 15
            @Override
            public void actionPerformed(ActionEvent e) {
            	scrollAlpha += 0.05f;
                if (scrollAlpha >= 1.0f) {
                	scrollAlpha = 1.0f;
                    ((Timer) e.getSource()).stop();
                }
                scrollPane.repaint();
            }
        });

        Timer fadeOutTimer = new Timer(hymnTempo / 6 * 2, new ActionListener() {	// orig 30
            @Override
            public void actionPerformed(ActionEvent e) {
            	scrollAlpha -= 0.05f;
                if (scrollAlpha <= 0.0f) {
                	scrollAlpha = 0.0f;
        			scrollPane.getVerticalScrollBar().setValue(scrollNumber);
                    ((Timer) e.getSource()).stop();
                    fadeInTimer.start();
                }
                scrollPane.repaint();
            }
        });
        fadeOutTimer.start();
	}
	
	private static void setupHymnLyrics(String[][] hymnData) {
        mainContainer.removeAll();
        mainContainer.revalidate();
        mainContainer.repaint();
        
        noteList.clear();
        
        // set up array of lines and make first line
        ArrayList<JPanel> hymnLines = new ArrayList<JPanel>();
        JPanel currentLine = addNewLine(hymnLines);
                                
        double currentBarLength = 0;
        for (int i = 0; i < (hymnNumber == 830 && musician == 1 ? hymnData.length - 2 : hymnData.length); i++) {
        	// increase bar length based on current note type
        	if (hymnData[i][0].equals("")) {
        		currentBarLength += 0;
            } else if (hymnData[i][1].equals("wwww")) {
        		currentBarLength += 4;
            } else if (hymnData[i][1].equals("www")) {
        		currentBarLength += 3;
        	} else if (hymnData[i][1].equals("ww.")) {
        		currentBarLength += 2.5;
        	} else if (hymnData[i][1].equals("ww")) {
        		currentBarLength += 2;
            } else if (hymnData[i][1].equals("w.")) {
        		currentBarLength += 1.5;
            } else if (hymnData[i][1].equals("wq")) {
        		currentBarLength += 1.25;
        	} else if (hymnData[i][1].equals("w")) {
        		currentBarLength += 1;
        	} else if (hymnData[i][1].equals("h.")) {
        		currentBarLength += 0.75;
        	} else if (hymnData[i][1].equals("h")) {
        		currentBarLength += 0.5;
        	} else if (hymnData[i][1].equals("q.")) {
        		currentBarLength += 0.375;
        	} else if (hymnData[i][1].equals("q")) {
        		currentBarLength += 0.25;
        	} else if (hymnData[i][1].equals("e.")) {
        		currentBarLength += 0.1875;
        	} else if (hymnData[i][1].equals("e")) {
        		currentBarLength += 0.125;
        	} else if (hymnData[i][1].equals("s")) {
        		currentBarLength += 0.0625;
        	}
        	Note newNote;
        	
        	if (!(hymnData[i][0].equals("") && usePauses == false)) {
            	lineWordCount++;
            	if (hymnData[i][0].equals("*") && hymnData[i][1].equals("^")) {

            	} else if (hymnData[i][0].equals("*") && hymnData[i][1].equals("*")) {
            		lineWordCount = 0;
            		currentBarLength = 0; 
                    currentLine = addNewLine(hymnLines);
                	currentLine.add(new Note("  ", "*", isScrollLine, hymnTempo).getLabel());
            	} else if (currentBarLength >= allowedLength && lineWordCount >= (hymnNumber == 830 ? 3 : 1)) {
            		lineWordCount = 0;
            		currentBarLength = 0;
            		if (hymnData[i][0].equals("") || hymnData[i][0].equals("%")) {
                		newNote = new Note(hymnData[i][0], hymnData[i][1], isScrollLine, hymnTempo);
            		} else if (hymnData[i][0].contains("@")) {
                		newNote = new Note(hymnData[i][0].replace("@",""), hymnData[i][1], isScrollLine, hymnTempo);
                	} else {
                		newNote = new Note(hymnData[i][0].replace("@","").concat("-"), hymnData[i][1], isScrollLine, hymnTempo);
                	}   
                	currentLine.add(newNote.getLabel());
                    currentLine = addNewLine(hymnLines);
                	noteList.add(newNote);
            	} else {
                	newNote = new Note(hymnData[i][0].replace("@",""), hymnData[i][1], isScrollLine, hymnTempo);
                	currentLine.add(newNote.getLabel());
                	if (hymnData[i][0].contains("@")) {
                		JLabel spaceLabel = new JLabel(" ");
                		spaceLabel.setFont(new Font("Arial", Font.BOLD, 60));
                		spaceLabel.setOpaque(false);
                		spaceLabel.setPreferredSize(new Dimension(spaceLabel.getPreferredSize().width,newNote.getLabel().getPreferredSize().height));
                		currentLine.add(spaceLabel);
                	}    
                	noteList.add(newNote);
            	}        		
        	}
        	
        }
        if (hymnLines.get(hymnLines.size() - 1).getComponentCount() == 0) {
        	hymnLines.remove(hymnLines.size() - 1);
        }
        int noteCount = 0;
        for (int j = 0; j < hymnLines.size(); j++) {
        	mainContainer.add(hymnLines.get(j));
//        	System.out.print(j + ": " + "(" + hymnLines.get(j).getComponentCount() + ")");
        	for (int k = 0; k < hymnLines.get(j).getComponentCount(); k++) {
        		String currentText = ((JLabel) hymnLines.get(j).getComponent(k)).getText();
//        		System.out.print("[" + noteCount + ":" + currentText + "] ");
    			if (currentText != " " && currentText != "  ") {
    				noteCount++;
    			}
    		}
//			System.out.print("\n");
			if (j % 4 == 3 && noteCount != noteList.size()) {
				noteList.get(noteCount - 1).setScroll();
				if (j < hymnLines.size() - 1 && ((JLabel) hymnLines.get(j + 1).getComponent(0)).getText().equals("")) {
					noteList.get(noteCount - 1).setSlowScroll();
				}
				if (j < hymnLines.size() - 1 && hymnLines.get(j + 1).getComponentCount() > 1 && ((JLabel) hymnLines.get(j + 1).getComponent(1)).getText().equals("")) {
					noteList.get(noteCount - 1).setSlowScroll();
				}
			}
        }
        
        
//        for (int k = 0; k < noteList.size(); k++) {
//        	System.out.println(noteList.get(k).getLabel().getText() + "," + noteList.get(k).getIsScroll());
//        }
        
        int lastPadding = 0;
        if (hymnLines.size() % 4 == 1) {
        	lastPadding = 105;
            hymnLines.get(hymnLines.size() - 1).setBorder(BorderFactory.createEmptyBorder(lastPadding, 0, lastPadding, 0));
        } else if (hymnLines.size() % 4 == 2) {
        	lastPadding = 70;
        	hymnLines.get(hymnLines.size() - 2).setBorder(BorderFactory.createEmptyBorder(lastPadding,0,0,0));
        	hymnLines.get(hymnLines.size() - 1).setBorder(BorderFactory.createEmptyBorder(0,0,lastPadding,0));
        } else if (hymnLines.size() % 4 == 3) {
        	lastPadding = 35;
        	hymnLines.get(hymnLines.size() - 3).setBorder(BorderFactory.createEmptyBorder(lastPadding,0,0,0));
        	hymnLines.get(hymnLines.size() - 1).setBorder(BorderFactory.createEmptyBorder(0,0,lastPadding,0));
        }
        
        currentTimer = noteList.get(0).getTimer();

	}
	
}

class CustomRadioIcon implements Icon {
    private final Color color;
    private final boolean isSelected;
    private final int size = 16; // Diameter of the radio circle

    public CustomRadioIcon(Color color, boolean isSelected) {
        this.color = color;
        this.isSelected = isSelected;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw outer border circle
        g2.setColor(Color.decode("#004400"));
        g2.drawOval(x, y, size - 1, size - 1);

        // Draw white interior background
        g2.setColor(Color.decode("#00DD00"));
        g2.fillOval(x + 1, y + 1, size - 2, size - 2);

        // Draw the custom colored inner dot if selected
        if (isSelected) {
            g2.setColor(color);
            g2.fillOval(x + 4, y + 4, size - 8, size - 8);
        }

        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return size;
    }

    @Override
    public int getIconHeight() {
        return size;
    }
}
