package fif_possibilisticLearning.utility;

import java.io.IOException;
import java.util.ArrayList;

public interface fileHandler {

	public void openFile() throws IOException;
	public void closeFile() throws IOException;
	public void writeFile(String a, String b, double value);
	public ArrayList<String> readFile() throws IOException;
	void writeFile(String a);
}
