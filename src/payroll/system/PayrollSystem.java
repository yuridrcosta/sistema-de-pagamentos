
package payroll.system;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Iterator;

public class PayrollSystem {
    
    public static int[] date = new int[3];
    static ArrayList<Employee> employees = new ArrayList<Employee>();
    static Scanner read = new Scanner(System.in);
    static Buffer buffer = new Buffer();
    
    public static void main(String[] args) {
        
        int iaux;
        float faux=0;
        int[] pay = new int[3];
        int menuOpt;
        String nm;
        String ad;
        int tp;
        int pm;
        boolean ss;
        int is;
        float tx;
        int nEmp = 0;
        
        System.out.println("Bem vindo ao sistema de folha de pagamentos!");
        System.out.println("Digite a data de hoje: (Formato: dd mm aaaa)");
        iaux = read.nextInt();
        date[0] = iaux;
        iaux = read.nextInt();
        date[1] = iaux;
        iaux = read.nextInt();
        date[2] = iaux;
        System.out.println("A data de hoje é: "+date[0]+"/"+date[1]+"/"+date[2]);
        showMainMenu();
        menuOpt = read.nextInt();
        
        while(menuOpt != 0)
        {
            switch (menuOpt) {
                case 1:
                    
                    nm = read.nextLine();
                    System.out.println("Cadastrando novo empregado");
                    System.out.println("Digite o nome:");
                    nm = read.nextLine();
                    System.out.println("Digite o endereço:");
                    ad = read.nextLine();
                    System.out.println("Digite o tipo de empregado (inteiro): (1 - Horista, 2 - Assalariado e 3 - Comissionado)");
                    tp = read.nextInt();
                    if(tp == 3){
                        System.out.println("Digite a porcentagem da comissão sobre as vendas: (real) (FORMATO:\"x,x\")");
                        faux = read.nextFloat();
                    }
                    System.out.println("Digite a data do próximo pagamento deste funcionário: (Formato: dd mm aaaa)");
                    iaux = read.nextInt();
                    pay[0] = iaux;
                    iaux = read.nextInt();
                    pay[1] = iaux;
                    iaux = read.nextInt();
                    pay[2] = iaux;
                    System.out.println("Digite o método de pagamento (inteiro): (1-Cheque pelos Correios, 2-Cheque em mãos, 3-Depósito)");
                    pm = read.nextInt();
                    System.out.println("O empregado é sindicalizado? (Digite \"true\" para SIM e digite \"false\" para NÃO)");
                    ss = read.nextBoolean();
                    if(ss == true){
                        System.out.println("Qual a identificação do empregado no sindicato?");
                        is = read.nextInt();
                        System.out.println("Qual a taxa que o sindicato cobra do empregado? (real)(FORMATO:\"x,x\")");
                        tx = read.nextFloat();
                    }
                    else
                    {
                        is = -1;
                        tx = 0;
                    }
                    
                    Employee emp = new Employee(nm,ad,tp,pm,ss,is,tx);
                    emp.setPayDate(pay);
                    emp.setComission(faux);
                    emp.setSales(0);
                    emp.ident = 1000 + nEmp;
                    nEmp++;
                    Operation op = new Operation(1,emp.ident,nm,ad,tp,pm,ss,is,tx);
                    
                    buffer.addOperUndo(op);
                    addNewEmployee(emp);
                    
                    System.out.println("Empregado cadastrado com sucesso!");      
                    
                    break;
                case 2:
                    //TODO Verificar o problema do último colocado a ser removido
                    nm = read.nextLine();
                    System.out.println("Digite o nome do empregado a ser removido:");
                    nm = read.nextLine();
                    removeEmployee(nm);
                    break;
                case 3:
                    listEmployees();
                    break;
                case 4:
                    //TODO Colocar operações no buffer
                    modifyEmployeeData();
                    break;
                case 5:
                    makeUndo();
                    break;
                case 6:
                    makeRedo();
                    break;
                case 7:
                    break;
                default:
                    break;
            }
            
            showMainMenu();
            menuOpt = read.nextInt();
        }
        
        
    }
    
    public static void showMainMenu(){
        System.out.println("Digite o número referente a operação a ser feita:");
        System.out.println("        1- Cadastrar novo empregado");
        System.out.println("        2- Remover um empregado");
        System.out.println("        3- Listar empregados");
        System.out.println("        4- Alterar dados de um empregado");
        System.out.println("        5- Desfazer operação");
        System.out.println("        6- Refazer operação");
        System.out.println("        7- Realizar pagamentos do dia");
        System.out.println("        0- Sair");
    }
    //makeUndo START
    //POSSIBILIDADE DE REFAZER TUDO PARA IMPLEMENTAR DATAS DE PAGAMENTO
    public static void makeUndo(){
        if(buffer.undo.size()>0){
            Operation last = buffer.peekOperUndo();
            Employee rem;
            String saux;
            int iaux;
            Operation newOp;

            System.out.println("A última operação realizada está sendo desfeita!");
            switch(last.oper){
                case 1:
                    rem = findIdent(last.ident);
                    if(rem != null){
                        newOp = new Operation(2,last.ident,last.name,last.address,last.type,last.payMet,last.sindStatus,last.identSind,last.tax);
                        buffer.addOperRedo(newOp);
                        buffer.removeOperUndo();
                        employees.remove(rem);
                    }else{
                        System.out.println("ERRO: Empregado não encontrado!");
                    }
                    break;
                case 2:
                    newOp = new Operation(1,last.ident,last.name,last.address,last.type,last.payMet,last.sindStatus,last.identSind,last.tax);
                    buffer.addOperRedo(newOp);
                    rem = new Employee(last.name,last.address,last.type,last.payMet,last.sindStatus,last.identSind,last.tax);
                    buffer.removeOperUndo();
                    employees.add(rem);
                    break;
                case 3:
                    //nesse caso para alterar o nome, o objeto operation deve salvar o nome antigo
                    rem = findIdent(last.ident);

                    saux = rem.name;
                    rem.name = last.name;

                    newOp = new Operation(3,last.ident,saux,last.address,last.type,last.payMet, last.sindStatus, last.identSind,last.tax);

                    buffer.removeOperUndo();
                    buffer.addOperRedo(newOp);

                    break;
                case 4:

                    rem = findIdent(last.ident);

                    saux = rem.getAddress();
                    rem.setAddress(last.address);

                    newOp = new Operation(4,last.ident,last.name,saux,last.type,last.payMet,last.sindStatus,last.identSind,last.tax);

                    buffer.removeOperUndo();
                    buffer.addOperRedo(newOp);
                    break;
                case 5:
                    //TODO Definir data de pagamentos
                    rem = findIdent(last.ident);

                    iaux = rem.getType();
                    rem.setType(last.type);
                    newOp = new Operation(5,last.ident,last.name,last.address,iaux,last.payMet,last.sindStatus,last.identSind,last.tax);

                    buffer.removeOperUndo();
                    buffer.addOperRedo(newOp);
                    break;
                case 6:

                    rem = findIdent(last.ident);

                    iaux = rem.getPayMet();
                    rem.setType(last.payMet);
                    newOp = new Operation(5,last.ident,last.name,last.address,last.type,iaux,last.sindStatus,last.identSind,last.tax);

                    buffer.removeOperUndo();
                    buffer.addOperRedo(newOp);
                    break;
                case 7:
                    rem = findIdent(last.ident);
                    int iaux1;
                    float faux;
                    boolean stat = last.sindStatus;

                    if(stat == true){
                        rem.setSindStatus(true);
                        rem.setIdentSind(last.identSind);
                        rem.setTax(last.tax);
                        newOp = new Operation(6,last.ident,last.name,last.address,last.type,last.payMet,false,-1,0);

                    }else{
                        iaux1 = rem.getIdentSind();
                        faux = rem.getTax();
                        rem.setSindStatus(false);
                        rem.setIdentSind(-1);
                        rem.setTax(0);
                        newOp = new Operation(6,last.ident,last.name,last.address,last.type,last.payMet,true,iaux1,faux);
                    }


                    buffer.removeOperUndo();
                    buffer.addOperRedo(newOp);
                    break;
                case 8:
                    break;
            }
        }else{
            System.out.println("ERRO: Não há operação a ser desfeita!");
        }
    }
    //makeUndo FINISH
    //makeRedo START
    public static void makeRedo(){
        if(buffer.undo.size()>0){
            Operation last = buffer.peekOperRedo();
            Operation newOp;
            Employee emp;
            String saux;
            int iaux;

            switch(last.oper){
                case 1:
                    //retirar
                    emp = findIdent(last.ident);

                    newOp = new Operation(2,last.ident,last.name,last.address,last.type,last.payMet,last.sindStatus,last.identSind,last.tax);
                    buffer.addOperUndo(newOp);
                    buffer.removeOperRedo();

                    employees.remove(emp);

                    break;
                case 2:
                    //tenho que adicionar de volta
                    newOp = new Operation(1,last.ident,last.name,last.address,last.type,last.payMet,last.sindStatus,last.identSind,last.tax);
                    buffer.addOperUndo(newOp);
                    emp = new Employee(last.name,last.address,last.type,last.payMet,last.sindStatus,last.identSind,last.tax);
                    buffer.removeOperRedo();
                    employees.add(emp);
                    break;
                case 3:

                    emp = findIdent(last.ident);

                    saux = emp.name;
                    emp.name = last.name;

                    newOp = new Operation(3,last.ident,saux,last.address,last.type,last.payMet, last.sindStatus, last.identSind,last.tax);

                    buffer.removeOperRedo();
                    buffer.addOperUndo(newOp);

                    break;
                case 4:
                    emp = findIdent(last.ident);

                    saux = emp.getAddress();
                    emp.setAddress(last.address);

                    newOp = new Operation(4,last.ident,last.name,saux,last.type,last.payMet,last.sindStatus,last.identSind,last.tax);

                    buffer.removeOperRedo();
                    buffer.addOperUndo(newOp);
                    break;
                case 5:
                    //TODO Definir data de pagamento
                    emp = findIdent(last.ident);

                    iaux = emp.getType();
                    emp.setType(last.type);
                    newOp = new Operation(5,last.ident,last.name,last.address,iaux,last.payMet,last.sindStatus,last.identSind,last.tax);

                    buffer.removeOperRedo();
                    buffer.addOperUndo(newOp);
                    break;
                case 6:
                    emp = findIdent(last.ident);

                    iaux = emp.getPayMet();
                    emp.setType(last.payMet);
                    newOp = new Operation(5,last.ident,last.name,last.address,last.type,iaux,last.sindStatus,last.identSind,last.tax);

                    buffer.removeOperRedo();
                    buffer.addOperUndo(newOp);
                    break;
                case 7:
                    emp = findIdent(last.ident);
                    int iaux1;
                    float faux;
                    boolean stat = last.sindStatus;

                    if(stat == true){
                        emp.setSindStatus(true);
                        emp.setIdentSind(last.identSind);
                        emp.setTax(last.tax);
                        newOp = new Operation(6,last.ident,last.name,last.address,last.type,last.payMet,false,-1,0);

                    }else{
                        iaux1 = emp.getIdentSind();
                        faux = emp.getTax();
                        emp.setSindStatus(false);
                        emp.setIdentSind(-1);
                        emp.setTax(0);
                        newOp = new Operation(6,last.ident,last.name,last.address,last.type,last.payMet,true,iaux1,faux);
                    }


                    buffer.removeOperRedo();
                    buffer.addOperUndo(newOp);
                    break;
                case 8:
                    break;
            }
        }else{
            System.out.println("ERRO: Não há operação a ser refeita!");
        }
    }
    //makeRedo FINISH
    public static void addNewEmployee(Employee p){
        employees.add(p);
    }
    
    public static void removeEmployee(String name){
        Employee st;
        st = findEmployee(name);
        Operation op = new Operation(2,st.ident,st.name,st.address,st.type,st.payMet,st.sindStatus,st.identSind,st.tax);
        buffer.addOperUndo(op);
        if(st != null){
            employees.remove(st);
        }
        else
        {
            System.out.println("O empregado não está cadastrado");
        }
    }
    
    public static void listEmployees()
    {
        String type;
        String payMet;
        String nm;
        for (Iterator<Employee> it = employees.iterator(); it.hasNext();) {
            Employee st = it.next();
            //nm = st.getName();
            if(st!=null){
                if(st.getType() == 1){
                    type = "Horista";
                }else if(st.getType() == 2){
                    type = "Assalariado";
                }else{
                    type = "Comissionado";
                }

                if(st.getPayMet() == 1){
                    payMet = "Cheque pelos Correios";
                }else if(st.getPayMet() == 2){
                    payMet = "Cheque em mãos";
                }else{
                    payMet = "Depósito em conta";
                }
               //System.out.println("- " + nm);
               if(st.getSindStatus() == true){
                   System.out.printf("Nome: %s - Endereço: %s - Tipo: %s - Método de Pagamento: %s - Sindicalizado: Sim\n", 
                        st.getName(), st.getAddress(), type, payMet);
               }
               else{
                   System.out.printf("Nome: %s - Endereço: %s - Tipo: %s - Método de Pagamento: %s - Sindicalizado: Não\n", 
                        st.getName(), st.getAddress(), type, payMet);
               }
           }
        }
    }
    
    public static Employee findEmployee(String name){
        String nm;
        Employee st = null;
        int exit = 0;
        for (Iterator<Employee> it = employees.iterator(); it.hasNext();) {
            st = it.next();
            nm = st.getName();
            if(nm.equals(name)){
                exit = 1;
                break;
            }
        }
        
        if(exit == 1){
            return st;
        }
        else{
            return null;
        }
    }
    
    public static Employee findIdent(int searchedId){
        int aux;
        Employee st = null;
        int exit = 0;
        for(Iterator<Employee> it = employees.iterator(); it.hasNext();){
            st = it.next();
            aux = st.getIdent();
            if(aux == searchedId){
                exit = 1;
                break;
            }
        }
        if(exit == 1){
            return st;
        }
        else{
            return null;
        }
    }
    
    public static void modifyEmployeeData(){
        
        int menu;
        int iaux;//Usado para alterar dados do tipo inteiro (geral)
        float faux;
        String saux;
        String nm;
        String type;
        String payMet;
        Employee emp;
        Operation newOp;
        
        nm = read.nextLine();
        System.out.println("Digite o nome do empregado:");
        nm = read.nextLine();
        
        emp = findEmployee(nm);
        if(emp != null){

            if(emp.getType() == 1){
                type = "Horista";
            }else if(emp.getType() == 2){
                type = "Assalariado";
            }else{
                type = "Comissionado";
            }

            if(emp.getPayMet() == 1){
                payMet = "Cheque pelos Correios";
            }else if(emp.getPayMet() == 2){
                payMet = "Cheque em mãos";
            }else{
                payMet = "Depósito em conta";
            }

            if(emp.getSindStatus() == true){       
                System.out.printf("[1] Nome: %s - [2] Endereço: %s - [3] Tipo: %s - [4] Método de Pagamento: %s - [5] Sindicalizado: Sim - [6] Identificação no Sindicato: %d - [7] Taxa do Sindicato: %.2f%\n", 
                    emp.getName(), emp.getAddress(), type, payMet, emp.getIdentSind(), emp.getTax());

                System.out.println("Digite o número referente a operação a ser feita:");
                System.out.println("        1- Alterar nome");
                System.out.println("        2- Alterar endereço");
                System.out.println("        3- Alterar tipo de empregado");
                System.out.println("        4- Alterar método de pagamento");
                System.out.println("        5- Alterar status de sindicalização");
                System.out.println("        6- Alterar identificação no sindicato");
                System.out.println("        7- Alterar taxa do sindicato");
                System.out.println("        0- Sair");

                menu = read.nextInt();
                switch (menu){
                    case 5:
                        
                        newOp = new Operation(7,emp.ident,emp.name,emp.address,emp.type,emp.payMet,emp.sindStatus,emp.identSind,emp.tax);
                        buffer.addOperUndo(newOp);
                        emp.setSindStatus(false);
                        emp.setIdentSind(-1);
                        emp.setTax(0);

                        break;
                    case 6:
                        System.out.println("Digite o novo número de identificação no sindicato(inteiro):");
                        iaux = read.nextInt();
                        emp.setIdentSind(iaux);

                        break;
                    case 7:
                        System.out.println("Digite a nova taxa sindical(real)(FORMATO:x,x): ");
                        faux = read.nextFloat();
                        emp.setTax(faux);

                        break;
                }

            } else {

                System.out.printf("Nome: %s - Endereço: %s - Tipo: %s - Método de Pagamento: %s - Sindicalizado: Não\n", 
                    emp.getName(), emp.getAddress(), type, payMet);

                System.out.println("Digite o número referente a operação a ser feita:");
                System.out.println("        1- Alterar nome");
                System.out.println("        2- Alterar endereço");
                System.out.println("        3- Alterar tipo de empregado");
                System.out.println("        4- Alterar método de pagamento");
                System.out.println("        5- Alterar status de sindicalização");
                System.out.println("        0- Sair");

                menu = read.nextInt();

                if(menu == 5){
                    newOp = new Operation(7,emp.ident,emp.name,emp.address,emp.type,emp.payMet,false,-1,0);
                    buffer.addOperUndo(newOp);
                    System.out.println("Digite o número de identificação sindical:");
                    iaux = read.nextInt();
                    emp.setIdentSind(iaux);
                    System.out.println("Digite o valor da taxa sindical: (FORMATO: x,x)");
                    faux = read.nextFloat();
                    emp.setTax(faux);
                    emp.setSindStatus(true);

                }

            }

            switch(menu){
                case 1:
                    newOp = new Operation(3,emp.ident,emp.name,emp.address,emp.type,emp.payMet,emp.sindStatus,emp.identSind,emp.tax);
                    buffer.addOperUndo(newOp);
                    saux = read.nextLine(); 
                    System.out.println("Digite o novo nome:");
                    saux = read.nextLine(); 
                    emp.setName(saux);

                    break;
                case 2:
                    newOp = new Operation(4,emp.ident,emp.name,emp.address,emp.type,emp.payMet,emp.sindStatus,emp.identSind,emp.tax);
                    buffer.addOperUndo(newOp);
                    saux = read.nextLine();
                    System.out.println("Digite o novo endereço:");
                    saux = read.nextLine();
                    emp.setAddress(saux);

                    break;
                case 3:
                    //TODO Horas trabalhadas e salário
                    newOp = new Operation(5,emp.ident,emp.name,emp.address,emp.type,emp.payMet,emp.sindStatus,emp.identSind,emp.tax);
                    buffer.addOperUndo(newOp);
                    System.out.printf("Digite:\n 1 para alterar para HORISTA\n 2 para alterar para ASSALARIADO\n 3 para alterar para COMISSIONADO\n");
                    iaux = read.nextInt();

                    if(iaux == 1){

                        emp.setType(1);

                    }else if(iaux == 2){

                        emp.setType(2);

                    }else{

                        emp.setType(3);

                    }

                    break;
                case 4:
                    newOp = new Operation(6,emp.ident,emp.name,emp.address,emp.type,emp.payMet,emp.sindStatus,emp.identSind,emp.tax);
                    buffer.addOperUndo(newOp);
                    System.out.printf("Digite:\n 1 para alterar para CHEQUE PELOS CORREIOS\n 2 para alterar para CHEQUE EM MÃOS\n 3 para alterar para DEPÓSITO EM CONTA\n");
                    iaux = read.nextInt();

                    if(iaux == 1){

                        emp.setPayMet(1);

                    }else if(iaux == 2){

                        emp.setPayMet(2);

                    }else{

                        emp.setPayMet(3);

                    }

                    break;

            }

            if(menu!=0)System.out.println("Alterado com sucesso!");
        }else{
            System.out.println("Empregado não encontrado!");
        }
        
    }
}

