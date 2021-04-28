package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	Map<Integer, ArtObject> idMap;
	
	public Model() {
		dao=new ArtsmiaDAO();
		idMap=new HashMap<Integer, ArtObject>();
	}
	
	public void creaGrafo() {
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Aggiunta vertici
		//1_recupero tutti gli ArtObject dal DB (potrei dover 'filtrare' i vertici in altre situazioni)
		//2_li inserisco come vertici
		dao.listObjects(idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		
		//Aggiunta archi
		//--APPROCCIO 1 (OK se #vertici MOLTO basso, altrimenti non performante) 
		//doppio ciclo for sui vertici
		//dati due vertici controllo se devono essere collegati
		/*
		for(ArtObject a1:this.grafo.vertexSet()) {
			for(ArtObject a2:this.grafo.vertexSet()) {
				if(!a1.equals(a2) && !this.grafo.containsEdge(a1, a2)) {
					//devo collegare a1 ad a2?
					int peso=dao.getPeso(a1, a2);
					if(peso>0) {
						Graphs.addEdge(this.grafo, a1, a2, peso);
					}
				}
			}
		} //fine for esterno
		*/
		//APPROCCIO 2 (approccio intermedio)
		//blocco un vertice e vedo a quali altri è collegato
		
		//APPROCCIO 3 (Se ho più di 20 vertici)
		//Non blocco niente, mi faacio passare dal dao le coppie con relativo peso
		for(Adiacenza a:dao.getAdiacenze()) {
				Graphs.addEdge(this.grafo, idMap.get(a.getId1()), idMap.get(a.getId2()), a.getPeso());
		}
		
		System.out.println("Grafo creato");
		System.out.println("#vertici: "+grafo.vertexSet().size());
		System.out.println("#archi: "+grafo.edgeSet().size());

	}

}
