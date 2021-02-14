package Practica_1;

public class IntegerParseInt {

	public static void main(String[] args) {
		String numero="2020";
		 
		int valor=Integer.parseInt(numero);
		
		System.out.println("valor String : "+ numero+ "\n después de parse in : "+ valor);
		
		valor+=10;
		
		System.out.println(valor);
		
		String decimal="10.52";
		
		float valorDecimal=Float.parseFloat(decimal);
		
		System.out.println(valorDecimal);
		
		
		

	}

}
