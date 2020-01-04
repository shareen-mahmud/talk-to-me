package talktome;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AnnotationsManager {

	private static AnnotationsManager manager = new AnnotationsManager();
	private static Connection conn;
	private JSONTranscriptParser jtp;
	
	private AnnotationsManager() {
		this.jtp = new JSONTranscriptParser();
	}
	
	public static AnnotationsManager getInstance( ) {
	      return manager;
   }
	
	public static void getConnection() 
	{
        try 
        {
            // db parameters
            String url = "jdbc:sqlite:cpsc507.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }
	
	public static void closeConnection()
	{
		try
		{
			conn.close();
		}
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	public ArrayList<AnnotationObject> getAnnotations(String jFileName) throws SQLException, ClassNotFoundException
	{
		getConnection();
		String sql = "SELECT * FROM filetable WHERE jfile='" + jFileName + "'";
		ArrayList<AnnotationObject> annotationList = new ArrayList<AnnotationObject>();
        int fid = -1;
        try (
             Statement fileStmt  = conn.createStatement();
             ResultSet rs    = fileStmt.executeQuery(sql)){
        	 while (rs.next()) 
        	 {    
        		 System.out.println(rs.getInt("id") + " " + rs.getString("jfile"));
        		 fid = rs.getInt("id");
        	 }
        	 
        	 sql = "SELECT * FROM annotationtable WHERE fid=" + fid;
        	 Statement annotationStmt = conn.createStatement();
        	 ResultSet annotationResults = annotationStmt.executeQuery(sql);
        	 while(annotationResults.next())
        	 {
        		 annotationList.add(new AnnotationObject(annotationResults.getString("audiofile"), annotationResults.getString("transcriptfile"), annotationResults.getInt("lineStart"), annotationResults.getInt("lineEnd")));
        		 //System.out.println(annotationResults.getString("audiofile") + " " + annotationResults.getString("transcriptfile") + " " + annotationResults.getInt("lineStart") + " " + annotationResults.getInt("lineEnd"));
        	 }
        	 //closeConnection();
        	 return annotationList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection();
        return null;
	}

	public ArrayList<TranscriptionWord> GetTranscription(String transcriptFile) {
		return this.jtp.GetTranscriptionWords(transcriptFile);
	}
}
