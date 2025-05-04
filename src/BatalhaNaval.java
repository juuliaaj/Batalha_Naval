import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class BatalhaNaval {

    public static int TAMANHO_MAPA = 10;
    public static int [] TAMANHOS_NAVIOS = { 4, 3, 2, 1 };
    public static int [] QUANTIDADES_NAVIOS = { 1, 2, 3, 4 };
    public static char [][] matrizMapa;
    public static Scanner ler = new Scanner(System.in);
    public static int linha, coluna;

    public static void main(String[] args) {
        System.out.println("=== BEM VINDO À BATALHA NAVAL ====\n");

        int jogarNovamente;
        do{
            prepararMapa();
            prepararNavios();
            exibirMapa();
            iniciarJogo();
            System.out.println("Jogar Novamente? [1 para sim, outro número para sair]");
            jogarNovamente = ler.nextInt();
        } while(jogarNovamente == 1);
        System.out.println("Obrigado por jogar");
    }

    public static void prepararMapa() {
        System.out.println("O Mapa está sendo inciado. Aguarde um instante.");
        matrizMapa = new char [TAMANHO_MAPA][TAMANHO_MAPA];
        for (int i = 0; i < TAMANHO_MAPA; i++) {
            Arrays.fill(matrizMapa[i], '.');
        }
    }

    public static void prepararNavios (){
        Random aleatorio = new Random();

        for (int j = 0; j < QUANTIDADES_NAVIOS.length; j++) {
            int tamanho = TAMANHOS_NAVIOS[j];
            int quantidade = QUANTIDADES_NAVIOS[j];

            for (int i = 0; i < quantidade;) {
                int posI = aleatorio.nextInt(TAMANHO_MAPA);
                int posJ = aleatorio.nextInt(TAMANHO_MAPA);
                if(inserirNavio(posI, posJ, tamanho)){
                    i++;
                }
            }
        }
    }

    public static boolean inserirNavio(int i, int j, int tamanho) {
        if (i + tamanho <= TAMANHO_MAPA) {
            if (!temNavioColuna(i, j, tamanho)) {
                colocaNavio(i, j, false, tamanho);
                return true;
            }
        } else if (j + tamanho <= TAMANHO_MAPA) {
            if (!temNavioLinha(i, j, tamanho)) {
                colocaNavio(i, j, false, tamanho);
                return true;
            }
        }
        return false;
    }

    public static boolean temNavioColuna(int i, int j, int tamanho){
        return contaPartesNavioColuna (i, j, '#', tamanho) != 0;
    }

    public static int contaPartesNavioColuna(int i, int j, char symbol, int tamanho){
        int contarNavio = 0;
        for (int k = i; k < (i + tamanho); k++) {
            if (matrizMapa[k][j] == symbol) contarNavio++;
        }
        return contarNavio;
    }

    public static void colocaNavio (int i, int j, boolean isRow, int tamanho){
        for (int k = i; k < (i + tamanho) ; k++) {
            if(isRow){
                matrizMapa[j][k] = '#';
            } else {
                matrizMapa[k][j] = '#';
            }
        }
    }

    public static boolean temNavioLinha(int i, int j, int tamanho){
        return contaPartesNavioLinha(i, j, '#', tamanho) != 0;
    }

    public static int contaPartesNavioLinha(int i, int j, char symbol, int tamanho){
        int contarNavio = 0;
        for (int k = j; k < (j + tamanho); k++) {
            if (matrizMapa[i][k] == symbol) contarNavio++;
        }
        return contarNavio;
    }

    public static void exibirLinha(char [] tab){
        for (int i = 0; i < tab.length; i++) {
            if(tab[i] == '#'){
                System.out.print(". ");
            } else{
                System.out.print(tab[i] + " ");
            }
            System.out.println();
        }
    }

    public static void exibirMapa (){
        for (int i = 0; i < TAMANHO_MAPA; i++) {
            System.out.print((char) (i+65) + " |");
            exibirLinha(matrizMapa[i]);
        }
        exibeLinhaInferiorMapa();
    }

    public static void exibeLinhaInferiorMapa(){
        System.out.print("----------------\n  ");
        for (int i = 0; i < TAMANHO_MAPA; i++) {
            System.out.print((i+1) + " ");
        }
        System.out.println("\n");
    }
}

