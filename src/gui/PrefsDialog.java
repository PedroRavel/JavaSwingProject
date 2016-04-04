package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

public class PrefsDialog extends JDialog {
	
	private JButton okButton;
	private JButton cancelButton;
	private JSpinner portSpinner;
	private SpinnerNumberModel spinnerModel;
	private JTextField userField;
	private JPasswordField passField;
	private PrefsListener listener;
	
	
	public PrefsDialog(JFrame parent) {
		super(parent,"Preferences",false);
		okButton = new JButton("OK");
		cancelButton= new JButton("Cancel");
		spinnerModel = new SpinnerNumberModel(3306,0,9999,1);
		portSpinner = new JSpinner(spinnerModel);
		
		userField = new JTextField(10);
		passField = new JPasswordField(10);
		passField.setEchoChar('*');
	
		
		layoutControls();
		
		okButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				Integer value = (Integer)portSpinner.getValue();
				
				String user = userField.getText();
				char[] password = passField.getPassword();
				
				System.err.println(user + ":" + new String(password));
				
				if(listener!=null){
					listener.prefrencesSet(user, new String(password), value);
				}
				
				setVisible(false);
			}
			
		});
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		setSize(320,230);
		setLocationRelativeTo(parent);
	}
	public void setDefaults(String user, String password, int port) {
		userField.setText(user);
		passField.setText(password);
		portSpinner.setValue(port);
	}

	private void layoutControls(){
		
		JPanel controlsPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		int space = 15;
		Border spaceBorder = BorderFactory.createEmptyBorder(space,space,space,space);
		Border titleBorder = BorderFactory.createTitledBorder("Database Preferences");
		
		controlsPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder,titleBorder));
	//	buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		controlsPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.gridy = 0;
		Insets rightPadding = new Insets(0,0,0,15);
		Insets noPadding = new Insets(0,0,0,0);
		
		//////////////First Row//////////////
		
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.EAST;
		gc.insets = rightPadding;
		controlsPanel.add(new JLabel("User: "), gc);
		
		gc.gridx++;
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = noPadding;
		controlsPanel.add(userField,gc);
		
		
		
		////////////Next Row////////////
		
		gc.gridy++;
		
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.EAST;
		gc.insets = rightPadding;
		controlsPanel.add(new JLabel("Password: "), gc);
		
		gc.gridx++;
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = noPadding;
		controlsPanel.add(passField,gc);
		
		////////////Next Row////////////
		
		gc.gridy++;
		
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.EAST;
		gc.insets = rightPadding;
		controlsPanel.add(new JLabel("Port: "), gc);
		
		gc.gridx++;
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = noPadding;
		controlsPanel.add(portSpinner,gc);
		
		
		///////////////Buttons Panel////////////
		
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		gc.gridy++;
		gc.gridx = 0;
		buttonsPanel.add(okButton);
		
		gc.gridx++;
		buttonsPanel.add(cancelButton,gc);
		
		setLayout(new BorderLayout());
		add(controlsPanel,BorderLayout.CENTER);
		add(buttonsPanel,BorderLayout.SOUTH);
		
		Dimension btnSize = cancelButton.getPreferredSize();
		okButton.setPreferredSize(btnSize);
		
	}
	public void setPrefsListener(PrefsListener prefsListener) {
		// TODO Auto-generated method stub
		this.listener = prefsListener;
	}
}

