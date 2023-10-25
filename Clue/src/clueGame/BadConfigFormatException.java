package clueGame;
import java.util.Scanner;
import java.io.*;
public class BadConfigFormatException extends Exception{
	public BadConfigFormatException(String specialMessage) {
		super(specialMessage);
		FileWriter file;
		try {
			file = new FileWriter("logfile.txt");
			PrintWriter out = new PrintWriter(file);
			out.print(specialMessage);
			out.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	// Default constructor that throws an error message and then will write to a log file
	public BadConfigFormatException() {
		super("Error bad config format, please fix and try again, if problem continues please send us your logfile");
		FileWriter file;
		try {
			file = new FileWriter("logfile.txt");
			PrintWriter out = new PrintWriter(file);
			out.print("Error bad config format, please fix and try again, if problem continues please send us your logfile");
			out.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
