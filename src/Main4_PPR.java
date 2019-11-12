/*
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
*/

import java.io.IOException;


public class Main4_PPR {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		/*
		 * Usa Lexer3, que é a implamentação com Autômatos Finitos
		 * A Tabela de Símbolos é implementada como um HashMap 
		 */
		
		/*
		Lexer3 l = new Lexer3(args[0]);
		HashMap<String, Token> tokens = l.lex();
		System.out.println("Tokens: " +tokens.size());
		
		tokens.forEach ((conteudo, token) -> {System.out.println(token.toString());} );
		*/
		
		
		//https://stackoverflow.com/a/22978490/2396999
		//System.out.print(System.getProperty("user.dir"));
		//PPR ppr = new PPR(args[0]);
		PPR ppr = new PPR("src/prog1.lpd");
		
		
	}

}
