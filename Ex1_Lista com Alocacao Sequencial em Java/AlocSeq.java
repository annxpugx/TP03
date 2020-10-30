import java.io.*;

public class AlocSeq {

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

        String[] entrada2 = new String[1000];

        try{
            entrada2 = ler(); // leitura das linhas do arquivo
        }catch(Exception e){
            MyIO.println(e.getMessage());
        }

        ListaSimples lista = new ListaSimples(); 

        for(int i = 0; i < numEntrada; i++){
            Jogador j = new Jogador(entrada2[ids[i]]); // linhas dos ids do pub in -> jogadores
            lista.inserirFim(j);
        }

        int qtd = MyIO.readInt();
        String[] linhas = new String[qtd];

        for(int i = 0; i < qtd; i++){
            linhas[i] = MyIO.readLine();
            String split[] = linhas[i].split(" ");
            try{
                if(split[0].equals("R*")){
                    lista.remover(Integer.parseInt(split[1]));
                } else if(split[0].equals("I*")){
                    Jogador j = new Jogador(entrada2[Integer.parseInt(split[2])]);
                    lista.inserir(j, Integer.parseInt(split[1]));
                }
                else if(split[0].equals("II")){
                    Jogador j = new Jogador(entrada2[Integer.parseInt(split[1])]);
                    lista.inserirInicio(j);
                }
                else if(split[0].equals("IF")){
                    Jogador j = new Jogador(entrada2[Integer.parseInt(split[1])]);
                    lista.inserirFim(j);
                }
                else if(split[0].equals("RI")){ lista.removerInicio(); }
                else if(split[0].equals("RF")){ lista.removerFim(); }

            } catch (Exception e){
                MyIO.println(e.getMessage());
            }
        }

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
        String str = " ## " + getNome() + " ## " + getAltura() + " ## " + getPeso() + " ## " +  getAnoNascimento()
        + " ## " +getUniversidade()+ " ## " + getCidadeNascimento() + " ## " + getEstadoNascimento() + " ## ";
        return str;
    }
}

class Celula {
	public Jogador jogador; // Elemento inserido na celula.
    public Celula prox; // Aponta a celula prox.

	public Celula() {
		this(null);
	}

	public Celula(Jogador jogador) {
      this.jogador = jogador;
      this.prox = null;
	}
}

class ListaSimples { // CONTÉM NÓ CABEÇA -> posição 0 é o primeiro elemento inserido (ignora-se o nó)

    private Celula primeiro, ultimo;

    public ListaSimples() {
        primeiro = new Celula();
        ultimo = primeiro;
    }

    // INSERÇÕES --------------------------------------------------------

    public void inserir(Jogador j, int pos) throws Exception {
        int tamanho = tamanho();
        if (pos < 0 || pos > tamanho) {
            throw new Exception("Erro!"); // evita de adicionar em posição não existente
        } else if (pos == 0) {
            inserirInicio(j);
        } else if (pos == tamanho) {
            inserirFim(j);
        } else {
            Celula i = primeiro;
            for (int x = 0; x < pos; x++, i = i.prox);
            // iteração até i "ser" (mesmo endereço) a celula antes da posicao desejada
            Celula tmp = new Celula(j);
            tmp.prox = i.prox;          // nova celula aponta para a mesma celula de i
            i.prox = tmp;               // i (celula da posicao anterior à pos) começa a apontar para a nova celula
            tmp = i = null;
        }
    }

    public void inserirInicio(Jogador j) {
        Celula tmp = new Celula(j);
        tmp.prox = primeiro.prox; // } colocando x entre o nó cabeça
        primeiro.prox = tmp;      // } e o antigo primeiro elemento
        if (primeiro == ultimo)
            ultimo = tmp;
        tmp = null;
    }

    public void inserirFim(Jogador j) { // igual o inserir da Fila -> fila insere no final
        ultimo.prox = new Celula(j);
        ultimo = ultimo.prox;
    }

    // REMOÇÕES --------------------------------------------------------

    public Jogador remover(int pos) throws Exception{
        Jogador jog; int tamanho = tamanho();
        if(primeiro == ultimo || pos < 0 || pos >= tamanho)
            throw new Exception ("Erro!");
        else if (pos == 0) jog = removerInicio();
        else if (pos == tamanho - 1) jog = removerFim();
        else{
            Celula i = primeiro;
            for(int j = 0; j < pos; j++, i = i.prox);
            // iteração até i "ser" (mesmo endereço) a celula antes da posicao desejada
            Celula tmp = i.prox;
            jog = tmp.jogador; // elemento removido -> elemento da posicao desejada    
            i.prox = tmp.prox;       // referencia para o elemento "removido" se perde
            tmp.prox = null;
            i = tmp = null;
        }

        MyIO.println("(R) " + jog.getNome());
        return jog;
    }

    public Jogador removerInicio() throws Exception { // igual o remover da Fila -> fila remove do inicio (First In First Out)
        if (primeiro == ultimo)
            throw new Exception("Erro!");
        Celula tmp = primeiro.prox; // tmp recebe a celula a ser removida
        primeiro.prox = primeiro.prox.prox; // o proximo após o nó cabeça -> o que vem após o elemento removido
                                            // (perde-se a referencia da celula removida)
        Jogador jog = tmp.jogador; // elemento removido
        tmp.prox = null; // LIBERAR MEMORIA -> OBRIGATORIO NA PROVA (novo "primeiro elemento" agr só é
                         //                                             referenciado pelo nó)
        tmp = null; // LIBERAR MEMORIA -> OBRIGATORIO NA PROVA
        MyIO.println("(R) " + jog.getNome());
        return jog;
    }

    public Jogador removerFim() throws Exception {
        if (primeiro == ultimo)
            throw new Exception("Erro!");
        Celula i;
        for (i = primeiro; i.prox != ultimo; i = i.prox);
        // iteração até i "ser" (mesmo endereço) a celula antes do "antigo" (ainda nao foi remov) ultimo
        Jogador jog = ultimo.jogador; // elemento removido (antigo ultimo)
        ultimo = i;                     // novo ultimo é i (celula antes do antigo ultimo) -> remoção
        i = ultimo.prox = null;         // libera memória -> OBRIGATORIO!!!!!
        MyIO.println("(R) " + jog.getNome());
        return jog;
    }

    // EXTRA -----------------------------------------------------------------

    public void mostrar() {
        int j = 0;
		for (Celula i = primeiro.prox; i != null; i = i.prox) {
            System.out.println("["+j+"] " + i.jogador.toString());
            j++;
		}
	}

    public int tamanho() {
        int tamanho = 0;
        for (Celula i = primeiro; i != ultimo; i = i.prox, tamanho++);
        return tamanho;
    }
}
