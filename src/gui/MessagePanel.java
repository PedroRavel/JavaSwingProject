package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import model.Message;
import controller.MessageServer;


public class MessagePanel extends JPanel implements ProgressDialogListener{
	private JTree serverTree;
	private ServerTreeCellRenderer treeCellRenderer;
	private ServerTreeCellEditor treeCellEditor;
	private ProgressDialog progressDialog;
	private SwingWorker<List<Message>, Integer> worker;
	private TextPanel textPanel;
	private JList messageList;
	private JSplitPane upperPane;
	private JSplitPane lowerPane;
	private DefaultListModel messageListModel;
	
	private Set<Integer> selectedServers;
	private MessageServer messageServer;
	
	public MessagePanel(JFrame parent){
		messageListModel = new DefaultListModel();
		progressDialog = new ProgressDialog(parent,"Messages Downloading...");
		selectedServers = new TreeSet<Integer>();
		messageServer = new MessageServer();
		progressDialog.setListener(this);
		selectedServers.add(0);
		selectedServers.add(1);
		selectedServers.add(4);
		
		treeCellRenderer = new ServerTreeCellRenderer();
		treeCellEditor = new ServerTreeCellEditor();
		serverTree = new JTree(createTree());
		serverTree.setCellRenderer(treeCellRenderer);
		serverTree.setCellEditor(treeCellEditor);
		serverTree.setEditable(true);
		
		serverTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		messageServer.setSelectedServer(selectedServers);
		treeCellEditor.addCellEditorListener(new CellEditorListener(){

			@Override
			public void editingCanceled(ChangeEvent arg0) {

			}

			@Override
			public void editingStopped(ChangeEvent arg0) {
				ServerInfo info = (ServerInfo)treeCellEditor.getCellEditorValue();
			//	System.out.println(info + ": " + info.getId() + ": " +  info.isChecked());
				
				int serverId = info.getId();
				if(info.isChecked()) {
					selectedServers.add(serverId);
				} else {
					selectedServers.remove(serverId);
				}
				messageServer.setSelectedServer(selectedServers);
				retrieveMessages();
	
			}
			
		});
		
		
		
		/* CHECKS WHETHER NODE HAS BEEN SELECTED OR NOT (NOT NEEDED ANYMORE)
		serverTree.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent arg0) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)serverTree.getLastSelectedPathComponent();
				
				//String userObject = (String)node.getUserObject().toString();
				Object user = node.getUserObject();
				
				if(user instanceof ServerInfo) {
					int id = ((ServerInfo)user).getId();
					System.out.println("Got user object with id: " + id);
				}
				
				System.out.println(user);
			}
			
		});
		**/
		setLayout(new BorderLayout());
		textPanel = new TextPanel();
		messageList = new JList(messageListModel);
		messageList.setCellRenderer(new MessageListRenderer());
		
		messageList.addListSelectionListener(new ListSelectionListener(){

			
			public void valueChanged(ListSelectionEvent arg0) {
				Message message = (Message)messageList.getSelectedValue();
				textPanel.setText(message.getContents());
			}
			
		});
		JScrollPane pane = new JScrollPane(serverTree);
		lowerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,new JScrollPane(messageList),textPanel);
		upperPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,pane,lowerPane);
		
		pane.setMinimumSize(new Dimension(0,100));
		textPanel.setMinimumSize(new Dimension(0,100));
		messageList.setMinimumSize(new Dimension(0,100));
		upperPane.setResizeWeight(0.5);
		lowerPane.setResizeWeight(0.5);
		add(upperPane,BorderLayout.CENTER);
		
	}
	
	public void refresh(){
		retrieveMessages();
	}
	private void retrieveMessages(){
		progressDialog.setMaximum(messageServer.getMessageCount());

				progressDialog.setVisible(true);


		
		worker = new SwingWorker<List<Message>,Integer>() {
				
			@Override
			protected void done() {
				try {
					progressDialog.setVisible(false);
					if(isCancelled()) return;
					List<Message> retrieveMessages = get();
					messageListModel.removeAllElements();
					for(Message message: retrieveMessages){
						messageListModel.addElement(message);
						
					}
					messageList.setSelectedIndex(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				progressDialog.setVisible(false);
			}

			@Override
			protected void process(List<Integer> counts) {
				int retrieved = counts.get(counts.size() - 1);
				
				progressDialog.setValue(retrieved);
		
			}

			@Override
			protected List<Message> doInBackground() throws Exception {
				
				List<Message> retrievedMessages = new ArrayList<Message>();
				
				int count = 0;
				for(Message message: messageServer){
					if(isCancelled()) break;
					System.out.println(message.getTitle());
					
					retrievedMessages.add(message);
					count++;
					publish(count);
				}
				return retrievedMessages;
			}
			
		};
		worker.execute();
		

	}
	
	private DefaultMutableTreeNode createTree(){
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Location");
		
		DefaultMutableTreeNode branch1 = new DefaultMutableTreeNode("USA");
		DefaultMutableTreeNode server1 = new DefaultMutableTreeNode(new ServerInfo("New York",0,selectedServers.contains(0)));
		DefaultMutableTreeNode server2 = new DefaultMutableTreeNode(new ServerInfo("Boston",1,selectedServers.contains(1)));
		DefaultMutableTreeNode server3 = new DefaultMutableTreeNode(new ServerInfo("Los Angeles",2,selectedServers.contains(2)));
		branch1.add(server1);
		branch1.add(server2);
		branch1.add(server3);
		
		DefaultMutableTreeNode branch2 = new DefaultMutableTreeNode("UK");
		DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(new ServerInfo("London",3,selectedServers.contains(3)));
		DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(new ServerInfo("Edinborough",4,selectedServers.contains(4)));
		branch2.add(server4);
		branch2.add(server5);
		top.add(branch1);
		top.add(branch2);
		return top;
	}
	@Override
	public void progressDialogCanceled() {
		if(worker != null) {
			worker.cancel(true);
		}
		
	}


}


