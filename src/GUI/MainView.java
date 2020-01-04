package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.TreeSelectionEvent;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainView extends JFrame {

	private JPanel contentPane;
	private ArrayList<TextEditorView> openedEditors;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView frame = new MainView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainView() {		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1366, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel topPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) topPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		JButton btnOpen = new JButton("Open");
		topPanel.add(btnOpen);
		
		JLabel lblEclipsefaketoolbar = new JLabel("");
		topPanel.add(lblEclipsefaketoolbar);
		lblEclipsefaketoolbar.setIcon(new ImageIcon(MainView.class.getResource("/assets/eclipseToolBar.png")));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		this.openedEditors = new ArrayList<TextEditorView>();

		// TODO: Do it but when open project. Do not hardcode
		//DefaultMutableTreeNode root = CreateTreeModel(new File(System.getProperty("user.dir")));
		
		JTree tree = new JTree();
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode(".") {
				{
				}
			}
		));
		tree.setRootVisible(false);
		JScrollPane scrollPanel = new JScrollPane(tree);
		scrollPanel.setPreferredSize(new Dimension(175, 300));
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanel, tabbedPane);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		btnOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				
				if (fc.showOpenDialog(MainView.this) == JFileChooser.APPROVE_OPTION) {
					 File file = fc.getSelectedFile();
					 DefaultMutableTreeNode root = CreateTreeModel(file);
					 DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
					 
					 model.setRoot(root);	
					 tree.setRootVisible(true);
				}
			}
		});
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent selection) {
				Object object = tree.getLastSelectedPathComponent();
				
				if (object instanceof DefaultMutableTreeNode) {
					Object userObject = ((DefaultMutableTreeNode) object).getUserObject();
					
					if (userObject instanceof File) {
						File file = (File) userObject;
						
						if (!file.isDirectory()) {
							TextEditorView editor = SearchEditor(file.getName());							
								
							if (editor == null) {
								editor = new TextEditorView(file);
								tabbedPane.addTab(editor.GetFilename(), null, editor, null);
								openedEditors.add(editor);
							} else {
								tabbedPane.setSelectedComponent(editor);
							}
						}
					}
				}
			}
		});
	}
	
	protected void OpenProject() {
		
	}
	
	protected DefaultMutableTreeNode CreateTreeModel(File file) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
		
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				node.add(CreateTreeModel(f));
			}
		}
		
		return node;
	}
	
	protected TextEditorView SearchEditor(String name) {
		for (TextEditorView editor : this.openedEditors) {
			if (editor.GetFilename().equals(name)) return editor;
		}
		
		return null;
	}

}
