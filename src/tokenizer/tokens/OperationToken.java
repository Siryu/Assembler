package tokenizer.tokens;

public class OperationToken implements Token {

	private String value;
	private String flag;
	
	public OperationToken(String piece) {
		this.value = piece;
	}

	public void addFlag(String string) {
		this.flag = string;
	}
	
	public String getValue() {
		return this.value;
	}

	public String getFlag() {
		return this.flag;
	}
}
