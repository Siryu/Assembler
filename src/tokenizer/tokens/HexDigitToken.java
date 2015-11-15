package tokenizer.tokens;

public class HexDigitToken implements Token{
	private char value;
	
	public HexDigitToken(char currentPart) {
		this.value = currentPart;
	}

	public char getValue() {
		return this.value;
	}
}
