package tokenizer.tokens;

public class LdrStrToken implements Token {

	private String value;
	
	public LdrStrToken(String string) {
		this.value = string;
	}

	public String getValue() {
		return this.value;
	}
}
