package pipeline;

import java.util.*;

import base.Instruction;

public class Pipeline {
	public int[] memoria = new int[32];
	public int[] registradores = new int[32];
	public int pc = 0;

	static RegistradorPipeline ifID = new RegistradorPipeline();
	static RegistradorPipeline idEx = new RegistradorPipeline();
	static RegistradorPipeline exMem = new RegistradorPipeline();
	static RegistradorPipeline memWb = new RegistradorPipeline();
	static RegistradorPipeline regAUX = new RegistradorPipeline();
	
	static RegistradorPipeline ifIDAUX = new RegistradorPipeline();
	static RegistradorPipeline idExAUX = new RegistradorPipeline();
	static RegistradorPipeline exMemAUX = new RegistradorPipeline();
	static RegistradorPipeline memWbAUX = new RegistradorPipeline();
	
	// Função para o Main poder acessar os valores da memoria
	// Entrada: Nenhuma
	// Saida: Valor referente a posicao na memoria
	public int[] getMemoria() {
		return memoria;
	}

	// Função para o Main poder acessar os valores dos registradores
	// Entrada: Nenhuma
	// Saida: Valor referente a posicao no registrador
	public int[] getRegistradores() {
		return registradores;
	}
	
	// Função para o Main poder acessar o valor do PC
	// Entrada: Nenhuma
	// Saida: Valor referente ao PC
	public int getPc() {
		return pc;
	}
	
	// Função pra inicializar os registradores e vetores com 1
	// Entrada: Nenhuma
	// Saida: Nenhuma
	public void inicializarVetores() {
		for(int i = 0; i < 32; i++) {
			registradores[i] = 1;
			memoria[i] = 0;
		}
	}
	
	// Função pra imprimir todas as posições do registrador
	// Entrada: Vetor de registradores
	// Saida: Nenhuma
	public void imprimirRegistradores(int [] registradores) {
		int i, j;
		for (i = 0, j = 16; i < 16 && j < 32; i++, j++) {
			System.out.println("registrador[" + i + "] = " + registradores[i]+ "    " + "registradores[" + j + "] = " + registradores[j]);
		}
	}
	
	// Função pra imprimir todas as posições da memoria
	// Entrada: Vetor da memoria
	// Saida: Nenhuma
	public void imprimirMemoria(int [] memoria) {
		int i, j;
		for (i = 0, j = 16; i < 16 && j < 32; i++, j++) {
			System.out.println("memoria[" + i + "] = " + memoria[i] + "    " + "memoria[" + j + "] = " + memoria[j]);
		}
	}
	
	// Funcao necessaria para ir atualizando os vamos dos registradores do pipeline, para que as instruções possam trafegar entre as funções
	// Entrada: Nenhuma
	// Saida: Nenhuma
	public void atualizarRegistradoresPipeline() {
		ifID = ifIDAUX;
		idEx = idExAUX;
		exMem = exMemAUX;
		memWb = memWbAUX;
		
		ifIDAUX =  new RegistradorPipeline();
		idExAUX =  new RegistradorPipeline();
		exMemAUX = new RegistradorPipeline();
		memWbAUX = new RegistradorPipeline();
	}
	
	// Função para verificar se em algum dos estados está com alguma instrução sendo executada
	// Entrada: Nenhuma
	// Saida: retorna 1 se ainda tiver instrução executando e 0 caso contrario
	public boolean estaExecutando() { 
		return ifID.instruction != null || idEx.instruction != null ||
				exMem.instruction != null || memWb.instruction != null;
	}

	// Estágio IF
	// Função inicial no pipeline que vai receber a instrucao e atualizar o valor de PC
	// Entrada: A lista de instruções que serão lidas
	// Saida: Nenhuma
	public void IF(List<Instruction> lista_instrucoes) {
		if (pc / 4 < lista_instrucoes.size()) {
			ifIDAUX.instruction = lista_instrucoes.get(pc / 4);
			ifIDAUX.pc = pc;
			pc +=4;
			
		}
		ifIDAUX.imprimirIF("IF");
	}

	// Estágio ID 
	// Recebida a instrução do ciclo anterior, ele decodifica e ativa os sinais de controle referente
	// Entrada: Nenhuma
	// Saida: Nenhuma
	public void ID() {
		// Recebe a instrução do ciclo anterior
		idExAUX.instruction = ifID.instruction;
		idExAUX.pc = ifID.pc;
		
		if (idExAUX.instruction != null) {
			idExAUX.readData1 = registradores[idExAUX.instruction.getRs1()];
			idExAUX.readData2 = registradores[idExAUX.instruction.getRs2()];
			
			if (idExAUX.instruction.getOpcode() == 51) { 		//TIPO-R -> add, sub, and e or
				idExAUX.sinaisControle.put("RegWrite", 1);
				idExAUX.sinaisControle.put("ALUOp", 2);  		// Para fazer soma ou subtracao
				idExAUX.sinaisControle.put("ALUsrc", 0);
				idExAUX.sinaisControle.put("MemtoReg", 1); 
			} 
			else if (idExAUX.instruction.getOpcode() == 19) { 	// TIPO-I  -> addi
				idExAUX.sinaisControle.put("RegWrite", 1);
				idExAUX.sinaisControle.put("ALUOp", 1); 
				idExAUX.sinaisControle.put("ALUsrc", 1);
				idExAUX.sinaisControle.put("MemtoReg", 0); 
				
			} else if (idExAUX.instruction.getOpcode() == 35) { // TIPO-S  -> sw
				idExAUX.sinaisControle.put("MemWrite", 1);
				idExAUX.sinaisControle.put("ALUOp", 1);
				idExAUX.sinaisControle.put("ALUsrc", 1);
				idExAUX.sinaisControle.put("MemtoReg", 0);
				
			} else if (idExAUX.instruction.getOpcode() == 99) { // TIPO-B  -> beq
				idExAUX.sinaisControle.put("RegWrite", 0);
				idExAUX.sinaisControle.put("Branch", 1);
				idExAUX.sinaisControle.put("ALUOp", 3);
				idExAUX.sinaisControle.put("ALUsrc", 0);
				idExAUX.sinaisControle.put("MemtoReg", 0);
				
			} else if (idExAUX.instruction.getOpcode() == 3) { // TIPO-I  -> lw
				idExAUX.sinaisControle.put("RegWrite", 1);
				idExAUX.sinaisControle.put("ALUOp", 1);
				idExAUX.sinaisControle.put("ALUsrc", 1);
				idExAUX.sinaisControle.put("MemRead", 1); 
				idExAUX.sinaisControle.put("MemtoReg", 1);
			}
			
		}
		ifID.instruction = null;
		idExAUX.imprimirID("ID");
		
	}

	// Estágio EX
	// Recebida a instrução do ciclo anterior, ele executa os calculos necessarios de acordo com seu tipo
	// Entrada: Nenhuma
	// Saida: Nenhuma
	public void EX() {
		// Recebe a instrução do ciclo anterior
		exMemAUX.instruction = idEx.instruction;
		exMemAUX.pc = idEx.pc;
		exMemAUX.sinaisControle = idEx.sinaisControle;
		exMemAUX.readData1 = idEx.readData1;
	    exMemAUX.readData2 = idEx.readData2;

		if (exMemAUX.instruction != null) {
			if (exMemAUX.sinaisControle.get("ALUOp") == 2) { //TIPO-R
				if(exMemAUX.instruction.getFunc3() == 0 && exMemAUX.instruction.getFunc7() == 0) { //caso de soma
					exMemAUX.aluResult = registradores[exMemAUX.instruction.getRs1()] + registradores[exMemAUX.instruction.getRs2()];					
				}else if(exMemAUX.instruction.getFunc3() == 0 && exMemAUX.instruction.getFunc7() == 32){ // caso de subtracao
					exMemAUX.aluResult = exMemAUX.readData1 - exMemAUX.readData2;		
				}
			} 
			
			else if (exMemAUX.sinaisControle.get("ALUOp") == 1) { // TIPO-I e TIPO-S
				exMemAUX.aluResult = registradores[exMemAUX.instruction.getRs1()]  + exMemAUX.instruction.getImm();
			} 
			
			else if (exMemAUX.sinaisControle.get("ALUOp") == 3) {  // TIPO-B
				exMemAUX.aluResult = (registradores[exMemAUX.instruction.getRs1()] == registradores[exMemAUX.instruction.getRs2()]) ? 1 : 0;
				if (exMemAUX.aluResult == 1) {
					pc = exMemAUX.pc + exMemAUX.instruction.getImm();
					ifID.instruction = null;
					idEx.instruction = null;
					exMem.instruction = null;
				}
			} 	
		}
		idEx.instruction = null;
		exMemAUX.imprimirEX("EX");
	}

	// Estágio MEM
	// Recebida a instrução do ciclo anterior, Le, escreve ou não faz nada dependendo seu tipo
	// Entrada: Nenhuma
	// Saida: Nenhuma
	public void MEM() {
		// Recebe a instrução do ciclo anterior
		memWbAUX.instruction = exMem.instruction;
		memWbAUX.pc = exMem.pc;
		memWbAUX.sinaisControle = exMem.sinaisControle;
		memWbAUX.aluResult = exMem.aluResult;
		
		if (memWbAUX.instruction != null) {
			
			if (memWbAUX.sinaisControle.get("MemWrite") != null && memWbAUX.sinaisControle.get("MemWrite") == 1) {
				int rs2 = memWbAUX.instruction.getRs2();
				System.out.println("Posicao a ser escrita na memoria: " + memWbAUX.aluResult);
				if (memWbAUX.aluResult >= 0 && memWbAUX.aluResult < memoria.length) {
					memoria[memWbAUX.aluResult] = registradores[rs2];
				} else {
					System.out.println("Erro: Índice " + memWbAUX.aluResult + " está fora dos limites do array memória.");
				}
			}
			
			if (memWbAUX.sinaisControle.get("MemRead") != null && memWbAUX.sinaisControle.get("MemRead") == 1) {
				System.out.println("Posicao a ser lida na memoria: " + memWbAUX.aluResult);
				if (memWbAUX.aluResult >= 0 && memWbAUX.aluResult < memoria.length) {
					memWbAUX.aluResult = memoria[memWbAUX.aluResult];
	
				} else {
					System.out.println("Erro: Índice " + memWbAUX.aluResult + " está fora dos limites do array memória.");
				}
			}
		}
		
		imprimirMemoria(memoria);
		System.out.println();
		exMem.instruction = null;
		memWbAUX.imprimirMEM("MEM");
	}
	
	// Estágio WB
	// Recebida a instrução do ciclo anterior, escreve os valores calculos na posicao indicada no registrador
	// Entrada: Nenhuma
	// Saida: Nenhuma
	public void WB() {
		// Recebe as instruções do ciclo anterior
		regAUX.instruction = memWb.instruction;
		regAUX.sinaisControle = memWb.sinaisControle;
		regAUX.aluResult = memWb.aluResult;
		
		if(regAUX.instruction != null) { 
			// Se for do tipo R escreve na memoria o resultado da ULA
			if(regAUX.instruction.getType() == "R" && regAUX.instruction.getOpcode()== 51) {
				int rd = regAUX.instruction.getRd();
				registradores[rd] = regAUX.aluResult;
			}
			// Se for do tipo I (addi) escreve na memoria o resultado da ULA
			else if(regAUX.instruction.getType() == "I" && regAUX.instruction.getOpcode() == 19) { 
				int rd = regAUX.instruction.getRd();
				registradores[rd] = regAUX.aluResult;
			}
			// Se for do tipo R (lw) escreve na memoria o resultado da ULA
			else if(regAUX.instruction.getType() == "I" && regAUX.instruction.getOpcode() == 3) {
				int rd = regAUX.instruction.getRd();
				registradores[rd] = regAUX.aluResult;
			}	
		}
		
		imprimirRegistradores(registradores);
		System.out.println();
		regAUX.imprimirWB("WB");
		memWb.instruction = null;
		regAUX.instruction = null;
	}
	
}
