
// Starter code for LP4
// Do not rename this file or move it away from cs6301/g??

// change following line to your group number
package cs6301.g40;
import cs6301.g40.Graph.Vertex;
import cs6301.g40.Graph.Edge;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;

import static cs6301.g40.BFS.*;
import static cs6301.g40.Graph.*;
import static cs6301.g40.Graph.Vertex.getVertex;

public class LP4 {
    Graph g;
    Vertex s;

    // common constructor for all parts of LP4: g is graph, s is source vertex
    public LP4(Graph g, Vertex s) {
	this.g = g;
	this.s = s;
    }


    // Part a. Return number of topological orders of g
	
	/**
	 *
	 * @return Number of topological orders of graph g
	 *
	 *The function calls countTopologicalOrders(XGraph xg) passing XGraph
	 */
    public long countTopologicalOrders() {
	// To do
	XGraph xg = new XGraph(g);
	return countTopologicalOrders(xg);
	
    }
	
	/**
	 *
	 * @param xg : XGraph passed from the calling functions contains graph information
	 * @return Number of topological orders of xg
	 *
	 * The function works on the principle to remove a node which has an indegree of zero and recursively call the function on the
	 * smaller graph.
	 */
	public long countTopologicalOrders(XGraph xg){
    	long count = 0;
    	Iterator<Vertex> it = xg.iterator();
		if(!it.hasNext()) return 1;
		do{
			XGraph.XVertex v = (XGraph.XVertex) it.next();
			Iterator<Graph.Edge> revIt = v.revAdj.iterator();
			boolean check = false;
			while(revIt.hasNext()){
				Vertex temp =  revIt.next().otherEnd(v);
				XGraph.XVertex u = xg.xv[temp.getName()];
				
				if(!u.isDisabled()){
					check = true;
				     break;
				}
			}
			if(v.revAdj.size()==0 || !check) {
				v.disable();
				Iterator<XGraph.XEdge> adjIt = v.xadj.iterator();
				count = count + countTopologicalOrders(xg);
				v.disabled = false;
			}
		}while(it.hasNext());
    	return count;
	}


    // Part b. Print all topological orders of g, one per line, and 
    //	return number of topological orders of g
	
	/**
	 *
	 * @return Number of topological orders of graph g
	 *
	 * The function calls enumerateTopologicalOrders(XGraph xg, LinkedList<XGraph.Vertex> list) passing XGraph xg and
	 * a LinkedList which will hold numbers as per the topological order
	 */
    public long enumerateTopologicalOrders() {
	// To do
		XGraph xg = new XGraph(g);
		LinkedList<XGraph.Vertex> list = new LinkedList<>();
		return enumerateTopologicalOrders(xg,list);
    }
	
	/**
	 *
	 * @param xg :
	 * @param list : a LinkedList which will hold numbers as per the topological order
	 * @return : Number of topological orders of graph g
	 *
	 * The function works on the principle to remove a node which has an indegree of zero and recursively call the function on the
	 * smaller graph. It also prints  all topological orders
	 */
	public long enumerateTopologicalOrders(XGraph xg, LinkedList<XGraph.Vertex> list) {
		long count = 0;
		Iterator<Vertex> it = xg.iterator();
		//	cs6301.g40.XGraph.XGraphIterator it = new cs6301.g40.XGraph.XGraphIterator(xg);
		if(!it.hasNext()){
			for (XGraph.Vertex w:list
				 ) {
				System.out.print(w.getName()+1+" ");
			}
			System.out.println();
			return 1;
		}
		do{
			XGraph.XVertex v = (XGraph.XVertex) it.next();
			Iterator<Graph.Edge> revIt = v.revAdj.iterator();
			boolean check = false;
			while(revIt.hasNext()){
				Vertex temp =  revIt.next().otherEnd(v);
				XGraph.XVertex u = xg.xv[temp.getName()];
				
				if(!u.isDisabled()){
					check = true;
					break;
				}
			}
			if(v.revAdj.size()==0 || !check) {
				v.disable();
				list.add(v);
				Iterator<XGraph.XEdge> adjIt = v.xadj.iterator();
				/*while(adjIt.hasNext()){
					XGraph.XEdge xe = (cs6301.g40.XGraph.XEdge) adjIt.next();
					xe.disabled = true;
				}*/
				count = count + enumerateTopologicalOrders(xg,list);
				v.disabled = false;
				list.removeLast();
			/*	while(adjIt.hasNext()){
					XGraph.XEdge xe = (cs6301.g40.XGraph.XEdge) adjIt.next();
					xe.disabled = false;
				}*/
			}
		}while(it.hasNext());
		return count;
	}
	
	/**
	 *
	 * @param xg : XGraph corresponding to inputted graph g
	 * @param parent :  An array which holds the parent of the nodes and enable us to trace the shortest path
	 * @param distance : An array which contains entry corresponding to each node i.e shortest distance from s
	 * @param count : An array which contains entry corresponding to each node to indicate how much shortest path it is member of
	 * @param seen : An array which contains entry corresponding to each node to point out whether it has been visited in the tour
	 * @param deq : Array Deque which holds nodes while enumerating all shortest path. Initially it holds s.
	 *
	 *  The function initializes Arrays: parent, distance, count, seen
	 *  and pushes Node s into the deq
	 */
	public void initializeNodes(XGraph xg,XGraph.XVertex [] parent, long [] distance, long [] count, boolean [] seen, Deque<XGraph.XVertex> deq){
		final long Infinity = Long.MAX_VALUE;
		for (Vertex u:g.v
			 ) {
			parent[u.getName()]=null;
			count[u.getName()] =0;
			distance[u.getName()] =Infinity;
			seen[u.getName()] = false;
		}
		distance[s.getName()]=0;
		seen[s.getName()]=true;
		deq.push(xg.getVertex(s));
	}


    // Part c. Return the number of shortest paths from s to t
    // 	Return -1 if the graph has a negative or zero cycle
	
	/**
	 *
	 * @param t : The destination Node at which shortest path ends
	 * @return  : -1 if graph has negative cycle, -2 if destination is not reachable else count of all shortest paths
	 * The function initializes Arrays :- distance, parent, count, seen and Array Deque deq with initializeNodes() function
	 * With updated seen, count, parent ,distance arrays it calls countShortestPaths() function.
	 *
	 */
    public long countShortestPaths(Vertex t) {
	// To do
		
		XGraph xg = new XGraph(g);
		XGraph.XVertex v = xg.getVertex(s);
		BFS reachable = new BFS(xg,v);
		reachable.bfs();
		if(reachable.distance(t) == INFINITY) {
			System.out.println("Destination is not reachable from source ");
			return -2;
		}
			XGraph.XVertex [] parent = new XGraph.XVertex[xg.size()];
		long [] distance = new long[xg.size()];
		long [] count = new long [xg.size()];
		boolean [] seen = new boolean[xg.size()];
		Deque<XGraph.XVertex> deq = new ArrayDeque<>();
		initializeNodes(xg, parent, distance, count, seen, deq);
		return countShortestPaths(s,t,deq,xg,parent,seen,count,distance);
    }
	
	/**
	 * @param s : Starting node of the Shortest Path
	 * @param t : Final node of the Shortest Path
	 * @param deq : Array Deque which holds nodes while counting all shortest path. Initially it holds s.
	 * @param xg : XGraph corresponding to inputted graph g
	 * @param parent : An array which holds the parent of the nodes and enable us to trace the shortest path
	 * @param seen : An array which contains entry corresponding to each node to point out whether it has been visited in the tour
	 * @param count : An array which contains entry corresponding to each node to indicate how much shortest path it is member of
	 * @param distance : An array which contains entry corresponding to each node i.e shortest distance from s
	 * @return : returns -1 if graph has a cycle else count of shortest path.
	 *
	 * The function implements Bellman ford algorithm to find the count of shortest path
	 */
	
	public long countShortestPaths(Vertex s, Vertex t, Deque<XGraph.XVertex> deq, XGraph xg, XGraph.XVertex [] parent, boolean [] seen, long [] count,long [] distance ) {
		long [] SPcnt = new long [xg.size()];
		for(int i=0;i<xg.size();i++){
			SPcnt[i]=1;
		}
		long SPcount = 1 ;
		while(!deq.isEmpty()){
			XGraph.XVertex u = deq.removeLast();
			seen[u.getName()]=false;
			count[u.getName()] +=1;
			if(count[u.getName()]>=g.size()){
				System.out.println("Non-positive cycle in graph.  Unable to solve problem");
				return -1;
			}
			Iterator<XGraph.XEdge> edgeIt = u.xadj.iterator();
			while(edgeIt.hasNext()){
				XGraph.XEdge e =  edgeIt.next();
				XGraph.XVertex v = (XGraph.XVertex) e.otherEnd(u);
				if(distance[v.getName()]>distance[u.getName()]+e.getWeight()){
					distance[v.getName()]=distance[u.getName()]+e.getWeight();
					parent[v.getName()] = u;
					SPcnt[v.getName()] =1;
					if(!seen[v.getName()]){
						deq.push(v);
						smallLabelFirst(deq,distance);
						seen[v.getName()] = true;
					}
				}
				else if(distance[v.getName()]==distance[u.getName()]+e.getWeight() && parent[v.getName()]!=u){
					SPcnt[v.getName()]+=1;
				}
				
			}
		}
		for(int i=0;i<xg.size();i++){
			
			SPcount +=SPcnt[i]-1;
		}
		
		return SPcount;
	}
	
	
    // Part d. Print all shortest paths from s to t, one per line, and 
    //	return number of shortest paths from s to t.
    //	Return -1 if the graph has a negative or zero cycle.
	
	/**
	 *
	 * @param t : The destination Node at which shortest path ends
	 * @return  : -1 if graph has negative cycle, -2 if destination is not reachable, else count of all shortest paths
	 *
	 * The function initializes Arrays :- distance, parent, count, seen and Array Deque deq with initializeNodes() function
	 * With updated seen, count, parent ,distance arrays it calls enumerateShortestPaths() function.
	 *
	 */
    public long enumerateShortestPaths(Vertex t) {
        // To do
		XGraph xg = new XGraph(g);
		XGraph.XVertex [] parent = new XGraph.XVertex[xg.size()];
		XGraph.XVertex v = xg.getVertex(s);
		BFS reachable = new BFS(xg,v);
		reachable.bfs();
		if(reachable.distance(t) == INFINITY) {
			System.out.println("Destination is not reachable from source ");
			return -2;
		}
		long [] distance = new long[xg.size()];
		long [] count = new long [xg.size()];
		boolean [] seen = new boolean[xg.size()];
		Deque<XGraph.XVertex> deq = new ArrayDeque<>();
		initializeNodes(xg, parent, distance, count, seen, deq);
		return enumerateShortestPaths(s,t,deq,xg,parent,seen,count,distance);
    }
	
	/**
	 * @param s : Starting node of the Shortest Path
	 * @param t : Final node of the Shortest Path
	 * @param deq : Array Deque which holds nodes while enumerating all shortest path. Initially it holds s.
	 * @param xg : XGraph corresponding to inputted graph g
	 * @param parent : An array which holds the parent of the nodes and enable us to trace the shortest path
	 * @param seen : An array which contains entry corresponding to each node to point out whether it has been visited in the tour
	 * @param count : An array which contains entry corresponding to each node to indicate how much shortest path it is member of
	 * @param distance : An array which contains entry corresponding to each node i.e shortest distance from s
	 * @return : returns -1 if graph has a cycle else count of shortest path. It also prints all shortest paths, if any
	 *
	 * The function implements Bellman ford algorithm to display all the shortest path
	 */
	public long enumerateShortestPaths(Vertex s, Vertex t, Deque<XGraph.XVertex> deq, XGraph xg, XGraph.XVertex [] parent, boolean [] seen, long [] count,long [] distance ) {
		Deque<Graph.Vertex> [] prevList = new Deque[xg.size()];
		long [] SPcnt = new long [xg.size()];
		for(int i=0;i<xg.size();i++){
			SPcnt[i]=1;
			prevList[i]= new LinkedList<>();
		}
		long SPcount = 1 ;
		while(!deq.isEmpty()){
			XGraph.XVertex u = deq.removeLast();
			seen[u.getName()]=false;
			count[u.getName()] +=1;
			if(count[u.getName()]>=g.size()){
				System.out.println("Non-positive cycle in graph.  Unable to solve problem");
				return -1;
			}
			Iterator<XGraph.XEdge> edgeIt = u.xadj.iterator();
			while(edgeIt.hasNext()){
				XGraph.XEdge e =  edgeIt.next();
				XGraph.XVertex v = (XGraph.XVertex) e.otherEnd(u);
				if(distance[v.getName()]>distance[u.getName()]+e.getWeight()){
					distance[v.getName()]=distance[u.getName()]+e.getWeight();
					parent[v.getName()] = u;
					SPcnt[v.getName()] =1;
					prevList[v.getName()]= new LinkedList<>();
					prevList[v.getName()].add(u);
					if(!seen[v.getName()]){
						deq.push(v);
						smallLabelFirst(deq,distance);
						seen[v.getName()] = true;
					}
				}
				else if(distance[v.getName()]==distance[u.getName()]+e.getWeight() && parent[v.getName()]!=u){
					SPcnt[v.getName()]+=1;
					prevList[v.getName()].add(u);
				}
				
			}
		}
		for(int i=0;i<xg.size();i++){
			
			SPcount +=SPcnt[i]-1;
		}
		
		Deque<Graph.Vertex> stack = new ArrayDeque<>();
		stack.push(t);
		
		//	stack.push(parent[stack.peek().getName()]);
		
		while(parent[stack.peek().getName()]!=null)
			stack.push(prevList[stack.peek().getName()].removeLast());
		//Iterator it = stack.descendingIterator();
		/*while(it.hasNext()){
			System.out.print(it.next()+" ");
		}
		System.out.println();*/
		
		while(!stack.isEmpty()){
				if(parent[stack.peek().getName()]==null){
				Iterator itr = stack.iterator();
					
				while(itr.hasNext()){
					Graph.Vertex ver = (Graph.Vertex) itr.next();
					System.out.print(ver+" ");
				}
				System.out.println();
				
					while(!stack.isEmpty() && prevList[stack.peek().getName()].isEmpty()){
						 stack.remove();
						}
				}
			
		//	stack.pop();
			
	//		while(!prevList[stack.peek().getName()].isEmpty() && parent[stack.peek().getName()]!=null){
				if(!stack.isEmpty())
				stack.push(prevList[stack.peek().getName()].removeLast());
		   
		}
		return SPcount;
	}

    public static class NodeInfo{
		int edgeCount;
		int pathWeight;
		Graph.Vertex prev;
		NodeInfo(int count, int weight, Graph.Vertex prev){
			this.edgeCount = count;
			this.pathWeight = weight;
			this.prev = prev;
		}
	}
    // Part e. Return weight of shortest path from s to t using at most k edges
	
	/**
	 *
	 * @param t : The destination Node at which shortest path ends
	 * @param k : Number of edges allowed in the shortest path
	 * @return : Weight of the shortest path with at most k edges
	 *
	 * The function implements Bellman Ford Algorithm for Shortest Path to find out weight of shortest path at most k edges
	 */
    public int constrainedShortestPath(Vertex t, int k) {
		XGraph xg = new XGraph(g);
		Deque<XGraph.XVertex> deq = new ArrayDeque<>();
	
		XGraph.XVertex xv = xg.getVertex(s);
		BFS reachable = new BFS(xg,xv);
		reachable.bfs();
		if(reachable.distance(t) == INFINITY) {
			System.out.println("Destination is not reachable from source ");
			return -2;
		}
        	
		Deque<NodeInfo> [] prevList = new Deque[xg.size()];
		long [] SPcnt = new long [xg.size()];
		boolean [] seen = new boolean[xg.size()];
		long [] count = new long [xg.size()];
		NodeInfo first = new NodeInfo(0, 0, null);
	
		for(int i=0;i<xg.size();i++){
			SPcnt[i]=1;
			prevList[i]= new LinkedList<>();
			seen[i] = false;
			count[i] = 0;
		}
		prevList[s.getName()].add(first);
		deq.push(xg.getVertex(s));
		
		while(!deq.isEmpty()){
			XGraph.XVertex u = deq.removeLast();
			seen[u.getName()]=false;
			count[u.getName()] +=1;
			if(count[u.getName()]>=g.size()){
				System.out.println("Non-positive cycle in graph.  Unable to solve problem");
				return -1;
			}
			Iterator<XGraph.XEdge> edgeIt = u.xadj.iterator();
			while(edgeIt.hasNext()){

				XGraph.XEdge e =  edgeIt.next();
				XGraph.XVertex v = (XGraph.XVertex) e.otherEnd(u);
				Iterator it = prevList[u.getName()].iterator();
				while(it.hasNext()) {
					NodeInfo prevInfo = (NodeInfo) it.next();
						int edgeCount = prevInfo.edgeCount;
						int pathWeight = prevInfo.pathWeight;
					if (edgeCount < k) {
						//	distance[v.getName()]=distance[u.getName()]+e.getWeight();
						//parent[v.getName()] = u;
						SPcnt[v.getName()] = 1;
						//prevList[v.getName()]= new LinkedList<>();
						NodeInfo ninfo = new NodeInfo(edgeCount + 1, pathWeight + e.getWeight(), u);
						prevList[v.getName()].add(ninfo);
						
					}
				}
				if (!seen[v.getName()]) {
					deq.push(v);
				
					seen[v.getName()] = true;
				}
			}
		}
	
		Iterator itLast = prevList[t.getName()].iterator();
		int minPath = Integer.MAX_VALUE;
		while(itLast.hasNext()) {
			NodeInfo prevInfo = (NodeInfo) itLast.next();
			int edgeCount = prevInfo.edgeCount;
			int pathWeight = prevInfo.pathWeight;
			if(edgeCount<=k && minPath>pathWeight)
				minPath = pathWeight;
		}
		return minPath ;
	}
		
    


    // Part f. Reward collection problem
    // Reward for vertices is passed as a parameter in a hash map
    // tour is empty list passed as a parameter, for output tour
    // Return total reward for tour
	
	
	/**
	 *
	 * @param vertexRewardMap : Inputted HashMap which maps vertex to the reward
	 * @param tour : A Linkedlist to hold the vertex of the tour
	 * @return : Total rewards collected during tour
	 *
	 *The function initializes Arrays :- distance, count, seen and Array Deque deq and call findShortestPath() function over graph xg,
	 * With updated seen, distance arrays it calls reward () function.
	 */
    public int reward(HashMap<Vertex,Integer> vertexRewardMap, List<Vertex> tour) {
	    
		XGraph xg = new XGraph(g);
		XGraph.XVertex [] parent = new XGraph.XVertex[xg.size()];
	
		XGraph.XVertex v = xg.getVertex(s);
	   /* boolean flag = false;
		for (XGraph.XEdge e: v.xadj
			 ) {
			e.disabled = true;
			XGraph.XVertex x = (cs6301.g40.XGraph.XVertex) e.otherEnd(v);
			BFS reachable = new BFS(xg, x);
			reachable.bfs();
			e.disabled = false;
			if (reachable.distance(v) < INFINITY) {
			//	System.out.println("Destination is not ");
				flag = true;
				break;
			}
		}
			
		if (!flag){
			System.out.println("There is no tour starting from source going back to source");
			return -2;
		}
		*/
		long [] distance = new long[xg.size()];
		long [] count = new long [xg.size()];
		boolean [] seen = new boolean[xg.size()];
		Deque<XGraph.XVertex> deq = new ArrayDeque<>();
		initializeNodes(xg, parent, distance, count, seen, deq);
	    int result = findShortestPath(deq,xg,parent,seen,count,distance);
	    seen[s.getName()]=true;
		if(result==1){
			BFS reachable = new BFS(xg,v );
			int finalReward = reward(s,s, xg,vertexRewardMap,tour,seen,distance,0,reachable);
			if(finalReward < 0)
			{
				finalReward =0;
			}
				
			return finalReward;
		}
		return -1;
    }
	
	/**
	 *
	 * @param s : Source from which tour will start
	 * @param d : Destination to which tour will end
	 * @param xg : XGraph on which tour we will find the tour
	 * @param vertexRewardMap : HashMap which holds reward corresponding to a Node
	 * @param tour : A Linked list which holds tour
	 * @param seen : An array which contains entry corresponding to each node to point out whether it has been visited in the tour
	 * @param shortestDistance : An array which holds entry corresponding to each node which is shortest distance from s
	 * @param distance : It holds the distance of s from the starting point of the tour
	 * @return : Total reward collected during the tour
	 *
	 * The function uses dynamic programming to find the tour with maximum reward. From a node s, it checks for the optimal sub-tour with maximum reward
	 * starting from node on the other side of outgoing edges and add its own reward(as per the given condition).
	 */
    public int reward(Vertex s, Vertex d, XGraph xg, HashMap<Vertex,Integer> vertexRewardMap, List<Vertex> tour, boolean [] seen , long [] shortestDistance, long distance, BFS reachable){
		 XGraph.XVertex u = xg.getVertex(s);
		List<Vertex> subTour = new LinkedList<>();
		List<Vertex>  tempTour = new LinkedList<>();
		 int max = Integer.MIN_VALUE;
		 
		 XGraph.XVertex next = null;
		 int val = Integer.MIN_VALUE;
		Iterator edgeIt = u.iterator();
		while(edgeIt.hasNext()){
			Graph.Edge e = (cs6301.g40.Graph.Edge) edgeIt.next();
			XGraph.XVertex v = (XGraph.XVertex) e.otherEnd(s);
			if( !seen[v.getName()]){
				if((distance+e.getWeight()==shortestDistance[v.getName()]) ){
				//max=0;
				u.disabled = true;
				seen[v.getName()] = true;
				subTour = new java.util.LinkedList<>();
				 val = reward(v,d,xg,vertexRewardMap,subTour,seen,shortestDistance,distance+e.getWeight(),reachable);
				u.disabled = false;
				
				}
				else{
					XGraph.XVertex last = xg.getVertex(d);
					last.disabled = false;
				//	BFS reachable = new BFS(xg,v);
				//	reachable.reinitialize(v);
					reAssignSeen(reachable,seen,v,last);
					reachable.bfs();
					if(reachable.distance(d) < INFINITY){
					   val = 0;
					   subTour.add(d);
					   Vertex temp = d ;
					   while(reachable.getParent(temp)!=v){
					   	temp = reachable.getParent(temp);
					   	subTour.add(temp);
					   }
					   subTour.add(v);
					   Collections.reverse(subTour);
					}
					last.disabled = true;
				}
				
				if(val>max){
					next = v;
					max = val;
				tempTour = subTour;
				}
			}
					
		}
		if(max == Integer.MIN_VALUE){
			XGraph.XVertex last = xg.getVertex(d);
			last.disabled = false;
		//	BFS reachable = new BFS(xg,u);
			reAssignSeen(reachable,seen,u,last);
			reachable.bfs();
			if(reachable.getParent(d)==u && !tour.isEmpty()){
				max =0;
				tour.add(d);
			}
			
		}
		
		
		if(max >=0){
			tour.add(s);
			while(!tempTour.isEmpty())
				tour.add(tempTour.remove(0));
			
		}
		int reward  = vertexRewardMap.get(s);
		seen[s.getName()]=false;
		
		return max+reward;
	}
	
	/**
	 *
	 * @param reachable : An instance of BFS class used to call bfs on graph g
	 * @param seen : An array which contains entry corresponding to each node to point out whether it has been visited in the tour
	 * @param newSrc : Starting node from which BFS would be called
	 * @param last : The last node which we check if it is reachable from the starting node by calling bfs
	 *
	 * The function updates the seen member variable of BFSVertex objects before bfs() gets called
	 */
	public void reAssignSeen(BFS reachable, boolean [] seen, Graph.Vertex newSrc,Graph.Vertex last){
    	reachable.src = newSrc;
		for(Graph.Vertex u: g) {
			BFSVertex bu = reachable.node[u.getName()];
			bu.seen = seen[u.getName()];
			bu.parent = null;
			bu.distance = INFINITY;
			}
		reachable.node[newSrc.getName()].seen = true;
		reachable.node[last.getName()].seen = false;
		reachable.node[newSrc.getName()].distance = 0;
	}
	
	/**
	 *
	 * @param deq : Array deque which contains the vertex and initially contains root when passed as parameter
	 * @param xg :  XGraph which is created from the Graph g
	 * @param parent : An array of XVertex to contain the parent of each XVertex. Required to trace the path
	 * @param seen : Boolean array to check if given Vertex is present in Array deque deg or not
	 * @param count : count array helps to detect the presence of cycle in the graph
	 * @param distance : This array maintains the distance of each vertex from the starting vertex
	 * @return : 1 if shortest path exist else returns -1 in case of negative cycle
	 *
	 * The function implements Bellman Ford Algorithm shortest path Algorithm
	 */
	public int findShortestPath(Deque<XGraph.XVertex> deq, XGraph xg, XGraph.XVertex [] parent, boolean [] seen, long [] count,long [] distance ) {
		
			while(!deq.isEmpty()){
			XGraph.XVertex u = deq.removeLast();
			seen[u.getName()]=false;
			count[u.getName()] +=1;
			if(count[u.getName()]>=g.size()){
				System.out.println("Non-positive cycle in graph.  Unable to solve problem");
				return -1;
			}
			Iterator<XGraph.XEdge> edgeIt = u.xadj.iterator();
			while(edgeIt.hasNext()){
				XGraph.XEdge e =  edgeIt.next();
				XGraph.XVertex v = (XGraph.XVertex) e.otherEnd(u);
				if(distance[v.getName()]>distance[u.getName()]+e.getWeight()){
					distance[v.getName()]=distance[u.getName()]+e.getWeight();
					parent[v.getName()] = u;
					if(!seen[v.getName()]){
						deq.push(v);
						seen[v.getName()] = true;
						smallLabelFirst(deq,distance);
					}
				}
				
			}
		}
		return 1;
	}
	
	/**
	 *
	 * @param deq : Array deque which contains the vertex and initially contains root when passed as parameter
	 * @param distance : It holds the distance of s from the starting point of the tour
	 *
	 * The function shuffles the position of first element and last element in the deq to make Bellman Ford Algorithm Faster
	 */
	public void smallLabelFirst(Deque<XGraph.XVertex> deq,long [] distance){
		XGraph.XVertex front = deq.getLast();
		XGraph.XVertex back = deq.getFirst();
		if(distance[front.getName()]>distance[back.getName()]){
			deq.removeLast();
			deq.pop();
			deq.push(front);
			deq.addLast(back);
		}
	}
    // Do not modify this function
    static void printGraph(Graph g, HashMap<Vertex,Integer> map, Vertex s, Vertex t, int limit) {
	System.out.println("Input graph:");
	for(Vertex u: g) {
	    if(map != null) { 
		System.out.print(u + "($" + map.get(u) + ")\t: ");
	    } else {
		System.out.print(u + "\t: ");
	    }
	    for(Edge e: u) {
		System.out.print(e + "[" + e.weight + "] ");
	    }
	    System.out.println();
	}
	if(s != null) { System.out.println("Source: " + s); }
	if(t != null) { System.out.println("Target: " + t); }
	if(limit > 0) { System.out.println("Limit: " + limit + " edges"); }
	System.out.println("___________________________________");
    }
}
