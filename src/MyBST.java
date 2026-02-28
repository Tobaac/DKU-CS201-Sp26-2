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
	
	// get info

	public int[] getInfo() {
		return new int[] {size, height(root)};
	}
	
	// height definition: empty tree = 0, single node tree = 1
	private int height(Node node) {
		if (node == null) return 0;
		
		return 1+ Math.max(height(node.left), height(node.right));
	}
	
	// search 
    // Return all records with same given+family name (case-insensitive)
	
	public ArrayList<PeopleRecord> search(String given, String family){
		ArrayList<PeopleRecord> res = new ArrayList<>();
		searchInOrder(root, given,family, res);
		return res;
	}
	
	private void searchInOrder(Node node, String given, String family, ArrayList<PeopleRecord> res) {
		if(node == null) return;
		
		searchInOrder(node.left, given, family,res);
		
		if(node.data.sameName(given, family)) {
			res.add(node.data);
		}
		
		searchInOrder(node.right, given,family,res);
	}
}