package tokenizer.tokens;

public class InstructionToken implements Token {
	private Token label;
	private Token opCode;
	private Token cond;
	private Token params;
	
	public InstructionToken(Token label, Token opCode, Token condition, Token params) {
		this.label = label;
		this.opCode = opCode;
		this.cond = condition;
		this.params = params;
	}

	public Token getLabel() {
		return this.label;
	}
	
	public Token getOperation() {
		return this.opCode;
	}
	
	public Token getConditional() {
		return this.cond;
	}
	
	public Token getParams() {
		return this.params;
	}
	
	@Override
	public String toString() {
		OperationToken op = (OperationToken)this.opCode;
		return op.getValue();
	}
}
