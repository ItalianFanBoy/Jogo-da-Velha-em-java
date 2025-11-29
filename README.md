# Jogo da Velha em Java

Uma versão completa do jogo da velha com interface Swing, placar e modo contra IA (minimax).

## Como executar

1. Compile os arquivos:
   ```bash
   javac -d out $(find src/main/java -name "*.java")
   ```
2. Execute a aplicação:
   ```bash
   java -cp out com.example.tictactoe.TicTacToeApp
   ```
   Ao abrir, informe a senha **1234** para acessar o jogo.

## Funcionalidades

- Interface gráfica em Swing com destaques para jogadas vencedoras.
- Escolha de quem inicia (X ou O) e botão de nova partida.
- Placar persistente durante a sessão para vitórias de X, O e empates.
- Modo solo contra IA usando minimax (joga como O).
- Botão para zerar o placar e recomeçar rapidamente.
