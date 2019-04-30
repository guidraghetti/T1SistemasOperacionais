package SJF;

//package SJF;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import com.sun.xml.internal.bind.v2.util.FatalAdapter;

public class Escalanador {

	public static void main(String[] args) throws IOException {
		int fatiaDeTempo;
		int numProcessos;
		int timer = 0;
		int tempoEntradaSaida = 3;
		int trocaContexto = 1;

		FileReader lerArquivo = new FileReader("t2a.txt");
		BufferedReader s = new BufferedReader(lerArquivo);

		ArrayList<Processo> lstProcesso = new ArrayList<Processo>();

		String line;
		line = s.readLine();
		numProcessos = Integer.parseInt(line);
		line = s.readLine();
		fatiaDeTempo = Integer.parseInt(line);
		line = s.readLine();
		int k = 0;
		while (line != null) {
			String[] strs = line.trim().split("\\s+");
			Processo p1 = new Processo();
			p1.nome = "P" + k;
			p1.fatiaDeTempo = fatiaDeTempo;
			for (int i = 0; i < strs.length; i++) {
				if (i == 0) {
					p1.instanteDeChegada = Integer.parseInt(strs[i]);
				} else if (i == 1) {
					p1.unidadesDeTempoRestante = Integer.parseInt(strs[i]);
					timer = timer + p1.unidadesDeTempoRestante;
				} else {
					p1.listEntradaESaida.add(Integer.parseInt(strs[i]));
				}
			}
			line = s.readLine();
			lstProcesso.add(p1);
			k++;
		}
		s.close();
		System.out.print("- C");
		
		while (timer != 0) {

			// ordena a lista por instante de chegada, incluindo entrada e saída
			lstProcesso = compare(lstProcesso);

			// verifica se a fatia de tempo do processo acabou, caso acabe renova a fatia;
			if (lstProcesso.get(0).fatiaDeTempo == 0) {
				lstProcesso.get(0).fatiaDeTempo = fatiaDeTempo;
			}
			// Verifica se as unidades de tempo restante são menor que a fatia de tempo;
			if (lstProcesso.get(0).unidadesDeTempoRestante <= lstProcesso.get(0).fatiaDeTempo) {
				// se for,verifica se o processo tem E/S;
				//
				//
				if (lstProcesso.get(0).listEntradaESaida.size() != 0
						&& lstProcesso.get(0).unidadesDeTempoRestante > lstProcesso.get(0).listEntradaESaida.get(0)) {

					for (int j = 0; j < lstProcesso.get(0).listEntradaESaida.get(0); j++) {
						System.out.print(" " + lstProcesso.get(0).nome);
					}
					// diminui a fatia de tempo do processo
					lstProcesso.get(0).fatiaDeTempo -= lstProcesso.get(0).listEntradaESaida.get(0);
					// timer controla o tempo de execução, quando o timer zerar é porque todos os
					// processsos já executaram;
					timer = timer - lstProcesso.get(0).listEntradaESaida.get(0);
					// diminui as unidades de tempo restante;
					lstProcesso.get(0).unidadesDeTempoRestante -= lstProcesso.get(0).listEntradaESaida.get(0);
					System.out.print(" C");
					if (lstProcesso.size() < 2 && lstProcesso.get(0).listEntradaESaida.size() != 0) {
						System.out.print(" -");
						System.out.print(" C");
					}

					lstProcesso.get(0).instanteDeChegada = trocaContexto + tempoEntradaSaida
							+ lstProcesso.get(0).listEntradaESaida.get(0) + lstProcesso.get(0).instanteDeChegada;
					// remover a primeira entrada e saída após ser utilizada
					lstProcesso.get(0).listEntradaESaida.remove(0);

				} else {
					for (int j = 0; j < lstProcesso.get(0).unidadesDeTempoRestante; j++) {
						System.out.print(" " + lstProcesso.get(0).nome);
					}
					timer = timer - lstProcesso.get(0).unidadesDeTempoRestante;
					System.out.print(" C");

					lstProcesso.remove(0);
				}
			} else {
				// verifica se o processo tem E/S e se as unidades de tempo restante são maior
				// que a E/S;
				if (lstProcesso.get(0).listEntradaESaida.size() != 0
						&& lstProcesso.get(0).unidadesDeTempoRestante > lstProcesso.get(0).listEntradaESaida.get(0)) {
					// verifica se as unidades de tempo até fazer E/S são maior que a fatia de tempo
					// restante do processo;
					if (lstProcesso.get(0).listEntradaESaida.get(0) > lstProcesso.get(0).fatiaDeTempo) {
						// se for, diminui E/S pela fatia de tempo restante;
						Integer ES = (lstProcesso.get(0).listEntradaESaida.get(0) - lstProcesso.get(0).fatiaDeTempo);
						for (int j = 0; j < lstProcesso.get(0).fatiaDeTempo; j++) {
							System.out.print(" " + lstProcesso.get(0).nome);
						}
						// verifica se as unidades de tempo até E/S já foram executadas, se foram remove
						// a E/S da lista de E/S;
						if (ES == 0) {
							lstProcesso.get(0).listEntradaESaida.remove(0);
							// se não, seta a E/S pelo valor do cálculo feito acima;
						} else {
							lstProcesso.get(0).listEntradaESaida.set(0, ES);
						}
						lstProcesso.get(0).unidadesDeTempoRestante -= lstProcesso.get(0).fatiaDeTempo;
						timer = timer - lstProcesso.get(0).fatiaDeTempo;
						System.out.print(" C");
						lstProcesso.get(0).instanteDeChegada = trocaContexto + tempoEntradaSaida
								+ lstProcesso.get(0).listEntradaESaida.get(0) + lstProcesso.get(0).instanteDeChegada;

					} else {

						for (int j = 0; j < lstProcesso.get(0).listEntradaESaida.get(0); j++) {
							System.out.print(" " + lstProcesso.get(0).nome);
						}
						timer = timer - lstProcesso.get(0).listEntradaESaida.get(0);
						lstProcesso.get(0).fatiaDeTempo = lstProcesso.get(0).fatiaDeTempo
								- lstProcesso.get(0).listEntradaESaida.get(0);
						lstProcesso.get(0).unidadesDeTempoRestante -= lstProcesso.get(0).listEntradaESaida.get(0);
						System.out.print(" C");
						if (lstProcesso.size() < 2 && lstProcesso.get(0).listEntradaESaida.size() != 0) {
							System.out.print(" -");
							System.out.print(" C");
						}

						lstProcesso.get(0).instanteDeChegada = trocaContexto + tempoEntradaSaida
								+ lstProcesso.get(0).listEntradaESaida.get(0) + lstProcesso.get(0).instanteDeChegada;
						// remover a primeira entrada e saída após ser utilizada
						lstProcesso.get(0).listEntradaESaida.remove(0);
					}

				} else {
					for (int j = 0; j < lstProcesso.get(0).fatiaDeTempo; j++) {
						System.out.print(" " + lstProcesso.get(0).nome);
					}
					timer = timer - lstProcesso.get(0).fatiaDeTempo;
					lstProcesso.get(0).unidadesDeTempoRestante -= lstProcesso.get(0).fatiaDeTempo;
					lstProcesso.get(0).instanteDeChegada += lstProcesso.get(0).fatiaDeTempo + trocaContexto;
					lstProcesso.get(0).fatiaDeTempo -= lstProcesso.get(0).fatiaDeTempo;
					System.out.print(" C");
				}
			}
		}

	}

	// reorganiza a lista dando preferência ao tempo de chegada;
	public static ArrayList<Processo> compare(ArrayList<Processo> lstProcesso) {

		Collections.sort(lstProcesso, new Comparator<Processo>() {
			@Override
			public int compare(Processo p1, Processo p2) {
				return p1.instanteDeChegada - p2.instanteDeChegada; // Ascending
			}

		});

		return lstProcesso;
	}

}
