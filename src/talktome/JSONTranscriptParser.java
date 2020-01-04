package talktome;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONTranscriptParser {
	
	public ArrayList<TranscriptionWord> GetTranscriptionWords(String filename)
	{
		ArrayList<TranscriptionWord> transcriptionWordsList = new ArrayList<>();
		
		try {
			Object mObj = new JSONParser().parse(new FileReader(filename));
			
			JSONObject jMObj = (JSONObject) mObj;
			
			JSONArray resultsArray = (JSONArray) jMObj.get("results");
			Iterator resIterator = resultsArray.iterator();
			
			while(resIterator.hasNext())
			{
				JSONObject jIn = (JSONObject) resIterator.next();
				
				JSONArray altArray = (JSONArray) jIn.get("alternatives");
				
				JSONObject altObj = (JSONObject) altArray.get(0);
				JSONArray timeArray = (JSONArray) altObj.get("timestamps");
				Iterator timeIterator = timeArray.iterator();
				while(timeIterator.hasNext())
				{
					JSONArray eachWord = (JSONArray) timeIterator.next();
					System.out.println(eachWord.get(0) + " " + eachWord.get(1) + " " + eachWord.get(2));
					transcriptionWordsList.add(new TranscriptionWord((String) eachWord.get(0), (double) eachWord.get(1), (double) eachWord.get(2)));
				}
				
			}
			
			return transcriptionWordsList;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String GetFullTranscript(String filename)
	{
		StringBuilder transcriptBuider = new StringBuilder();
		try {
			Object mObj = new JSONParser().parse(new FileReader("D:/Test/transcript.json"));
			
			JSONObject jMObj = (JSONObject) mObj;
			
			JSONArray resultsArray = (JSONArray) jMObj.get("results");
			Iterator resIterator = resultsArray.iterator();
			while(resIterator.hasNext())
			{
				JSONObject jIn = (JSONObject) resIterator.next();
				
				JSONArray altArray = (JSONArray) jIn.get("alternatives");
				
				JSONObject altObj = (JSONObject) altArray.get(0);
				String uncorrectedStr = altObj.get("transcript").toString().trim();
				String correctedStr = Character.toUpperCase(uncorrectedStr.charAt(0)) + uncorrectedStr.substring(1);
				transcriptBuider.append(correctedStr);
				transcriptBuider.append(". ");
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return transcriptBuider.toString();
	}
	
	public static void JSONExecute()
	{
		try {
			Object mObj = new JSONParser().parse(new FileReader("D:/Test/transcript.json"));
			
			JSONObject jMObj = (JSONObject) mObj;
			
			JSONArray resultsArray = (JSONArray) jMObj.get("results");
			Iterator resIterator = resultsArray.iterator();
			StringBuilder sbTranscript = new StringBuilder();
			while(resIterator.hasNext())
			{
				JSONObject jIn = (JSONObject) resIterator.next();
				
				JSONArray altArray = (JSONArray) jIn.get("alternatives");
				
				JSONObject altObj = (JSONObject) altArray.get(0);
				sbTranscript.append((String)altObj.get("transcript"));
				sbTranscript.append(". ");
				//System.out.println((String)altObj.get("transcript"));
				
				JSONArray timeArray = (JSONArray) altObj.get("timestamps");
				Iterator timeIterator = timeArray.iterator();
				while(timeIterator.hasNext())
				{
					JSONArray eachWord = (JSONArray) timeIterator.next();
					System.out.println(eachWord.get(0) + " " + eachWord.get(1) + " " + eachWord.get(2));
				}
				
			}
			System.out.println("");
			System.out.println(sbTranscript.toString());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
