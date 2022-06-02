package br.com.tictactoe.core;

import java.io.IOException;

import br.com.tictactoe.Constants;
import br.com.tictactoe.UI.UI;
import br.com.tictactoe.score.FileScoreManager;
import br.com.tictactoe.score.ScoreManager;

public class Game {

	private Board board = new Board();
    private Player[] players = new Player[Constants.SYMBOL_PLAYERS.length];
    private int currentPlayerIndex = -1;
    private ScoreManager scoreManager;
    	
	public void play() throws IOException {
		scoreManager =  cretaeScoreManager();
		
		
		UI.printGameTitle();
		
		for (int i = 0; i < players.length; i++) {
			players[i] = createPlayer(i);
		}
		
		boolean gameEnded = false;
		Player currentPlayer = nextPlayer();
		Player winner = null;
		
		while(!gameEnded) {
			board.print();
		
			boolean sequenceFound; 
			try {
			 sequenceFound = currentPlayer.play();
			
			} catch (invaliMoveException e) {
				UI.printText("ERRO:" + e.getLocalizedMessage());
				continue;
			}
			
			if(sequenceFound) {
				gameEnded = true;
				winner = currentPlayer;
				
			} else if(board.isFull()){
				gameEnded = true;
			} else {
			currentPlayer = nextPlayer();
			}
		}
		
		if (winner == null) {
			 UI.printText("O jogo terminou empatado");
		} else {
			UI.printText("O jogador '" + winner.getName() + "' venceu o jogo!");
			
			scoreManager.saveScore(winner);
		}
		
		board.print();
		UI.printText("Fim do jogo!");
	}
	
	private Player createPlayer(int index) {
		String name = UI.readInput("Jogador " + (index + 1) + " =>");
		char symbol = Constants.SYMBOL_PLAYERS[index];
		Player player = new Player(name, board, symbol);
		
		
		Integer score = scoreManager.getScore(player);
		
		if (score !=null) {
			UI.printText("O jogador '" + player.getName() + "' j� possui " + score + " vit�ria(s)!");
		}
	
		UI.printText("o jogador '" + name + "' vai usar o s�mbolo'" + symbol + "'");
	
		return player;
	}
	private Player nextPlayer() {
		currentPlayerIndex = (currentPlayerIndex +1) % players.length;
		return players[currentPlayerIndex];
	}
	
	private ScoreManager cretaeScoreManager() throws IOException {
		return new FileScoreManager();
	}
}