package GUI;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultHighlighter;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.misc.IOUtils;

import javax.swing.JSplitPane;

import talktome.PlayAudio;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

public class TextEditorView extends JPanel {
	
	private File file;
	private TranscriptionView transcription;
	private RecordingsView recordings;
	private  DefaultHighlighter highlighter;
	private JTextArea textArea;
	private JSplitPane splitPaneRight;
	private JSplitPane splitPaneMain;
	private PlayAudio audio;
	private JPanel audioPanel;
	private JPanel panel_1;
	private JButton btnPlay;
	private JButton btnStop;
	
	private Boolean playingAudio;
	RSyntaxTextArea rtextArea;
	
	/**
	 * Create the panel.
	 */
	public TextEditorView(File file) {
		this.file = file;
		this.playingAudio = false;
		
		setLayout(new BorderLayout(0, 0));
		
		audioPanel = new JPanel();
		add(audioPanel, BorderLayout.NORTH);
		audioPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblInformationlabel = new JLabel("Playing audio...");
		audioPanel.add(lblInformationlabel, BorderLayout.CENTER);
		
		panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		audioPanel.add(panel_1, BorderLayout.EAST);
		
		btnPlay = new JButton("Play");
		panel_1.add(btnPlay);
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (playingAudio) {
					audio.pause();
					playingAudio = false;
				} else {
					try {
						audio.resumeAudio();
						playingAudio = true;
					} catch (UnsupportedAudioFileException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		btnStop = new JButton("Stop");
		btnStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				StopAudio();
			}
		});
		panel_1.add(btnStop);
		/*
		textArea = new JTextArea();
		textArea.setTabSize(3);
		JScrollPane scrollPane = new JScrollPane(textArea);
		TextLineNumber tln = new TextLineNumber(textArea);
		scrollPane.setRowHeaderView(tln);
		*/
		
		rtextArea = new RSyntaxTextArea();
		rtextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		rtextArea.setCodeFoldingEnabled(true);
		RTextScrollPane sp = new RTextScrollPane(rtextArea);
		
		
		/**/
		
		try {
			String fileContent = new String(Files.readAllBytes(Paths.get(this.file.getAbsolutePath())));
			this.rtextArea.setText(fileContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.highlighter = (DefaultHighlighter) rtextArea.getHighlighter();
		this.highlighter.setDrawsLayeredHighlights(false);
		
		this.transcription = new TranscriptionView(this);
		this.recordings = new RecordingsView(this, transcription);

		splitPaneRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT, recordings, transcription);		
		splitPaneMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, splitPaneRight);
		add(splitPaneMain, BorderLayout.CENTER);
				
		splitPaneMain.setResizeWeight(1.0);
		splitPaneRight.setResizeWeight(1.0);
	}
	
	public String GetFilename() {
		return this.file.getName();
	}

	public void HighlightLines(int lineStart, int lineEnd) {
        DefaultHighlighter.DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
        
        try {
        	int start = this.rtextArea.getLineStartOffset(lineStart-1);
            int end = this.rtextArea.getLineEndOffset(lineEnd-1);
            this.highlighter.addHighlight(start, end, painter);
        } catch(Exception e) {
        	System.out.println(e);
        }
		
	}

	public void DeleteHighlight(int lineStart, int lineEnd) {
		DefaultHighlighter.DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.WHITE);

        try {
        	int start = this.rtextArea.getLineStartOffset(lineStart-1);
            int end = this.rtextArea.getLineEndOffset(lineEnd-1);
            this.highlighter.addHighlight(start, end, painter);
        } catch(Exception e) {
        	System.out.println(e);
        }
		
	}

	public void PlayAudio(String filename, double start) throws IOException {
		StopAudio();
		
		try {
			this.audio = new PlayAudio(filename);
			this.audio.jump(start);
			playingAudio = true;
			this.audioPanel.setVisible(true);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void StopAudio() {
		if (this.audio == null) return;
		
		try {
			this.audio.stop();
			playingAudio = false;
			this.audioPanel.setVisible(false);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
