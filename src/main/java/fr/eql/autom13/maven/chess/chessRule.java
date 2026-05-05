package fr.eql.autom13.maven.chess;

import java.util.Scanner;

public class chessRule {

    private static final int SIZE = 8;
    private char[][] chessBoard = new char[SIZE][SIZE];
    private Scanner scanner = new Scanner(System.in);
    private boolean whitePlayer;

    public chessRule(boolean whitePlayer) {
        this.whitePlayer = whitePlayer;
        initChess();
    }

    public void startGame() {
        boolean continuer = true;
        ChessDisplay.displayChessBoard(chessBoard, whitePlayer);
        System.out.println("Vous jouez les " + (whitePlayer ? "Blancs (majuscules)." : "Noirs (majuscules)."));
        System.out.println("Entrez vos coups au format : e2 e4");
        System.out.println("Tapez 'quitter' pour arrêter la partie.");

        while (continuer) {
            System.out.print("Votre coup : ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("quitter")) {
                continuer = false;
                continue;
            }

            String[] coup = input.split(" ");
            if (coup.length != 2) {
                System.out.println("Format invalide. Utilisez le format : e2 e4");
                continue;
            }

            String start = coup[0];
            String finish = coup[1];

            if (!isValide(start) || !isValide(finish)) {
                System.out.println("Position invalide. Utilisez des coordonnées comme a1, h8, etc.");
                continue;
            }

            int x1 = start.charAt(0) - 'a';
            int y1 = SIZE - (start.charAt(1) - '0');
            int x2 = finish.charAt(0) - 'a';
            int y2 = SIZE - (finish.charAt(1) - '0');

            char piece = chessBoard[y1][x1];
            if (piece == ' ') {
                System.out.println("Aucune pièce à cette position.");
                continue;
            }

            boolean pieceOfPlayer = (whitePlayer && Character.isUpperCase(piece)) ||
                    (!whitePlayer && Character.isLowerCase(piece));
            if (!pieceOfPlayer) {
                System.out.println("Cette pièce ne vous appartient pas.");
                continue;
            }

            if (moveIsValide(x1, y1, x2, y2, piece)) {
                chessBoard[y2][x2] = piece;
                chessBoard[y1][x1] = ' ';
                ChessDisplay.displayChessBoard(chessBoard, whitePlayer);
            } else {
                System.out.println("Déplacement invalide pour cette pièce.");
                ChessDisplay.displayChessBoard(piece);
            }
        }
        scanner.close();
    }

    private void initChess() {
        // Pièces noires
        chessBoard[0][0] = 't';
        chessBoard[0][1] = 'c';
        chessBoard[0][2] = 'f';
        chessBoard[0][3] = 'd';
        chessBoard[0][4] = 'r';
        chessBoard[0][5] = 'f';
        chessBoard[0][6] = 'c';
        chessBoard[0][7] = 't';
        for (int i = 0; i < SIZE; i++) {
            chessBoard[1][i] = 'p';
        }

        // Pièces blanches
        chessBoard[7][0] = 'T';
        chessBoard[7][1] = 'C';
        chessBoard[7][2] = 'F';
        chessBoard[7][3] = 'D';
        chessBoard[7][4] = 'R';
        chessBoard[7][5] = 'F';
        chessBoard[7][6] = 'C';
        chessBoard[7][7] = 'T';
        for (int i = 0; i < SIZE; i++) {
            chessBoard[6][i] = 'P';
        }

        // Cases vides
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < SIZE; j++) {
                chessBoard[i][j] = ' ';
            }
        }
    }

    private boolean isValide(String pos) {
        if (pos.length() != 2) return false;
        char col = pos.charAt(0);
        char row = pos.charAt(1);
        return col >= 'a' && col <= 'h' && row >= '1' && row <= '8';
    }

    private boolean moveIsValide(int x1, int y1, int x2, int y2, char piece) {
        piece = Character.toLowerCase(piece);
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        switch (piece) {
            case 'p':
                if (whitePlayer) {
                    return (x1 == x2 && y2 == y1 - 1);
                } else {
                    return (x1 == x2 && y2 == y1 + 1);
                }
            case 't':
                return (x1 == x2 || y1 == y2) && !(dx == 0 && dy == 0);
            case 'c':
                return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
            case 'f':
                return dx == dy && dx > 0;
            case 'd':
                return (x1 == x2 || y1 == y2 || dx == dy) && !(dx == 0 && dy == 0);
            case 'r':
                return dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0);
            default:
                return false;
        }
    }
}
