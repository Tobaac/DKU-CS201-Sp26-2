import java.util.ArrayList;

class MyBST{
	
	private static class Node{
		PeopleRecord data;
		Node left;
		Node right;
		
		Node(PeopleRecord data){
			this.data = data;
		}
	}
	
	private Node root;
	private int size;
	
	// insertt
	
	public void insert(PeopleRecord rec) {
		if(rec == null) return;
		
		root = insertRec(root, rec);
	}
	
	private Node insertRec(Node cur, PeopleRecord rec) {
		if(cur == null) {
			size++;
			return new Node(rec);
		}
		
		int cmp = rec.compareTo(cur.data);
		
		if(cmp<0) {
			cur.left = insertRec(cur.left,rec);
		}else {
			cur.right = insertRec(cur.right, rec);
		}
		
		return cur;
	}
}