package pipeline;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.Instruction;

public class Pipeline {
	static int[] registradores = new int [32];
	static Map <Integer, Integer> memoria = new HashMap<>();
	static int pc = 0;
	
	static RegistradorPipeline ifID= new RegistradorPipeline();
	static RegistradorPipeline idEx= new RegistradorPipeline();
	static RegistradorPipeline exMem= new RegistradorPipeline();
	static RegistradorPipeline memWb= new RegistradorPipeline();
	
	public static void main(String [] args) {
		// Onde definir as intrucoes que serao processadas no pipeline
		// (tipo, opcode, rs1, rs2, rd, func3, func7, imm)
		List<Instruction> lista_instrucoes = Arrays.asList(
				new Instruction("R", 51, 1, 2, 3, 0, 32, 0), 	// add  x3, x1, x2
				new Instruction("I", 19, 1, 0, 4, 0, 0, 100),	// addi x4, x1, 100
				new Instruction("S", 35, 4, 5, 0, 0, 0, 16),	// sw 	x5, 16(x4)
				new Instruction("B", 99, 1, 2, 0, 0, 0, 4),		// beq	x1, x2, 4
				new Instruction("I", 3, 6, 0, 7, 0, 0, 20));	// lw 	x7, 20(x6)
		
		int ciclo = 0;
		// Loop que vai processar todas as instruções ate que todas ja tenham acabado o seu processamento
		while (pc / 4 < lista_instrucoes.size() || estaExecutando()) {
			System.out.println("CICLO: " + ciclo + "\n");
			WB();
			MEM();
			EX();
			ID();
			IF(lista_instrucoes);
			ciclo ++;
			System.out.println("********************");
		}
	}
	
	
	static boolean estaExecutando() { // funcao pra verificar se em algum dos estados esta com alguma instrucao sendo executada 
		return ifID.instruction != null || idEx.instruction != null ||
				exMem.instruction != null || memWb.instruction != null;
	}
	
	
	// Estagio IF
	static void IF (List<Instruction> lista_instrucoes) {
		if(pc / 4 < lista_instrucoes.size()) {
			ifID.instruction = lista_instrucoes.get(pc/4);
			ifID.pc = pc;
			pc += 4;
		}
		ifID.imprimirConteudo("IF");
	}
	
	
	// Estagio ID
	static void ID () {
		idEx.instruction = ifID.instruction;
		idEx.pc = ifID.pc;
		
		if(idEx.instruction != null) {
			
			if(idEx.instruction.getType().equals("R")) {
				idEx.sinaisControle.put("RegWrite", 1);
				idEx.sinaisControle.put("ALUOp", 2);
			}
			
			else if(idEx.instruction.getType().equals("I")) {
				idEx.sinaisControle.put("RegWrite", 1);
				idEx.sinaisControle.put("ALUOp", 1);
			}
			else if(idEx.instruction.getType().equals("S")) {
				idEx.sinaisControle.put("MemWrite", 1);
				idEx.sinaisControle.put("ALUOp", 0);
			}
			else if(idEx.instruction.getType().equals("B")) {
				idEx.sinaisControle.put("Branch", 1);
				idEx.sinaisControle.put("ALUOp", 3);
			}
		}
		ifID.instruction = null;
		idEx.imprimirConteudo("ID");
	}
	
	// Estagio EX
	static void EX () {
		exMem.instruction = idEx.instruction;
		exMem.pc = idEx.pc;
		exMem.sinaisControle = idEx.sinaisControle;
		
		if (exMem.instruction != null) {
            if (exMem.sinaisControle.get("ALUOp") == 2) {
                exMem.aluResult = registradores[exMem.instruction.getRs1()] + registradores[exMem.instruction.getRs2()];
            } 
            else if (exMem.sinaisControle.get("ALUOp") == 1) {
                exMem.aluResult = registradores[exMem.instruction.getRs1()] + exMem.instruction.getImm();
            }             
            else if (exMem.sinaisControle.get("ALUOp") == 3) {
                exMem.aluResult = (registradores[exMem.instruction.getRs1()] == registradores[exMem.instruction.getRs2()]) ? 1 : 0;                
                if (exMem.aluResult == 1) {
                    pc = exMem.pc + exMem.instruction.getImm();
                    // Clear pipeline registers to simulate pipeline flush
                    ifID.instruction = null;
                    idEx.instruction = null;
                    exMem.instruction = null;
                }
            }
        }
		idEx.instruction = null;
		exMem.imprimirConteudo("EX");
	}
	
	// Estagio MEM
	static void MEM() {
		memWb.instruction = exMem.instruction;
		memWb.pc = exMem.pc;
		memWb.sinaisControle = exMem.sinaisControle;
		memWb.aluResult = exMem.aluResult;
		
		if (memWb.instruction != null) {
			if(memWb.sinaisControle.get("MemWrite") != null && memWb.sinaisControle.get("MemWrite") == 1) {
				memoria.put(memWb.aluResult, registradores[memWb.instruction.getRs2()]);
			}
			
			if(memWb.sinaisControle.get("MemRead") != null && memWb.sinaisControle.get("MemRead") == 1) {
				memWb.memoryData = memoria.get(memWb.aluResult);
			}
		}
		exMem.instruction = null;
		memWb.imprimirConteudo("MEM");	
	}
	
	// Estagio WB
	static void WB() {
		if(memWb.instruction != null && memWb.sinaisControle.get("RegWrite") != null) {
			registradores[memWb.instruction.getRd()] = memWb.aluResult;
		}
		memWb.instruction = null;
		memWb.imprimirConteudo("WB");
	}
}


