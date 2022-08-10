import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MaxFlow
{
    HashMap<Integer,ArrayList<Edge>> adj_list;      // adjacency list representation of graph
    int []parent;                                   // parent array used in bfs
    int N;                                          // total number of nodes

    /** TODO
    * initialize constructor function. 
    * @param N : number of nodes
    */
    public MaxFlow(int N)
    {
        this.N = N;
        this.adj_list = new HashMap<>();

    }

    /** TODO
    * gradually build the graph by inserting edges
    * this function inserts a new edge into the graph
    *
    * hint : remember to consider the opposite direction of flow
    *
    * @param source : source node
    * @param destination : destination node
    * @param flow_rate : maximum rate of flow through the edge
    */
    public void insEdge(int source, int destination, int flow_rate)
    {
        ArrayList<Edge> arrayList = new ArrayList<Edge>();
        ArrayList<Edge> arrayList2 = new ArrayList<Edge>();
        Edge newEdge = new Edge(destination, flow_rate);


        if(adj_list.get(source) == null) {
            arrayList.add(newEdge);
            adj_list.put(source, arrayList);
        } else {
            arrayList = adj_list.get(source);
            arrayList.add(newEdge);
            adj_list.put(source, arrayList);
        }

        Edge newEdge2 = new Edge(source, 0);
        if(adj_list.get(destination) == null) {
            arrayList2.add(newEdge2);
            adj_list.put(destination, arrayList2);
        } else {
            arrayList2 = adj_list.get(destination);
            arrayList2.add(newEdge2);
            adj_list.put(destination, arrayList2);
        }

    }

    /** TODO
    * implement BFS function        
    *
    * @return true if there is a path; if no, return false.
    */
    boolean bfs(int source, int destination)
    {
        boolean visited[] = new boolean[N+2];
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(source);
        parent[source] = -1;
        visited[source] = true;

        while(!queue.isEmpty()){
            int u = queue.remove();
            for (int v = 0; v < adj_list.get(u).size(); v++){
                int w = adj_list.get(u).get(v).destination;
                if(!visited[w] && adj_list.get(u).get(v).flow_rate > 0){
                    if(w == destination){
                        parent[w] = u;
                        return true;
                    }
                    queue.add(w);
                    parent[w] = u;
                    visited[w] = true;
                }
            }
        }
        return false;

    }

    /** TODO
    * implement path augmentation
    *
    * traverse the graph using BFS to find a path from source to sink
    * find the possible amount of flow along the path
    * add the flow to the total maximum flow
    * update the flow rate of the edges along the path 
    * repeat as long as a path exist from source to sink with nonzero flow
    *
    * @return maximum amount of flow
    */
    int pathAugmentation()
    {
//        System.out.println(adj_list);
        int source = 0;
        int sink = N + 1;
        int maxflow = 0;
        this.parent = new int[N + 2];

        while (bfs(source,sink)){
            ArrayList<Integer> arrayList = new ArrayList<>();
            int current = sink;
            while(current != source){
                arrayList.add(current);
                current = parent[current];
            }
            arrayList.add(source);
            int flow = amtFlow(source, sink);
            for(int i = 0; i + 1 < arrayList.size(); i++){
                flow = Math.min(flow, getFlow(arrayList.get(i+1), arrayList.get(i)));
            }
            for(int i = 0; i + 1 < arrayList.size(); i++){
                setFlow(arrayList.get(i + 1), arrayList.get(i), getFlow(arrayList.get(i+1), arrayList.get(i)) - flow);
                setFlow(arrayList.get(i), arrayList.get(i+1), getFlow(arrayList.get(i), arrayList.get(i + 1)) + flow);
            }
            maxflow = maxflow + flow;
        }

        /*default value provided*/
        return maxflow;
    }

    /** TODO
    * get the flow along a certain edge
    *
    * @param source : source node of the directed edge
    * @param destination : destination node of the directed edge
    *
    * @return flow rate along the edge
    */
    int getFlow(int source, int destination)
    {
        for(int i = 0; i < adj_list.get(source).size(); i++) {
            if (adj_list.get(source).get(i).destination == destination){
                return adj_list.get(source).get(i).flow_rate;
            }
        }
        return 0;
    }


    int amtFlow(int source, int destination){
        int result = Integer.MAX_VALUE;
        int current = destination;
        while(current != source){
            for(int i = 0; i < adj_list.get(parent[current]).size(); i++) {
                if(adj_list.get(parent[current]).get(i).destination == current) {
                    int temp = adj_list.get(parent[current]).get(i).flow_rate;
                    if (result > temp){
                        result = temp;
                    }
                }
            }
            current = parent[current];
        }
        return result;
    }
    /** TODO
    * set the value of flow along a certain edge
    *
    * @param source : source node of the directed edge
    * @param destination : destination node of the directed edge
    * @param flow_rate : flow rate along the edge        
    */
    void setFlow(int source, int destination, int flow_rate)
    {
        for(int i = 0; i < adj_list.get(source).size(); i++) {
            if (adj_list.get(source).get(i).destination == destination) {
                adj_list.get(source).get(i).flow_rate = flow_rate;
            }
        }
    }

    public static void main(String []args)
    {
        try {
            MaxFlow obmax = new MaxFlow(0);
            File myObj = new File("C:\\Users\\Eddy\\IdeaProjects\\CS 251 Project 3\\src\\sampleMaxFlowData.txt");
            Scanner myReader = new Scanner(myObj);
            int line = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
                if(line == 0)
                {
                    int tot = Integer.parseInt(data);
                    System.out.println(tot);
                    obmax = new MaxFlow(tot);
                }
                else
                {
                    String []comp = data.split(" ");
                    int s = Integer.parseInt(comp[0]);
                    int d = Integer.parseInt(comp[1]);
                    int f = Integer.parseInt(comp[2]);
                    System.out.println(s+" "+d+" "+f);
                    obmax.insEdge(s, d, f);
                }
                line += 1;
            }
            myReader.close();
            int mflow = obmax.pathAugmentation();
            System.out.println("Maxflow is: "+mflow);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
