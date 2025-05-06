import java.util.Scanner;
import java.util.Random;


public class BatalhaNaval {


    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();



    static final int TAMANHO = 10;
    static final char AGUA = '.';
    static final char NAVIO = '#';
    static final char ERROU = '~';
    static final char ACERTOU = 'x';



    static final int[] TAMANHOS_NAVIOS = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};


    static char[][] mapaJogador1 = new char[TAMANHO][TAMANHO];
    static char[][] mapaJogador2 = new char[TAMANHO][TAMANHO];
    static char[][] tirosJogador1 = new char[TAMANHO][TAMANHO];
    static char[][] tirosJogador2 = new char[TAMANHO][TAMANHO];



    static String nome1;
    static String nome2;
    static boolean contraPC = false;
    static boolean posicionamentoAutomatico = false;



    public static void main(String[] args) {
        System.out.println("==== BATALHA NAVAL ====");


        System.out.println("Digite o nome do Jogador 1:");
        nome1 = scanner.nextLine();


        System.out.println("Deseja jogar contra o computador? (s/n):");
        String resposta = scanner.nextLine();
        if (resposta.equalsIgnoreCase("s")) {
            contraPC = true;
            nome2 = "Computador";
        } else {
            System.out.println("Digite o nome do Jogador 2:");
            nome2 = scanner.nextLine();
        }


        System.out.println("Deseja posicionamento automático dos navios? (s/n):");
        resposta = scanner.nextLine();
        if (resposta.equalsIgnoreCase("s")) {
            posicionamentoAutomatico = true;
        }



        inicializarMapas();
        System.out.println("\nPosicionando navios de " + nome1);
        posicionarNavios(mapaJogador1);

        System.out.println("\nPosicionando navios de " + nome2);
        if (contraPC || posicionamentoAutomatico) {
            posicionarNavios(mapaJogador2);
        } else {
            posicionarNavios(mapaJogador2);
        }



        boolean jogoAtivo = true;
        boolean vezDoJogador1 = true;


        while (jogoAtivo) {
            if (vezDoJogador1) {
                System.out.println("\nVez de " + nome1);
                jogoAtivo = realizarJogada(mapaJogador2, tirosJogador1);
            } else {
                System.out.println("\nVez de " + nome2);
                if (contraPC) {
                    jogoAtivo = jogadaComputador(mapaJogador1, tirosJogador2);
                } else {
                    jogoAtivo = realizarJogada(mapaJogador1, tirosJogador2);
                }
            }

            vezDoJogador1 = !vezDoJogador1;
        }
    }


    public static void inicializarMapas() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                mapaJogador1[i][j] = AGUA;
                mapaJogador2[i][j] = AGUA;
                tirosJogador1[i][j] = AGUA;
                tirosJogador2[i][j] = AGUA;
            }
        }
    }



    public static void posicionarNavios(char[][] mapa) {
        for (int i = 0; i < TAMANHOS_NAVIOS.length; i++) {
            int tamanho = TAMANHOS_NAVIOS[i];
            boolean posicionado = false;


            while (!posicionado) {
                int linha, coluna;
                boolean horizontal;


                if (posicionamentoAutomatico || mapa == mapaJogador2 && contraPC) {
                    linha = random.nextInt(TAMANHO);
                    coluna = random.nextInt(TAMANHO);
                    horizontal = random.nextBoolean();
                } else {
                    System.out.println("Posicionando navio de tamanho " + tamanho);
                    exibirMapa(mapa, true);
                    linha = lerLinha();
                    coluna = lerColuna();
                    System.out.println("Deseja posicionar horizontal? (s/n):");
                    String dir = scanner.next();
                    horizontal = dir.equalsIgnoreCase("s");
                }


                if (podeColocarNavio(mapa, linha, coluna, tamanho, horizontal)) {
                    for (int j = 0; j < tamanho; j++) {
                        if (horizontal) {
                            mapa[linha][coluna + j] = NAVIO;
                        } else {
                            mapa[linha + j][coluna] = NAVIO;
                        }
                    }
                    posicionado = true;
                } else {
                    if (!(posicionamentoAutomatico || mapa == mapaJogador2 && contraPC)) {
                        System.out.println("Posição inválida. Tente novamente.");
                    }
                }
            }
        }
    }


    public static boolean podeColocarNavio(char[][] mapa, int linha, int coluna, int tamanho, boolean horizontal) {
        if (horizontal) {
            if (coluna + tamanho > TAMANHO) return false;
            for (int i = 0; i < tamanho; i++) {
                if (mapa[linha][coluna + i] != AGUA) return false;
            }
        } else {
            if (linha + tamanho > TAMANHO) return false;
            for (int i = 0; i < tamanho; i++) {
                if (mapa[linha + i][coluna] != AGUA) return false;
            }
        }

        return true;
    }


    public static boolean realizarJogada(char[][] mapaInimigo, char[][] tiros) {
        int linha, coluna;
        while (true) {
            exibirMapa(tiros, false);
            System.out.println("Digite a linha (A-J): ");
            linha = lerLinha();
            System.out.println("Digite a coluna (1-10): ");
            coluna = lerColuna();


            if (linha >= 0 && linha < TAMANHO && coluna >= 0 && coluna < TAMANHO) {
                if (tiros[linha][coluna] == AGUA) {
                    if (mapaInimigo[linha][coluna] == NAVIO) {
                        System.out.println("Acertou!");
                        tiros[linha][coluna] = ACERTOU;
                        mapaInimigo[linha][coluna] = ACERTOU;
                        if (todosNaviosAfundados(mapaInimigo)) {
                            System.out.println("Parabéns! Você venceu!");
                            return false;
                        }
                        continue;
                    } else {
                        System.out.println("Errou.");
                        tiros[linha][coluna] = ERROU;
                        return true;
                    }
                } else {
                    System.out.println("Você já atirou aqui.");
                }
            } else {
                System.out.println("Coordenadas inválidas.");
            }
        }
    }



    public static boolean jogadaComputador(char[][] mapaInimigo, char[][] tiros) {
        int linha, coluna;
        while (true) {
            linha = random.nextInt(TAMANHO);
            coluna = random.nextInt(TAMANHO);


            if (tiros[linha][coluna] == AGUA) {
                if (mapaInimigo[linha][coluna] == NAVIO) {
                    System.out.println("O computador acertou em " + (char)(linha + 'A') + (coluna + 1));
                    tiros[linha][coluna] = ACERTOU;
                    mapaInimigo[linha][coluna] = ACERTOU;
                    if (todosNaviosAfundados(mapaInimigo)) {
                        System.out.println("O computador venceu!");
                        return false;
                    }
                    continue;
                } else {
                    System.out.println("O computador errou em " + (char)(linha + 'A') + (coluna + 1));
                    tiros[linha][coluna] = ERROU;
                    return true;
                }
            }
        }
    }


    public static boolean todosNaviosAfundados(char[][] mapa) {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (mapa[i][j] == NAVIO) return false;
            }
        }
        return true;
    }


    public static void exibirMapa(char[][] mapa, boolean mostrarNavios) {
        System.out.print("  ");
        for (int i = 1; i <= TAMANHO; i++) {
            System.out.print(i + " ");
        }
        System.out.println();


        for (int i = 0; i < TAMANHO; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < TAMANHO; j++) {
                char c = mapa[i][j];
                if (!mostrarNavios && c == NAVIO) {
                    System.out.print(AGUA + " ");
                } else {
                    System.out.print(c + " ");
                }
            }
            
            System.out.println();
        }
    }


    public static int lerLinha() {
        while (true) {
            String entrada = scanner.next().toUpperCase();
            if (entrada.length() == 1) {
                char letra = entrada.charAt(0);
                if (letra >= 'A' && letra <= 'J') {
                    return letra - 'A';
                }
            }

            System.out.println("Linha inválida. Use letras de A a J.");
        }
    }


    public static int lerColuna() {
        while (true) {
            try {
                int numero = scanner.nextInt();
                if (numero >= 1 && numero <= 10) {
                    return numero - 1;
                }
            } catch (Exception e) {
                scanner.next();
            }
            System.out.println("Coluna inválida. Use números de 1 a 10.");
        }
    }
}
