package tokenizer.tokens;

public class DecToken implements Token {

	private String value;
	
	public DecToken(String piece) {
		this.value = piece;
	}

	public String getValue() {
		return this.value;
	}
}
