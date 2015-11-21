package parser;

import java.util.HashMap;
import java.util.Map;

import tokenizer.TokenBox;
import tokenizer.tokens.CondToken;
import tokenizer.tokens.InstructionToken;
import tokenizer.tokens.LabelToken;
import tokenizer.tokens.OperationToken;
import tokenizer.tokens.Params2Token;
import tokenizer.tokens.Params3Token;
import tokenizer.tokens.RegisterToken;
import tokenizer.tokens.Token;

public class Parser {

	public static byte[] parse(TokenBox tokens) {
		Map<String, Integer> labelTable = new HashMap<String, Integer>();
		int counter = 0;
		// not reporting hasnext correctly
		while(tokens.hasNext()) {
			counter++;
			InstructionToken token = (InstructionToken)tokens.next();
			if(null != token.getLabel()) {
				LabelToken label = (LabelToken)token.getLabel();
				labelTable.put(label.getValue(), counter);
			}
			OperationToken opToken = (OperationToken)token.getOperation();
			int instruction;
			switch(opToken.getValue()) {
				case "MOVW": {
					instruction = createMovw(token);
					break;
				}
				case "MOVT": {
					instruction = createMovt(token);
					break;
				}
				case "ADD": {
					instruction = createAdd(token);
					break;
				}
				case "SUB": {
					instruction = createSub(token);
					break;
				}
			}
		}
		return null;
	}
	
	private static int getConditional(Token conditional) {
		int condValue = 0;
		CondToken cond = (CondToken)conditional;
		switch(cond.getValue()) {
			case "AL": {
				condValue = 0xE0000000;
				break;
			}
			case "EQ": {
				condValue = 0x0;
				break;
			}
			case "NE": {
				condValue = 0x10000000;
				break;
			}
		}
		return condValue;
	}
	
	private static int createSub(InstructionToken token) {
		int conditionalValue = getConditional(token.getConditional());
		int firstByte = conditionalValue | 0x02000000;
		OperationToken opToken = (OperationToken)token.getOperation();
		int secondByte = 0;
		if(null != opToken.getFlag() && opToken.getFlag().equals("S")) {
			secondByte = firstByte | 0x00500000;
		}
		else {
			secondByte = firstByte | 0x00400000;
		}
		Params3Token paramsToken = (Params3Token)token.getParams();
		int sReg = Integer.parseInt(paramsToken.getSourceRegister());
		secondByte = secondByte | sReg * 0x00010000;
		int tReg = Integer.parseInt(paramsToken.getTargetRegister());
		int thirdByte = secondByte | tReg * 0x00001000;
		int param = Integer.parseInt(paramsToken.getHexNumber(), 16);
		int machineCode = thirdByte | param;
		return machineCode;
	}

	private static int createAdd(InstructionToken token) {
		// add r5, r1, 0x10
		int conditionalValue = getConditional(token.getConditional());
		int firstByte = conditionalValue | 0x02000000;
		OperationToken opToken = (OperationToken)token.getOperation();
		int secondByte = 0;
		if(null != opToken.getFlag() && opToken.getFlag().equals("S")) {
			secondByte = firstByte | 0x00900000;
		}
		else {
			secondByte = firstByte | 0x00800000;
		}
		Params3Token paramsToken = (Params3Token)token.getParams();
		int sReg = Integer.parseInt(paramsToken.getSourceRegister());
		secondByte = secondByte | sReg * 0x00010000;
		
		int tReg = Integer.parseInt(paramsToken.getTargetRegister());
		int thirdByte = secondByte | tReg * 0x00001000;
		int param = Integer.parseInt(paramsToken.getHexNumber(), 16);
		int machineCode = thirdByte | param;
		// e285100a converts this from above code.
		return machineCode;
	}

	private static int createMovt(InstructionToken token) {
		int conditionalValue = getConditional(token.getConditional());
		int firstByte = conditionalValue | 0x03000000;
		int secondByte = firstByte | 4 * 0x00100000;
		Params2Token params = (Params2Token)token.getParams();
		int moveValue = Integer.parseInt(params.getHexNumber(), 16);
		int shiftedForImm4 = (moveValue >>> 28);
		secondByte = secondByte | shiftedForImm4 * 0x00010000;
		int targetR = Integer.parseInt(params.getTargetRegister());
		int thirdByte = secondByte | targetR * 0x00001000;
		int shiftedToGetValue = (moveValue << 4);
		int shiftedForImm12 = (shiftedToGetValue >>> 20);
		int machineCode = thirdByte | shiftedForImm12 * 0x00000001;
		System.out.println(Integer.toHexString(machineCode));
		return machineCode;
	}

	private static int createMovw(InstructionToken token) {
		// 4bit cond, 4bit 0011, 4 bit 0000, imm4, Rd, imm12
		int conditionalValue = getConditional(token.getConditional());
		int firstByte = conditionalValue | 0x03000000;
		int secondByte = firstByte | 0 * 0x00100000;
		Params2Token params = (Params2Token)token.getParams();
		int moveValue = Integer.parseInt(params.getHexNumber(), 16);
		int shiftedForImm4 = (moveValue >>> 12);
		secondByte = secondByte | shiftedForImm4 * 0x00010000;
		int targetR = Integer.parseInt(params.getTargetRegister());
		int thirdByte = secondByte | targetR * 0x00001000;
		int shiftedToGetValue = (moveValue << 20);
		int shiftedForImm12 = (shiftedToGetValue >>> 20);
		int machineCode = thirdByte | shiftedForImm12 * 0x00000001;
		return machineCode;
	}
}