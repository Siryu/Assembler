package tokenizer.tokens;

public class AlphaToken implements Token{
	private char value;
	
	
	public AlphaToken(char currentPart) {
		this.value = currentPart;
	}

	public char getValue() {
		return this.value;
	}
}
