package fif_possibilisticLearning.utility;

import java.io.*;
import java.util.ArrayList;

public class fileWriter implements fileHandler {
	private FileWriter out;
	private PrintWriter outPrint;
	private BufferedWriter bout;

	public fileWriter(String path) throws IOException {
		out = new FileWriter(path);
		outPrint = new PrintWriter(out);
	}

	@Override
	public void openFile() {
		bout = new BufferedWriter(out);
	}

	@Override
	public void writeFile(String a, String b, double value) {
		outPrint.println(a + " | " + b + " | " + value);
	}

	@Override
	public void writeFile(String a) {
		outPrint.println(a);
	}

	@Override
	public void closeFile() throws IOException {
		bout.close();
	}

	@Override
	public ArrayList<String> readFile() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
