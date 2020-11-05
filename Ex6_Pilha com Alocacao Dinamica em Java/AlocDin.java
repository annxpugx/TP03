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

        Pilha pilha = new Pilha(); 

        for(int i = 0; i < numEntrada; i++){
            Jogador j = new Jogador(entrada2[ids[i]]); // linhas dos ids do pub in -> jogadores
            pilha.inserir(j);
        }

        int qtd = MyIO.readInt();
        String linha;

        for(int i = 0; i < qtd; i++){
            linha = MyIO.readLine();
            String split[] = linha.split(" ");
            try{
                if(split[0].equals("I")){
                    Jogador j = new Jogador(entrada2[Integer.parseInt(split[1])]);
                    pilha.inserir(j);
                }
                else if(split[0].equals("R")){ MyIO.println("(R) " + pilha.remover()); }
            } catch (Exception e){
                MyIO.println(e.getMessage());
            }
        }

        pilha.mostrar();
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
        String str = " ## " + getNome() + " ## " + getAltura() + " ## " + getPeso() + " ## " +  getAnoNascimento()
        + " ## " +getUniversidade()+ " ## " + getCidadeNascimento() + " ## " + getEstadoNascimento() + " ## ";
        return str;
    }
}

class Celula {
	public Jogador jogador; // Elemento inserido na celula.
    public Celula prox; // Aponta a celula prox.

	public Celula(Jogador jogador) {
      this.jogador = jogador;
      this.prox = null;
	}
}

class Pilha{

    private Celula topo;    
    
    public Pilha(){
        this.topo = null;
    }

	public void inserir(Jogador x) {
		Celula tmp = new Celula(x);
		tmp.prox = topo;
		topo = tmp;
		tmp = null;
	}

    public String remover() throws Exception {
		if (topo == null) {
			throw new Exception("Erro ao remover!");
		}
		String resp = topo.jogador.nome;
		Celula tmp = topo;
		topo = topo.prox;
		tmp.prox = null;
		tmp = null;
		return resp;
	}

    public void mostrar(){
        mostrar(topo, tamanho()-1);
    }

    public int tamanho() {
        int tamanho = 0;
        for (Celula i = topo; i != null; i = i.prox, tamanho++);
        return tamanho;
    }

    public void mostrar(Celula i, int j){
        if(i != null){
            mostrar(i.prox, j-1);
            System.out.print("["+j+"]");
            System.out.println(i.jogador.toString());
        }
    }
}

//System.out.println("["+j+"]"+i.jogador.toString());

