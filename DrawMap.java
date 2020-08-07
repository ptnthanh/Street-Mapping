/* Thanh Pham
 * tpham8@u.rochester.edu
 * Lab: TR 4:50-6:05pm
*/
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

public class DrawMap extends JComponent {
	
	ArrayList<Node> locations;
	double minLat, maxLat, minLon, maxLon;
	List<Node> path = null;
	List<Edge> mst = null;
	
	public DrawMap(ArrayList<Node> locations, double minLat, double maxLat, double minLon, double maxLon) {
		this.locations = locations;
		this.minLat = minLat;
		this.maxLat = maxLat;
		this.minLon = minLon;
		this.maxLon = maxLon;
	}
	
	public void setPath(List<Node> path) {
		this.path = path;
	}
	
	public void setMST(List<Edge> mst) {
		this.mst = mst;
	}
	
	public void paintComponent(Graphics g) {
		for (Node n : locations) {		//draw all the intersections and roads
			int x1 = (int) ((n.longitude-minLon)/(maxLon-minLon)*getWidth());
			int y1 = getHeight() - (int) ((n.latitude-minLat)/(maxLat-minLat)*getHeight());
			g.fillOval(x1-1, y1-1, 2, 2);
			for (Node adjN : n.neighbors) {
				int x2 = (int) ((adjN.longitude-minLon)/(maxLon-minLon)*getWidth());
				int y2 = getHeight() - (int) ((adjN.latitude-minLat)/(maxLat-minLat)*getHeight());
				g.drawLine(x1, y1, x2, y2);
			}
		}
		if (this.path!=null)			//if direction is required
		for (int i=0; i<path.size()-2; i++) {
			int x1 = (int) ((path.get(i).longitude-minLon)/(maxLon-minLon)*getWidth());
			int y1 = getHeight() - (int) ((path.get(i).latitude-minLat)/(maxLat-minLat)*getHeight());
			int x2 = (int) ((path.get(i+1).longitude-minLon)/(maxLon-minLon)*getWidth());
			int y2 = getHeight() - (int) ((path.get(i+1).latitude-minLat)/(maxLat-minLat)*getHeight());
			g.setColor(Color.RED);
			g.drawLine(x1, y1, x2, y2);
		}
		
		if (this.mst != null) 
		for (int j = 0; j < mst.size(); j++) {
			Edge e = mst.get(j);
			Node v = e.v;
			Node w = e.w;
			int x1 = (int) ((v.longitude-minLon)/(maxLon-minLon)*getWidth());
			int y1 = getHeight() - (int) ((v.latitude-minLat)/(maxLat-minLat)*getHeight());
			int x2 = (int) ((w.longitude-minLon)/(maxLon-minLon)*getWidth());
			int y2 = getHeight() - (int) ((w.latitude-minLat)/(maxLat-minLat)*getHeight());
			g.setColor(Color.BLUE);
			g.drawLine(x1, y1, x2, y2);
		}
	}
	
	
}
