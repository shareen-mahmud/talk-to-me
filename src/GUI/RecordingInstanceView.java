package GUI;

import javax.swing.JPanel;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.Color;
import javax.swing.UIManager;

import talktome.AnnotationsManager;
import talktome.TranscriptionWord;
import talktome.AnnotationObject;

public class RecordingInstanceView extends JPanel {
	private TextEditorView editor;
	private TranscriptionView transcriptionView;
	private RecordingsView recordingsView;
	private AnnotationObject annotation;

	/**
	 * Create the panel.
	 */
	public RecordingInstanceView(AnnotationObject annotation, TextEditorView editor, TranscriptionView transcriptionView, RecordingsView recordingsView) {
		setLayout(new BorderLayout(0, 0));
		
		this.transcriptionView = transcriptionView;
		this.editor = editor;
		this.recordingsView = recordingsView;
		this.annotation = annotation;
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout(0, 0));
		
		JButton btnPlay = new JButton("Play");
		panel.add(btnPlay, BorderLayout.WEST);
		
		JButton btnRemove = new JButton("Remove");
		panel.add(btnRemove, BorderLayout.EAST);
		
		String lineLabelText = (annotation.getLineStart() == annotation.getLineEnd()) 
									? "  " + Integer.toString(annotation.getLineStart()) 
									: "  " + annotation.getLineStart() + " - " + annotation.getLineEnd();		
		
		JLabel lblLines = new JLabel(lineLabelText);
		add(lblLines, BorderLayout.WEST);
		
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				LoadTranscription();
			}
		});
		
		btnRemove.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DeleteEntry();
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setBackground(Color.YELLOW);			
				validate();
				repaint();
				editor.HighlightLines(annotation.getLineStart(), annotation.getLineEnd());
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setBackground(UIManager.getColor("Panel.background"));
				validate();
				repaint();
				editor.DeleteHighlight(annotation.getLineStart(), annotation.getLineEnd());
			}
		});

	}
	
	protected void DeleteEntry() {
		this.recordingsView.RemoveEntry(this);	
	}

	protected void LoadTranscription() {	
		this.transcriptionView.LoadTranscription(this.annotation);
	}

}
