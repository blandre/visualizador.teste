package muppet.bug;

import java.util.Scanner;

class Evento{
	private int streamID;
	private int key;
	private int timestamp;

	public Evento(int streamID, int key, int timestamp) {
		this.streamID = streamID;
		this.key = key;
		this.timestamp = timestamp;
	}

	public int getStreamID() {
		return streamID;
	}

	public int getKey() {
		return key;
	}

	public int getTimestamp() {
		return timestamp;
	}

	@Override
	public Evento clone(){
		return new Evento(this.streamID, this.key, this.timestamp);
	}
}

class Fila{
	private int capacidade;
	private Evento[] fila;
	private int primeiro;
	private int ocupados;

	public Fila(int capacidade){
		this.capacidade = capacidade;
		this.primeiro = 0;
		this.ocupados = 0;

		this.fila = new Evento[capacidade];
		for(int i = 0; i < capacidade; i++){
			this.fila[i] = new Evento(-1, -1, -1);
		}
	}

	public int getCapacidade() {
		return capacidade;
	}

	public int getPrimeiro() {
		return primeiro;
	}

	public int getOcupados() {
		return ocupados;
	}	

	public int getKeyPrimeiro(){
		int key = this.fila[primeiro].getKey();
		return key;
	}

	public boolean inserir(Evento evento){
		boolean retorno = false;
		if(this.ocupados < this.capacidade){
			this.fila[(primeiro+ocupados)%capacidade] = evento.clone();
			this.ocupados++;
			retorno = true;
		}

		return retorno;
	}

	public void processar(){		
		if(this.ocupados > 0){
			this.primeiro = (this.primeiro + 1)%this.capacidade;
			this.ocupados--;
		}
	}

}

public class Filas {
	private static int streamAlvo;
	private static int numWorkers;
	private static int capacidadeWorker;
	private static int muitoVazio;
	private static int tempoAtual;

	private static Fila[] workers;

	private static int filaPrimaria(int key){
		int fila = key % numWorkers;
		return fila;
	}

	public static void main(String[] args) {

		tempoAtual = 0;

		Scanner io = new Scanner(System.in);

		String inicial = io.nextLine();

		streamAlvo = Integer.parseInt(inicial.split(" ")[0]);
		numWorkers = Integer.parseInt(inicial.split(" ")[1]);
		capacidadeWorker = Integer.parseInt(inicial.split(" ")[2]);
		muitoVazio = Integer.parseInt(inicial.split(" ")[3]);

		workers = new Fila[numWorkers];
		for(int i = 0; i < numWorkers; i++){
			workers[i] = new Fila(capacidadeWorker);
		}

		String strEvento = io.nextLine();
		while(!strEvento.equals("-1 -1 -1")){
			int stream = Integer.parseInt(strEvento.split(" ")[0]);
			int timestamp = Integer.parseInt(strEvento.split(" ")[1]);
			/*if(timestamp > tempoAtual){
				tempoAtual = timestamp;
			}*/
			while(tempoAtual < timestamp){
				for(int i = 0; i < numWorkers; i++){
					workers[i].processar();
				}
				tempoAtual++;
			}

			if(stream == streamAlvo){


				if(timestamp == tempoAtual){
					Evento evento = new Evento(stream, Integer.parseInt(strEvento.split(" ")[2]), timestamp);
					int filaPrimaria = filaPrimaria(evento.getKey());
					int filaSecundaria = (filaPrimaria +1)%numWorkers;

					if(workers[filaPrimaria].getKeyPrimeiro() == evento.getKey() && workers[filaPrimaria].getOcupados() < workers[filaPrimaria].getCapacidade()){
						
						System.out.println("Primaria processando igual");
						if(workers[filaPrimaria].inserir(evento.clone())){
							System.out.println(filaPrimaria);
						}
						else{
							System.out.println("NÃO");
						}

					}
					else if(workers[filaSecundaria].getKeyPrimeiro() == evento.getKey() && workers[filaSecundaria].getOcupados() < workers[filaSecundaria].getCapacidade()){
						System.out.println("Secundaria processando igual");
						if(workers[filaSecundaria].inserir(evento.clone())){
							System.out.println(filaSecundaria);
						}
						else{
							System.out.println("NÃO");
						}						

					}
					else{
						if(workers[filaSecundaria].getOcupados() < muitoVazio){
							if(workers[filaSecundaria].inserir(evento.clone())){
								System.out.println(filaSecundaria);
							}
							else{
								System.out.println("NÃO");
							}
						}
						else{
							if(workers[filaPrimaria].getOcupados() == workers[filaPrimaria].getCapacidade()){
								if(workers[filaSecundaria].getOcupados() < workers[filaSecundaria].getCapacidade()){
									if(workers[filaSecundaria].inserir(evento.clone())){
										System.out.println(filaSecundaria);
									}
									else{
										System.out.println("NÃO");
									}
								}
								else{
									System.out.println("NÃO");
								}
							}
							else{
								if(workers[filaPrimaria].inserir(evento.clone())){
									System.out.println(filaPrimaria);
								}
								else{
									System.out.println("NÃO");
								}	
							}
						}
					}
				}
				else{
					System.out.println("NÃO");
				}
			}
			else{

				System.out.println("NÃO");
			}

			strEvento = io.nextLine();

		}


	}

}
