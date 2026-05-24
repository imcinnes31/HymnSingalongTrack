import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import static javax.swing.ScrollPaneConstants.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
	private static Hymn selectedMenuItem = null;
	private static String[][] currentHymnData;
	
    private static final String DB_HOST = "co28d739i4m2sb7j.cbetxkdyhwsb.us-east-1.rds.amazonaws.com";
    private static final String DB_NAME = "raw5l6mnqtlyokfw";
    private static final String USER = "q6b7fx1pny2hkold";
    private static final String PASS = "i84r6te0qju8rb7w";
    private static final String PORT = "3306";
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//        	menuFrame = new JFrame("Hymn Sing Along");
//        	menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        	menuFrame.setSize(700,200);
//        	menuFrame.setLocationRelativeTo(null); 

//        	icons = new ArrayList<Image>();
//        	icons.add(Toolkit.getDefaultToolkit().getImage("src\\cross16.png"));
//        	icons.add(Toolkit.getDefaultToolkit().getImage("src\\cross32.png"));
//        	icons.add(Toolkit.getDefaultToolkit().getImage("src\\cross48.png"));
//        	icons.add(Toolkit.getDefaultToolkit().getImage("src\\cross256.png"));
//        	menuFrame.setIconImages(icons);
        	
//            int yOffset = 100; 
//        	
//        	Rectangle menuBounds = menuFrame.getBounds();
//        	menuFrame.setLocation(menuBounds.x, yOffset);
        	
        	menuPane = new JPanel();
        	Dimension menuPaneDim = new Dimension(700,200);
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

        	JButton quitButton = new JButton("Quit");
        	quitButton.setBackground(Color.PINK);
        	Font quitButtonFont = new Font("Times New Roman", Font.BOLD, 18);
        	quitButton.setFont(quitButtonFont);
        	quitButton.setFocusPainted(false);
        	
            gbc.gridx = 1; // Second column
            gbc.gridy = 0; // First row
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.anchor = GridBagConstraints.FIRST_LINE_END; // Align to top-right
            gbc.insets = new Insets(10, 10, 10, 10); // Optional padding
            appHeader.add(quitButton, gbc);

            JLabel menuHeaderLabelChurch = new JLabel("St. John's Presbyterian Church");
            Font customFont = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 24);
            menuHeaderLabelChurch.setFont(customFont);
            menuHeaderLabelChurch.setHorizontalAlignment(SwingConstants.CENTER);
            menuHeaderLabelChurch.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        	
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2; // Span both columns to ensure true center
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.CENTER;
            appHeader.add(menuHeaderLabelChurch, gbc);
            
        	appHeader.setBackground(Color.decode("#F9EECF"));
            menuPane.add(appHeader);
        	
        	JLabel menuHeaderLabelApp = new JLabel("Hymn Singalong");
        	menuHeaderLabelApp.setAlignmentX(Component.CENTER_ALIGNMENT);
            Font customFont2 = new Font("Times New Roman", Font.ITALIC, 18);
            menuHeaderLabelApp.setFont(customFont2);
            menuHeaderLabelApp.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        	menuPane.add(menuHeaderLabelApp);
        	
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
                
//                hymnList.add(new Hymn(2,11,"The Lord's My Shepherd"));
//                hymnList.add(new Hymn(3,638,"Take Time To Be Holy"));
//                hymnList.add(new Hymn(4,637,"Take My Life, And Let It Be"));
//                hymnList.add(new Hymn(5,0,"My Peace I Give Unto You"));
//                hymnList.add(new Hymn(6,243,"Jesus Christ Is Risen Today"));
//                hymnList.add(new Hymn(7,260,"Alleluia, Alleluia, Give Thanks To The Risen Lord"));
//                hymnList.add(new Hymn(8,258,"Thine Be The Glory"));
//                hymnList.add(new Hymn(9,250,"I Danced In The Morning / Lord Of The Dance"));
                
                hymnList.sort(Comparator.comparing(Hymn::getHymnNumber));

                Hymn[] items = hymnList.toArray(new Hymn[hymnList.size()]);
                
                comboBox = new JComboBox<Hymn>(items);  
                
//                String[] speedOptions = {"50", "100", "200", "500"};
                
//                JComboBox speedBox = new JComboBox<String>(speedOptions);
                
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
                
//                JPanel comboContainer = new JPanel();
//                
//                Dimension comboContainerSize = comboContainer.getPreferredSize();
//                
//                comboContainerSize.height = 35;
//                comboContainerSize.width = 700;
//                comboContainer.setMinimumSize(comboContainerSize);                
//                comboContainer.setMaximumSize(comboContainerSize);                
//                comboContainer.setPreferredSize(comboContainerSize);                
                
                Dimension comboBoxSize = comboBox.getPreferredSize();

                comboBoxSize.height = 25; 
                comboBoxSize.width = 650;
                comboBox.setMinimumSize(comboBoxSize);                
                comboBox.setMaximumSize(comboBoxSize);                
                comboBox.setPreferredSize(comboBoxSize);                
                
//                Dimension speedBoxSize = comboBox.getPreferredSize();
//
//                speedBoxSize.height = 25; 
//                speedBoxSize.width = 150;
//                speedBox.setMinimumSize(speedBoxSize);                
//                speedBox.setMaximumSize(speedBoxSize);                
//                speedBox.setPreferredSize(speedBoxSize);                

                comboBox.addActionListener(e -> {
             		Object selected = comboBox.getSelectedItem();
             		if (selected instanceof Hymn) {
             			selectedMenuItem = (Hymn) selected;
                    	comboBox.setEditable(false);
             		    currentHymnID = selectedMenuItem.getHymnID();
             		}
                });
             	
                JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();

//                comboContainer.add(comboBox);
//                comboContainer.add(speedBox);
//                menuPane.add(comboContainer);
                
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
        	startButton.setFocusPainted(false);
        	startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        	        	        	
        	buttonPanel.add(startButton);
        	buttonPanel.setBackground(Color.decode("#F9EECF"));
        	menuPane.add(buttonPanel);
        	
        	menuPane.setBackground(Color.decode("#F9EECF"));
        	menuPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        	
//        	menuFrame.add(menuPane);
        	
//            singFrame = new JFrame("Hymn Text Window");
//            singFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            singFrame.setLocationRelativeTo(null); 
//            singFrame.setUndecorated(true);
//            singFrame.setBackground(Color.GREEN);
//        	singFrame.setIconImages(icons);
        	
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
            
//            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//            GraphicsDevice[] gd2 = ge.getScreenDevices();
//
//            // Choose the secondary monitor (index 1)
//            if (gd2.length > 1) {
//                Rectangle bounds = gd2[1].getDefaultConfiguration().getBounds();
//                // Position the frame on the second screen
//                singFrame.setLocation(bounds.x, bounds.y);
//            }
//            singFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                                    
//            Rectangle singBounds = singFrame.getBounds();
//            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//            int newY = screenSize.height - singBounds.height - yOffset;
//            
//            singFrame.setLocation(singBounds.x, newY);
            
//            singFrame.setUndecorated(true);
                        
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
                    	} else if (menuPane.isVisible() == true) {
                    		if (comboBox.isPopupVisible() == true) {
                    			comboBox.setSelectedIndex(0);
                    		} else {
                    			startButton.doClick();
                    		}
                    	}
                    } else if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    	if (isTest == true) {
                    		System.exit(0);
                    	} else if (menuPane.isVisible() == true) {
                    		
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
                    	} else {
                    		songEnded = false;
                    		menuPane.setVisible(true);
                    	}
                    } else if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_OPEN_BRACKET) {
                    	changeTrackSpeed(1);
                    } else if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_CLOSE_BRACKET) {
                    	changeTrackSpeed(-1);
                    }
                    return false;
                }
            });
            
//            Action spacebarAction = new AbstractAction() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    if (timerStatus == false && lyricsUp == true) {
//                    	currentTimer.start();
//                    	timerStatus = true;
//                    } else {
//                    	currentTimer.stop();
//                    	timerStatus = false;
//                    }
//                    // Add your custom logic here
//                }
//            };
            
//            Action enterAction = new AbstractAction() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    if (infoUp == true) {
//                    	infoFadeOutTimer.start();
//                    }
//                }
//            };

//            KeyStroke spacebarPressed = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false); // false means on key press
//            KeyStroke enterKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
            
//            ActionListener escapeActionListener = new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    System.exit(0); 
//                }
//            };

//            singFrame.getRootPane().registerKeyboardAction(
//                    escapeActionListener, 
//                    KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), 
//                    JComponent.WHEN_IN_FOCUSED_WINDOW // Condition for when the action is available
//                );
            
            // 3. Bind the Key Stroke to the Action
//            mainContainer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(spacebarPressed, "performSpacebarAction");
//            mainContainer.getActionMap().put("performSpacebarAction", spacebarAction);
//            InputMap inputMap = mainContainer.getInputMap(JComponent.WHEN_FOCUSED);
//            ActionMap actionMap = mainContainer.getActionMap();
//            String actionName = "performEnterAction";
//            inputMap.put(enterKeyPressed, actionName);
//            actionMap.put(actionName, enterAction);
            
            // 1. Define an Action for the '[' key
//            Action leftBracketAction = new AbstractAction() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    changeTrackSpeed(1);
//                }
//            };

            // 2. Get the InputMap
            // WHEN_IN_FOCUSED_WINDOW makes the binding work when frame has focus
//            InputMap inputMapLeft = mainContainer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//            ActionMap actionMapLeft = mainContainer.getActionMap();

            // 3. Map the Key Stroke to a key
//            KeyStroke leftBracketKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, 0);
//            String leftBracketKey = "triggerLeftBracketAction";
//            inputMapLeft.put(leftBracketKeyStroke, leftBracketKey);

            // 4. Map the key string to the Action object
//            actionMapLeft.put(leftBracketKey, leftBracketAction);
            
            // 1. Define an Action for the '[' key
//            Action rightBracketAction = new AbstractAction() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    changeTrackSpeed(-1);
//                }
//            };

            // 2. Get the InputMap
            // WHEN_IN_FOCUSED_WINDOW makes the binding work when frame has focus
//            InputMap inputMapRight = mainContainer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//            ActionMap actionMapRight = mainContainer.getActionMap();

            // 3. Map the Key Stroke to a key
//            KeyStroke rightBracketKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, 0);
//            String rightBracketKey = "triggerRightBracketAction";
//            inputMapRight.put(rightBracketKeyStroke, rightBracketKey);

            // 4. Map the key string to the Action object
//            actionMapRight.put(rightBracketKey, rightBracketAction);

            mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
            
            scrollPane = new JScrollPane(mainContainer,
                    VERTICAL_SCROLLBAR_NEVER,
                    HORIZONTAL_SCROLLBAR_NEVER);
            
//            setLayout(null);
//            scrollPane.setBounds(0,0,1200,325);
//            scrollPane.setViewportBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));  
            scrollPane.getViewport().setBackground(new Color(0,0,0,0));
            scrollPane.setBorder(null);
            
         // Get the input map for when the component (e.g., JTextField) has focus
            InputMap im = mainContainer.getInputMap(JComponent.WHEN_FOCUSED);

            // Bind the UP and DOWN arrow keys to a "none" action to override defaults
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "none");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "none");

            // Ensure "none" is mapped in the ActionMap to effectively consume the event
            mainContainer.getActionMap().put("none", new AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {}
            });
            
            currentHymnData = null;

            if (isTest == true) {
                String[][] testHymnNames = 
                	{
                			{"Praise God From Whom All Blessings Flow","830","45","1.5"},	// 1:03:21
                			{"Blest Are They","624","18","3"},	// 15:48
                			{"Spirit, Spirit Of Gentleness","399","31","2"},
                	};
                String[][][] testHymnData = 
                	{
                			{
    	            			{"PRAISE@","h"},
    	            			{"GOD@","q"},
    	            			{"FROM@","q"},	//BAR
    	            			{"WHOM@","q"},
    	            			{"ALL@","q"},
    	            			{"BLESS","h"},	//BAR
    	            			{"INGS@","h"},
    	            			{"FLOW@","h"},	//BAR
    	            			{"","w"},	//BAR
    	            			{"PRAISE@","h"},
    	            			{"HIM@","q"},
    	            			{"ALL@","q"},	//BAR
    	            			{"CREA","q"},
    	            			{"TURES@","q"},
    	            			{"HERE@","h"},	//BAR
    	            			{"BE","h"},
    	            			{"LOW@","h"},	//BAR
    	            			{"","w"},	//BAR
    	            			{"PRAISE@","h"},
    	            			{"HIM@","q"},
    	            			{"A","q"},	//BAR
    	            			{"BOVE@","q"},
    	            			{"YE@","q"},
    	            			{"HEAVEN","h"},	//BAR
    	            			{"LY@","h"},
    	            			{"HOST@","h"},	//BAR
    	            			{"","w"},
    	            			{"PRAISE@","h"},
    	            			{"FA","q"},
    	            			{"THER@","q"},	//BAR
    	            			{"SON@","q"},
    	            			{"AND@","q"},
    	            			{"HO","h."},	
    	            			{"LY@","h"},	//BAR
    	            			{"GHOST@","h."},
    	            			{"","w"},
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
                			}
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
            
            JPanel controlPanel = new JPanel();
            Dimension controlPanelSize = new Dimension(1000,48);
            controlPanel.setPreferredSize(controlPanelSize);
            controlPanel.setMinimumSize(controlPanelSize);
            controlPanel.setMaximumSize(controlPanelSize);
            controlPanel.setBackground(new Color(0, 0, 0, 0));
	        JLabel controlSample = new JLabel("St. John's Presbyterian Church");
	        controlSample.setFont(new Font("Serif", Font.BOLD, 24)); 
	        controlSample.setForeground(Color.decode("#008800"));
	        controlPanel.add(controlSample);

//            JPanel controlPanel1 = new JPanel();
//            Dimension control1Size = new Dimension(150,30);
//            controlPanel1.setPreferredSize(control1Size);
//            controlPanel1.setMinimumSize(control1Size);
//            controlPanel1.setMaximumSize(control1Size);
//            controlPanel1.setBackground(new Color(0, 0, 0, 0));
//            JLabel controlInfo1 = new JLabel("Hymn #: 830");
//            controlInfo1.setFont(new Font("Serif", Font.BOLD, 18)); 
//            controlInfo1.setForeground(Color.decode("#008800"));
//            controlPanel1.add(controlInfo1);
//            controlPanel.add(controlPanel1);
//
//            JPanel controlPanel2 = new JPanel();
//            Dimension control2Size = new Dimension(700,30);
//            controlPanel2.setPreferredSize(control2Size);
//            controlPanel2.setMinimumSize(control2Size);
//            controlPanel2.setMaximumSize(control2Size);
//            controlPanel2.setBackground(new Color(0, 0, 0, 0));
//            JLabel controlInfo2 = new JLabel("Praise God From Whom All Blessings Flow");
//            controlInfo2.setFont(new Font("Serif", Font.BOLD, 18)); 
//            controlInfo2.setForeground(Color.decode("#008800"));
//            controlPanel2.add(controlInfo2);
//            controlPanel.add(controlPanel2);
//            
//            JPanel controlPanel3 = new JPanel();
//            Dimension control3Size = new Dimension(150,30);
//            controlPanel3.setPreferredSize(control3Size);
//            controlPanel3.setMinimumSize(control3Size);
//            controlPanel3.setMaximumSize(control3Size);
//            controlPanel3.setBackground(new Color(0, 0, 0, 0));
//            JLabel controlInfo3 = new JLabel("31");
//            controlInfo3.setFont(new Font("Serif", Font.BOLD, 18)); 
//            controlInfo3.setForeground(Color.decode("#008800"));
//            controlPanel3.add(controlInfo3);
//            controlPanel.add(controlPanel3);
                                    
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new OverlayLayout(mainPanel));
//            mainPanel.setBackground(new Color(0, 0, 0, 0));
//            mainPanel.setOpaque(false);
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
                        
            largePanel = new JPanel(new GridBagLayout()) {
//                @Override
//                protected void paintComponent(Graphics g) {
//                    super.paintComponent(g);
//                    Graphics2D g2d = (Graphics2D) g;
//                    // Set the transparency level using AlphaComposite.SrcOver
//                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, infoAlpha));
//
//                }
            };
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
            
            GridBagConstraints gbcControl = new GridBagConstraints();
            gbcControl.gridx = 0;
            gbcControl.gridy = 0;
            gbcControl.weightx = 1.0; // Horizontal centering
            gbcControl.weighty = 1.0; // Pushes component down
            gbcControl.anchor = GridBagConstraints.SOUTH;
            gbcControl.insets = new Insets(0, 0, 25, 0); 
            
            fullPanel.add(mainPanel, gbcText);
            fullPanel.add(controlPanel, gbcControl);
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
//	        Timer firstFadeInTimer = new Timer(100, new ActionListener() {
//	            @Override
//	            public void actionPerformed(ActionEvent e) {
//	            	((Timer)e.getSource()).stop(); 
//	            	smallPanel.setVisible(true);
//	            	smallPanel.repaint();
//			        infoFadeInTimer.start();
//	            }
//	        });
//	        largePanel.repaint();
//	        smallPanel.repaint();
//	        firstFadeInTimer.setRepeats(false);
//	        firstFadeInTimer.start();
//	        infoFadeInTimer.start();
//	        infoUp = true;
	        
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
		                                	hymnTempo = rsHymnInfo.getInt("hymnTempo");
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
		                    	    	menuPane.setVisible(false);
		                    	    	
//		                    	    	firstFadeInTimer.start();      
		                    	    	songStarted = true;

		                            } catch (SQLException se) {
		                            	JLabel failureMessage = new JLabel("Error. Cannot connect to hymn database.");
		                            	failureMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		                            	failureMessage.setForeground(Color.RED);
		                            	menuPane.add(failureMessage);
		                            	menuPane.revalidate(); // Recompute the layout
		                            	menuPane.repaint();
		                            	
		                                se.printStackTrace();
		                            } catch (ArrayIndexOutOfBoundsException ae) {
		                            	JLabel failureMessage = new JLabel("No data for this hymn yet.");
		                            	failureMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		                            	failureMessage.setForeground(Color.RED);
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
//			if (currentNoteNumber > 0 && noteList.get(currentNoteNumber - 1).getIsScroll()) {
//				scrollNumber += 280;
////    			scrollPane.getVerticalScrollBar().setValue(scrollNumber);
//
//		        Timer fadeInTimer = new Timer(hymnTempo / 6 * 2, new ActionListener() {	// orig 15
//		            @Override
//		            public void actionPerformed(ActionEvent e) {
//		            	scrollAlpha += 0.05f;
//		                if (scrollAlpha >= 1.0f) {
//		                	scrollAlpha = 1.0f;
//		                    ((Timer) e.getSource()).stop();
//		                }
//		                scrollPane.repaint();
//		            }
//		        });
//
//		        Timer fadeOutTimer = new Timer(hymnTempo / 6 * 2, new ActionListener() {	// orig 30
//		            @Override
//		            public void actionPerformed(ActionEvent e) {
//		            	scrollAlpha -= 0.05f;
//		                if (scrollAlpha <= 0.0f) {
//		                	scrollAlpha = 0.0f;
//		        			scrollPane.getVerticalScrollBar().setValue(scrollNumber);
//		                    ((Timer) e.getSource()).stop();
//		                    fadeInTimer.start();
//		                }
//		                scrollPane.repaint();
//		            }
//		        });
//		        fadeOutTimer.start();
//			}

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
        for (int i = 0; i < hymnData.length; i++) {
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
        	
        	// NEW CODE
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
        		if (hymnData[i][0].equals("")) {
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
        	
//        	if (addedNewLine == false && (currentBarLength >= allowedLength && i + 1 < hymnData.length && lineWordCount >= 3)) {
//        		currentBarLength = 0;
//            	newLineCount++;
//                if (newLineCount % 4 == 0 && newLineCount > 0) {
//            		isScrollLine = true;
//            	} else {
//            		isScrollLine = false;
//            	}
//        		if (hymnData[i][0].equals("")) {
//            		newNote = new Note(hymnData[i][0], hymnData[i][1], isScrollLine, hymnTempo);
//        		} else if (hymnData[i][0].contains("@")) {
//            		newNote = new Note(hymnData[i][0].replace("@",""), hymnData[i][1], isScrollLine, hymnTempo);
//            	} else {
//            		newNote = new Note(hymnData[i][0].replace("@","").concat("-"), hymnData[i][1], isScrollLine, hymnTempo);
//            	}
//            	currentLine.add(newNote.getLabel());
//                currentLine = addNewLine(hymnLines);
//                addedNewLine = true;
//                isScrollLine = false;
////            	if (newLineCount % 4 == 0 && newLineCount > 0) {
////            		isScrollLine = true;
////            	} else {
////            		isScrollLine = false;
////            	}
//        	} else {
//            	if (hymnData[i][0].equals("") && addedNewLine == false) {
//                	newLineCount++;
//                	lineWordCount = 0;
////                	if (newLineCount % 4 == 0 && newLineCount > 0) {
////                		isScrollLine = true;
////                	} else {
////                		isScrollLine = false;
////                	}
//            		addedNewLine = true;
//            		currentBarLength = 0;
//                    currentLine = addNewLine(hymnLines);
//                	newNote = new Note(hymnData[i][0].replace("@",""), hymnData[i][1], isScrollLine, hymnTempo);
//                	currentLine.add(newNote.getLabel());
//                	isScrollLine = false;
//            	} else {
//                	newNote = new Note(hymnData[i][0].replace("@",""), hymnData[i][1], isScrollLine, hymnTempo);
//                	currentLine.add(newNote.getLabel());
//                	if (hymnData[i][0].contains("@")) {
//                		JLabel spaceLabel = new JLabel(" ");
//                		spaceLabel.setFont(new Font("Arial", Font.BOLD, 60));
//                		spaceLabel.setOpaque(false);
////                		spaceLabel.setBackground(Color.LIGHT_GRAY);
//                		spaceLabel.setPreferredSize(new Dimension(spaceLabel.getPreferredSize().width,newNote.getLabel().getPreferredSize().height));
//                		currentLine.add(spaceLabel);
//                	}  
//                	addedNewLine = false;   
//                	isScrollLine = false;
//            	}
//        	}
//        	noteList.add(newNote);
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
				if (j < hymnLines.size() - 1 && ((JLabel) hymnLines.get(j + 1).getComponent(1)).getText().equals("")) {
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