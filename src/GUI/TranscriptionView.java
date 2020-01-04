package GUI;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.beans.PropertyChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import talktome.AnnotationObject;
import talktome.AnnotationsManager;
import talktome.TranscriptionWord;

public class TranscriptionView extends JPanel {
	
	private JTextArea textArea;
	private ArrayList<TranscriptionWord> words;
	private RecordingInstanceView record;
	private TextEditorView editor;
	private String audioSource;
	
	protected int GetSelectedWord() {
		String selectedText = textArea.getSelectedText().split(" ")[0];	// Selected text, we get the first word
		int selectionStart = textArea.getSelectionStart();	// Offset of the start word
		int selectionEnd = textArea.getSelectionEnd();
		
		String left = "";
		String right = selectedText;
		int wordNumber = 0;
		
		// In case the user select a character of a word, we must figure out the complete word
		try {
			for (selectionStart--; !textArea.getText(selectionStart, 1).equals(" "); selectionStart--) {
				left += textArea.getText(selectionStart, 1);
			}
			
			selectionStart++;
		} catch (BadLocationException e) {
			selectionStart = 0;
		}
		
		try {
			for (; !textArea.getText(selectionEnd, 1).equals(" "); selectionEnd++) {
				right += textArea.getText(selectionEnd, 1);
			}
			
			selectionEnd--;
		} catch (BadLocationException e) {
			selectionEnd = textArea.getText().length();
		}
		
		selectedText = (new StringBuffer(left).reverse().toString()) + right;
		
		
		// Figure out which word number is it.
		try {
			for (int i = 0; i < selectionStart; i++) {
				if (textArea.getText(i, 1).equals(" ")) wordNumber++;
			}
		} catch (BadLocationException e) {}
		
		
		/*System.out.println("Selected text: " + selectedText);
		System.out.println("Selected start: " + selectionStart);
		System.out.println("Selected end: " + selectionEnd);
		System.out.println("Selected Number: " + wordNumber);/**/
		
		return wordNumber;
	}

	
	/**
	 * Create the panel.
	 */
	public TranscriptionView(TextEditorView editor) {
		setLayout(new BorderLayout(0, 0));
		
		this.editor = editor;
		
		JLabel lblTitle = new JLabel("Transcription");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblTitle, BorderLayout.NORTH);
		
		this.textArea = new JTextArea();
		textArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (me.getButton() != MouseEvent.BUTTON3) return;
				
				int selectedNum = GetSelectedWord();
				TranscriptionWord word = words.get(selectedNum);
				
				double timeStart = word.GetStartTime();
				
				// TODO: Return to this point on recording		
				try {
					editor.PlayAudio(audioSource, timeStart);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		
		JScrollPane scrollPanel = new JScrollPane(textArea);
		scrollPanel.setPreferredSize(new Dimension(230, 300));
		add(scrollPanel);		
	}
		
	// TODO: Modify this method to get an Array of TranscriptionWord and set this as text.
	public void LoadTranscription(AnnotationObject annotation) {
		AnnotationsManager manager = AnnotationsManager.getInstance();		
		
		if (this.words != null) this.words.clear();
		this.audioSource = annotation.getAudioFile();
		this.words = manager.GetTranscription(annotation.getTranscriptFile());
		
		String text = "";
		
		for (TranscriptionWord word : this.words) {
			text += word.GetWord() + " ";
		}
		
		this.textArea.setText(text);
		
		try {
			this.editor.PlayAudio(this.audioSource, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
