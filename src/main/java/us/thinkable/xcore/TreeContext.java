package us.thinkable.xcore;

/**
 * a context object that is passed into each call to the recursive Tree.dfs() method.
 * 
 * As each node is 'entered' the 'enter' method is called with the node currently being examined.
 * 
 * Then, each child is passed to dfs()
 * 
 * After all the children have been processed, 'exit' is called.
 * 
 * This allows both pre-order and post-order processing of the dfs() tree.
 * 
 * 
 * @author Gregory Smith
 *
 */
public class TreeContext {

	/**
	 * this method is called by Tree.dfs() as each node is entered.
	 * 
	 * @param node the node to be searched
	 * @return true if the search should stop (we found what we were looking for) or false to continue
	 */
	public boolean enter(Tree node) {
		return false;
	}

	/**
	 * this method is called by Tree.dfs() as each node is entered.
	 * 
	 * @param node the node to be searched
	 * @return true if the search should stop (we found what we were looking for) or false to continue
	 */
	public boolean exit(Tree node) {
		return false;
	}
}
