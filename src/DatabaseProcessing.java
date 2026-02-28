import java.io.*;
import java.util.*;
public class DatabaseProcessing{
	
	private MyBST bst;
	
	public DatabaseProcessing() {
		this.bst = new MyBST();
	}
	
	public void loadData(String fileName) {
		bst = new MyBST();
		
		int count = 0;
		
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
			String line;
			
			while((line = br.readLine()) != null) {
				line = 	line.trim();
				if(line.isEmpty()) continue;
				
				PeopleRecord r = PeopleRecord.fromLine(line);
				if(r != null) {
					bst.insert(r);
					count++;
				}
			}
			
			System.out.println("Loaded records: " + count);
			System.out.println("BST info: " + bst.getInfo());
		}catch (FileNotFoundException e) {
			System.out.println("ERROR: file not found -> " + fileName);
		}catch(IOException e){
			System.out.println("ERROR: problem reading file -> " + e.getMessage());
		}
	}
	
	public ArrayList<PeopleRecord> search(String given, String family){
		if(bst == null) {
			bst = new MyBST();
		}
		return bst.search(given, family);
	}
	
}
