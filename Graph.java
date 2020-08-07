/* Thanh Pham
 * tpham8@u.rochester.edu
 * Lab: TR 4:50-6:05pm
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.JFrame;

public class Graph {
	
	HashMap<String, Node> map = new HashMap<String, Node>();
	
	LinkedList<Node> path = new LinkedList<>();
	ArrayList<Node> locations = new ArrayList<>();
	Double INFINITY = Double.MAX_VALUE;
	int NO_PATH = -1;
	double R = 6371000.0; // Earth Radius in meter
	
	PriorityQueue<Edge> edges = new PriorityQueue<>();
	
	double minLat = Double.POSITIVE_INFINITY;
	double maxLat = Double.NEGATIVE_INFINITY;
	double minLon = minLat;
	double maxLon = maxLat;
	
	public Graph readFromFile(String filename) throws IOException {
		File infile = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(infile));		
		
		Graph gr = new Graph();
		
		String line;
		while ((line = br.readLine()) != null) {
			String[] read = line.split("\\s+");
			if (line.charAt(0) == 'i') {
				double lat = Double.parseDouble(read[2]);
				double lon = Double.parseDouble(read[3]);
				Node site = new Node(read[1], lat, lon);
				if (lat<minLat) minLat = lat; 
				if (lat>maxLat) maxLat = lat;
				if (lon<minLon) minLon = lon;
				if (lon>maxLon) maxLon = lon;
				locations.add(site);
				map.put(read[1], site);
				
			} else if (line.charAt(0) == 'r') {
				String[] info = line.split("\\s+");
				String roadID = info[1];
				
				Node n1 = map.get(info[2]);
				Node n2 = map.get(info[3]);
				
				n1.addNeighbor(n2);
				n2.addNeighbor(n1);
				
				Edge e = new Edge(roadID, n1, n2, getLength(n1,n2));
				edges.add(e);
			}
		}
		
		minLat = minLat - 0.0001;
		maxLat = maxLat + 0.0001;
		minLon = minLon - 0.0001;
		maxLon = maxLon + 0.0001;
		
		br.close();
		return gr;
	}
	
	public double getLength(Node n1, Node n2) {
		double dLat = Math.toRadians(n2.latitude - n1.latitude);
		double dLon = Math.toRadians(n2.longitude - n1.longitude);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
			    Math.cos(Math.toRadians(n1.latitude)) * Math.cos(Math.toRadians(n2.latitude)) * 
			    Math.sin(dLon/2) * Math.sin(dLon/2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		double d = R * c; // Distance in m
		return d;		
	}
		
	public void dijkstra(Node s) {
		PriorityQueue<Node> pq = new PriorityQueue<Node>();
		s.dist = 0;
		pq.add(s);
		while (!pq.isEmpty()) {
			Node v = pq.poll();
			v.visited = true;
			for (Node w : v.neighbors) {
				if (!w.visited) {
					double length = getLength(v,w);
					if (v.dist + length < w.dist) {
						w.dist = v.dist + length;
						w.parent = v;
						pq.remove(w);
						pq.offer(w);
					}
				}
			}
		}		
	}
	
	public List<Node> shortestPath(String a, String b) {
		Node source = map.get(a);
		Node destination = map.get(b);
		dijkstra(source);
		path.add(destination);
		Node current = destination;
		while (true) {
			if (current.parent != null) 
				current = current.parent;
			else 
				return null;
			if (!(current.compareTo(source) == 0)) 
				path.add(current);
			else break;
		}
		path.add(source);
		return path;
	}
	
	public List<Edge> MST(PriorityQueue<Edge> edges) {
		List<Edge> mst = new ArrayList<Edge>();
		
		UnionFind uf = new UnionFind();
		uf.makeSet(locations);
			
		while (!edges.isEmpty() && mst.size() < edges.size()-1) {
			Edge next = edges.remove();
			Node x = uf.find(next.v);
			Node y = uf.find(next.w);
			
			if (x != y) {
				uf.union(x, y);	
				mst.add(next);		
			}
		}
		
		return mst;
	}
	
	public static void main (String[] args) throws IOException {
		String fileName = args[0];
		Graph g = new Graph();
		g.readFromFile(fileName);
		JFrame frame = new JFrame("MAP");
		frame.setSize(800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DrawMap graph = new DrawMap(g.locations, g.minLat, g.maxLat, g.minLon, g.maxLon);
		
		DecimalFormat df = new DecimalFormat("#.##");
		if (args[1].equals("-show")) {
			if (args.length > 2)
			if (args[2].equals("-directions")) {
				String start = args[3];
				String end = args[4];
				if (!g.map.containsKey(start)) 
					System.out.println(start + " not found");
				else if (!g.map.containsKey(end)) 
					System.out.println(end + " not found");
				else {
					List<Node> path = g.shortestPath(start, end);
					if (path==null) System.out.println("There's no path from " + start + " to " + end);
					else {
						double length = 0.0;
						for (int i=path.size()-1; i>0; i--) {
							System.out.print(path.get(i).ID + ", ");
							length += g.getLength(path.get(i), path.get(i-1));
						}
						System.out.println(path.get(0).ID);
						String format = df.format(length/1609.34);
						System.out.println("The distance from " + path.get(path.size()-1).ID + " to " + path.get(0).ID + " is " +
								format + " miles");
						graph.setPath(path);
					}
				}
			}
			else if (args[2].equals("-meridianmap")) {	
				List<Edge> kruskal = g.MST(g.edges);
				graph.setMST(kruskal);		
				for (int i = 0; i < kruskal.size(); i++) {
					System.out.print(kruskal.get(i).ID + " ");
				}
			}
			frame.add(graph);
			frame.setVisible(true);
		}
		else if (args[1].equals("-directions")) {
			String start = args[2];
			String end = args[3];
			if (!g.map.containsKey(start)) System.out.println(start + " not found");
			else if (!g.map.containsKey(end)) System.out.println(end + "not found");
			else {
				List<Node> path = g.shortestPath(start, end);
				if (path==null) System.out.println("There's no path from " + start + " to " + end);
				else {
					double length = 0.0;
					for (int i=path.size()-1; i>0; i--) {
						System.out.print(path.get(i).ID + ", ");
						length += g.getLength(path.get(i), path.get(i-1));
					}
					System.out.println(path.get(0).ID);
					String format = df.format(length/1609.34);
					System.out.println("The distance from " + path.get(path.size()-1).ID + " to " + path.get(0).ID + " is " 
							+ format + " miles");
					graph.setPath(path);
				}
				if (args.length==5) {
					frame.add(graph);
					frame.setVisible(true);
				}
			}
		}
		else if (args[1].equals("-meridianmap")) {
			List<Edge> kruskal = g.MST(g.edges);
			for (int i = 0; i < kruskal.size(); i++) {
				System.out.print(kruskal.get(i).ID + " ");
			}
			
		}
	}
	
}
