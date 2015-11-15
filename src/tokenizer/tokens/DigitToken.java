package tokenizer.tokens;

public class DigitToken implements Token {
private char value;

	public DigitToken(char number) {
		this.value = number;
	}
	
	public char getValue() {
		return this.value;
	}
}
