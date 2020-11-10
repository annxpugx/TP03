import java.io.*;

public class AlocDin {

    public static void main(String[] args){
        String[] entrada = new String[1000];
        int numEntrada = 0;       
        // Leitura da entrada padrao
        do {
            entrada[numEntrada] = MyIO.readLine();
        } while (isFim(entrada[numEntrada++]) == false);
        numEntrada--; // Desconsiderar ultima linha contendo a palavra FIM

        int ids[] = new int[numEntrada];

        for(int i = 0; i < numEntrada; i++){
            ids[i] = Integer.parseInt(entrada[i]); // transformando id em inteiro
        }

        String[] entrada2 = new String[4000];

        try{
            entrada2 = ler(); // leitura das linhas do arquivo
        }catch(Exception e){
            MyIO.println(e.getMessage());
        }

        Jogador j = new Jogador(entrada2[ids[0]]);
        ListaDupla lista = new ListaDupla(j); 

        for(int i = 1; i < numEntrada; i++){
            j = new Jogador(entrada2[ids[i]]);
            lista.inserirFim(j);
        }

        lista.quicksort();

        Arq.openWrite("694370_quicksort2");
        Arq.print("694370\t"+lista.comp+"\t"+lista.mov);
        Arq.close();

        lista.mostrar();
    }

    public static boolean isFim(String s) {
        return (s.length() >= 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M');
    }

    public static String[] ler() throws Exception {

        String[] entrada = new String[4000];
        int numEntrada = 0;
        File file = new File("/tmp/players.csv");

        BufferedReader br = new BufferedReader(new FileReader(file));
        // Leitura da entrada padrao
        String lixo = br.readLine();
        do {
            entrada[numEntrada] = br.readLine();
        } while (entrada[numEntrada++] != null);
        numEntrada--;

        br.close();
        return entrada;
    }
}

class Jogador {
    int id;
    String nome;
    int altura;
    int peso;
    String universidade;
    int anoNascimento;
    String cidadeNascimento;
    String estadoNascimento;

    public Jogador() {
    }

    public Jogador(String linha) {
        String campos[] = linha.split(",");
        this.id = Integer.parseInt(campos[0]);
        this.nome = campos[1];
        this.altura = Integer.parseInt(campos[2]);
        this.peso = Integer.parseInt(campos[3]);
        this.universidade = (campos[4].isEmpty()) ? "nao informado" : campos[4];
        this.anoNascimento = Integer.parseInt(campos[5]);
        if (campos.length > 6) {
            this.cidadeNascimento = (campos[6].isEmpty())? "nao informado": campos[6];
            if (campos.length < 8) {
                this.estadoNascimento = "nao informado";
            } else {
                this.estadoNascimento = campos[7];
            }
        } else {
            this.cidadeNascimento = "nao informado";
            this.estadoNascimento = "nao informado";
        }
    }

    // id,Player,height,weight,collage,born,birth_city,birth_state
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public void setAnoNascimento(int anoNascimento){
        this.anoNascimento = anoNascimento;
    }

    public int getAnoNascimento(){
        return anoNascimento;
    }

    public String getUniversidade() {
        return universidade;
    }

    public void setUniversidade(String universidade) {
        this.universidade = universidade;
    }

    public String getCidadeNascimento() {
        return cidadeNascimento;
    }

    public void setCidadeNascimento(String cidadeNascimento) {
        this.cidadeNascimento = cidadeNascimento;
    }

    public String getEstadoNascimento() {
        return estadoNascimento;
    }

    public void setEstadoNascimento(String estadoNascimento) {
        this.estadoNascimento = estadoNascimento;
    }

    public void clone(Jogador J) {

        this.setId(J.getId());
        this.setCidadeNascimento(J.getCidadeNascimento());
        this.setEstadoNascimento(J.getEstadoNascimento());
        this.setNome(J.getNome());
        this.setAltura(J.getAltura());
        this.setPeso(J.getPeso());
        this.setAnoNascimento(J.getAnoNascimento());
        this.setUniversidade(J.getUniversidade());

    }

    public String toString() {
        String str = "[" + getId() + " ## " + getNome() + " ## " + getAltura() + " ## " + getPeso() + " ## " +  getAnoNascimento()
        + " ## " +getUniversidade()+ " ## " + getCidadeNascimento() + " ## " + getEstadoNascimento() + "]";
        return str;
    }
}

class Celula {
	public Jogador jogador;
	public Celula ant; 
	public Celula prox; 
	/**
	 * Construtor da classe.
	 */
	public Celula() {
		this(null);
	}
	/**
	 * Construtor da classe.
	 * @param elemento int inserido na celula.
	 */
	public Celula(Jogador j) {
        this.jogador = j;
        this.ant = this.prox = null;
	}
}

class ListaDupla {

    private Celula primeiro, ultimo;
    public int comp = 0, mov = 0;

    public ListaDupla(Jogador j) {
        primeiro = new Celula(j);
        ultimo = primeiro;
    }

    // INSERÇÕES --------------------------------------------------------
  
     
    public void inserirFim(Jogador j){
        ultimo.prox = new Celula(j);
        ultimo.prox.ant = ultimo;
        ultimo = ultimo.prox;
    }
     
    public void mostrar() {
        Celula i;
        int j = 0;
        for (i = primeiro; i != null; i = i.prox) {
            MyIO.println(i.jogador.toString());
        }
    }

    public int tamanho() {
        int tamanho = 0;
        for(Celula i = primeiro; i != ultimo; i = i.prox, tamanho++);
        return tamanho;
    }

    // QUICK -----------------------------------------------------------------------
	
	public void quicksort(){
		quicksortRec(primeiro, ultimo, 0, tamanho());
	}
	public void quicksortRec(Celula cel_esquerda, Celula cel_direita, int esq, int dir){

		Celula esquerda = cel_esquerda, direita = cel_direita;
		int	i = esq, j = dir, tam = (dir+esq)/2;

        Celula temp = primeiro;
		for (int x = 0; x < tam; x++, temp = temp.prox);
		Jogador pivo = temp.jogador;
	
		while(i<=j){
			while(esquerda.jogador.getEstadoNascimento().compareTo(pivo.getEstadoNascimento()) < 0 || (esquerda.jogador.getEstadoNascimento().compareTo(pivo.getEstadoNascimento()) == 0 && esquerda.jogador.getNome().compareTo(pivo.getNome()) < 0)){
                if((esquerda.jogador.getEstadoNascimento().compareTo(pivo.getEstadoNascimento()) < 0) == false) comp +=2;
                i++;
                esquerda=esquerda.prox;
                comp++;
			}
			while(direita.jogador.getEstadoNascimento().compareTo(pivo.getEstadoNascimento()) > 0 || (direita.jogador.getEstadoNascimento().compareTo(pivo.getEstadoNascimento()) == 0 && direita.jogador.getNome().compareTo(pivo.getNome()) > 0)){
                if((direita.jogador.getEstadoNascimento().compareTo(pivo.getEstadoNascimento()) < 0) == false) comp +=2;
                j--;
                direita = direita.ant;
                comp++;
			}
			if(i<=j){
                swap(esquerda,direita);
                mov+=3;
				i++;
				j--;
				esquerda = esquerda.prox;
				direita = direita.ant;
			}
		}
		if (esq < j)
			quicksortRec(cel_esquerda, direita, esq, j);
		if (i < dir)
			quicksortRec(esquerda, cel_direita, i, dir);
	}

	public void swap(Celula A, Celula B){
		Jogador aux = A.jogador;
		A.jogador = B.jogador;
		B.jogador = aux;
	}
}
