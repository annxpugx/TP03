#include <stdio.h>
#include <stdlib.h>
#include <string.h>
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

typedef struct
{
    int id;
    int peso;
    int altura;
    char nome[70];
    char universidade[70];
    int anoNascimento;
    char cidadeNascimento[70];
    char estadoNascimento[70];
} Jogador;

typedef struct Celula
{
    Jogador jogador;     // Elemento inserido na celula.
    struct Celula *prox; // Aponta a celula prox.
} Celula;

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

/**
 * @param jogador Ponteiro para o jogador a receber os dados
 * @param linha Linha do CSV. Ex.: "65,Joe Graboski,201,88,,1930,,"
 */
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
    printf(" ## %s ## %d ## %d ## %d ## %s ## %s ## %s ##\n",
           jogador->nome,
           jogador->altura,
           jogador->peso,
           jogador->anoNascimento,
           jogador->universidade,
           jogador->cidadeNascimento,
           jogador->estadoNascimento);
}

Jogador clone(Jogador *jogador)
{
    Jogador retorno;

    retorno.id = jogador->id;
    strcpy(retorno.nome, jogador->nome);
    retorno.altura = jogador->altura;
    retorno.peso = jogador->peso;
    retorno.anoNascimento = jogador->anoNascimento;
    strcpy(retorno.universidade, jogador->universidade);
    strcpy(retorno.cidadeNascimento, jogador->cidadeNascimento);
    strcpy(retorno.estadoNascimento, jogador->estadoNascimento);

    return retorno;
}

// METODOS CELULA --------------------------------------------------------------------------------

Celula *novaCelula(Jogador j)
{
    Celula *nova = (Celula *)malloc(sizeof(Celula));
    nova->jogador = j;
    nova->prox = NULL;
    return nova;
}

//LISTA PROPRIAMENTE DITA =======================================================
Celula *primeiro;
Celula *ultimo;
int pos = 0;

/**
 * Cria uma fila sem elementos (somente no cabeca).
 */
void start()
{
    Jogador init;
    primeiro = novaCelula(init);
    ultimo = primeiro;
}
/**
 * Remove elemento da fila (politica FIFO).
 * @return Elemento removido.
 */
Jogador remover()
{
    if (primeiro == ultimo)
    {
        errx(1, "Erro ao remover!");
    }
    Celula *tmp = primeiro;
    primeiro = primeiro->prox;
    Jogador resp = primeiro->jogador;
    tmp->prox = NULL;
    free(tmp);
    tmp = NULL;
    pos--;

    return resp;
}
/**
 * Insere elemento na fila (politica FIFO).
 * @param x int Elemento a inserir.
 */
void inserir(Jogador x)
{
    if (pos >= MAXTAM) {
        Jogador j = remover();
    }

    ultimo->prox = novaCelula(x);
    ultimo = ultimo->prox;
    pos++;
}

/**
 * Mostra os elementos separados por espacos.
 */
void mostrar()
{
    Celula *i;
    int j = 0;
    for (i = primeiro->prox; i != NULL; i = i->prox)
    {
        printf("[%d]", j++);
        imprimir(&i->jogador);
    }
}

void inserir_media(Jogador x)
{
    inserir(x);

    int soma = 0;
    int quantidade = 0;
    float media = 0;
    Celula *i;
    for (i = primeiro->prox; i != NULL; i = i->prox){
        soma += i->jogador.altura;
    }
    media = (float) soma / (pos);
    media = round(media);
    printf("%.0f\n", media);
}

bool isVazia()
{
    return (primeiro == ultimo);
}

// MAIN --------------------------------------------------------------------------------

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

    start(); // inicio da lista

    for (int i = 0; i < numEntrada_id; i++)
    {
        setJogador(&_Jogadores[i], saida[entrada_inteiro[i]]); // criação dos jogadores e inserção na lista
        inserir_media(_Jogadores[i]);
    }

    int qtd;
    scanf("%d", &qtd); // leitura da qtd de operações

    char *linha = (char *)malloc(sizeof(char) * TAM_MAX_LINHA);
    int id, pos;

    for (int i = 0; i < qtd; i++)
    { // teste das operações
        scanf("%s", linha);

        if (strcmp(linha, "I") == 0)
        {
            scanf("%d", &id);

            Jogador j;
            setJogador(&j, saida[id]);
            inserir_media(j);
        }
        else if (strcmp(linha, "R") == 0)
        {
            Jogador j = remover();
            printf("(R) %s\n", j.nome);
        }
    }
    mostrar();
}
