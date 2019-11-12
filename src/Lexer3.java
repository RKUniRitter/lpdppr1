import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.PushbackReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class Lexer3 {
	
	PushbackReader r; //Arquivo a ser parseado
	int intch; //código do caracter sendo analisado
	char ch; //caracter sendo analisado
	int linha = 1;
	int coluna = 0;
	HashMap<String, Token> ltoken = new HashMap<String, Token>(); //Lista de tokens
	
	/**
	 * Inicia stream a partir do arquivo-fonte de entrada
	 * @param f Arquivo fonte de entrada
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public Lexer3(String f) throws UnsupportedEncodingException, FileNotFoundException {
	    //Pág. 62 da apostila
  	    //https://www.javamex.com/tutorials/io/character_stream_reader.shtml

		r = new PushbackReader (new BufferedReader(new InputStreamReader(
		          //new FileInputStream(f), "US-ASCII")));
			  	  new FileInputStream(f), "UTF-8")));

	}
	
	
	/**
	 * Devolve caracter para stream e decrementa coluna e linha conforme necessário
	 * @throws IOException
	 */
	public void devolve() throws IOException {
		r.unread(ch);
		if (ch == '\n')
			linha--;
		else
			coluna--;		
	}
	
	
	/**
	 * Lê próximo caracter do stream e incrementa colunas e linhas conforme necessário
	 * @throws IOException
	 */
	public char leCh() throws IOException {
		intch = r.read();
	    ch = (char) intch;
	    if (ch == '\n') {
	    	linha++;
	    	coluna = 0;
	    } else
		    coluna++;
	    if (intch != -1)
	    	return ch;
	    else
	    	return '@';
	}

	
	/** AFD conforme doc/AFD Léxico.jpg
	 *  Retorna próximo token
	*/
	public Token buscaToken() throws IOException {
		
		/**
		 * Lexema que está sendo lido
		 */
		String lexema = "";
		
		/**
		 * Estado corrente do AFD
		 */
		int state = 0;
		
		/**
		 * Coluna inicial do lexema que está sendo lido
		 */
		int col = 0;
		
		//Lê próximo caracter
		ch = leCh();

		while (ch != '@') {
			switch (state) {			
				case 0:
					if (ch == ':') {
						state = 1;
					} else if (ch == '+') {
						return new Token(Tipo.SMAIS, "+", linha, coluna);
					} else if (ch == '-') {
						return new Token(Tipo.SMENOS, "-", linha, coluna);
					} else if (ch == '*') {
						return new Token(Tipo.SMULTIPLICACAO, "*", linha, coluna);
					} else if (ch == '/') {
						return new Token(Tipo.SDIVISAO, "/", linha, coluna);
					} else if (ch == ';') {
						return new Token(Tipo.SPONTO_E_VIRGULA, ";", linha, coluna);
					} else if (ch == ',') {
						return new Token(Tipo.SVIRGULA, ",", linha, coluna);
					} else if (ch == '.') {
						return new Token(Tipo.SPONTO, ".", linha, coluna);
					} else if (ch == ':') {
						return new Token(Tipo.SDOISPONTOS, ":", linha, coluna);
					} else if (ch == '(') {
						return new Token(Tipo.SABRE_PARENTESIS, "(", linha, coluna);
					} else if (ch == ')') {
						return new Token(Tipo.SFECHA_PARENTESIS, ")", linha, coluna);
					} else if (Character.isDigit(ch)) { 
						col = coluna;	//guarda coluna do início do lexema
						lexema += ch;
						state = 12;
					} else if (Character.isLetter(ch)) { 
						col = coluna;	//guarda coluna do início do lexema
						lexema += ch;
						state = 14;
					}
					break;
				case 1: 
					if (ch == '=') 
						return new Token(Tipo.SATRIBUICAO, ":=", linha, coluna);
					else {
						devolve();
						return new Token(Tipo.STIPO, ":", linha, coluna);
					}
				case 12:
					if (Character.isDigit(ch)) {
						lexema += ch;
						state = 12;						
					} else {
						devolve();
						return new Token(Tipo.SNUMERO, lexema, linha, col);						
					}
					break;
				case 14:
					if (Character.isLetter(ch)) {
						lexema += ch;
						state = 14;						
					} else {
						devolve();
						switch (lexema) {
							case "programa": return new Token(Tipo.SPROGRAMA, lexema, linha, col);
							case "inteiro":  return new Token(Tipo.SINTEIRO, lexema, linha, col);
							case "booleano": return new Token(Tipo.SBOOLEANO, lexema, linha, col);
							case "inicio":   return new Token(Tipo.SINICIO, lexema, linha, col);
							case "fim":  	 return new Token(Tipo.SFIM, lexema, linha, col);
							default: 	 	 return new Token(Tipo.SIDENTIFICADOR, lexema, linha, col);						
						}
					}
					break;
			} //Fim Switch
			leCh();			
		} //Fim While
		lexema += ch;
		//Não era para chegar aqui. Se chegou, há algum erro
		return new Token(Tipo.SERRO, lexema, linha, coluna);
	}

	
	
	/**
	 * Cria lista de tokens para todo o arquivos de entrada
	 * @return
	 * @throws IOException
	 */
	public HashMap<String, Token> lex() throws IOException {
				
	  Token t;
		
	  t = buscaToken();
	  
	  while ( t.tipo != Tipo.SERRO ) {
		  ltoken.put(t.conteudo, t);
		  //System.out.println("Token: " +t.toString());
	      t = buscaToken() ;
	  }

	  return ltoken;
	}
	
}
