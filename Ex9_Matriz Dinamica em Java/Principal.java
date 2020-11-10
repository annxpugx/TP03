public class Principal {
    public static void main(String[] args){
        int total_casos = MyIO.readInt();
        Matriz [] casos = new Matriz [2];

        for(int x = 0; x < total_casos; x++){

            int l[] = new int [2];
            int c[] = new int [2];
            int [][][] matrizes = new int[2][10][10];

            for(int y = 0; y < 2; y++){

                l[y] = MyIO.readInt();
                c[y] = MyIO.readInt();

                for(int i = 0; i < l[y]; i++){
                    String line = MyIO.readLine();
                    String lines[] = line.split(" ");
                    for(int j = 0; j < c[y]; j++){
                        int sep = Integer.parseInt(lines[j]);
                        matrizes[y][i][j] = sep;
                    }
                }
            }

            casos[0] = new Matriz(l[0], c[0], matrizes[0]);
            casos[1] = new Matriz(l[1], c[1], matrizes[1]);

            casos[0].mostrarDiagonalPrincipal();
            casos[0].mostrarDiagonalSecundaria();
            casos[0].soma(casos[1]);
            casos[0].multiplicacao(casos[1]);

        }
    
    }
}

class Matriz {
    private Celula inicio = new Celula();
    private int linhas, colunas;

    public Matriz() {
        this(3, 3);
    }

    public Matriz(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;

        Celula[][] conjunto = new Celula[linhas][colunas];

        for(int i = 0; i < linhas; i++){
            for(int j = 0; j < colunas; j++){
                conjunto[i][j] = new Celula();
            }
        }

        inicio = conjunto[0][0];

        for(int i = 0; i < linhas; i++){
            for(int j = 0; j < colunas; j++){
                if(i < linhas-1)
                    conjunto[i][j].inf = conjunto[i+1][j];
                if(j < colunas-1)
                    conjunto[i][j].dir = conjunto[i][j+1];
                if(i > 0)
                    conjunto[i][j].sup = conjunto[i-1][j];
                if(j > 0)
                    conjunto[i][j].esq = conjunto[i][j-1];
            }
        }
    }

    public Matriz(int[][] mat) {
        this(3, 3, mat);
    }

    public Matriz(int linhas, int colunas, int[][] mat) {
        this.linhas = linhas;
        this.colunas = colunas;

        Celula[][] conjunto = new Celula[linhas][colunas];

        for(int i = 0; i < linhas; i++){
            for(int j = 0; j < colunas; j++){
                conjunto[i][j] = new Celula(mat[i][j]);
            }
        }

        inicio = conjunto[0][0];

        for(int i = 0; i < linhas; i++){
            for(int j = 0; j < colunas; j++){
                if(i < linhas-1)
                    conjunto[i][j].inf = conjunto[i+1][j];
                if(j < colunas-1)
                    conjunto[i][j].dir = conjunto[i][j+1];
                if(i > 0)
                    conjunto[i][j].sup = conjunto[i-1][j];
                if(j > 0)
                    conjunto[i][j].esq = conjunto[i][j-1];
            }
        }
    }

    public void mostrar(){
        Celula cel = inicio;

        for(int i = 0; i < linhas; i++){
            MyIO.print(cel.elemento + " ");
            for(int j = 0; j < colunas-1; j++){
                cel = cel.dir;
                MyIO.print(cel.elemento + " ");
            }
            MyIO.println("");
            for(int x = 0; x < colunas-1; x++){
                cel = cel.esq;
            }
            cel = cel.inf;
        }
    }

    public void soma (Matriz m) {

        Matriz mat = new Matriz(this.linhas, m.colunas);

        Celula c1 = m.inicio;
        Celula c2 = this.inicio;
        Celula c3 = mat.inicio;

        for(int i = 0; i < linhas; i++){
            c3.elemento = c1.elemento + c2.elemento;
            for(int j = 0; j < colunas-1; j++){
                c1 = c1.dir;
                c2 = c2.dir;
                c3 = c3.dir;
                c3.elemento = c1.elemento + c2.elemento;
            }
            for(int x = 0; x < colunas-1; x++){
                c1 = c1.esq;
                c2 = c2.esq;
                c3 = c3.esq;
            }
            c1 = c1.inf;
            c2 = c2.inf;
            c3 = c3.inf;
        }
 
        mat.mostrar();
    }

    public void multiplicacao (Matriz m){
        
        if (isQuadrada(m)) {

            Matriz mat = new Matriz(this.linhas, m.colunas);

            Celula c1 = this.inicio;
            Celula c2 = m.inicio;
            Celula c3 = mat.inicio;

            for(int i = 0; i < linhas; i++){
                for(int j = 0; j < colunas; j++){
                    c3.elemento += c1.elemento * c2.elemento;
                    while(c1.dir != null && c2.inf != null){
                        c1 = c1.dir;
                        c2 = c2.inf;
                        c3.elemento += c1.elemento * c2.elemento;
                    }
                    while(c1.esq != null && c2.sup != null){
                        c1 = c1.esq;
                        c2 = c2.sup;
                    }
                    if(c2.dir != null)
                        c2 = c2.dir;
                    else j = colunas;

                    if(c3.dir != null){
                        c3 = c3.dir;
                    } else {
                        while(c3.esq != null)
                            c3 = c3.esq;
                        if(c3.inf != null)
                            c3 = c3.inf;
                    }
                }
                if(c1.inf != null)
                    c1 = c1.inf;
                while(c2.esq != null){
                    c2 = c2.esq;
                }
            }   

            mat.mostrar();
        }
    }

    public boolean isQuadrada(){
       return (this.linhas == this.colunas);
    }

    public boolean isQuadrada(Matriz m){
        return (this.linhas == m.linhas && this.colunas == m.colunas);
     }

    public void mostrarDiagonalPrincipal() {
        Celula c3 = inicio;
        if (isQuadrada()) {
            MyIO.print(c3.elemento + " ");
            for(int i = 1; i < colunas; i++){
                if(c3.dir != null && c3.dir.inf != null){
                    c3 = c3.dir.inf;
                    MyIO.print(c3.elemento + " ");
                }
            }
        }
        MyIO.println("");
    }

    public void mostrarDiagonalSecundaria() {
        Celula c3 = inicio;
        if (isQuadrada()) {
            while(c3.dir != null){
                c3 = c3.dir;
            }
            MyIO.print(c3.elemento + " ");
            while(c3.esq != null && c3.esq.inf != null){
                c3 = c3.esq.inf;
                MyIO.print(c3.elemento + " ");
            }
        }
        MyIO.println("");
    }
}

class Celula {
    public int elemento;
    public Celula inf, sup, esq, dir;

    public Celula() {
        this(0, null, null, null, null);
    }

    public Celula(int elemento) {
        this(elemento, null, null, null, null);
    }

    public Celula(int elemento, Celula inf, Celula sup, Celula esq, Celula dir) {
        this.elemento = elemento;
        this.inf = inf;
        this.sup = sup;
        this.esq = esq;
        this.dir = dir;
    }
}


