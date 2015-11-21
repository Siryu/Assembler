package assembler;

import parser.Parser;
import tokenizer.TokenBox;
import tokenizer.Tokenizer;

public class Assembler {

	public static void main(String[] args) {
		// read assembly code from file
			String[] instructions = FileIO.importFile(args[0]);
		// create tokens from assembly code
			TokenBox tokens = Tokenizer.tokenize(instructions);
//			while(tokens.hasNext()) {
//				System.out.println(tokens.next());
//			}
		// parse tokens into ARM code
			byte[] parsedInstructions = Parser.parse(tokens);
		// write kernel7.img 
			//FileIO.writeFile(parsedInstructions, args[1]);
	}
}