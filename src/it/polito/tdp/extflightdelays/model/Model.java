package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;
import it.polito.tdp.extflightdelays.db.Rotta;

public class Model {
	SimpleWeightedGraph<Airport, DefaultWeightedEdge> graph;
	Map<Integer, Airport> aIdMap;
	Map<Airport, Airport> visit;
	
	public Model() {
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		aIdMap = new HashMap<Integer, Airport>();
		
	}
	
	public void creaGrafo(int distanzaMedia) {
		ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO();
		dao.loadAllAirports(aIdMap);
		
		
		// Aggiungo i vertici
		Graphs.addAllVertices(graph, aIdMap.values());
		
		for(Rotta r : dao.loadRotte(aIdMap, distanzaMedia)) {
			//Voglio solo un arco tra due rotte
			
			DefaultWeightedEdge edge = graph.getEdge(r.getSource(), r.getTarget());
			if (edge == null) {
				Graphs.addEdge(graph, r.getSource() , r.getTarget(), r.getAvgDistance());
			} else {
				double peso = graph.getEdgeWeight(edge);
				double newPeso = (peso + r.getAvgDistance()) / 2;
				graph.setEdgeWeight(edge, newPeso);
			}
		}
		
		System.out.println("Grafo creato. Vertici "+graph.vertexSet().size()+" Archi "+graph.edgeSet().size());
	}
	
	// Controllare se due sono linkati faccio una ricerca a partire dal primo e vedo se il secondo e' stato visitato
	
	public Boolean testLink(Integer a1, Integer a2) {
		Set<Airport> visited = new HashSet<Airport>();
		Airport partenza = aIdMap.get(a1);
		Airport arrivo = aIdMap.get(a2);
		
		System.out.println("Cerco percorso tra "+partenza.getAirportName()+" e "+arrivo.getAirportName());
		
		BreadthFirstIterator<Airport, DefaultWeightedEdge> it = new BreadthFirstIterator<>(this.graph, partenza);
	
		while (it.hasNext()) {
			visited.add(it.next());
		}
		
		if(visited.contains(arrivo))
			return true;
		else
			return false;
		
	}
	
	public List<Airport> findWalk(Integer a1, Integer a2) {
		visit = new HashMap<Airport, Airport>();
		List<Airport> walk = new ArrayList<Airport>();
		Airport partenza = aIdMap.get(a1);
		Airport arrivo = aIdMap.get(a2);
		BreadthFirstIterator<Airport, DefaultWeightedEdge> it = new BreadthFirstIterator<>(this.graph, partenza);
		
		//Ricordati di aggiungere nodo radice
		visit.put( partenza, null);
		
		it.addTraversalListener(new TraversalListener<Airport, DefaultWeightedEdge>() {

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> ev) {
				Airport source = graph.getEdgeSource(ev.getEdge());
				Airport target = graph.getEdgeTarget(ev.getEdge());
				
				if(!visit.containsKey(target) && visit.containsKey(source)) {
					visit.put(target, source);
				} else if(!visit.containsKey(source) && visit.containsKey(target)) {
					visit.put(source, target);
				}
				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Airport> arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Airport> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		while(it.hasNext())
			it.next();
		
		if(!visit.containsKey(partenza) || !visit.containsKey(arrivo)) {
			return null;
		}
		
		Airport step = arrivo;
		while (step!= null) {
			walk.add(step);
			step = visit.get(step);
		}
		
		return walk;
	}

}
