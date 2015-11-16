package tokenizer.tokens;

public class CondToken implements Token{

	private String value;
	
	public CondToken(String string) {
		this.value = string;
	}

	public String getValue() {
		return this.value;
	}
}
