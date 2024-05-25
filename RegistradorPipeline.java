package pipeline;

import java.util.HashMap;
import java.util.Map;

import base.Instruction;

public class RegistradorPipeline {
	Instruction instruction;
	Map<String, Integer> sinaisControle;
	
	int aluResult;
	int memoryData;
	int pc;
	int readData1;
	int readData2;
	
	RegistradorPipeline(){
		this.sinaisControle = new HashMap<>();
	}
	
	// Estágio ID 
	// Função que vai imprimir os dados que estão no Estagio ID
	// Entrada: Nenhuma
	// Saida: 	Nenhuma
	void imprimirIF(String nomeEstagio) {
		System.out.println("ESTAGIO -> " + nomeEstagio);
		
		if(instruction != null) {
			if(instruction.getType() == "R") 
				System.out.println("Instrucao: " + instruction.getType());
			else if (instruction.getType() == "I")
				System.out.println("Instrucao: " + instruction.getType());
			else if (instruction.getType() == "S")
				System.out.println("Instrucao: " + instruction.getType());
			else if(instruction.getType() == "B")
				System.out.println("Instrucao: " + instruction.getType());
			
			System.out.println("PC: " + pc);
			System.out.println();
		}
		else {
			System.out.println("Sem instrucao");
			System.out.println("PC: " + pc);
			System.out.println();
		}
	}
	
	// Estágio IF 
	// Função que vai imprimir os dados que estão no Estagio IF
	// Entrada: Nenhuma
	// Saida: 	Nenhuma
	void imprimirID(String nomeEstagio) {
		System.out.println("ESTAGIO -> " + nomeEstagio);
		
		if(instruction != null) {
			System.out.println("Instrucao: " + instruction.getType());
			System.out.println("Opcode: " + instruction.getOpcode());
			System.out.println("Rs1: " + instruction.getRs1());
			System.out.println("Rs2: " + instruction.getRs2());
			System.out.println("Rd: " + instruction.getRd());
			System.out.println("Func3: " + instruction.getFunc3());
			System.out.println("Func7: " + instruction.getFunc7());
			System.out.println("Imm: " + instruction.getImm());
			
		}
		else {
			System.out.println("Sem instrucao");
		}
		
		System.out.println("Sinais de Controle: " + sinaisControle);
		System.out.println("Read Data 1: " + readData1);
        System.out.println("Read Data 2: " + readData2);
		System.out.println("PC: " + pc);
		System.out.println();
	}
	
	// Estágio EX
	// Função que vai imprimir os dados que estão no Estagio ID
	// Entrada: Nenhuma
	// Saida: 	Nenhuma
	void imprimirEX(String estagio) {
		System.out.println("ESTAGIO -> " + estagio);

		if (instruction != null) {
			System.out.println("Instrucao: " + instruction.getType());
			System.out.println("Opcode: " + instruction.getOpcode());
			System.out.println("Rs1: " + instruction.getRs1());
			System.out.println("Rs2: " + instruction.getRs2());
			System.out.println("Rd: " + instruction.getRd());
			System.out.println("Func3: " + instruction.getFunc3());
			System.out.println("Func7: " + instruction.getFunc7());
			System.out.println("Imm: " + instruction.getImm());

		} else {
			System.out.println("Sem instrucao");
		}

		System.out.println("Sinais de Controle: " + sinaisControle);
		System.out.println("Resultado da ALU: " + aluResult);
		System.out.println("Read Data 1: " + readData1);
        System.out.println("Read Data 2: " + readData2);
		System.out.println("PC: " + pc);
		System.out.println();
	}
	
	// Estágio MEM
	// Função que vai imprimir os dados que estão no Estagio MEM
	// Entrada: Nenhuma
	// Saida: 	Nenhuma
	void imprimirMEM (String estagio) {
		System.out.println("ESTAGIO -> " + estagio);

		if (instruction != null) {
			System.out.println("Instrucao: " + instruction.getType());
			System.out.println("Opcode: " + instruction.getOpcode());
			System.out.println("Rs1: " + instruction.getRs1());
			System.out.println("Rs2: " + instruction.getRs2());
			System.out.println("Rd: " + instruction.getRd());

		} else {
			System.out.println("Sem instrucao");
		}

		System.out.println("Sinais de Controle: " + sinaisControle);
		System.out.println("Resultado da ALU: " + aluResult);
		System.out.println("PC: " + pc);
		System.out.println();
	}
	
	// Estágio WB
	// Função que vai imprimir os dados que estão no Estagio WB
	// Entrada: Nenhuma
	// Saida: 	Nenhuma
	void imprimirWB (String estagio) {
		System.out.println("ESTAGIO -> " + estagio);

		if (instruction != null) {
			System.out.println("Instrucao: " + instruction.getType());
			System.out.println("Opcode: " + instruction.getOpcode());
			System.out.println("Rs1: " + instruction.getRs1());
			System.out.println("Rs2: " + instruction.getRs2());
			System.out.println("Rd: " + instruction.getRd());

		} else {
			System.out.println("Sem instrucao");
		}

		System.out.println("Sinais de Controle: " + sinaisControle);
		//System.out.println("Resultado da ALU: " + aluResult);
		System.out.println("PC: " + pc);
		System.out.println();
	}

}
















