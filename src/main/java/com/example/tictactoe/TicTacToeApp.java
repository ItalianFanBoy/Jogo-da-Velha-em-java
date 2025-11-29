package com.example.tictactoe;

import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public final class TicTacToeApp {
    private TicTacToeApp() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (authenticate()) {
                TicTacToeFrame frame = new TicTacToeFrame();
                frame.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }

    private static boolean authenticate() {
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(
                null,
                passwordField,
                "Digite a senha para acessar o jogo",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION && "1234".equals(new String(passwordField.getPassword()))) {
            return true;
        }

        JOptionPane.showMessageDialog(null, "Senha incorreta. Encerrando aplicação.");
        return false;
    }
}
