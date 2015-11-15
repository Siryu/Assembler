package tokenizer.tokens;

public class RegisterToken implements Token{

	private String value;
	
	public RegisterToken(String num) {
		this.value = num;
	}

	public String getValue() {
		return this.value;
	}
}
