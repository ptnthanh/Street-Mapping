/* Thanh Pham
 * tpham8@u.rochester.edu
 * Lab: TR 4:50-6:05pm
*/

import java.util.ArrayList;

public class UnionFind {
	
	public void makeSet(ArrayList<Node> nodes) {
		for (int i = 0; i < nodes.size(); i++) {
			Node ni = nodes.get(i);
			ni.parent = ni;
			ni.dist = 1;
		}	
	}
	
	public Node find(Node x) {
		while (x.compareTo(x.parent) != 0) {
			x.parent = x.parent.parent;
			x = x.parent;
		}
		return x;
	}
	
	public void union(Node x, Node y) {
		Node fx = find(x);
		Node fy = find(y);
		if (fx.compareTo(fy) == 0) 
			return;
		else if (fx.compareTo(fy) < 0) {
			fy.parent = fy=x;
			fy.dist += fx.dist;
		}
		else if (fx.compareTo(fy) > 0) {
			fx.parent = fy;
			fx.dist += fy.dist; 
		}			
	}
	
	
}
