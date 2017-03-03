/*
    Andre' Green
    This class simply generates a map with the shortest path.
 */
import java.util.*;

public class Pathfinding
{
    /**
     *
     * @param board
     * @param start_position
     * @param end_position
     * @return
     *
     * Returns a map giving the shortest path using Dijkstra's algorithm.
     * Will try to swap out for A* at some later point.
     */
    public static Map<PathNode,PathNode> getHeading( PathNode[][] board, PathNode start_position, PathNode end_position)
    {
        if(start_position == null) return null;

        PriorityQueue<PathNode> frontier = new PriorityQueue<>();
        frontier.add(start_position);
        Map<PathNode,PathNode> came_from = new HashMap<>();
        Map<PathNode,Double> cost_so_far = new HashMap<>();

        cost_so_far.put(start_position,0.0);
        came_from.put(start_position,null);
        double new_cost;

        PathNode current = null;
        LinkedList<PathNode> neighbors = new LinkedList<>();

        //System.out.println("let's begin");
        double dist;
        while( !frontier.isEmpty() )
        {

            current = frontier.poll();
            if (current == end_position) break;

            neighbors.removeAll(neighbors);
            // And now add the new neighbors.
            if( board[current.x+1][current.y+1] != null ){ neighbors.add( board[current.x+1][current.y+1]); }
            if( board[current.x+1][current.y-1] != null ){ neighbors.add( board[current.x+1][current.y-1]); }
            if( board[current.x+1][current.y] != null ){ neighbors.add( board[current.x+1][current.y]); }
            if( board[current.x-1][current.y+1] != null ){ neighbors.add( board[current.x-1][current.y+1]); }
            if( board[current.x-1][current.y-1] != null ){ neighbors.add( board[current.x-1][current.y-1]); }
            if( board[current.x-1][current.y] != null ){ neighbors.add( board[current.x-1][current.y]); }
            if( board[current.x][current.y+1] != null ){ neighbors.add( board[current.x][current.y+1]); }
            if( board[current.x][current.y-1] != null ){ neighbors.add( board[current.x][current.y-1]); }

            for(int i = 0; i < neighbors.size(); i++)
            {
                /* I don't square the middle terms b/c they're just -1 or 1.
                 We want to use double because of the cost associated for diagonals (sqrt(2)) is not easily
                 covered over by clever int arithmetic. */
                dist = Math.sqrt( Math.abs(current.x-neighbors.get(i).x) + Math.abs(current.y-neighbors.get(i).y));
                new_cost = cost_so_far.get(current) + dist;
                if( !came_from.containsKey(neighbors.get(i)) || new_cost < cost_so_far.get(neighbors.get(i)))
                {
                    cost_so_far.put(neighbors.get(i),new_cost);
                    neighbors.get(i).priority = new_cost;
                    frontier.add(neighbors.get(i));
                    came_from.put(neighbors.get(i),current);
                }
            }

        }
        return came_from;
    }



}
