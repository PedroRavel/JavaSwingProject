package gui;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class FormPanel extends JPanel {
	private JLabel nameLabel;
	private JLabel occupationLabel;
	private JTextField nameField;
	private JTextField occupationField;
	private JButton okBtn;
	private FormListener formListener;
	private JList ageList;
	private String[] ageRange;
	private JComboBox empCombo;
	private JCheckBox citizenCheck;
	private JTextField taxField;
	private JLabel taxLabel;

	private JRadioButton maleRadio;
	private JRadioButton femaleRadio;
	private ButtonGroup genderGroup;

	public FormPanel() {
		Dimension dim = getPreferredSize();
		dim.width = 250;
		setPreferredSize(dim);
		setMinimumSize(dim);

		nameLabel = new JLabel("Name: ");
		occupationLabel = new JLabel("Occupation: ");
		nameField = new JTextField(10);
		occupationField = new JTextField(10);
		ageList = new JList();
		empCombo = new JComboBox();
		empCombo.setEditable(true);
		citizenCheck = new JCheckBox();
		taxField = new JTextField(10);
		taxLabel = new JLabel("Tax ID: ");
		okBtn = new JButton("OK");
		
		//set Mnemonics
		okBtn.setMnemonic(KeyEvent.VK_O);
		nameLabel.setDisplayedMnemonic(KeyEvent.VK_N);
		nameLabel.setLabelFor(nameField);
		
		maleRadio = new JRadioButton("male");
		femaleRadio = new JRadioButton("female");
		maleRadio.setActionCommand("male");
		femaleRadio.setActionCommand("female");
		maleRadio.setSelected(true);

		genderGroup = new ButtonGroup();

		// Set up gender Radios
		genderGroup.add(maleRadio);
		genderGroup.add(femaleRadio);

		// Set up tax ID
		taxLabel.setEnabled(false);
		taxField.setEnabled(false);

		citizenCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean isTicked = citizenCheck.isSelected();
				taxLabel.setEnabled(citizenCheck.isSelected());
				taxField.setEnabled(citizenCheck.isSelected());
			}
		});
		// Set up list box
		DefaultListModel ageModel = new DefaultListModel();
		ageRange = new String[] { "Under 18", "18 to 65", "65 or over" };
		for (int i = 0; i < ageRange.length; i++)
			ageModel.addElement(new AgeCategory(i, ageRange[i]));
		ageList.setModel(ageModel);

		ageList.setPreferredSize(new Dimension(110, 66));
		ageList.setBorder(BorderFactory.createEtchedBorder());
		ageList.setSelectedIndex(0);

		// Set up combo box
		DefaultComboBoxModel empModel = new DefaultComboBoxModel();
		empModel.addElement("employed");
		empModel.addElement("self-employed");
		empModel.addElement("unemployed");
		empCombo.setModel(empModel);

		
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = nameField.getText();
				String occupation = occupationField.getText();
				AgeCategory ageCat = (AgeCategory) ageList.getSelectedValue();
				String empCat = (String) empCombo.getSelectedItem();
				String taxId = taxField.getText();
				boolean usCitizen = citizenCheck.isSelected();

				String gender = genderGroup.getSelection().getActionCommand();
				System.out.println(ageCat.getId() + ageCat.getText());

				FormEvent ev = new FormEvent(this, name, occupation, ageCat
						.getId(), empCat, taxId, usCitizen, gender);

				if (formListener != null)
					formListener.formEventOccured(ev);
			}

		});
		Border innerBorder = BorderFactory.createTitledBorder("Add Person");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		layoutComponents();

	}

	public void setFormListener(FormListener listener) {
		this.formListener = listener;
	}

	public void layoutComponents() {
		setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();
		// //First Row////////////////////
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;

		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(nameLabel, gc);

		gc.gridx = 1;
		gc.gridy = 0;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(nameField, gc);

		// //Second row
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;

		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(occupationLabel, gc);

		gc.gridx = 1;
		gc.gridy = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(occupationField, gc);

		// //Third row
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.2;
		gc.gridx = 0;

		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Age: "), gc);
		gc.gridx = 1;

		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(ageList, gc);
		// //fourth f=row
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.2;
		gc.gridx = 0;

		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Employment: "), gc);
		gc.gridx = 1;

		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(empCombo, gc);

		// //fourth f=row
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.2;
		gc.gridx = 0;

		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("US Citizen: "), gc);
		gc.gridx = 1;

		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(citizenCheck, gc);
		// //fourth f=row
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.2;
		gc.gridx = 0;

		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(taxLabel, gc);
		gc.gridx = 1;

		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(taxField, gc);
		// //fourth f=row
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.02;
		gc.gridx = 0;

		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Gender: "), gc);
		gc.gridx = 1;

		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(maleRadio, gc);

		// //fourth f=row
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.2;
		gc.gridx = 0;

		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		// add(new JLabel("Gender: "), gc);
		gc.gridx = 1;

		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(femaleRadio, gc);
		// //Five row
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 2;

		gc.gridx = 1;

		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(okBtn, gc);

	}

}

class AgeCategory {
	private String text;
	private int id;

	public AgeCategory(int id, String text) {
		this.id = id;
		this.text = text;
	}

	public String toString() {
		return text;
	}

	public String getText() {
		return text;
	}

	public int getId() {
		return id;
	}
}