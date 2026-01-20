//***************************************************************
// TODO: This is mostly done for you. See the "YOUR CODE HERE" spots below.
//***************************************************************

import edu.uci.ics.jung.graph.Graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.ArrayList;

/**
 *  This algorithm attempts to solve the TSP problem by
 *  checking all possible travel plans and choosing the
 *  smallest one.
 *  
 *  @author Katherine "Raven" Russell and jan blanca
 */
class TSPPermutations extends TSPAlg {
	//*********************************************************************
	//*******                                                       *******
	//*******   You should not need to edit anything in this part.  *******
	//*******   However, you may need to read everything here.      *******
	//*******                                                       *******
	//*********************************************************************
	
	/**
	 *  A iterator over all permutations of cities (except
	 *  the first city which is "locked in").
	 */
	Iterator<List<City>> permItr;
	
	/**
	 *  The current permutation being examined by the algorithm.
	 */
	List<City> currentPerm = null;
	
	/**
	 *  {@inheritDoc}
	 */
	public void reset(Graph<City, Flight> graph) {
		super.reset(graph);
		permItr = null;
		currentPerm = null;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void start() {
		super.start();
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void finish() {
		System.out.println("TSP Via Permutation:");
		if(visitOrder != null) {
			this.visitOrder.add(0, this.startingCity);
		}
		super.finish();
	}
	
	//*********************************************************************
	//*******                                                       *******
	//*******    You have a number of things to do in this part.    *******
	//*******                                                       *******
	//*********************************************************************
	
	/**
	 *  Get the next permutation. Note that a permutation does
	 *  NOT include the starting city since this should be "locked
	 *  in place". If you include the starting city in the permuations
	 *  you end up examining "rotations" of trips you have already
	 *  looked at.
	 *  
	 *  @return the next permutation of cities (other than the starting city)
	 */
	public List<City> permutate() {
		//YOUR CODE HERE
		
		
		if(permItr == null) {

			ArrayList<City> city = new ArrayList<>(graph.getVertices());

			city.remove(startingCity); //starting city not permutated

			permItr = new PermutationIterator<>(city);

		}


		if(permItr.hasNext() == false) {

			return null;

		}

		return permItr.next();


		
		
		/*
		Basic instructions:
			- if there is not permutation iterator, get one and continue
			- if the permutation iterator doesn't have anything remaining,
				return null
			- otherwise return the next permutation from the permutation
				iterator
		
		Details:
			- The code for the permuation iterator is in PermutationIterator.java
			- It is a basic Java Iterator, so it has everything you'd expect from one of those
			- To create a new permuation iterator, you need to:
				(1) get all the vertices in the graph, the graph is a JUNG graph so see
					http://jung.sourceforge.net/doc/api/edu/uci/ics/jung/graph/Graph.html,
					for documentation, especially the parent interface (HyperGraph) methods
				(2) from the list of vertices, remove the starting city, see description
					of this method for why
				(3) create a permuation iterator for the remaining cities and assign
					it to the appropriate instance variable
		*/
		
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public boolean setupNextStep() {
		//YOUR CODE HERE

		currentPerm = permutate();

		if(currentPerm == null) {

			return false;

		}

		return true;
		
		/*
		Basic instructions:
			- set the current permutation to the next permutation
				(using the method you wrote above to get the next permuation)
			- if there is no next permuation, return false
			- otherwise return true
		*/
		
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void doNextStep() {
		//YOUR CODE HERE
		if(currentPerm == null || currentPerm.isEmpty()) {

			return;

		}

		int loss = 0;

		City previous = startingCity;

		for(int i = 0; i < currentPerm.size(); i++) {

			City next = currentPerm.get(i);

			Flight fly = graph.findEdge(previous, next);

			if(fly != null) {

				fly.setColor(COLOR_ACTIVE_EDGE);

				loss = loss + fly.getCost();

			}

			previous = next;

		}

		Flight backwards = graph.findEdge(previous, startingCity);

		backwards.setColor(COLOR_ACTIVE_EDGE);

		loss = loss + backwards.getCost();

		if(loss < visitOrderCost) {

			visitOrderCost = loss;

			visitOrder = new ArrayList<>(currentPerm);

		}


		/*
		Basic instructions:
			- using the current permuation, determine the cost of the route
			- if it is better than the best route found so far, store it in
				the visit order list and visit order cost (from the parent
				TSPAlg class)
			- while you're doing this, highlight the edges for the current
				permutation
		
		Details:
			- a permutation is just a list of cities (excluding the starting city)
			- to get the cost of a flight between two cities you need to use the graph
			- the graph is a JUNG graph so see http://jung.sourceforge.net/doc/api/edu/uci/ics/jung/graph/Graph.html,
				for documentation, especially the parent interface (HyperGraph) methods
			- an edge in the graph is a flight, so to find out how to get the cost of
				a flight see the Flight class (in Flight.java) and the parent class
				GraphEdge (in GraphEdge.java)
			- to set the color of an edge in the graph, see methods in those same Flight
				and GraphEdge classes (the color should be COLOR_ACTIVE_EDGE from TSPAlg)
			- DO NOT FORGET to add in the cost from the start city to the first city in
				the permutation AND back again!
		*/
	}
}
