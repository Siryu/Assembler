package tokenizer.tokens;

public class DataProcToken implements Token{

	private String value;
	
	public DataProcToken(String string) {
		this.value = string;
	}
	
	public String getValue() {
		return this.value;
	}
}