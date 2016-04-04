package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.Controller;


public class MainFrame extends JFrame {
	
	private JButton btn;
	private Toolbar toolbar;
	private FormPanel formPanel;
	private JFileChooser fileChooser;
	private Controller controller;
	private TablePanel tablePanel;
	private PrefsDialog prefsDialog;
	private Preferences prefs;
	private JSplitPane splitPane;
	private JTabbedPane tabPane;
	private MessagePanel messagePanel;
	
	public MainFrame() {
		super("Occupation Form");

		setLayout(new BorderLayout());
		
		toolbar = new Toolbar();
		
		btn = new JButton("Click Me!");
		formPanel = new FormPanel();
		tablePanel = new TablePanel();
		prefsDialog = new PrefsDialog(this);
		tabPane = new JTabbedPane();
		messagePanel = new MessagePanel(this);
		//splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel,tablePanel);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel,tabPane);
		
		splitPane.setOneTouchExpandable(true);
		tabPane.addTab("Person Database", tablePanel);
		tabPane.addTab("Messages", messagePanel);
		
	
		
		prefs = Preferences.userRoot().node("db");
		
		controller = new Controller();
		
		tablePanel.setData(controller.getPeople());
		
		tablePanel.setPersonTableListener(new PersonTableListener(){
			public void rowDeleted(int row){
				controller.removePerson(row);
			}
		});
		
		tabPane.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
				int tabIndex = tabPane.getSelectedIndex();
				if(tabIndex == 1){
					messagePanel.refresh();
				}
			}
			
		});
		
		prefsDialog.setPrefsListener(new PrefsListener(){
			public void prefrencesSet(String user, String password, int port) {
				System.out.println(user + password);
				prefs.put("user", user);
				prefs.put("password", password);
				prefs.putInt("port", port);
				
				try {
					controller.configure(port, user, password);
				} catch( Exception e){
					JOptionPane.showMessageDialog(MainFrame.this, "Unable to reconnect");
				}
			}
			
		});
		
		String user = prefs.get("user", "");
		String password = prefs.get("password","");
		Integer port = prefs.getInt("port",3306);
		
		prefsDialog.setDefaults(user, password, port);
		try {
			controller.configure(port, user, password);
		} catch( Exception e){
			System.err.println("Cant connect to database");
		}
		fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new PersonFileFilter());
		
		setJMenuBar(createMenuBar());
		
		toolbar.setToolbarListener(new ToolbarListener(){
			public void saveEventOccured(String text) {
				connect();

				try {
					controller.save();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(MainFrame.this, "CANNOT SAVE", "Database Connection Problem", JOptionPane.ERROR_MESSAGE);
				}
			}

			public void refreshEventOccured() {
				connect();
			 try {
				controller.load();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(MainFrame.this, "CANNOT LOAD", "Database Connection Problem", JOptionPane.ERROR_MESSAGE);
			}
			 tablePanel.refresh();
			}
		});
		
		formPanel.setFormListener(new FormListener() {
			public void formEventOccured(FormEvent e) {
				/*
				String name = e.getName();
				String occupation = e.getOccupation();
				int ageCat = e.getAgeCategory();
				String empCat = e.getEmployeeCategory();
				
				textPanel.appendText(name + ": " + occupation + ": ID " + ageCat + ", " + empCat + "\n");
				
				System.out.println(e.getGender());
				*/
				controller.addPerson(e);
				tablePanel.refresh();
			}
		});
		addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Window closing");
				controller.disconnect();
				dispose();
				System.gc();
			}
			
		});
		add(toolbar, BorderLayout.PAGE_START);
		add(splitPane, BorderLayout.CENTER);
		//add(tablePanel, BorderLayout.CENTER);
		//add(formPanel, BorderLayout.WEST);
		
		setMinimumSize(new Dimension(500,400));
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
	
	private void connect(){
		try {
			controller.connect();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(MainFrame.this, "CANNOT CONNECT", "Database Connection Problem", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private JMenuBar createMenuBar() {
			JMenuBar menuBar = new JMenuBar();
			
			JMenu fileMenu = new JMenu("File");
			
			JMenu windowMenu = new JMenu("Window");
			JMenuItem exportDataItem = new JMenuItem("Export Data...");
			JMenuItem importDataItem = new JMenuItem("Import Data...");
			JMenuItem exitItem = new JMenuItem("Exit");
			exitItem.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent arg0) {
					
					//GOOD FOR ASKING QUESTIONS USE A BITWISE OR PIPE TO CHANGE ICONS
					//String text = JOptionPane.showInputDialog(MainFrame.this, "Enter Your User Name", "User Name", JOptionPane.OK_OPTION|JOptionPane.QUESTION_MESSAGE);
					
					int action = JOptionPane.showConfirmDialog(MainFrame.this, "Do You Really Want To Exit The APP?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);
					if(action == JOptionPane.OK_OPTION){
						WindowListener[] listeners = getWindowListeners();
						
						for(WindowListener listener: listeners) {
							listener.windowClosing(new WindowEvent(MainFrame.this,0));
						}
					}	
				}
			});
			fileMenu.add(exportDataItem);
			fileMenu.add(importDataItem);
			fileMenu.addSeparator();
			fileMenu.add(exitItem);
			
			JMenu showMenu = new JMenu("Show   ");
			JMenuItem prefsItem = new JMenuItem("Prefences...");
			JCheckBoxMenuItem showFormItem = new JCheckBoxMenuItem("Person Form");
			showFormItem.setSelected(true);
			
			windowMenu.add(showMenu);
			windowMenu.add(prefsItem);
			
			prefsItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					prefsDialog.setVisible(true);
				}
			});
			
			showMenu.add(showFormItem);

			
			menuBar.add(fileMenu);
			menuBar.add(windowMenu);
			
			showFormItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)ev.getSource();
					if(menuItem.isSelected()){
					splitPane.setDividerLocation((int)formPanel.getMinimumSize().getWidth());
					}
					formPanel.setVisible(menuItem.isSelected());
				}
				
			});
			fileMenu.setMnemonic(KeyEvent.VK_F);
			exitItem.setMnemonic(KeyEvent.VK_X);
			prefsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,ActionEvent.CTRL_MASK));
			exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
			importDataItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,ActionEvent.CTRL_MASK));
			
			importDataItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					if(fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION){
						try {
							controller.loadFromFile(fileChooser.getSelectedFile());
							tablePanel.refresh();
						} catch (IOException e) {
							JOptionPane.showMessageDialog(MainFrame.this, "Could Not Data From File.","Error",JOptionPane.ERROR_MESSAGE);
						}
					
					}
				}
			});
			
			exportDataItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					if(fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION){
						
							try {
								controller.saveToFile(fileChooser.getSelectedFile());
							} catch (IOException e) {
								JOptionPane.showMessageDialog(MainFrame.this, "Could Not Save Data To File.","Error",JOptionPane.ERROR_MESSAGE);
							}
						
					}
				}
			});
			
			return menuBar;
	}
}
