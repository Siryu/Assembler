package tokenizer.tokens;

public class Params2Token implements Token{

	private String targetRegister;
	private String hexNumber;
	
	public Params2Token(String targetRegister, String hexValue) {
		this.targetRegister = targetRegister;
		this.hexNumber = hexValue;
	}

	public String getTargetRegister() {
		return this.targetRegister;
	}
	
	public String getHexNumber() {
		return this.hexNumber;
	}
}