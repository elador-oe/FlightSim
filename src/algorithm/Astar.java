package algorithm;

import java.util.ArrayList;
import java.util.HashSet;

public class Astar<Solution,heuristic> extends CommonSearcher<Solution> {
	
	
	public interface Heuristic{
		public double cost (State s, State goalState);
	}
	Heuristic heuristic;
	public Astar (Heuristic heuristic) {
		this.heuristic = heuristic;
	}
	@Override
	public Solution search(Searchable s) {
		double nCost,stateCost;
		openList.add(s.getInitialState());
		HashSet< State > closedSet=new HashSet< State >();
		while(openList.size()>0)
		{
			State n=openList.poll ();// dequeue
			closedSet.add(n);
			ArrayList< State > successors=s.getAllPossibleStates(n); //however it is implemented
			n.setState(n.cost+heuristic.cost(n,s.getGoalState()));
			if(n.equals(s.getGoalState()))
				return (Solution)n.getState ();
				// private method, back traces through the parents
			for( State state : successors){
				state.setState (state.getCost ()+ heuristic.cost(state,s.getGoalState()));
				if(!closedSet.contains(state) && ! openList.contains(state)){
					state.setState (n);
					openList.add(state);
				}
				else if(n.getCost ()+(state.getCost ()-state.getCost ())<state.getCost ())
					 	if(openList.contains(state))
					 		state.setState (n);
						else  {
							state.setState (n);
							closedSet.remove(state);
							openList.add(state);
					}
			}
		}
		return (Solution)s.getGoalState();
	
	}


}
