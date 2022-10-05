package poo.progetti;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;
import java.util.StringTokenizer;

import poo.math.BigInt;
import poo.math.BigIntLL;

class MyComparator implements Comparator<Character> {

	@Override
	public int compare(Character o1, Character o2) {

		String high = "^";
		String mid = "*/%";
		String low = "+-";
		
		if( (high.contains(o1+"")&&high.contains(o2+"")) || 
			(mid.contains(o1+"")&&mid.contains(o2+"")) || 
			(low.contains(o1+"")&&low.contains(o2+""))) 
			return 0;
		
		if( (high.contains(o1+"")&&mid.contains(o2+"")) ||
			(high.contains(o1+"")&&low.contains(o2+"")) ||
			(mid.contains(o1+"")&&low.contains(o2+"")))
			return 1;
		
		return -1;
	}
	
}

public class VE {
	private String e;
	private MyComparator myComp = new MyComparator();
	
	public VE(String e) {
		this.e=e;
	}
	
	//metodo che scorre la stringa e la valuta e calcola secondo i criteri richiesti.
	private int valutaEspressione(StringTokenizer st) {
		Stack<Integer> operandi = new Stack<>();
		Stack<Character> operatori = new Stack<>();
		
		while(st.hasMoreTokens()) {
			String c = st.nextToken();
			char opc = '0';

			if(isOperator(c)) {
				opc = c.charAt(0);
				if(opc=='(') {
					operandi.push(valutaEspressione(st));
					continue;
				}
				if(opc==')') {
					break;
				}

				while(!operatori.empty()&&myComp.compare(opc, operatori.peek())<=0) {

					char op = operatori.pop();
					int o2 = operandi.pop(), o1 = operandi.pop();
					System.out.println("c");
					operandi.push(calcola(o1,op,o2));
				}
				operatori.push(opc);
				continue;
				
			}
			else {
				operandi.push(Integer.parseInt(c));
			}
		}

		while(!operatori.empty()) {
			char op = operatori.pop();
			int o2 = operandi.pop(), o1 = operandi.pop();
			
			operandi.push(calcola(o1,op,o2));
		}
		if(operandi.size()!=1) throw new IllegalArgumentException("Espressione Malformata"); 
		//System.out.println(operandi +"\n"+operatori);
		return operandi.pop();
	}
	
	//metodo che calcola l'espressione che riceve come parametro o1 op o2 (es. 1 + 2)  
	private Integer calcola(int o1, char op, int o2) {
		switch(op) {
			case '+': return o1+o2; 
			case '-': return o1-o2; //� possibile implementare controllo su o1>=o2 se non si vuole un risultato negativo
				/*if(o1<o2) throw new IllegalArgumentException("Espressione Malformata : impossibile effettuare la sottrazione");
				 *return o1-o2
				 * */
			case '*': return o1*o2; 
			case '/': 
				if(o2==0)
					throw new IllegalArgumentException("Espressione Malformata : impossibile dividere per 0"); 
				return o1/o2;
			case '%': 
				if(o2==0)
					throw new IllegalArgumentException("Espressione Malformata : impossibile calcolare il resto per 0"); 
				return o1%o2; 
			case '^': return (int) Math.pow(o1, o2); 
			default : throw new IllegalArgumentException("Espressione Malformata"); 
		}
	}
	
	//metodo che restituisce true se la stringa ricevuta � un operatore , false se � un operando, eccezione se nessuno dei due;
	private boolean isOperator(String c) { //stringa per poter valutare anche gli operandi da pi� di una cifra;
		String regexOperator = "[\\(\\)\\^\\*\\/\\%\\+\\-]+";
		String regexOperand = "[0-9]+";
		if(c.matches(regexOperand))
			return false;
		else if(c.matches(regexOperator))
			return true;
		else
			throw new IllegalArgumentException("Espressione Malformata");
	}
	
	//metodo per fare partire la risoluzione
	private int risolvi() {
		StringTokenizer st = new StringTokenizer(e,"-+/*%^()",true);
		return valutaEspressione(st);
	}
	
	
	public static void main(String[] args) {
		String e = "6*((1-3)*10)+(0-10)";

		VE ve = new VE(e);
		int res = ve.risolvi();
		System.out.println(e+" = "+res);
	}
}
