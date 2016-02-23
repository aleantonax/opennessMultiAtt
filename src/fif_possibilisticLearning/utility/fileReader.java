package fif_possibilisticLearning.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class fileReader implements fileHandler{
	
	private FileReader in;
	private BufferedReader bin;
	public fileReader (String path) throws IOException {
		in=new FileReader(path);
	    bin=new BufferedReader(in); 
	}
	
	@Override
	public ArrayList<String> readFile() throws IOException{
	      String c;
	      ArrayList<String> v = new ArrayList<String>();
	      while((c=bin.readLine()) != null){ 
	    	  v.add(c);
	      }
	      return v;
	}

	@Override
	public void openFile() throws IOException{
		bin=new BufferedReader(in); 
	}
	
	@Override
	public void closeFile() throws IOException{
		bin.close();
	}

	@Override
	public void writeFile(String a, String b, double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeFile(String a) {
		// TODO Auto-generated method stub
		
	}
}
