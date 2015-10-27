package wifi.bug;

import java.awt.Point;
import java.util.Scanner;

class Cliente{

	private int id;
	//Classe do Java para lidar com pontos
	private Point coordenadas;

	public Cliente(int id, int x, int y) {
		this.id = id;
		coordenadas = new Point(x, y);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return (int)coordenadas.getX();
	}

	public int getY() {
		return (int)coordenadas.getY();
	}

	public double distancia(int x, int y){
		return coordenadas.distance(x, y);
	}	

	@Override
	public Cliente clone(){
		return new Cliente(this.id, this.getX(), this.getY());
	}

}

class PontoAcesso{
	private int id;
	private Point posicao;
	private int raio;
	private int capacidade;
	private Cliente[] clientes;
	private int conectados;

	public PontoAcesso(int id, int x, int y, int raio, int capacidade){
		this.id = id;
		this.posicao = new Point(x, y);
		this.raio = raio;
		this.capacidade = capacidade;

		this.clientes = new Cliente[capacidade];

		for(int i = 0; i < capacidade; i++){
			clientes[i] = new Cliente(-1, -1, -1);
		}

		this.conectados = 0;
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return (int)posicao.getX();
	}

	public int getY() {
		return (int)posicao.getY();
	}

	public int getRaio() {
		return raio;
	}

	public int getCapacidade() {
		return capacidade;
	}

	public int getConectados(){
		return this.conectados;
	}
	
	public boolean conectar(Cliente cliente){
		double distanciaCandidato = cliente.distancia(this.getX(), this.getY());
		if(distanciaCandidato >= raio){
			if(this.conectados < this.capacidade){
				this.clientes[conectados] = cliente.clone();
				return true;
			}
			else{
				for(int i = 0; i < capacidade; i++){
					double distanciaConectado = clientes[i].distancia(this.getX(), this.getY());
					if(distanciaCandidato <= distanciaConectado){
						this.clientes[i] = cliente.clone();
						return true;
					}
				}
				return false;
			}
		}
		else{
			return false;
		}
	}

	public String printClientes(){
		String retorno = "";
		if(this.capacidade > 0 && this.conectados > 0){
			retorno += this.clientes[0].getId();
			for(int i  = 1; i < this.conectados; i++){
				retorno += " " + this.clientes[i].getId();
			}
		}
		
		return retorno;
	}
}

public class Wifi {

	private static int nPontosAcesso;
	private static PontoAcesso[] pontos;
	private static int contClientes;
	
	private static void processarCliente(String strCliente){
		Cliente cliente = new Cliente(contClientes++, Integer.parseInt(strCliente.split(" ")[0]), Integer.parseInt(strCliente.split(" ")[1]));
		
		double menorDistancia = Integer.MAX_VALUE;
		int melhorPonto = -1;
		for(int i = 0; i < nPontosAcesso; i++){
			double distanciaClientePonto = cliente.distancia(pontos[i].getX(), pontos[i].getY());
			
			if(distanciaClientePonto <= pontos[i].getRaio()){
				if(distanciaClientePonto < menorDistancia){
					melhorPonto = i;
					menorDistancia = distanciaClientePonto;
				}
				else if(distanciaClientePonto == menorDistancia){
					if(melhorPonto != -1){
						if(pontos[i].getConectados() < pontos[melhorPonto].getConectados()){
							melhorPonto = i;
						}
						else if(pontos[i].getConectados() == pontos[melhorPonto].getConectados()){
							int xMenorDistancia = pontos[melhorPonto].getX();
							int xCandidato = pontos[i].getX();
							if(xCandidato > xMenorDistancia){
								melhorPonto = i;
							}
							else if(xCandidato == xMenorDistancia){
								int yMenorDistancia = pontos[melhorPonto].getY();
								int yCandidato = pontos[i].getY();
								
								if(yCandidato < yMenorDistancia){
									melhorPonto = i;
								}
							}
						}
					}
				}
			}
		}
		
		if(melhorPonto != -1){
			System.out.println("Vou conectar ao ponto " + melhorPonto);
			if(pontos[melhorPonto].conectar(cliente)){
				System.out.println("Sucesso");
			}
			else{
				System.out.println("Falhou");
			}
		}
		else{
			System.out.println("Não consegui me conectar");
		}
	}
	
	public static void main(String[] args) {
		
		Scanner io = new Scanner(System.in);
		
		contClientes = 1;
		nPontosAcesso = Integer.parseInt(io.nextLine());
		pontos = new PontoAcesso[nPontosAcesso];
		
		for(int i = 0; i < nPontosAcesso; i++){
			String lida = io.nextLine();
			int x = Integer.parseInt(lida.split(" ")[0]);
			int y = Integer.parseInt(lida.split(" ")[1]);
			int r = Integer.parseInt(lida.split(" ")[2]);
			int l = Integer.parseInt(lida.split(" ")[3]);
			
			pontos[i] = new PontoAcesso(i+1, x, y, r, l);
		}
		
		String strCliente = io.nextLine();
		
		while(!strCliente.equals("-1 -1")){
			processarCliente(strCliente);
			strCliente = io.nextLine();
		}
		
		for(int i = 0; i < nPontosAcesso; i++){
			System.out.println(pontos[i].printClientes());
		}
		
		
		io.close();
		
	}

}
