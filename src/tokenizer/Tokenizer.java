package tokenizer;

import tokenizer.tokens.CondToken;
import tokenizer.tokens.DecToken;
import tokenizer.tokens.HexToken;
import tokenizer.tokens.InstructionToken;
import tokenizer.tokens.LabelToken;
import tokenizer.tokens.OperationToken;
import tokenizer.tokens.Params2Token;
import tokenizer.tokens.Params3Token;
import tokenizer.tokens.RegisterToken;
import tokenizer.tokens.Token;

public class Tokenizer {
	
	public enum State {
		COMMAND, PARAMS;
	}
	
	private static State currentState = State.COMMAND; 
	private static TokenBox tokenBox;
	
	public static TokenBox tokenize(String[] instructions) {
		tokenBox = new TokenBox();
		for(String piece : instructions) {
			piece = piece.toUpperCase();
			switch(currentState) {
				case COMMAND: {
					if(piece.endsWith(":")) {
						// LABEL
						// remove the :
						// create LabelToken
						piece = piece.replace(":", "");
						tokenBox.pushOntoBack(new LabelToken(piece));
					}
					else if(piece.startsWith("B")) {
						// BRANCH
						// remove the b
						piece = piece.replace("B", "");
						// create BranchToken
						tokenBox.pushOntoBack(new OperationToken("B"));
					}
					else if(piece.startsWith("MOVW")) {
						tokenBox.pushOntoBack(new OperationToken("MOVW"));
						piece = piece.replace("MOVW", "");
					}
					else if(piece.startsWith("MOVT")) {
						tokenBox.pushOntoBack(new OperationToken("MOVT"));
						piece = piece.replace("MOVT", "");
					}
					else if(piece.startsWith("LDR")) {
						tokenBox.pushOntoBack(new OperationToken("LDR"));
						piece = piece.replace("LDR", "");
					}
					else if(piece.startsWith("STR")) {
						tokenBox.pushOntoBack(new OperationToken("STR"));
						piece = piece.replace("STR", "");
					}
					else if(piece.startsWith("ADD")) {
						tokenBox.pushOntoBack(new OperationToken("ADD"));
						piece = piece.replace("ADD", "");
					}
					else if(piece.startsWith("ORR")) {
						tokenBox.pushOntoBack(new OperationToken("ORR"));
						piece.replace("ORR", "");
					}
					else if(piece.startsWith("SUB")) {
						tokenBox.pushOntoBack(new OperationToken("SUB"));
						piece.replace("SUB", "");
					}
					checkForFlag(piece);
					tryGetOpCode(piece);
					currentState = State.PARAMS;
					break;
				}
				
				case PARAMS: {
					if(piece.startsWith("R")) {
						// register
						piece = piece.replace(",", "");
						piece = piece.replace("R", "");
						tokenBox.pushOntoBack(new RegisterToken(piece));
					}
					else if(piece.startsWith("0X")) {
						// hex number
						piece = piece.replace("0X", "");
						tokenBox.pushOntoBack(new HexToken(piece));
					}
					else if(piece.matches("-*\\d+")) {
						tokenBox.pushOntoBack(new DecToken(piece));
					}
					else if(piece.contains("\\N")) {
						// end the instruction
						// break down token box to create the instructionToken
						tryCreateInstruction();
						currentState = State.COMMAND;
					}
					break;
				}
			}
		}
		return tokenBox;
	}

	private static void tryGetOpCode(String piece) {
	
		if(piece.contains("EQ")) {
			// equal
			tokenBox.pushOntoBack(new CondToken("EQ"));
		}
		else if(piece.contains("NE")) {
			// not equal
			tokenBox.pushOntoBack(new CondToken("NE"));
		}
		else {
			// Always
			tokenBox.pushOntoBack(new CondToken("AL"));
		}
	}
	
	private static void checkForFlag(String piece) {
		if(piece.startsWith("S")) {
			OperationToken op = (OperationToken)tokenBox.getFromBack();
			op.addFlag("S");
			tokenBox.pushOntoBack(op);
		}
	}
	
	private static void tryCreateInstruction() {
		// reverse back to null or back to instruction
		Token lastToken = tokenBox.getFromBack();
		if(lastToken.getClass().equals(HexToken.class)) {
			Token reg2 = tokenBox.getFromBack();
			if(reg2.getClass().equals(RegisterToken.class)) {
				Token reg1 = tokenBox.getFromBack();
				if(reg1.getClass().equals(RegisterToken.class)) {
					// params3
					RegisterToken targetR = (RegisterToken)reg2;
					RegisterToken sourceR = (RegisterToken)reg1;
					HexToken offset = (HexToken)lastToken;
					tokenBox.pushOntoBack(new Params3Token(targetR.getValue(), sourceR.getValue(), offset.getValue()));
				}
				else {
					// params2
					tokenBox.pushOntoBack(reg1);
					RegisterToken register = (RegisterToken)reg2;
					HexToken offset = (HexToken)lastToken;
					tokenBox.pushOntoBack(new Params2Token(register.getValue(), offset.getValue()));
				}
			}
		}
		else if(lastToken.getClass().equals(RegisterToken.class)) {
			RegisterToken targetR = (RegisterToken)tokenBox.getFromBack();
			RegisterToken sourceR = (RegisterToken)lastToken;
			tokenBox.pushOntoBack(new Params3Token(targetR.getValue(), sourceR.getValue(), "0"));
		}
		else if(lastToken.getClass().equals(DecToken.class)) {
			tokenBox.pushOntoBack(lastToken);
		}
		
		Token params = tokenBox.getFromBack();
		Token condition = tokenBox.getFromBack();
		Token opCode = tokenBox.getFromBack();
		Token optionalLabel = tokenBox.getFromBack();
		if(null != optionalLabel && optionalLabel.getClass().equals(LabelToken.class)) {
			tokenBox.pushOntoBack(new InstructionToken(optionalLabel, opCode, condition, params));
		}
		else if(null == optionalLabel) {
				tokenBox.pushOntoBack(new InstructionToken(null, opCode, condition, params));
			}
		else if(InstructionToken.class.equals(optionalLabel.getClass())) {
			tokenBox.pushOntoBack(optionalLabel);
			tokenBox.pushOntoBack(new InstructionToken(null, opCode, condition, params));
		}
	}
}