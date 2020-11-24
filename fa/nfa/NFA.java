package fa.nfa;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import fa.State;
import fa.dfa.DFA;

/**
 * @author Adel Touati
 *
 */
public class NFA implements NFAInterface{


	private Set<NFAState> allStates;
	private Set<NFAState> finalStates;
	private Set<Character> alphabet;
	private NFAState startState;
	
/**
 * Initializing parameters 
 */
public NFA() {
allStates = new LinkedHashSet<NFAState>();	
finalStates = new LinkedHashSet<NFAState>();	
alphabet = new LinkedHashSet<Character>();	
}
	
	/* (non-Javadoc)
	 * @see fa.FAInterface#addStartState(java.lang.String)
	 */
	@Override
	public void addStartState(String name) {
		// create new NFA State
		startState = new NFAState(name);
		// setting the state as start state
		startState.setStart();
		//adding the start state to the set of states
		allStates.add(startState);
		
	}


	/**
	 * @param name
	 * @return the state we getting
	 */
	private NFAState getState(String name) {
		NFAState gState = null;
		NFAState states= null;
		//iterator to iterate all the states
		Iterator<NFAState> iterator = allStates.iterator();
		// flag to quit the loop when is needed
		boolean flag = true;
		//iterate the states
		while(iterator.hasNext()){
		 states = iterator.next();
		 //making sure to return the right state and quit the loop
			if(states.getName().equals(name) && flag == true){
				gState = states;
				flag = false;
			}
		}
		return gState;
	}


	/* (non-Javadoc)
	 * @see fa.FAInterface#addState(java.lang.String)
	 */
	@Override
	public void addState(String name) {
		//create new NFA state
		NFAState state = new NFAState(name);
		//adding the state to the set of states
		allStates.add(state);
		
	}

	/* (non-Javadoc)
	 * @see fa.FAInterface#addFinalState(java.lang.String)
	 */
	@Override
	public void addFinalState(String name) {
		// create a new state and setting final to true 
		NFAState finalState = new NFAState(name, true);
		//adding the final state to the set of final states
		finalStates.add(finalState);
		//adding the final state to the set of states
		allStates.add(finalState);
		
	}

	/* (non-Javadoc)
	 * @see fa.FAInterface#addTransition(java.lang.String, char, java.lang.String)
	 */
	@Override
	public void addTransition(String fromState, char onSymb, String toState) {
		//adding the transition using the from state the symbol and the to state
		(getState(fromState)).addTransition(onSymb, getState(toState));
		//making sure the symbol is not in the alphabet set
		if (!alphabet.contains(onSymb)) {
			//making sure the symbol is not epsilon transition
			if(!(onSymb == 'e')) {
				//adding the new symbol
			alphabet.add(onSymb);
			}
		}
	}
		
	

	/* (non-Javadoc)
	 * @see fa.FAInterface#getStates()
	 */
	@Override
	public Set<? extends State> getStates() {
		//creating new set of states
		Set<State> gStates = new LinkedHashSet<State>();
		//adding all the states to the set of states
		gStates.addAll(allStates);
		return gStates;
	}

	@Override
	public Set<? extends State> getFinalStates() {
		Set<State> gfState = new LinkedHashSet<State>();
		NFAState state= null;
		//iterate all the states
		Iterator<NFAState> iterator = allStates.iterator();
		while(iterator.hasNext()){
		 state = iterator.next();
		//making sure the state is final before adding it to the set
			if(state.isFinal()){
				gfState.add(state);
			}
		}
		return gfState;
	}

	/* (non-Javadoc)
	 * @see fa.FAInterface#getStartState()
	 */
	@Override
	public State getStartState() {
		return startState;
	}

	/* (non-Javadoc)
	 * @see fa.FAInterface#getABC()
	 */
	@Override
	public Set<Character> getABC() {
		return alphabet;
	}
	
	/* (non-Javadoc)
	 * @see fa.nfa.NFAInterface#eClosure(fa.nfa.NFAState)
	 */
	public Set<NFAState> eClosure(NFAState s){
		//creating new set of NFA states
		HashSet<NFAState> eStates = new HashSet<>();
		// calling DFCeClosure method 
		return DFCeClosure(s, eStates);
	}
	
	
	private HashSet<NFAState> DFCeClosure(NFAState s, HashSet<NFAState> listE) {
		//Adding the actual state to the list 
		listE.add(s);
		//create a new set of states where the actual state can go with epsilon
		HashSet<NFAState> eStates = s.getTo('e');
		if (eStates != null) {
			//iterate through out eStates 
			for (NFAState iterateEstates : eStates) {
				//making sure the state is not already in the list 
				if(!(listE.contains(iterateEstates))) 
					//calling recursively to process all the states
				DFCeClosure(iterateEstates, listE);
				}
		}
		return listE;
	}

	/* (non-Javadoc)
	 * @see fa.nfa.NFAInterface#getDFA()
	 */
	public DFA getDFA() {
		//create a new DFA
		DFA dfa = new DFA();
		//to make is final or not 
		boolean finalFlag = false;
		//adding the eclosure of the start state to the set
		Set<NFAState> Q = eClosure(startState);
		//adding eclosure of the starting state to the DFA 
		dfa.addStartState(Q.toString());
		// Creating the queue to process the states
		ArrayDeque<HashSet<NFAState>> toProcess = new ArrayDeque<HashSet<NFAState>>();
		//adding the eclosure of the start state to the Queue
		toProcess.add((HashSet<NFAState>) Q);
		//creating a set of sets for checked States
		Set<Set<NFAState>> checkedStates = new HashSet<>();
		//making sure that start state is final 
		if(startState.isFinal()) {
			//adding the start state to DFA as final state
			dfa.addFinalState(startState.getName());
		}
		while(!(toProcess.isEmpty())) {
			//getting the unchecked state from the Queue
			HashSet<NFAState> uncheckedStates = toProcess.pollLast();
			//Keeping the value of the state
			String curState = uncheckedStates.toString();
			//Adding the unchecked states in checked state set
			checkedStates.add(uncheckedStates);
			//iterating through out the alphabet
			for(Character c : alphabet){
				HashSet<NFAState> notHundeledSet = new HashSet<NFAState>();
				//iterating through out the unchecked states and make is final or not
				for(NFAState s : uncheckedStates){
					if(s.isFinal()) {
						finalFlag = true;
					}
					//set of new set of transition sets
					HashSet<NFAState> newStatesT = s.getTo(c) ;
					//making sure that new states transition is not empty
						if(!newStatesT.isEmpty()) {
							//iterating through out the new states transition to get each state
							for (NFAState eachState : newStatesT) {
								//adding the set of eclosure of each new state
				         notHundeledSet.addAll(eClosure(eachState));
								//making sure is final
								if (eachState.isFinal()) {
								finalFlag = true;
								}
						}
					}
				}
				//getting the new to state
				String newToState = notHundeledSet.toString();
				//making sure that empty state is not final 
				if(newToState.equals("[]")) {
					finalFlag = false;
				}
				//checking the new state if is not final and add it to dfa 
				if (finalFlag == false) {
					if (!(toProcess.contains(notHundeledSet)) && !(checkedStates.contains(notHundeledSet))) {
						dfa.addState(newToState);
					}
					dfa.addTransition(curState, c, newToState);
				}else {
					//checking the new state to dfa as final
					if (!(toProcess.contains(notHundeledSet)) && !(checkedStates.contains(notHundeledSet))) {
						dfa.addFinalState(newToState);
					}
					dfa.addTransition(curState , c, newToState);
				}
				//adding the states not handled to the to process Queue
				if (!(toProcess.contains(notHundeledSet)) && !(checkedStates.contains(notHundeledSet))) {
					toProcess.addLast(notHundeledSet);

				}
				
			
			}
     }
		return dfa;
			
		}
		
		

	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		from = getToState(from.getName());
		if(from != null)
		{
			return from.getTo(onSymb);
		}
		return new LinkedHashSet<>();
	}

	private NFAState getToState(String FState){
		Iterator<NFAState> iterator1 = allStates.iterator();
		while(iterator1.hasNext()) {
			NFAState newState = iterator1.next();
			if(newState.getName().compareTo(FState)==0) {
				return newState;
			}
		}
		return null;
	
	}

}
