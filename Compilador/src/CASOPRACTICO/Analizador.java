package CASOPRACTICO;


import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import Nebrija.traductor.*;

public class Analizador {

	private ComponenteLexico componenteLexico;
	private Lexico lexico;
	private Hashtable<String, TipoDato> simbolos;
	private Vector<String> errores=new Vector<String>();
	private String comparaciontipo=null;
	private Stack<String> pila = new Stack<String>();

	//constructor
	public Analizador(Lexico lexico) {
		this.simbolos = new Hashtable<String, TipoDato>();
		this.lexico = lexico;
		this.componenteLexico = this.lexico.getComponenteLexico();
	}

	public String tablaSimbolos() {
		String simbolos = "";

		Set<Map.Entry<String, TipoDato>> s = this.simbolos.entrySet();

		for(Map.Entry<String, TipoDato> k: s)
			simbolos = simbolos + "<'" + k.getKey() + "'," + k.getValue().toString() + "> \n";

		return simbolos;
	}
	public String Pila() {
		String deVuelta="";
		for(String elem:this.pila){
			deVuelta+=elem+"\n";
		}
		return deVuelta;
	}


	private void compara(String etiqueta) {
		if(this.componenteLexico.getEtiqueta().equals(etiqueta)) {
			//System.out.println(this.componenteLexico.toString());
			this.componenteLexico = this.lexico.getComponenteLexico(); //AVANZA
		}else 
			System.out.println("Error, se esperaba " + etiqueta);
	}

	//programa -> void main { declaraciones }
	public void programa() {
		compara("void");
		compara("main");
		compara("open_bracket"); // {

		declaraciones();
		instrucciones();

		compara("closed_bracket");
		pila.add("halt"); // }

	} 

	//declaraciones -> declaración-variable declaraciones | epsilon
	private void declaraciones() {
		String etiqueta = this.componenteLexico.getEtiqueta();

		if(etiqueta.equals("int") || etiqueta.equals("float") || etiqueta.equals("boolean")) {
			declaracionVariable();
			declaraciones();
		}else {
			//epsilon
		}

	}

	public void declaracionVariable() {
		String tipo = tipoPrimitivo();// te dice el tipo que es Int o FLoat boolean
		int tamaño = 1;
		// partimos de que tipoPrimitivo ya ha hecho el cambio 
		//si despues del tipo viene un open_square_bracket es una declacion de un tipo-vector, 
		//en caso contrario, es una lista de identificadores de tipos primitivos int o float 

		if (this.componenteLexico.getEtiqueta().equals("open_square_bracket")){ 
			// tipo-vector 

			compara("open_square_bracket");  // [ avanza

			if(this.componenteLexico.getEtiqueta().equals("int")) {
				NumeroEntero numero = (NumeroEntero) this.componenteLexico; //casting 
				tamaño = numero.getValor();
			}
			compara("int");// avance
			compara("closed_square_bracket");  // ] completa vector y avanza

			if(this.componenteLexico.getEtiqueta().equals("id")) {
				Identificador id = (Identificador) this.componenteLexico;
				this.simbolos.put(id.getLexema(), new TipoArray(tipo,tamaño));
			}

			compara("id");
			compara("semicolon"); //

		}else { // tipo-primitivo
			this.comparaciontipo=tipo;
			//tipo primitivo int o float , boolean
			listaDeIdentificadores(tipo);
			compara("semicolon");
			this.comparaciontipo=null;

		}

	}

	//lista-identificadores -> id asignacion-declaración más-identificadores
	private void listaDeIdentificadores(String tipo) {
		if(this.componenteLexico.getEtiqueta().equals("id")) {// tipo ya te hace la transicion

			Identificador id = (Identificador) this.componenteLexico;
			if(this.simbolos.get(id.getLexema())!=null) {
				this.errores.add("ERROR, en la linea "+this.lexico.getLineas()+" , varibale "+
						id.getLexema() +" ya ha sido declarada");
			}else {
				//añade el lexema del id y su tipo a la tabla de simbolos 
				simbolos.put(id.getLexema(), new TipoPrimitivo(tipo));
				pila.add("lvalue "+id.getLexema());
			}
			compara("id");
			asignacion();// pasar el id , para la tabla de simbolos.
			masIdentificadores(tipo);

		}
	}

	//más-identificadores -> , id asignacion-declaración más-identificadores | epsilon
	private void masIdentificadores(String tipo) {

		if(this.componenteLexico.getEtiqueta().equals("comma")) {
			compara("comma"); // , 

			if(this.componenteLexico.getEtiqueta().equals("id")) {

				Identificador id = (Identificador) this.componenteLexico; //casting 
				this.simbolos.put(id.getLexema(), new TipoPrimitivo(tipo));
				pila.add("lvalue "+id.getLexema());
				compara("id");
				asignacion();
				masIdentificadores(tipo);
			}

		}else {
			//epsilon
		}
	}

	//asignacion -> = expresión-logica | epsilon
	public void asignacion() {
		if(this.componenteLexico.getEtiqueta().equals("assignment")) {
			compara("assignment");
			expresionLogica();
			pila.add("=");
		}else {
			//epsion
		}

	}


	//tipo-primitivo -> int | float | boolean
	private String tipoPrimitivo() {
		String tipo = this.componenteLexico.getEtiqueta();

		if(tipo.equals("int")) {
			compara("int");
		}
		else if(tipo.equals("float")) {
			compara("float");
		}else if(tipo.equals("boolean")) {
			compara("boolean");
		}else{
			System.out.println("Error, se esperaba un tipo de dato Int , Float , Boolean");
		}
		return tipo;
	}

	//instrucciones -> 	instrucción instrucciones | epsilon
	public void instrucciones() {  
		if(this.componenteLexico.getEtiqueta().equals("closed_bracket")){

		}else {
			instruccion();
			instrucciones();
		}
	}

	/*instrucción -> declaración-variable |
					 variable = expresión-logica ; |
					 if (expresión-lógica) instrucción |
					 if (expresión-lógica) instrucción else instrucción |
					 while (expresión-lógica) instrucción |
 					 do instrucción while (expresión-lógica) ; |
					 print (variable) ; |
					 { instrucciones }
	 */


	/*expresion -> expresión + término |
	 * 			   expresión – término |
	 * 			   término
	 * 
	 * Quitando la recursividad por la derecha 
	 * 
	 * expresion => termino expresionPrima
	 * 		ExpresionPrima=> + termino expresionPrima
	 * 					  => - termino expresionPrima
	 * 					  => epsilon
	 */
	private void expresion() {
		termino();
		expresionPrima();
	}
	private void expresionPrima() {
		if(this.componenteLexico.getEtiqueta().equals("add")) {
			compara("add");
			termino();
			pila.add("+");
			expresionPrima();
		}else if(this.componenteLexico.getEtiqueta().equals("subtract")) {
			compara("subtract");
			termino();
			pila.add("-");
			expresionPrima();
		}else {
			//epsilon
		}
	}
	/*termino -> término * factor |
	 * 		   	 término / factor |
	 * 			 término % factor |
	 * 			 factor
	 * Quitando la recursividad por la izquierda
	 * 
	 * Termino=> factor Termino'
	 * 		Termino'=> * factor Termino'
	 * 		Termino'=> / factor Termino'
	 * 		Termino'=> % factor Termino'
	 * 		Termino'=> epsilon
	 */
	private void termino() {
		factor();
		terminoPrima();
	}

	private void terminoPrima() {
		if(	this.componenteLexico.getEtiqueta().equals("multiply")){
			compara("multiply");
			factor();
			pila.add("*");
			terminoPrima();
		}else if(this.componenteLexico.getEtiqueta().equals("divide")) {
			compara("divide");
			factor();
			pila.add("/");
			terminoPrima();
		}else if(this.componenteLexico.getEtiqueta().equals("remainder") ) {
			compara("remainder");
			factor();
			pila.add("%");
			terminoPrima();
		}else {
			//epsilon
		}
	}

	/*factor ->  ( expresión ) |
	 * 			  variable |
	 *  		  num
	 */
	private void factor() {

		if (this.componenteLexico.getEtiqueta().equals("open_parenthesis")) {
			compara("open_parenthesis");
			expresion();
			compara("closed_parenthesis");
		}
		else if (this.componenteLexico.getEtiqueta().equals("int")) {
			if(this.comparaciontipo!=null) {//si estamos haciendo una comparacion 
				if(!this.comparaciontipo.equals("int")) {
					this.errores.add("ERROR, en la liena "+this.lexico.getLineas()+""
							+ " se intenta asignar un "+this.comparaciontipo+" con un int");
				}
			}
			NumeroEntero numero = (NumeroEntero) this.componenteLexico;
			pila.add("push " + numero.getValor());
			compara("int");
		}else if(this.componenteLexico.getEtiqueta().equals("float")) {
			if(this.comparaciontipo!=null) {
				if(!this.comparaciontipo.equals("float")) {
					this.errores.add("ERROR, en la liena "+this.lexico.getLineas()+""
							+ " se intenta asignar un "+this.comparaciontipo+" con un float");
				}
			}
			NumeroReal numero = (NumeroReal) this.componenteLexico;
			pila.add("push " + numero.getValor());
			compara("float");
		}else {
			variable();
		}
	}

	//variable ->  id | id [ expresión ]
	private void variable() {
		if(this.componenteLexico.getEtiqueta().equals("id")) {
			Identificador id= (Identificador) this.componenteLexico;
			if(this.simbolos.get(id.getLexema())==null) {// si no encuentra el id en la tabal 
				this.errores.add("ERROR, en la linea "+this.lexico.getLineas()+" , varibale "+id.getLexema() +" no ha sido declarada");
			}// si lo ha encontrado
			compara("id");// avanzamos 
			if (this.componenteLexico.getEtiqueta().equals("open_square_bracket")) {//miramos si es un vector
				compara("open_square_bracket");  // [
				expresion();
				compara("closed_square_bracket");  // ]
			}
			if(this.comparaciontipo!=null) {
				if(this.simbolos.get(id.getLexema()).getTipo()!= this.comparaciontipo.toString()) {
					this.errores.add("ERROR, en la linea "+this.lexico.getLineas()+" ,incompatibilidad de tipos en la instruccion de asignacion"
							+ "   "+id.getLexema());
				}else {
					pila.add("rvalue "+id.getLexema());
				}
			}
			if(pila.lastElement()=="Print") {
				pila.setElementAt(("Print "+id.getLexema()), pila.size()-1);
			}
		}
	}

	/*expresion-logico -> expresion-logica || termino-logico |
	 * 					  termino-logico
	 * 
	 * Quitando la recursividad por la izquierda
	 * 
	 * expresionlogica -> termino-logico ExpresionLogica' | 
	 * 					EXP'=>	|| termino logico EXP' | epsilon
	 */
	private void expresionLogica() {
		terminoLogico();
		expresionLogicaPrima();

	}
	private void expresionLogicaPrima(){
		if (this.componenteLexico.getEtiqueta().equals("or")) {
			compara("or");
			terminoLogico();
			expresionLogicaPrima();
		}else {
			//epsilon
		}	
	}

	/*termino-logico -> termino-logico && factor-logico | 
	 * 					factor-logico
	 * 
	 * Quitando la recursividad por la izquierda
	 * Termino logico => factor Logico terminoLogico' 
	 * 		TerminoLogico'=> && factor logico Terminologico' | epsilon
	 */
	private void terminoLogico() {
		factorLogico();
		terminoLogicoPrimo();
	}
	private void terminoLogicoPrimo() {
		if (this.componenteLexico.getEtiqueta().equals("and")) {
			compara("and");
			factorLogico();
			terminoLogicoPrimo();
		}else {
			//epsilon
		}		
	}

	/*factor-logico -> ! factor-logico | true | false | 
	 * 				   expresion-relacional
	 */
	private void factorLogico() {
		if (this.componenteLexico.getEtiqueta().equals("not")) {
			compara("not");
			factorLogico();
		}else if(this.componenteLexico.getEtiqueta().equals("true")) {
			compara("true");
		}else if(this.componenteLexico.getEtiqueta().equals("false")) {
			compara("false");
		}else {
			expresionRelacional();
		}

	}

	/*expresion-relacional -> expresión operador-relacional expresión |
	 * 						  expresion
	 */
	private void expresionRelacional() {
		expresion();
		if( 
				this.componenteLexico.getEtiqueta().equals("greater_than") ||
				this.componenteLexico.getEtiqueta().equals("greater_equals") ||
				this.componenteLexico.getEtiqueta().equals("less_than") ||
				this.componenteLexico.getEtiqueta().equals("less_equals") ||
				this.componenteLexico.getEtiqueta().equals("equals") ||
				this.componenteLexico.getEtiqueta().equals("not_equals")
				) {
			operadorRelacional();
			expresion();
		}

	}

	//operador-relacional -> < | <= | > | >= | == | !=
	private void operadorRelacional() {
		if (this.componenteLexico.getEtiqueta().equals("less_than")) {
			compara("less_than");
			factor();
			pila.add("<");
		}
		else if (this.componenteLexico.getEtiqueta().equals("less_equals")) {
			compara("less_equals");
			factor();
			pila.add("<=");
		}
		else if (this.componenteLexico.getEtiqueta().equals("greater_than")) {
			compara("greater_than");
			factor();
			pila.add(">");
		}
		else if (this.componenteLexico.getEtiqueta().equals("greater_equals")) {
			compara("greater_equals");
			factor();
			pila.add(">=");
		}
		else if (this.componenteLexico.getEtiqueta().equals("equals")) {
			compara("equals");
			factor();
			pila.add("==");

		}
		else if (this.componenteLexico.getEtiqueta().equals("not_equals")) {
			compara("not_equals");
			factor();
			pila.add("!=");
		}
	}
	//////////////////////////////
	public String errores() {
		if(this.errores.isEmpty()) {
			return "El programa ha compilado correctamente";
		}else {
			String deVuelta="";
			for(String elem: this.errores) {
				deVuelta+="*"+elem+"\n";
			}
			return deVuelta;
		}
	}
	private void instruccion() { //detecta fallos de gramatica segun el componente que uses en las instrucciones
		if(this.componenteLexico.getEtiqueta().equals("int")||this.componenteLexico.getEtiqueta().equals("float")
				||this.componenteLexico.getEtiqueta().equals("boolean")) {
			declaracionVariable();
		}else if(this.componenteLexico.getEtiqueta().equals("id")){
			Identificador id =(Identificador) this.componenteLexico;
			pila.add("lvalue "+id.getLexema());
			variable();
			compara("assignment");
			this.comparaciontipo=(this.simbolos.get(id.getLexema())==null)?null:this.simbolos.get(id.getLexema()).getTipo();
			expresionLogica();this.comparaciontipo=null;
			pila.add("=");
			compara("semicolon");


		}else if(this.componenteLexico.getEtiqueta().equals("if")) {
			compara("if");
			compara("open_parenthesis");
			if(this.componenteLexico.getEtiqueta().equals("id")) {
				Identificador id= (Identificador) this.componenteLexico;
				pila.add("rvalue "+id.getLexema());
			}
			expresionLogica();//condicion
			compara("closed_parenthesis");
			pila.add("gofalse "+lastLabel("gofalse"));
			instruccion();
			pila.add("label_"+cantLavel());
		}else if(this.componenteLexico.getEtiqueta().equals("else")) {
			compara("else");
			pila.remove(pila.lastIndexOf(lastLabel("label")));
			pila.add("goto "+lastLabel("gofalse"));
			pila.add(lastLabel("label_gofalse"));
			instruccion();
			pila.add(lastLabel("label_goto"));

		}else if(this.componenteLexico.getEtiqueta().equals("while")) {
			compara("while");
			pila.add("label_"+cantLavel());
			compara("open_parenthesis");
			if(this.componenteLexico.getEtiqueta().equals("id")) {
				Identificador id= (Identificador) this.componenteLexico;
				pila.add("rvalue "+id.getLexema());
			}
			expresionLogica();
			compara("closed_parenthesis");
			if(this.componenteLexico.getEtiqueta().equals("semicolon")) {
				compara("semicolon");
				pila.remove(pila.lastIndexOf(lastLabel("label")));
				pila.add("gofalse label_"+cantLavel());
				pila.add("goto "+lastLabel("label"));
				pila.add(lastLabel("label_gofalse"));

			}else {
				pila.add("gofalse label_"+cantLavel());
				instruccion();
				pila.add("goto "+lastLabel("last_label_goto"));
				pila.add(lastLabel("label_gofalse"));
			}
		}else if(this.componenteLexico.getEtiqueta().equals("do")) {
			compara("do");
			pila.add("label_"+cantLavel());
			instruccion();
		}else if(this.componenteLexico.getEtiqueta().equals("print")) {
			pila.add("Print");
			compara("print");
			compara("open_parenthesis");
			variable();
			compara("closed_parenthesis");
			compara("semicolon");
		}else if(this.componenteLexico.getEtiqueta().equals("open_bracket")) {
			compara("open_bracket");
			instrucciones();
			compara("closed_bracket");
		}
	}
	/////////////////////////////////Extras
	public int cantLavel() {// declara
		int repeticiones=0;
		for(String elem:this.pila) {
			if(elem.contains("label_") && !elem.contains("gofalse") && !elem.contains("goto")) {
				repeticiones+=1;
			}
		}
		return repeticiones;
	}
	public String lastLabel(String comando) {// devuelve

		switch(comando) {
		case "label":{
			String deVuelta="";
			for(String elem:this.pila) {
				if(elem.contains("label_") && !elem.contains("gofalse") && !elem.contains("goto")) {
					deVuelta=elem;
				}
			}
			if(deVuelta=="")deVuelta="label_0";// devuelve el ultimo label registrado o 0
			return deVuelta;
		}
		case "gofalse":{// busca el ultimo gofalse 
			String comprobante="";
			for(String elem:this.pila) {
				if(elem.contains("label_") && !elem.contains("goto")) {
					comprobante=elem;
				}
			}
			if(comprobante.contains("gofalse")) {// si lo contiene , implica que está uno dentro de otro
				return "label_"+ (Character.getNumericValue(comprobante.charAt(comprobante.length()-1))+1);
			}else {
				return lastLabel("label");//buscamos el ultimo label
			}

		}
		case "label_gofalse" :{// me devuelve el label que no este en la pila d
			for(int i= this.pila.size()-1;i>=0;i--) {
				if(this.pila.get(i)!=null && this.pila.get(i).contains("gofalse")) {
					if(!this.pila.contains(this.pila.get(i).substring(8))) {
						return this.pila.get(i).substring(8);
					}
				}
			}
		}
		case "label_goto":{
			for(int i= this.pila.size()-1;i>=0;i--) {
				if(this.pila.get(i)!=null && this.pila.get(i).contains("goto")) {
					if(!this.pila.contains(this.pila.get(i).substring(5))) {
						return this.pila.get(i).substring(5);
					}
				}
			}
		}
		case "last_label_goto":
			for(int i= this.pila.size()-1;i>=0;i--) {
				if(this.pila.get(i)!=null && this.pila.get(i).contains("gofalse")) {
					if(!this.pila.contains(this.pila.get(i).substring(8))) {
						for(int j= i;j>=0;j--) {
							if(this.pila.get(j)!=null && 
									this.pila.get(j).contains("label_")&&
									!this.pila.get(j).contains("gofalse")&&
									!this.pila.get(j).contains("goto")
									) {
								return this.pila.get(j);
							}
						}
					}
				}
			}
		}
		return null ;
	}
}