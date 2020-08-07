/* Thanh Pham
 * tpham8@u.rochester.edu
 * Lab: TR 4:50-6:05pm
*/

import java.util.ArrayList;
import java.util.List;

// Node = Intersection

public class Node implements Comparable<Node> {
	
	String ID;
	double latitude, longitude;
	Node parent;
	List<Node> neighbors = new ArrayList<>();
	boolean visited = false;
	double dist;
	Double INFINITY = Double.MAX_VALUE;
	
	
	public Node(double latitude, double longtitude, Node parent) {
		this.latitude = latitude;
		this.longitude = longtitude;
		this.parent = parent;
		dist = INFINITY;
		ID = "";
	}
	
	public Node(String ID, double latitude, double longtitude) {
		this.ID = ID;
		this.latitude = latitude;
		this.longitude = longtitude;
		this.parent = null;
		dist = INFINITY;
	}
	
	public void addNeighbor(Node neighbor) {
		neighbors.add(neighbor);
	}
	
	@Override
	public int compareTo(Node other) {
		if (this.dist > other.dist) 
			return 1;
		else if (this.dist < other.dist)
			return -1;
		return 0;
	}

	
}
