import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PPR extends Parser {
	
	
	public PPR (String nomeArquivo ) throws IOException {
		super(nomeArquivo);
		
		//Pág. 74 da apostila
		analisaPrograma();
	}

	public boolean erro(String s) {
		System.out.println(s +": " +t.linha +", " +t.coluna);
		return false;
	}
	
	//Pág. 74 da apostila
	public boolean analisaPrograma() throws IOException {
		buscaToken();
		if (t.tipo == Tipo.SPROGRAMA) {
			System.out.print(t.tipo +" ");
			buscaToken();
			if (t.tipo == Tipo.SIDENTIFICADOR) {
				System.out.print(t.tipo +": " +t.conteudo +' ');
				//Adiciona identificador à tabela de símbolos
				ts.ts.put(t.conteudo, t);
				buscaToken();
				if (t.tipo == Tipo.SPONTO_E_VIRGULA) {
					System.out.print(t.tipo +" ");
					if (analisaBloco()) {
						buscaToken();
						if (t.tipo == Tipo.SPONTO)
							System.out.print(t.tipo +" ");
							return true;
					} else {
						System.out.println("Bloco principal não encontrado: " +t.linha +", " +t.coluna);
						return false;
					}
				} else {
					System.out.println("Ponto e vírgula esperado: " +t.linha +", " +t.coluna);
					return false;					
				}
			} else {
				System.out.println("Identificador esperado: " +t.linha +", " +t.coluna);
				return false;
			}
		} else {
			System.out.println("Programa principal não encontrado: " +t.linha +", " +t.coluna);
			return false;
		}
		//System.out.println("Erro genérico: " +t.linha +", " +t.coluna);
		//return false;
	}
	
	public boolean analisaBloco() throws IOException {
		buscaToken();
		analisaEtapaDeclaracaoDeVariaveis();
		//analisaSubRotinas();
		analisaComandos();
		return true;
	}
	
	public boolean analisaEtapaDeclaracaoDeVariaveis() throws IOException {
		if (t.tipo == Tipo.SVAR) {
			System.out.print(t.tipo +": " +t.conteudo +' ');
			buscaToken();
			if (t.tipo == Tipo.SIDENTIFICADOR) {
				while (t.tipo == Tipo.SIDENTIFICADOR) {
					analisaVariaveis();
					if (t.tipo == Tipo.SPONTO_E_VIRGULA ) {
						buscaToken();						
					} else
						erro("Missing ;");
				}
			} else
				return erro("Identificador esperado");
		} else
			return true; //Esta etapa é opcional, portanto não dá erro se não existir
		
		return true;
	}
	
	public boolean analisaVariaveis() throws IOException {
		do
			if (t.tipo == Tipo.SIDENTIFICADOR) {
				if (! tsExiste(t.conteudo)) {
					System.out.print(t.tipo +": " +t.conteudo +' ');
					//Adiciona identificador à tabela de símbolos
					ts.ts.put(t.conteudo, t);
					buscaToken();
					if (t.tipo == Tipo.SVIRGULA || t.tipo == Tipo.SDOISPONTOS) {
						if (t.tipo == Tipo.SVIRGULA) {
							buscaToken();
							if (t.tipo == Tipo.SDOISPONTOS)
								erro(": não esperado");
						}
					} else
						erro("Missing , or :");
	
				} else 
					erro("Este identificador já existe");
			}
		while (t.tipo != Tipo.SDOISPONTOS);
		buscaToken();
		return analisaTipo();
	}
	
	public boolean analisaTipo() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		if (t.tipo == Tipo.SINTEIRO || t.tipo == Tipo.SBOOLEANO) {
			//Adiciona tipo para todos identificadores sem tipo
			ts.ts.forEach((k, v) -> {
				if (v.tipoDado == "")
					v.tipoDado = t.conteudo;
			});			
			//ts.setAtributo(t.conteudo, "tipo", t.conteudo);
		} else
			return erro("Tipo não encontrado");
		buscaToken();
		return true;
	}

	public void analisaComandos() throws IOException {
		if (t.tipo == Tipo.SINICIO) {
			buscaToken();
			analisaComandoSimples();
			while (t.tipo != Tipo.SFIM) {
				if (t.tipo == Tipo.SPONTO_E_VIRGULA) {
					buscaToken();
					if (t.tipo != Tipo.SFIM) {
						analisaComandoSimples();
					} else
						erro("Missing FIM");
				} else
					erro("Missing ;");
			}
		} else
			erro("INICIO esperado");
	}
	
	public void analisaComandoSimples() {
		switch (t.tipo) {
			case Tipo.SIDENTIFICADOR: analisaAtribChProcedimento(); break;
			case Tipo.SSE: analisaSE(); break;
			case Tipo.SENQUANTO: analisaEnquanto(); break;
			case Tipo.SLEIA: analisaLeia(); break;
			case Tipo.SESCREVA: analisaEscreva(); break;
			default: analisaComandos();
		}
	}
	
	public void analisaAtribChProcedimento() {
		Token id = t;
		buscaToken();
		if (t.tipo == Tipo.SATRIBUICAO)
			analisaAtribuicao(id);
		else
			analisaChProcedimento(id);		
	}
	
	public void analisaLeia() throws IOException {
		buscaToken();
		if (t.tipo == Tipo.SABRE_PARENTESIS) {
			buscaToken();
			if (t.tipo == Tipo.SIDENTIFICADOR ) {
				if (ts.ts.get(t.conteudo) != null) {
					buscaToken();
					if (t.tipo == Tipo.SFECHA_PARENTESIS ) {
						buscaToken();
						//Executa comando de leitura e coloca valor em t.valor
					} else
						erro(") esperado");
				} else
					erro("Identificador não encontrado");
			}
		} else
			erro("( esperado");
	}
	
	public void analisaEscreva() throws IOException {
		buscaToken();
		if (t.tipo == Tipo.SABRE_PARENTESIS) {
			buscaToken();
			if (t.tipo == Tipo.SIDENTIFICADOR ) {
				if (ts.ts.get(t.conteudo) != null) {
					buscaToken();
					if (t.tipo == Tipo.SFECHA_PARENTESIS ) {
						buscaToken();
						//Executa comando de escrita com t.valor
					} else
						erro(") esperado");
				} else
					erro("Identificador não encontrado");
			}
		} else
			erro("( esperado");		
	}
	
	public void analisaAtribuicao(Token token) {
		token.valor = analisaExpressao();
	}
	
	public String analisaExpressao() {
		return analisaExpressaoSimples();
	}
	
	public String analisaExpressaoSimples() {
		if (t.tipo == Tipo.SMAIS || t.tipo == Tipo.SMENOS) {
			buscaToken();
			analisaTermo();
			while (t.tipo == Tipo.SMAIS || t.tipo == Tipo.SMENOS) {
				buscaToken();
				analisaTermo();				
			}
		}
	}
	
	public void analisaTermo() throws IOException {
		analisaFator();
		while (t.tipo == Tipo.SMULTIPLICACAO || t.tipo == Tipo.SDIVISAO) {
			buscaToken();
			analisaFator();							
		}
	}
	
	public void analisaFator() {
		if (t.tipo == Tipo.SIDENTIFICADOR ) { //Variável ou função
			Token to = (ts.ts.get(t.conteudo));
			if (to.tipoDado == "inteiro" || to.tipoDado == "booleano") 
				analisaChamadaFuncao();
			else { //Variável
				buscaToken();
			}
		if (t.tipo == Tipo.SNUMERO)
			buscaToken();
		} else
			erro ("Identificador esperado");
	}
}
