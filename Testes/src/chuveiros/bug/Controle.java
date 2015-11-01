package chuveiros.bug;

import java.util.Scanner;

class Chuveiro{
	private int vazao;
	private int tempo;

	public Chuveiro(int vazao, int tempo){
		this.vazao = vazao;
		this.tempo = tempo;
	}

	public int getVazao() {
		return vazao;
	}

	public int getTempo() {
		return tempo;
	}

	@Override
	public Chuveiro clone(){
		Chuveiro este = new Chuveiro(this.vazao, this.tempo);
		return este;
	}
}

class Predio{
	private Chuveiro[] chuveiros;
	private int[] tempoEntrada;
	private int ocupados;
	private int numChuveiros;
	private int capacidade;
	private int utilizado;
	private int tMinimo;
	private int tempo;

	public Predio(int capacidade, int tMinimo, int numChuveiros){
		this.capacidade = capacidade;
		this.tMinimo = tMinimo;
		this.numChuveiros = numChuveiros;

		this.ocupados = 0;
		this.utilizado = 0;
		this.tempo = 0;

		this.chuveiros = new Chuveiro[numChuveiros];
		this.tempoEntrada = new int[numChuveiros];
		for(int i = 0; i < numChuveiros; i++){
			chuveiros[i] = new Chuveiro(-1, -1);
			tempoEntrada[i] = Integer.MIN_VALUE;
		}
	}

	public int getOcupados() {
		return ocupados;
	}

	public int getNumChuveiros() {
		return numChuveiros;
	}

	public int getCapacidade() {
		return capacidade;
	}

	public int getUtilizado() {
		return utilizado;
	}

	public int gettMinimo() {
		return tMinimo;
	}

	public void avancarTempo(){
		this.tempo++;

		for(int i = 0; i < numChuveiros; i++){
			if(this.tempoEntrada[i] != Integer.MIN_VALUE){
				if(this.tempo >= this.tempoEntrada[i]){
					this.tempoEntrada[i] = Integer.MIN_VALUE;
					this.utilizado -= this.chuveiros[i].getVazao();
				}
			}
		}

	}

	public boolean liberarChuveiro(Chuveiro chuveiro){
		int indiceChuveiroMaisAntigo = -1;
		int tempoMaisAntigo = Integer.MAX_VALUE;
		//passa todos os chuveiros
		for(int i = 0; i < numChuveiros; i++){
			//pega somente os que estao a mais tempo que o minimo
			if(this.tempo - this.tempoEntrada[i] >= this.tMinimo){
				//se desligar esse chuveiro, o novo tem que entrar
				if(this.chuveiros[i].getVazao() >= chuveiro.getVazao()){
					if(this.tempoEntrada[i] < tempoMaisAntigo){
						tempoMaisAntigo = this.tempoEntrada[i];
						indiceChuveiroMaisAntigo = i;
					}
				}
			}
		}	

		if(indiceChuveiroMaisAntigo != -1){
			this.tempoEntrada[indiceChuveiroMaisAntigo] = Integer.MIN_VALUE;
			this.utilizado += this.chuveiros[indiceChuveiroMaisAntigo].getVazao();
			return true;
		}
		return false;
	}

	public boolean ligar(Chuveiro chuveiro){
		if(this.ocupados < this.numChuveiros){
			if(chuveiro.getVazao() <= (this.capacidade - this.utilizado)){
				for(int i = 0; i < numChuveiros; i++){
					if(tempoEntrada[i] == Integer.MIN_VALUE){
						this.chuveiros[i] = chuveiro.clone();
						this.tempoEntrada[i] = this.tempo;
						this.utilizado += chuveiro.getVazao();
						i = numChuveiros;
						return true;
					}
				}
			}
			else{
				int tempoMaisAntigo = tempo;
				int indiceMaisAntigoLigado = -1;
				for(int i = 0; i < numChuveiros; i++){
					if(this.tempo - this.tempoEntrada[i] >= this.tMinimo){
						if(tempoEntrada[i] < tempoMaisAntigo){
							tempoMaisAntigo = tempoEntrada[i];
							indiceMaisAntigoLigado = i;
						}
					}
				}
				if(indiceMaisAntigoLigado != -1){
					this.utilizado -= chuveiros[indiceMaisAntigoLigado].getVazao();
					this.chuveiros[indiceMaisAntigoLigado] = chuveiro.clone();
					this.tempoEntrada[indiceMaisAntigoLigado] = this.tempo;
					this.utilizado += chuveiro.getVazao();
					return true;
				}
				else
					return false;
			}
		}
		else{
			if(liberarChuveiro(chuveiro)){
				return ligar(chuveiro);
			}
		}
		return false;
	}
}


public class Controle {

	public static void main(String[] args) {

		Scanner io = new Scanner(System.in);		
		String primeira = io.nextLine();

		Predio p = null;

		String[] informacoesPredio = primeira.split(" ");
		if(informacoesPredio.length == 3){
			p = new Predio(Integer.parseInt(informacoesPredio[0]), Integer.parseInt(informacoesPredio[1]), Integer.parseInt(informacoesPredio[2]));
		}

		if(p != null){
			String apartamento = io.nextLine();
			while(!apartamento.equals("-1 -1")){
				p.avancarTempo();
				String[] chuveiro = apartamento.split(" ");
				Chuveiro novo = new Chuveiro(Integer.parseInt(chuveiro[0]), Integer.parseInt(chuveiro[1]));
				if(p.ligar(novo)){
					System.out.println("SIM utilizando " + p.getUtilizado());
				}
				else{
					System.out.println("NAO");
				}

				apartamento = io.nextLine();
			}
		}

		io.close();

	}

}
