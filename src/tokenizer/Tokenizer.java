package tokenizer;

import java.util.ArrayList;

import tokenizer.tokens.AlphaToken;
import tokenizer.tokens.CondToken;
import tokenizer.tokens.DataProcToken;
import tokenizer.tokens.DigitToken;
import tokenizer.tokens.HexDigitToken;
import tokenizer.tokens.LabelToken;
import tokenizer.tokens.LdrStrToken;
import tokenizer.tokens.NameToken;
import tokenizer.tokens.RegisterToken;
import tokenizer.tokens.Token;

public class Tokenizer {
	private static State currentState = State.A;	
	
	public enum State {
		A, B, C, D, E, F, G, H;
	}
	
	public static TokenBox tokenize(char[] instructions) {
		int counter = 0;
		TokenBox tokens = new TokenBox();
		while(counter < instructions.length) {
			char currentPart = instructions[counter++];
			switch(currentState) {
				case A: {
					if(currentPart == '0') {
						tokens.pushOntoBack(new DigitToken(currentPart));
						currentState = State.C;
					}
					else if(currentPart == 'm' || currentPart == 'M' || currentPart == 'a' || currentPart == 'A' || 
							currentPart == 'l' || currentPart == 'L' || currentPart == 'o' || currentPart == 'O' ||
							currentPart == 's' || currentPart == 'S' || currentPart == 'r' || currentPart == 'R') {
						tokens.pushOntoBack(new AlphaToken(currentPart));
						currentState = State.B;
					}
					else if(currentPart == 'b' || currentPart == 'B') {
						// branch
						tokens.push(new AlphaToken(currentPart));
						currentState = State.E;
					}
					break;
				}
				case B: {
					if(currentPart == ':') {
						// determine what line this label is on tooo!!!!!!!!!!!!!!!!!!!!!!
						String labelName = "";
						Token lastToken = tokens.getFromBack();
						if(lastToken.getClass().equals(AlphaToken.class)) {
							AlphaToken at = (AlphaToken)lastToken;
							labelName = labelName + at.getValue();
						}
						else {
							tokens.pushOntoBack(lastToken);
						}
						labelName = new StringBuilder(labelName).reverse().toString();
						tokens.pushOntoBack(new LabelToken(labelName));
						currentState = State.A;
					}
					else if(currentPart == ' ') {
						boolean done = false;
						ArrayList<Character> chars = new ArrayList<Character>();
						while(!done) {
							Token lastChar = tokens.getFromBack();
							if(lastChar.getClass().equals(AlphaToken.class)) {
								chars.add(((AlphaToken)lastChar).getValue());
							}
							else {
								tokens.pushOntoBack(lastChar);
								done = true;
							}
						}
						StringBuilder sb = new StringBuilder();
						for(int i = chars.size() - 1; i >= 0; i--) {
							sb.append(chars.get(i));
						}
						if(sb.toString().contains("[ls][dt]r")){
							tokens.pushOntoBack(new LdrStrToken(sb.toString()));
						}
						else {
							tokens.pushOntoBack(new DataProcToken(sb.toString()));
						}
					}
					else if(isDigit(currentPart)) {
						tokens.pushOntoBack(new DigitToken(currentPart));
					}
					else if(currentPart == ',') {
						Token secondNumber = tokens.getFromBack();
						Token firstNumber = tokens.getFromBack();
						if(firstNumber.getClass().equals(DigitToken.class)) {
							tokens.getFromBack();
							DigitToken num1 = (DigitToken)firstNumber;
							DigitToken num2 = (DigitToken)secondNumber;
							String num = num1.getValue() + "" + num2.getValue();
							tokens.pushOntoBack(new RegisterToken(num));
						}
						else {
							tokens.pushOntoBack(new RegisterToken(((DigitToken)secondNumber).getValue() + ""));
						}
						currentState = State.A;
					}
					else {
						tokens.pushOntoBack(new AlphaToken(currentPart));
					}
					break;
				}
				case C: {
					if(currentPart == 'x' || currentPart == 'X') {
						tokens.pushOntoBack(new AlphaToken(currentPart));
						currentState = State.D;
					}
					break;
				}
				case D: {
					if(isDigit(currentPart)) {
						tokens.pushOntoBack(new DigitToken(currentPart));
					}
					else if(currentPart == 'a' || currentPart == 'A' || currentPart == 'b' || currentPart == 'B' ||
							currentPart == 'c' || currentPart == 'C' || currentPart == 'd' || currentPart == 'D' ||
							currentPart == 'e' || currentPart == 'E' || currentPart == 'f' || currentPart == 'F') {
						tokens.pushOntoBack(new HexDigitToken(currentPart));
					}
					else {
						// go back through and convert to a HexToken
						currentState = State.A;
					}
					break;
				}
				case E: {
					if(currentPart == 'E' || currentPart == 'e' || currentPart == 'N' || currentPart == 'n' ||
					   currentPart == 'a' || currentPart == 'A' || currentPart == 'q' || currentPart == 'Q' || 
					   currentPart == 'l' || currentPart == 'L') {
						tokens.pushOntoBack(new AlphaToken(currentPart));
					}
					else if(currentPart == ' '){
						AlphaToken secondLetter = (AlphaToken)tokens.getFromBack();
						if(secondLetter.getValue() == 'b' || secondLetter.getValue() == 'B') {
							tokens.pushOntoBack(secondLetter);
							tokens.pushOntoBack(new CondToken("AL"));
						}
						else {
							AlphaToken firstLetter = (AlphaToken)tokens.getFromBack();
							tokens.pushOntoBack(new CondToken(firstLetter.getValue() + "" + secondLetter.getValue()));
						}
						currentState = State.F;
					}
				}
				case F: {
					if(currentPart != '\\' && currentPart != ' ') {
						tokens.pushOntoBack(new AlphaToken(currentPart));
					}
					else {
						boolean done = false;
						ArrayList<Character> nameChars = new ArrayList<Character>();
						while(!done) {
							Token lastChar = tokens.getFromBack();
							if(lastChar.getClass().equals(AlphaToken.class)) {
								nameChars.add(((AlphaToken)lastChar).getValue());
							}
							else {
								tokens.pushOntoBack(lastChar);
								done = true;
							}
						}
						StringBuilder sb = new StringBuilder();
						for(int i = nameChars.size() - 1; i >= 0; i--) {
							sb.append(nameChars.get(i));
						}
						tokens.pushOntoBack(new NameToken(sb.toString()));
					}
				}
				default: {
					break;
				}
			}
		}
	}
	
	private static boolean isDigit(char currentPart) {
		boolean isDigit = false;
		if(currentPart == '0' || currentPart == '1' || currentPart == '2' || currentPart == '3' || 
		   currentPart == '4' || currentPart == '5' || currentPart == '6' || currentPart == '7' || 
		   currentPart == '8' || currentPart == '9') {
			isDigit = true;
		}
		return isDigit;
	}
}