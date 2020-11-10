#include <iostream>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sstream>
#include <err.h>
#include <math.h>

#define MAXTAM 5
#define bool short
#define true 1
#define false 0

#define TAM_MAX_LINHA 250

bool isFim(char *s)
{
    return (strlen(s) >= 3 && s[0] == 'F' &&
            s[1] == 'I' && s[2] == 'M');
}

char *replaceChar(char *string, char toSearch, char toReplace)
{
    char *charPtr = strchr(string, toSearch);
    if (charPtr != NULL)
        *charPtr = toReplace;
    return charPtr;
}

void lerLinha(char *string, int tamanhoMaximo, FILE *arquivo)
{
    fgets(string, tamanhoMaximo, arquivo);
    replaceChar(string, '\r', '\0');
    replaceChar(string, '\n', '\0');
}

//TIPO JOGADOR - CELULA ===================================================================

struct Jogador {
    int id;
    int altura;
    int peso;
    int anoNascimento;
    char nome[100];
    char universidade[100];
    char cidadeNascimento[100];
    char estadoNascimento[100];

    void clone(Jogador J){
        this->id = J.id;
        strcpy(this->cidadeNascimento,J.cidadeNascimento);
        strcpy(this->estadoNascimento, J.estadoNascimento);
        strcpy(this->nome,J.nome);
        this->altura = J.altura;
        this->peso = J.peso;
        this->anoNascimento = J.anoNascimento;
        strcpy(this->universidade,J.universidade);
    }

};

typedef struct CelulaDupla {
	Jogador elemento;        // Elemento inserido na celula.
	struct CelulaDupla* prox; // Aponta a celula prox.
    struct CelulaDupla* ant;  // Aponta a celula anterior.
} CelulaDupla;


// METODOS JOGADOR --------------------------------------------------------------------------------

void inserirNaoInformado(char *linha, char *novaLinha)
{
    int tam = strlen(linha);
    for (int i = 0; i <= tam; i++, linha++)
    {
        *novaLinha++ = *linha;
        if (*linha == ',' && (*(linha + 1) == ',' || *(linha + 1) == '\0'))
        {
            strcpy(novaLinha, "nao informado");
            novaLinha += strlen("nao informado");
        }
    }
}

void tirarQuebraDeLinha(char linha[])
{
    int tam = strlen(linha);

    if (linha[tam - 2] == '\r' && linha[tam - 1] == '\n') // Linha do Windows
        linha[tam - 2] = '\0';                            // Apaga a linha

    else if (linha[tam - 1] == '\r' || linha[tam - 1] == '\n') // Mac ou Linux
        linha[tam - 1] = '\0';                                 // Apaga a linha
}

void setJogador(Jogador *jogador, char linha[])
{
    char novaLinha[TAM_MAX_LINHA];
    tirarQuebraDeLinha(linha);
    inserirNaoInformado(linha, novaLinha);

    jogador->id = atoi(strtok(novaLinha, ","));
    strcpy(jogador->nome, strtok(NULL, ","));
    jogador->altura = atoi(strtok(NULL, ","));
    jogador->peso = atoi(strtok(NULL, ","));
    strcpy(jogador->universidade, strtok(NULL, ","));
    jogador->anoNascimento = atoi(strtok(NULL, ","));
    strcpy(jogador->cidadeNascimento, strtok(NULL, ","));
    strcpy(jogador->estadoNascimento, strtok(NULL, ","));
}

void ler(char linhas_corrigidas[][TAM_MAX_LINHA])
{
    FILE *players;
    //abrindo o arquivo
    players = fopen("/tmp/players.csv", "r");

    char linhas[4000][TAM_MAX_LINHA];

    int i = 0;
    lerLinha(linhas[0], TAM_MAX_LINHA, players);
    do
    {
        lerLinha(linhas[i++], TAM_MAX_LINHA, players);
    } while (!feof(players));
    i--;

    for (int i = 0; i < 4000; i++)
    {
        inserirNaoInformado(linhas[i], linhas_corrigidas[i]);
    }
}

void imprimir(Jogador *jogador)
{
    printf("[%d ## %s ## %d ## %d ## %d ## %s ## %s ## %s]\n",
            jogador->id,
            jogador->nome,
            jogador->altura,
            jogador->peso,
            jogador->anoNascimento,
            jogador->universidade,
            jogador->cidadeNascimento,
            jogador->estadoNascimento);
}

// METODOS CELULA --------------------------------------------------------------------------------

CelulaDupla* novaCelulaDupla(Jogador elemento) {
   CelulaDupla* nova = (CelulaDupla*) malloc(sizeof(CelulaDupla));
   nova->elemento = elemento;
   nova->ant = nova->prox = NULL;
   return nova;
}


CelulaDupla* primeiro;
CelulaDupla* ultimo;

// QUICKSORT ---------------------------------------------------------------------------

int tamanho() {
    int tamanho = 0; 
    CelulaDupla* i;
    for(i = primeiro; i != ultimo; i = i->prox, tamanho++);
    return tamanho;
}

void swap(CelulaDupla *a, CelulaDupla *b)
{
   Jogador tmp = a->elemento;
   a->elemento = b->elemento;
   b->elemento = tmp;
}

void quicksortRec(CelulaDupla *celula_esq, CelulaDupla *celula_dir, int esq, int dir, int *comp, int *mov) {

        int i = esq;
        int j = dir;

        CelulaDupla *esquerda = celula_esq;
        CelulaDupla *direita = celula_dir;

        int meio = (esq+dir)/2;

        CelulaDupla *tmp = primeiro;
        for (int i = 0; i != meio; i++, tmp = tmp->prox);
        Jogador pivo = tmp->elemento;

        while (i <= j) {
            while ((strcmp(esquerda->elemento.estadoNascimento, pivo.estadoNascimento)) < 0 || ((strcmp(esquerda->elemento.estadoNascimento,pivo.estadoNascimento) == 0) && (strcmp(esquerda->elemento.nome, pivo.nome) < 0))){ 
                if(!strcmp(esquerda->elemento.estadoNascimento, pivo.estadoNascimento) > 0) (*comp)+=2;
                (*comp)++;
                i++;
                esquerda = esquerda->prox;
            }   
            while ((strcmp(direita->elemento.estadoNascimento, pivo.estadoNascimento)) > 0 || ((strcmp(direita->elemento.estadoNascimento,pivo.estadoNascimento) == 0) && (strcmp(direita->elemento.nome, pivo.nome) > 0))){
                if(!strcmp(direita->elemento.estadoNascimento, pivo.estadoNascimento) > 0) (*comp)+=2;
                j--;
                direita = direita->ant;
                (*comp)++;
            }
            if (i <= j) {
                swap(esquerda, direita); 
                (*mov)+=3;
                i++;
                j--;
                esquerda = esquerda->prox;
                direita = direita->ant;
            }
        }

        if (esq < j) {
            quicksortRec(celula_esq, direita, esq, j, comp, mov);
        }
        if (i < dir) {
            quicksortRec(esquerda, celula_dir, i, dir, comp, mov);
        }
    
}
//=============================================================================
void quicksort(int *comp, int *mov) {
    quicksortRec(primeiro, ultimo, 0, tamanho(), comp, mov);
}


//LISTA PROPRIAMENTE DITA =======================================================
void start(Jogador j){
   primeiro = novaCelulaDupla(j);
   ultimo = primeiro;
}

void inserirFim(Jogador j)
{
   ultimo->prox = novaCelulaDupla(j);
   ultimo->prox->ant = ultimo;
   ultimo = ultimo->prox;
}

void mostrar() {
    CelulaDupla* i;
    int j = 0;
    for (i = primeiro; i != NULL; i = i->prox) {
        imprimir(&i->elemento);
    }
}

// MAIN ----------------------------------------------------------------------------------------

int main(int argc, char **argv)
{
    char entrada_id[1000][10];
    int numEntrada_id = 0;
    do
    {
        lerLinha(entrada_id[numEntrada_id], 10, stdin);
    } while (isFim(entrada_id[numEntrada_id++]) == false); // pegar primeiros ids
    numEntrada_id--;

    int entrada_inteiro[1000];

    for (int i = 0; i < 1000; i++)
    {
        sscanf(entrada_id[i], "%d", &entrada_inteiro[i]); // transformação para inteiros
    }

    char saida[4000][TAM_MAX_LINHA];
    Jogador _Jogadores[1000];

    ler(saida); // leitura do arquivo completo


    for (int i = 0; i < numEntrada_id; i++){
        setJogador(&_Jogadores[i], saida[entrada_inteiro[i]]); // criação dos jogadores e inserção na lista
        if(i == 0){
            start(_Jogadores[i]); // inicio da lista
        }else{
            inserirFim(_Jogadores[i]);
        }
    }

    int comp = 0, mov = 0;

    quicksort(&comp, &mov);

    mostrar();

    FILE *matricula;
    matricula = fopen("694370_quicksort2.txt", "w+");

    fprintf(matricula, "694370\t %d \t %d", comp, mov);

    fclose(matricula);  

}