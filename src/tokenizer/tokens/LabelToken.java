package tokenizer.tokens;

public class LabelToken implements Token{

	private String value;
	
	public LabelToken(String labelName) {
		this.value = labelName;
	}

	public String getValue() {
		return this.value;
	}
}
