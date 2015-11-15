package tokenizer;

import java.util.ArrayDeque;
import java.util.Deque;

public class TokenBox {
	Deque<Token> tokens;
	
	public TokenBox() {
		this.tokens = new ArrayDeque<Token>();
	}
	
	public Token next() {
		return this.tokens.pop();
	}
	
	public boolean hasNext() {
		boolean hasNext = true;
		if(this.tokens.peek() == null) {
			hasNext = false;
		}
		return hasNext;
	}
	
	public void push(Token token) {
		this.tokens.push(token);
	}
	
	public void pushOntoBack(Token token){
		this.tokens.addLast(token);
	}
}