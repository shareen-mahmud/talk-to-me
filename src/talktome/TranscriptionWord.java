package talktome;

public class TranscriptionWord {
	
	private String word;
	private double timeStart;
	private double timeEnd;
	
	public TranscriptionWord(String word, double timeStart, double timeEnd)
	{
		this.word = word;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
	}
	
	public String GetWord()
	{
		return this.word;
	}
	
	public double GetStartTime()
	{
		return this.timeStart;
	}
	
	public double GetEndTime()
	{
		return this.timeEnd;
	}

}
