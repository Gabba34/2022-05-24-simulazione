package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	ItunesDAO dao;
	List<Genre> generi;
	List<Track> tracceByGenere;
	Graph<Track, DefaultWeightedEdge> grafo;
	List<Adiacenza> adiacenze;
	List<Track> tracceMigliori;
	
	public Model() {
		dao = new ItunesDAO();
		generi = new ArrayList<>();
		tracceByGenere = new ArrayList<>();
		adiacenze = new ArrayList<>();
		tracceMigliori = new ArrayList<>();
	}

	public List<Genre> getGeneri(){
		generi = new ArrayList<>(dao.getAllGenres());
		return generi;
	}
	
	public List<Track> getTracks(Genre genere){ // Vertici del grafo
		tracceByGenere = new ArrayList<>(dao.getTrackByGenre(genere));
		return tracceByGenere;
	}
	
	public String creaGrafo() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, tracceByGenere);
		for(Adiacenza a: adiacenze) {
			Graphs.addEdge(this.grafo, a.getT1(), a.getT2(), a.getPeso());
		}
		return "Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi.";
	}
	
	public List<Adiacenza> adiacenze(Genre g){ // Archi del grafo
		Map<Integer, Track> tracceMap = new HashMap<>();
		for(Track track : tracceByGenere) {
			tracceMap.put(track.getTrackId(), track);
		}
		adiacenze = new ArrayList<>(dao.getAdiacenze(g, tracceMap));
		return adiacenze;
	}
	
	public String adiacenzaMax(){
		List<Adiacenza> max = new ArrayList<>();
		List<String> result = new ArrayList<>();
		double m = 0.0;
		for(Adiacenza a: adiacenze) {
			if(!max.isEmpty()) {
				if(a.getPeso()>m) { 
					max.remove(max.size()-1);
					result.remove(result.size()-1);
					m = a.getPeso();
					max.add(a);
					result.add(new String(a.getT1().getName()+" *** "+a.getT2().getName()+" -> "+a.getPeso()));
				} else if (a.getPeso()==m){
					m = a.getPeso();
					max.add(a);
					result.add(new String(a.getT1().getName()+" *** "+a.getT2().getName()+" -> "+a.getPeso()));
				}
			} else {
				m = a.getPeso();
				max.add(a);
				result.add(new String(a.getT1().getName()+" *** "+a.getT2().getName()+" -> "+a.getPeso()));
			}
		}
		String r ="";
		for(String s: result) {
			r+=s;
		}
		return r;
	}
	
	// ricorsione
	public List<Track> cercaLista(Track traccia, int dimensione){
		tracceMigliori = new ArrayList<>();
		Set<Track> componenteConnessa;
		ConnectivityInspector<Track, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.grafo);
		componenteConnessa = ci.connectedSetOf(traccia);
		List<Track> tracceValide = new ArrayList<>();
		tracceValide.add(traccia);
		componenteConnessa.remove(traccia);
		tracceValide.addAll(componenteConnessa);
		List<Track> parziale = new ArrayList<>();
		parziale.add(traccia);
		cerca(parziale,tracceValide, dimensione, 1);
		return tracceMigliori;
	}

	private void cerca(List<Track> parziale, List<Track> tracceValide, int dimensione, int livello) {
		if(sommaMemoria(parziale)>dimensione) {
			return;
		}
		if(parziale.size()>tracceMigliori.size()) {
			tracceMigliori = new ArrayList<>(parziale);
		}
		if(livello==tracceValide.size()) {
			return;
		}
		parziale.add(tracceValide.get(livello));
		cerca(parziale, tracceValide, dimensione, livello+1);
		parziale.remove(tracceValide.get(livello));
		cerca(parziale, tracceValide, dimensione, livello+1);
	}

	private int sommaMemoria(List<Track> parziale) {
		int somma = 0;
		for(Track t: parziale) {
			somma+=t.getBytes();
		}
		return somma;
	}
	
	public List<Track> getVertex(){
		return new ArrayList<>(this.grafo.vertexSet());
	}
}
