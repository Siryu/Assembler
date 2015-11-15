package tokenizer;

import tokenizer.tokens.AlphaToken;
import tokenizer.tokens.DigitToken;
import tokenizer.tokens.HexDigitToken;
import tokenizer.tokens.LabelToken;
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
		while(counter++ < instructions.length) {
			char currentPart = instructions[counter];
			switch(currentState) {
				case A: {
					if(currentPart == '0') {
						tokens.pushOntoBack(new DigitToken(currentPart));
						currentState = State.C;
					}
					else if(currentPart == 'm' || currentPart == 'M' || currentPart == 'a' || currentPart == 'A' || 
							currentPart == 'l' || currentPart == 'L' || currentPart == 'o' || currentPart == 'O' ||
							currentPart == 's' || currentPart == 'S' || currentPart == 'b' || currentPart == 'B' || 
							currentPart == 'r' || currentPart == 'R') {
						tokens.pushOntoBack(new AlphaToken(currentPart));
						currentState = State.B;
					}
					break;
				}
				case B: {
					if(currentPart == ':') {
						// determine what line this label is on tooo!!!!!!!!!!!!!!!!!!!!!!
						String labelName = "";
						Token lastToken = tokens.next();
						if(lastToken.getClass().equals(AlphaToken.class)) {
							AlphaToken at = (AlphaToken)lastToken;
							labelName = labelName + at.getValue();
						}
						else {
							tokens.push(lastToken);
						}
						labelName = new StringBuilder(labelName).reverse().toString();
						tokens.pushOntoBack(new LabelToken(labelName));
						currentState = State.A;
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