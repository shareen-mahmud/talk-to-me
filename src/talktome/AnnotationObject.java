package talktome;

public class AnnotationObject {
	
	private String audioFile;
	private String transcriptFile;
	private int lineStart;
	private int lineEnd;
	
	
	public AnnotationObject(String audioFile, String transcriptFile, int lineStart, int lineEnd) {
		super();
		this.audioFile = audioFile;
		this.transcriptFile = transcriptFile;
		this.lineStart = lineStart;
		this.lineEnd = lineEnd;
	}


	public String getAudioFile() {
		return audioFile;
	}


	public String getTranscriptFile() {
		return transcriptFile;
	}


	public int getLineStart() {
		return lineStart;
	}


	public int getLineEnd() {
		return lineEnd;
	}
	
	
}
