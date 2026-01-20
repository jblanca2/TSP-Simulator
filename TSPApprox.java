//***************************************************************
// TODO: This is mostly done for you. See the "YOUR CODE HERE" spots below.
//***************************************************************

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.algorithms.shortestpath.MinimumSpanningForest;

import java.awt.Color;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 *  This algorithm attempts to solve the TSP problem by
 *  forming a MST and then doing a DFT to choose a flight
 *  path.
 *  
 *  @author Katherine "Raven" Russell and jan blanca
 */
class TSPApprox extends TSPAlg {
	//*********************************************************************
	//*******                                                       *******
	//*******   You should not need to edit anything in this part.  *******
	//*******   However, you may need to read everything here.      *******
	//*******                                                       *******
	//*********************************************************************
	
	/**
	 *  The step number for doing a MST.
	 */
	private static final int STEP_MST = 1;
	
	/**
	 *  The step number for starting the DFT.
	 */
	private static final int STEP_DFT = 2;
	
	/**
	 *  The step number for finishing.
	 *  NOTE: this one gets changed!
	 */
	private int stepDone = 3;
	
	/**
	 *  The current step.
	 */
	private int step = 0;
	
	/**
	 *  The MST found with Prim's algorithm.
	 */
	Forest<City,Flight> tree = null;
	
	/**
	 *  {@inheritDoc}
	 */
	public void reset(Graph<City, Flight> graph) {
		super.reset(graph);
		
		//reset steps
		step = 0;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void start() {
		super.start();
		
		//calculate how many steps of the DFT
		//will be displayed.
		stepDone = STEP_DFT + graph.getVertexCount();
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void finish() {
		System.out.println("TSP Via Approximation:");
		super.finish();
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public boolean setupNextStep() {
		step++;
		return (step != stepDone);
	}
	
	/**
	 *  Helper method to color all edges of a tree.
	 *  
	 *  @param tree the tree to highlight on the graph
	 *  @param c the color to use
	 */
	protected void colorAllEdges(Forest<City,Flight> tree, Color c) {
		for(City c1: tree.getVertices()) {
			for(City c2: tree.getVertices()) {
				if(tree.isNeighbor(c1, c2)) {
					Flight f = graph.findEdge(c1, c2);
					f.setColor(c);
				}
			}
		}
	}
	
	/**
	 *  Helper method to print out one step of the DFT at a time
	 *  to make the simulation more interesting.
	 */
	protected void stepDisplayDFT() {
		colorAllEdges(COLOR_DONE_EDGE_2);
		colorAllEdges(tree, COLOR_ACTIVE_EDGE);
		
		for(int i = 0; i < step-STEP_DFT; i++) {
			Flight f = null;
			
			if(i == stepDone-1) {
				f = graph.findEdge(visitOrder.get(i), this.startingCity);
			}
			else {
				f = graph.findEdge(visitOrder.get(i), visitOrder.get(i+1));
			}
			
			f.setColor(COLOR_DONE_EDGE_1);
		}
	}
	
	//*********************************************************************
	//*******                                                       *******
	//*******    You have a number of things to do in this part.    *******
	//*******                                                       *******
	//*********************************************************************
	
	/**
	 *  {@inheritDoc}`
	 */
	public void doNextStep() {
		//YOUR CODE HERE
		
		/*
		Basic instructions:
			- if the current step is STEP_MST, then call prim's MST to get a tree and color the edges
			- if the current step is STEP_DFT, then perform a DFT of the tree to get a visit order
			- uncomment the code for (step >= STEP_DFT) to highlight the DFT step-by-step
			- yes, you can write helper methods!
		
		Details for MST step:
			- you should store the tree from Prim's algorithm in the appropriate
				tree variable (see above section of code)
			- you do NOT need to implement Prim's MST algorithm, the JUNG library
				already has this built in! see documentation on this here:
				http://jung.sourceforge.net/doc/api/edu/uci/ics/jung/algorithms/shortestpath/MinimumSpanningForest.html
			- short version of using the above class:
				(1) construct an instance of MinimumSpanningForest
				(2) call getForest() on that instance (returns a tree of the right type)
			- parameters for a MinimumSpanningForest:
				(1) a graph -- you have one of these
				(2) a new tree -- you can create a new DelegateForest using the
					default constructor (see: http://jung.sourceforge.net/doc/api/edu/uci/ics/jung/graph/DelegateForest.html)
				(3) the root -- this is your starting city
				(4) a mapping of edges (Flights) to costs (doubles) -- create 
					yourself a HashMap<Flight,Double> for this and fill it with
					all (Flight --> Flight Costs) from your graph
					see http://jung.sourceforge.net/doc/api/edu/uci/ics/jung/graph/Graph.html,
					for on the graph class, especially the parent interface (HyperGraph) methods
			- to color the edges of the tree in the graph, use colorAllEdges() (see above) 
				providing the tree and the color COLOR_ACTIVE_EDGE (from the TSPAlg class)
					
		Details for DFT step:
			- complete the recursive depthFirstTraversal() static method (see
				lower down in this file) and use that to compute the visit order
			- compute the visit order cost after computing the visit order
			- DO NOT FORGET to add in the cost back to the starting city (the
				starting city IS in the tree, so you do NOT need to manually
				add it into the visit order)
			- the visit order list and visit order cost (from the parent
				TSPAlg class) should be set correctly after this step
			- you do NOT need to color the edges for the DFT, that is done
				by the (step >= STEP_DFT) code provided
		*/



		if(step == STEP_MST) 
		{

			if(graph == null || startingCity == null)
			{
				return;
			}

			Map<Flight, Double> edge = new HashMap<>();

			ArrayList<Flight> allEdges = new ArrayList<>(graph.getEdges());

			for(int i = 0; i < allEdges.size(); i++) {

				Flight fly = allEdges.get(i);

				edge.put(fly, (double)fly.getCost());

			}

			MinimumSpanningForest<City, Flight> minSpanFor = new MinimumSpanningForest<>(graph, new DelegateForest<>(), startingCity, edge);

			tree = minSpanFor.getForest();
			
			colorAllEdges(tree, COLOR_ACTIVE_EDGE);

		} else if (step == STEP_DFT) {

			ArrayList<City> sequence = new ArrayList<>();

			depthFirstTraversal(tree, startingCity, sequence);

			visitOrder = sequence;

			int loss = 0;

			for(int i = 0; i < visitOrder.size() - 1; i++) {

				Flight fly = graph.findEdge(visitOrder.get(i), visitOrder.get(i + 1));

				loss = loss + fly.getCost();

			}

			loss = loss + graph.findEdge(visitOrder.get(visitOrder.size() - 1), startingCity).getCost();

			visitOrderCost = loss;

		}

	
		if(step >= STEP_DFT) {
			stepDisplayDFT();
		}
		
	}
	
	static void depthFirstTraversal(Forest<City,Flight> treeToWalk, City currNode, List<City> dftVisitOrder) {
		//YOUR CODE HERE

		if(currNode == null) {

			return;

		}

		dftVisitOrder.add(currNode);

		Collection<City> childNodes = treeToWalk.getChildren(currNode);

		if(childNodes == null) {

			return;

		}

		List<City> list = new ArrayList<>(childNodes);

		for(int i = 0; i < list.size(); i++) {

			City child = list.get(i);

			depthFirstTraversal(treeToWalk, child, dftVisitOrder);

		}
		
		/*
		Basic instructions:
			- perform a recursive ***pre-order*** DFT of the tree provided
		
		Details:
			- base case should be that the current node is null (the recursive definition
				used in class)
			- the "do something" step should be ***pre-order*** and append the current node
				to the visit order list that's passed in
			- remember that this is a TREE not a GRAPH (so you don't have to worry about
				certain things...)
			- the Forest interface has some methods defined that will help with this,
				see: http://jung.sourceforge.net/doc/api/edu/uci/ics/jung/graph/Forest.html
		*/
	}
}
