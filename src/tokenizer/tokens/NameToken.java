package tokenizer.tokens;

public class NameToken implements Token {
	
	private String value;

	public NameToken(String string) {
		this.value = string;
	}

	public String getValue() {
		return this.value;
	}
}
