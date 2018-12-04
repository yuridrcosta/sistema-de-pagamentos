
package payroll.system;

public class Operation {
    
    int oper;//OPERAÇÃO QUE FOI FEITA!//1 para cadastrar empregado, 2 para remover empregado, 3 para alterar nome, 4 para alterar endereço, 
             //5 para alterar tipo de empregado, 6 para alterar método de pagamento, 7 alterar status de sindicalização
    int ident;
    public String name;//Se a operação for algo relacionado a trocar, salva o estado anterior antes de trocar
    public String address;
    public int type;
    public int payMet;
    public boolean sindStatus;
    public int identSind;
    public float tax;

    public Operation(int oper, int ident, String name, String address, int type, int payMet, boolean sindStatus, int identSind, float tax) {
        this.oper = oper;
        this.ident = ident;
        this.name = name;
        this.address = address;
        this.type = type;
        this.payMet = payMet;
        this.sindStatus = sindStatus;
        this.identSind = identSind;
        this.tax = tax;
    }
              
}


//obs.: nesse caso para alterar o nome, o objeto operation deve salvar o nome antigo