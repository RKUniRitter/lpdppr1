
public class Token {
	Tipo tipo; //tipo
	String conteudo; //conteúdo
	int linha; //linha inicial
	int coluna; //coluna inicial
	String tipoDado;
	String escopo;
	String endereco;
	String valor;
	
	public Token(Tipo t, String c, int l, int col) {
		this.tipo = t;
		this.conteudo = c;
		this.linha = l;
		this.coluna = col;
	}
	
	public String toString() {
		return tipo +", " +conteudo +", " +linha +", " +coluna;
	}
}
