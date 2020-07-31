import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Apriori {
	
	static int[][] Transactions;
	static int min_threshold = 2;
	static int min_Confidence = 60;
	static ArrayList<Set> singleSet = new ArrayList<Set>();
	static ArrayList<Set> pairsSets = new ArrayList<Set>();
	static ArrayList<Set> triSets = new ArrayList<Set>();
	
	static class Set{
		int dim;
		public int[] items;
		public float counter;
		public float confidence;
		
		public Set(int dimension){
			this.dim = dimension;
			items = new int[dimension];
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		ReadFromFile();
		//CreateSingleList();
		//CreatePairList();
		//JoinPart();
	}
	
	public static void ReadFromFile() throws FileNotFoundException
	{
		File file = new File("Transaction.txt");
		Scanner transFile = new Scanner(file);
		Transactions = new int[11][];
		
		int j=0;
		
		while(transFile.hasNextLine()) {
			String data = transFile.nextLine();
			String[] st;
			st = (data.split(","));
			System.out.print(st[0] + "\n");
			
			int i=0;
			for (String string : st) {
				//Transactions[j] = new int[st.length];
				i++;
			}
			
			j++;
		}
	}
	
	
	static int search(ArrayList<Set> array, int item)
	{
		int isHere = 0;
		
		for(int i=0;i<array.size();i++) {
			for(int j=0;j<array.get(i).dim;j++) {
				if(array.get(i).items[j] == item) {
					array.get(i).counter ++;
					isHere = 1;
					break;
				}
			}
		}
		
		return isHere;
	}
	
	static void PairsCounter()
	{
		
		for(int k=0;k<pairsSets.size();k++) {
			int counter=0;
			for(int i=0;i<Transactions.length;i++) {
				counter = 0;
				for(int j=0;j<Transactions[i].length;j++) {
					
					if(pairsSets.get(k).items[counter] == Transactions[i][j])
						counter++;
					
					if(counter == 2) {
						pairsSets.get(k).counter ++;
						break;
					}
				}
			}
		}
	}
	
	static void AssociationRule()
	{
		//Double
		for(int k=0;k<triSets.size();k++) {
			
			System.out.print( "{" + triSets.get(k).items[0] + ", " + triSets.get(k).items[1]
					+ ", "  + triSets.get(k).items[2] + "}\n" );
			int c1 = 0, c2 = 1;
			
			for(int t = 0;t<3; t++) {
				
				int exist = 0;
				int i = 0;
				for(;i<pairsSets.size();i++) {
				
					if(triSets.get(k).items[c1] == pairsSets.get(i).items[0]&&
							triSets.get(k).items[c2] == pairsSets.get(i).items[1]) {exist = 1; break;}
				}
				
				if(exist == 1) {
					System.out.print("=>{" + pairsSets.get(i).items[0] + ", " + pairsSets.get(i).items[1] + "} " +
							(triSets.get(k).counter/pairsSets.get(i).counter) * 100 + "\n");
				}
				
				if(t==0) { c1 = 0; c2 = 2;}
				else if(t==1) {c1 = 1; c2 = 2;}
			}
			
			for(int j = 0;j<singleSet.size();j++) {
				
				int confi = (int) ((triSets.get(k).counter/singleSet.get(j).counter) * 100);
				
				//if(confi > min_Confidence) {
					System.out.print(confi + "\n");
				//}
			}
			
		}
		
		//Single
	}
	
	static void JoinPart()
	{
		for(int i=0;i<singleSet.size() - 1;i++) {
			for(int j = i+1;j<singleSet.size();j++) {
				for(int k= j+1;k<singleSet.size();k++) {
					Set set = new Set(3);
					set.items[0] = singleSet.get(i).items[0];
					set.items[1] = singleSet.get(j).items[0];
					set.items[2] = singleSet.get(k).items[0];
					set.counter = 0;
					triSets.add(set);
				}
			}
		}
		
		for(int k=0;k<triSets.size();k++) {
			int c1 = 0, c2 = 1;
			
			for(int t = 0;t<3; t++) {
				
				int exist = 0;
				int i = 0;
				for(;i<pairsSets.size();i++) {
				
					if(triSets.get(k).items[c1] == pairsSets.get(i).items[0]&&
							triSets.get(k).items[c2] == pairsSets.get(i).items[1]) {exist = 1; break;}
				}
				
				if(exist == 0) {
					triSets.remove(k);
					k--;
					break;
				}
				
				if(t==0) { c1 = 0; c2 = 2;}
				else if(t==1) {c1 = 1; c2 = 2;}
			}
		}
		
		for(int k=0;k<triSets.size();k++) {
			for(int i=0;i<Transactions.length;i++) {
				int t = 0;
				for(int j=0;j<Transactions[i].length;j++) {
					if(triSets.get(k).items[t] == Transactions[i][j] )
						t++;
					
					if(t >= 3) {
						triSets.get(k).counter++;
						break;
					}
				}
			}
		}
		
		AssociationRule();
	}
	
	static void CreatePairList()
	{
		for(int i=0;i<singleSet.size();i++) {
			for(int j = i+1;j<singleSet.size();j++) {
				Set set = new Set(2);
				set.items[0] = singleSet.get(i).items[0];
				set.items[1] = singleSet.get(j).items[0];
				set.counter = 0;
				pairsSets.add(set);
			}
		}
		
		PairsCounter();
		
		for(int i=0;i<pairsSets.size();) {
			if(pairsSets.get(i).counter < min_threshold) {
				pairsSets.remove(i);
			}else i++;
		}
	}
	static void CreateSingleList()
	{	
		for(int i=0;i<Transactions[0].length;i++) {
			Set set = new Set(1);
			set.items[0] = Transactions[0][i];
			set.counter = 1;
			singleSet.add(set);
		}
		
		for (int i=1;i<Transactions.length;i++) {
			for(int j=0;j<Transactions[i].length;j++) {
				if(search(singleSet, Transactions[i][j]) == 0) {
					Set set = new Set(1);
					set.items[0] = Transactions[i][j];
					set.counter = 1;
					singleSet.add(set);
				}
			}
		}
		
		for(int i=0;i<singleSet.size();) {
			if(singleSet.get(i).counter < min_threshold) {
				singleSet.remove(i);
			}
			else i++;
		}
		//sort 
		
		for(int i=0;i<singleSet.size();i++) {
			for(int j=i+1;j<singleSet.size();j++) {
				if(singleSet.get(i).items[0] > singleSet.get(j).items[0]) {
					Collections.swap(singleSet, i, j);
				}
			}
		}
	}
}
