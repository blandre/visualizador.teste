package lampadas.bug;

import java.util.Scanner;

public class Lampadas {
	
	private static int numComodos;
	private static int tempoAcesa;
	private static int ultimoTempo;
	private static int mapaComodos[];

	private static boolean movimentoValido(int origem, int destino, int tempo){
		boolean retorno = (origem != -1);
		return retorno;
	}
	
	public static void main(String[] args) {
		Scanner io = new Scanner(System.in);
		
		String primeira = io.nextLine();
		numComodos = Integer.parseInt(primeira.split(" ")[0]);
		tempoAcesa = Integer.parseInt(primeira.split(" ")[1]);
		ultimoTempo = 0;
		
		mapaComodos = new int[numComodos];
		mapaComodos[0] = tempoAcesa;
		
		for(int i = 1; i < numComodos; i++){
			mapaComodos[i] = 0;
		}
		
		String fato = io.nextLine();
		
		int origem = Integer.parseInt(fato.split(" ")[0]);
		int destino = Integer.parseInt(fato.split(" ")[1]);
		int tempo = Integer.parseInt(fato.split(" ")[2]);
		
		while(movimentoValido(origem, destino, tempo)){
			ultimoTempo = tempo;
			mapaComodos[destino] = tempo + tempoAcesa;
			
			fato = io.nextLine();
			
			origem = Integer.parseInt(fato.split(" ")[0]);
			destino = Integer.parseInt(fato.split(" ")[1]);
			tempo = Integer.parseInt(fato.split(" ")[2]);
		}
		
		for(int i = 0; i < numComodos; i++){
			if(mapaComodos[i] >= ultimoTempo)
				System.out.println("1");
			else{
				System.out.println("0");
			}
		}
		
		
		io.close();
	}

}

