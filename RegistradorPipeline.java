package pipeline;

import java.util.HashMap;
import java.util.Map;

import modelo.Instruction;

public class RegistradorPipeline {
	Instruction instruction;
	Map<String, Integer> sinaisControle;
	int aluResult;
	int memoryData;
	int pc;
	
	RegistradorPipeline(){
		this.sinaisControle = new HashMap<>();
	}
	
	void imprimirConteudo(String nomeEstagio) {
		System.out.println("Estagio: " + nomeEstagio);
		
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
		System.out.println("Resultado da ALU: " + aluResult);
		System.out.println("Memory Data: " + memoryData);
		System.out.println("PC: " + pc);
		System.out.println();
	}

}
