package tokenizer.tokens;

public class HexToken implements Token{
	
	private String value;
	
	public HexToken(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}

}
