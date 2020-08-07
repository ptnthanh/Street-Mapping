/* Thanh Pham
 * tpham8@u.rochester.edu
 * Lab: TR 4:50-6:05pm
*/

public class Edge implements Comparable<Edge>{
	
	final Node v, w;
	String ID;
	double distance;
	
	public Edge(Node v, Node w) {  
		this.v = v;
		this.w = w;
		this.ID = "";
		this.distance = Double.MAX_VALUE;
	}
	
	public Edge(String ID, Node s, Node e, double distance) {
		this.v = s;
		this.w = e;
		this.ID = ID;
		this.distance = distance;
	}
	
	public Edge(String ID, Node s, Node e) {
		this.v = s;
		this.w = e;
		this.ID = ID;
		distance = Double.MAX_VALUE;
	}

	@Override
	public int compareTo(Edge other) {
		if (this.distance > other.distance)
			return 1;
		else if (this.distance < other.distance) 
			return -1;
		return 0;
	}
	
}
