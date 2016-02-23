package fif_learning;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import fif_core.FuzzySet;

public class ProximityTable {
	
	private Hashtable<Couple, Double> table = new Hashtable<Couple,Double>();
	
	private class Couple {
		
		String first;
		String second;
		
		public Couple(String a, String b){
			first = a;
			second = b;
		}
		
		
		public boolean equals(Couple a){
			
			if((this.first.equalsIgnoreCase(a.first) && this.second.equalsIgnoreCase(a.second)) || 
				this.first.equalsIgnoreCase(a.second) && this.second.equalsIgnoreCase(a.first)) {
				return true;
			} else {
				return false;
			}
		}
		
		public String toString(){
			return first.toLowerCase() + "-" + second.toLowerCase();
		}
	}
	
	public void insert(String a, String b, double value){
		assert (value >= 0 && value <= 1) : "Invalid value";
		
		Couple temp = new Couple(a,b);
	
		Set<Couple> set = table.keySet();
		
		for(Couple c : set){
			if( c.equals(temp)){
				table.remove(c);
			}
		}
			
		table.put(temp, value);
		
	}
	
	public void printProximityTable(){
		Set<Couple> s = new HashSet<Couple>(table.keySet());
		for (Couple c: s){
			System.out.println(c.toString()+" - "+table.get(c));
		}
	}
	
	public double getMaxValue(){
		double maxValue = 0;
		Set<Couple> set = table.keySet();
		for(Couple c : set){
			if (table.get(c)>=0){
				maxValue = table.get(c);
			}
		}
		return maxValue;
	}
	
	public FuzzySet getFuzzySet(String a){
				
		FuzzySet fs = new FuzzySet();
		Set<Couple> set = table.keySet();
		
		for(Couple c : set){
			if(c.first.equalsIgnoreCase(a)){
				fs.setValue(c.second, table.get(c));
				
			}
			if(c.second.equalsIgnoreCase(a)){
				fs.setValue(c.first, table.get(c));
			}
		}
		return fs;
	}
	/*
	 * Return all the members in the table
	 */
	public HashSet<String>extractAllMembers(){
		HashSet<String>allMembers=new HashSet<String>();
		for(Couple p:table.keySet()){
			allMembers.add(p.first);
			allMembers.add(p.second);
		}
		return allMembers;
	}
}
