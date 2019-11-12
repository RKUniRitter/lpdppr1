import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.PushbackReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Lexer1 {
	
	PushbackReader r; //Arquivo a ser parseado
	int intch; //código do caracter sendo analisado
	char ch; //caracter sendo analisado
	int linha = 1;
	int col = 0;
	ArrayList<Token> ltoken = new ArrayList<Token>(); //Lista de tokens
	
	
	public Token buscaToken(char ch) throws IOException {
		
		String lexema = "";
		int coluna;
		
		switch (ch) {
			case ':':
				if ((ch = leCh()) != '@' && ch == '=')
					return new Token(Tipo.SATRIBUICAO, ":=", linha, col);
				else 
					return new Token(Tipo.STIPO, ":", linha, col);
			case '+': return new Token(Tipo.SMAIS, "+", linha, col); 
			case '-': return new Token(Tipo.SMENOS, "-", linha, col); 
			case '*': return new Token(Tipo.SMULTIPLICACAO, "*", linha, col); 
			case ';': return new Token(Tipo.SPONTO_E_VIRGULA, ";", linha, col);
			case '.': return new Token(Tipo.SPONTO, ".", linha, col); 
			case '(': return new Token(Tipo.SABRE_PARENTESIS, "(", linha, col); 
			case ')': return new Token(Tipo.SFECHA_PARENTESIS, ")", linha, col); 
			default: 
				if (Character.isDigit(ch))	 {
					lexema += ch;
					coluna = col;
					while ((ch = leCh()) != '@'  && Character.isDigit(ch))
						lexema += ch;
						return new Token(Tipo.SNUMERO, lexema, linha, coluna); 
				}
				else if (Character.isLetter(ch)) {
					lexema += ch;
					coluna = col;
					while ((ch = leCh()) != '@'  && (Character.isLetter(ch) || Character.isDigit(ch))) {
						lexema += ch;						
						//System.out.println(lexema);
					}
					r.unread(ch);
					//lexema = lexema.substring(0, lexema.length() - 1);
					//System.out.println("Lexema: " +lexema);
					//System.out.println("Caracter: " +ch);
					switch (lexema) {
						case "programa": return new Token(Tipo.SPROGRAMA, lexema, linha, coluna); 
						case "inteiro" : return new Token(Tipo.SINTEIRO, lexema, linha, coluna); 
						case "inicio"  : return new Token(Tipo.SINICIO, lexema, linha, coluna); 
						case "fim" 	   : return new Token(Tipo.SFIM, lexema, linha, coluna); 
						default: 		 return new Token(Tipo.SIDENTIFICADOR, lexema, linha, coluna); 
					}
				}					
				else { //Erro
					System.out.println();
					return new Token(Tipo.SERRO, Character.toString(ch), linha, col);
				}
		}

		
		
	}

	
	public char leCh() throws IOException {
		intch = r.read();
	    ch = (char) intch;
	    if (ch == '\n') {
	    	linha++;
	    	col = 0;
	    } else
		    col++;
	    if (intch != -1)
	    	return ch;
	    else
	    	return '@';
	}
	
	
	public ArrayList<Token> lex(String f) throws IOException {

	  //Pág. 62 da apostila
	  //https://www.javamex.com/tutorials/io/character_stream_reader.shtml
				
	  r = new PushbackReader (new BufferedReader(new InputStreamReader(
		          //new FileInputStream(f), "US-ASCII")));
			  	  new FileInputStream(f), "UTF-8")));
	  try {
	    //int count = 0;
	    while ((ch = leCh()) != '@') {
	      /*
	      if (Character.isLetter(ch)) {
	        count++;
	      }*/
	      
	      //Pula comentários
	      if (ch == '{')
	        while (ch != '}' && intch != -1) {
	    	    ch = leCh();
	        }
	    	 
	      //Pula espaços em branco, tabs e nova linha
	      //if (ch == ' ')
	      while ((ch == ' ' || ch == '\n' || ch == '\t') && intch != -1){
			ch = leCh();
	      }

	      //System.out.println("Início do token: " +ch);
	      ltoken.add( buscaToken(ch) );
	      
  	      //System.out.println(count++);
	    }
	  } finally {
		//System.out.println("Encerrando.");
	    r.close();
	  }
	return ltoken;				
	}
	
}
