package fa.nfa;

import java.util.HashMap;
import java.util.LinkedHashSet;
import fa.State;


/**
 * @author Adel Touati
 *
 */
public class NFAState extends State {
	
	private HashMap<Character, LinkedHashSet<NFAState>> delta;
	private boolean isFinal;
	private boolean isStart;

	
	
	/**
	 * @param name
	 */
	private void initialization(String name){
		this.name = name;
		delta = new HashMap<Character, LinkedHashSet<NFAState>>();
	}

	/**
	 * @param name
	 */
	public NFAState(String name){
		initialization(name);
		isFinal = false;
		isStart = false;
		
	}
	/**
	 * @param name
	 * @param isFinal
	 */
	public NFAState(String name, boolean isFinal){
		initialization(name);
		this.isFinal = isFinal;
		
	}

	/**
	 * @return isFinal
	 */
	public boolean isFinal() {
		return isFinal;
	}
	
	
	public void setStart() {
		this.isStart = true;
	}
	/**
	 * @return isStart
	 */
	public boolean isStart() {
		return isStart;
	}
	/**
	 * @param symb
	 * @param toState
	 */
	public void addTransition(char symb, NFAState toState) {
		if (!delta.containsKey(symb)) {
			LinkedHashSet<NFAState> newSet = new LinkedHashSet<NFAState>();
			newSet.add(toState);
			delta.put(symb, newSet);
		} else {
			delta.get(symb).add(toState);
		}

	}

	/**
	 * @param symb
	 * @return HashMap of characters and LinkedHashSet of states 
	 */
	public LinkedHashSet<NFAState> getTo(char symb) {
		if (!delta.containsKey(symb)) {
			return new LinkedHashSet<NFAState>();
		} else {
			return delta.get(symb);
		}
	}
	
}
