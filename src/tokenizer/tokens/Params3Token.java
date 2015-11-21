package tokenizer.tokens;

public class Params3Token implements Token {

	private String targetRegister;  
	private String sourceRegitster;
	private String hexNumber;
	
	public Params3Token(String target, String source, String number) {
		this.targetRegister = target;
		this.sourceRegitster = source;
		this.hexNumber = number;
	}

	public String getTargetRegister() {
		return this.targetRegister;
	}
	
	public String getSourceRegister() {
		return this.sourceRegitster;
	}
	
	public String getHexNumber() {
		return this.hexNumber;
	}
}