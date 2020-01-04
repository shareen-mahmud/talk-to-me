package GUI;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import talktome.AnnotationsManager;
import talktome.AnnotationObject;

public class RecordingsView extends JPanel {
	
	private ArrayList<RecordingInstanceView> recordings;
	private JPanel entries;
	private JScrollPane scrollPanel;
	private AnnotationsManager manager;
	private int ENTRY_HEIGHT = 5;
	/**
	 * Create the panel.
	 */
	public RecordingsView(TextEditorView editorText, TranscriptionView transcriptionView) {				
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblTitle = new JLabel("Speech Comments");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblTitle, BorderLayout.NORTH);
		
		this.entries = new JPanel();
		entries.setLayout(new BoxLayout(entries, BoxLayout.Y_AXIS));

		
		
		this.manager = AnnotationsManager.getInstance();
		this.recordings = new ArrayList<RecordingInstanceView>();
		
		int panelHeight = 0;
		try {
			ArrayList<AnnotationObject> annotations = this.manager.getAnnotations(editorText.GetFilename());
			
			for (AnnotationObject a : annotations) {			
				RecordingInstanceView record = new RecordingInstanceView(a, editorText, transcriptionView, this);
				
				this.recordings.add(record);
				this.entries.add(record);
				panelHeight += ENTRY_HEIGHT;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scrollPanel = new JScrollPane(entries);
	    scrollPanel.setPreferredSize(new Dimension(400, panelHeight));
		add(scrollPanel, BorderLayout.CENTER);
		
	}
	
	public void RemoveEntry(RecordingInstanceView record) {		
		this.recordings.remove(record);
		this.entries.remove(record);
		this.scrollPanel.validate();
		this.scrollPanel.repaint();
	}

}
