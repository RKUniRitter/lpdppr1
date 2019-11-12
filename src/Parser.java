import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public abstract class Parser {
	
	TS ts;
	Lexer3 lexer;
	Token t;

	
	public Parser(String nomeArquivo) throws UnsupportedEncodingException, FileNotFoundException {
		ts = new TS();
		lexer = new Lexer3(nomeArquivo);
	}
	
	public void buscaToken() throws IOException {
		t = lexer.buscaToken();
	}


}
