package fif_learning;

public class ProximityTableTest {

	private static ProximityTable table;
	
	public ProximityTableTest(){
		
		table = new ProximityTable();
		table.insert("horror", "fantasy", 0.2);
		table.insert("Clooney", "Hanks", 0.2);
		table.insert("Thriller", "Horror", 0.2);
		table.insert("Thriller", "SCI-FI", 0.5);
		table.insert("Thriller", "Giallo", 0.6);
		table.insert("Thriller", "Rosa", 0.2);
		table.insert("Comedy", "splatter", 0.6);
	
	}
	
	public ProximityTable getTable(){
		return table;
	}


}
