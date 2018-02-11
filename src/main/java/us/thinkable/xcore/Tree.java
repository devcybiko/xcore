package us.thinkable.xcore;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree is a trivial tree data structure.  It is intended that the user will extend Tree to 
 * include other user-specified data.  Traversal of the tree is performed by the included
 * depth-first-search (dfs) method.
 * 
 * @author Gregory Smith
 *
 */
public class Tree {
	/**
	 * a list of Tree nodes which are the children to this node.
	 */
	private List<Tree> children = new ArrayList<Tree>();
	/**
	 * the single parent of this node.
	 */
	private Tree parent = null;

	/**
	 * trivial constructor
	 */
	public Tree() {
		init(null);
	}

	/**
	 * constructor with parent
	 * @param parent the parent node to this new node
	 */
	public Tree(Tree parent) {
		init(parent);
	}

	/**
	 * a general init method for all constructors
	 * @param parent
	 */
	private void init(Tree parent) {
		if (parent != null) {
			parent.add(this);
		}
	}

	/**
	 * add a node to the end of this node's list of children
	 * @param node the node to add
	 */
	public void add(Tree node) {
		node.remove();
		children.add(node);
		node.parent = this;
	}

	/**
	 * add a node to the this node's children, but at a specific location in the list of children
	 * (often used to place a node at the head of the list with index=0)
	 * @param index the position in the child list to add this new node to
	 * @param node the node to add
	 */
	public void add(int index, Tree node) {
		node.remove();
		children.add(index, node);
		node.parent = this;
	}

	/**
	 * remove this node from its parent's tree
	 */
	public void remove() {
		if (parent != null) {
			parent.children.remove(this);
			this.parent = null;
		}
	}

	/**
	 * get the parent to this node
	 * @return the parent of this node
	 */
	public Tree getParent() {
		return parent;
	}

	/**
	 * get the selected child from the list of this node's children
	 * @param index the child to retrieve
	 * @return the selected child
	 */
	public Tree getChild(int index) {
		return children.get(index);
	}

	/**
	 * returns the list of children to this node
	 * @return the list of (never null) children 
	 */
	public List<Tree> getChildren() {
		return children;
	}

	/**
	 * returns the root (node with no parent) node
	 * @return root node
	 */
	public Tree getRoot() {
		Tree root = this;
		while (root.parent != null) {
			root = root.parent;
		}
		return root;
	}

	/**
	 * recursively descend into the tree and call the "enter" and "exit" methods of the context.
	 * 
	 * @param context a user-supplied context that contains enter and exit methods to call during the descent into each node in the tree
	 * @return true if the dfs method was stopped prematurely, false otherwise.
	 */
	public boolean dfs(TreeContext context) {
		boolean stop = context.enter(this);
		if (!stop) {
			for (Tree node : children) {
				stop = node.dfs(context);
				if (stop) {
					return stop;
				}
			}
			stop = context.exit(this);
			if (stop) {
				return stop;
			}
		}
		return stop;
	}

}
