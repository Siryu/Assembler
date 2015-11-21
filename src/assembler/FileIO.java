package assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileIO {
	
	// input read from stackoverflow.com/questions/13151714/reading-text-into-a-char-array-in-java
	public static String[] importFile(String fileName) {
		String totalFile = "";
		File file = new File(fileName);
		try {
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				totalFile = totalFile  + scanner.nextLine() + " \\n ";
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("error finding file " + fileName);
			e.printStackTrace();
		}
		return totalFile.split(" ");
	}
	
	// output read from stackoverflow.com/questions/4350084/byte-to-file-in-java
	public static void writeFile(byte[] instructions, String fileLocation) {
		Path path = Paths.get(fileLocation);
		try {
			Files.write(path, instructions);
		} catch (IOException e) {
			System.err.println("unable to write to the specified file " + fileLocation);
			e.printStackTrace();
		}
	}
}