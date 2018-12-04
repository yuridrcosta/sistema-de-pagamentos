
package payroll.system;
//TODO Duas pilhas uma sendo UNDO e outra REDO com um struct com informações necessárias para um projeto ser feito/desfeito

import java.util.ArrayList;

public class Buffer {
    static ArrayList<Operation> undo = new ArrayList<Operation>();
    static ArrayList<Operation> redo = new ArrayList<Operation>();

    public Buffer() {
    }

    public static void addOperUndo(Operation op){
        undo.add(op);
    }
    
    public static void addOperRedo(Operation op){
        redo.add(op);
    }
    //OBS.: Essa aqui não retorna nada
    public static void removeOperUndo(){
        if(undo.size() > 0){
            Operation removed = undo.remove(undo.size() - 1);
        }else{
            System.out.println("ERRO: Não há operação a ser desfeita!");
        }
    }
    
    public static void removeOperRedo(){
        if(redo.size() > 0){
            Operation removed = redo.remove(undo.size() - 1);
        }else{
            System.out.println("ERRO: Não há operação a ser refeita!");
        }
    }
    
    public static Operation peekOperUndo(){
        return Buffer.undo.get(undo.size() - 1);
    }
    
    public static Operation peekOperRedo(){
        return Buffer.redo.get(redo.size() - 1);
    }
}
