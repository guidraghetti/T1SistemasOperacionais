package SJF;

import java.util.ArrayList;
import java.util.List;

public class Processo {
	public String nome;
	public int instanteDeChegada;
	public int fatiaDeTempo;
	public int unidadesDeTempoRestante;
	public ArrayList<Integer> listEntradaESaida;
	
	public Processo() {
		this.listEntradaESaida = new ArrayList<Integer>();

	}
	
	public Processo(int burst, int ut, int fatiaDeTempo) {
		this.fatiaDeTempo = fatiaDeTempo;
		this.instanteDeChegada = burst;
		this.unidadesDeTempoRestante = ut;
		this.listEntradaESaida = new ArrayList<Integer>();
	}
}
