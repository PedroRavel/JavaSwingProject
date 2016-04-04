package gui;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;



public class Toolbar extends JToolBar implements ActionListener{
	private JButton saveButton;
	private JButton refreshButton;
	private ToolbarListener textListener;
	
	public Toolbar() {
		//Get rid of border if you want toolbar dragable
		setBorder(BorderFactory.createEtchedBorder());
		saveButton = new JButton();
		
		saveButton.setIcon(Utils.createIcon("/images/save.gif"));
		saveButton.setToolTipText("Save");
		refreshButton = new JButton();
		refreshButton.setIcon(Utils.createIcon("/images/refresh.gif"));
		refreshButton.setToolTipText("Refresh");
	//	setLayout(new FlowLayout(FlowLayout.LEFT));
		
		add(saveButton);
		add(refreshButton);
		saveButton.addActionListener(this);
		refreshButton.addActionListener(this);
	}
	
//	private ImageIcon createIcon(String path){
//		URL url = getClass().getResource(path);
//		if(url == null){
//			System.err.println("Unable to load resource " + path);
//		}
//		ImageIcon icon = new ImageIcon(url);
//		return icon;
//	}

	public void setToolbarListener(ToolbarListener listener) {
		this.textListener = listener;
	}
	@Override
	public void actionPerformed(ActionEvent e) {

		JButton clicked = (JButton)e.getSource();

		if(clicked == saveButton){
			if(textListener != null) textListener.saveEventOccured("Hello\n");
		} 
		else if(clicked == refreshButton) {
			if(textListener != null) textListener.refreshEventOccured();
		}				  
	}
}
