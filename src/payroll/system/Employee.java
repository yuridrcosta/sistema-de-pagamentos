package payroll.system;

import java.util.Random;

public class Employee {
   
    int ident;//Gerará um número aleatório no intervalo [0,100]
    public String name;
    public String address;//Endereço
    public int type; //1==Horistas, 2==Assalariado e 3==Comissionado
    public int payMet;//1==Cheque pelos correios, 2==Cheque em mãos e 3==Depósito
    public int[] payDate = new int[3];
    public boolean sindStatus;
    public int identSind;//Número para identificação no sindicato
    public float tax;//Porcentagem do salário
    
    public Employee(){
        
    }

    public Employee(String name, String address, int type, int payMet, boolean sindStatus, int identSind, float tax) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.payMet = payMet;
        this.sindStatus = sindStatus;
        this.identSind = identSind;
        this.tax = tax;
    }
    
    public void setName(/*Employee empr,*/ String newName){
        this.name = newName;
    }

    public void setAddress(/*Employee empr,*/ String newAddress) {
        this.address = newAddress;
    }

    public void setType(/*Employee empr,*/ int newType){
        this.type = newType;
    }

    public void setSindStatus(/*Employee empr,*/ boolean newSindStatus){
        this.sindStatus = newSindStatus;
    }

    public void setIdentSind(/*Employee empr,*/ int newIdentSind){
        this.identSind = newIdentSind;
    }

    public void setTax(/*Employee empr,*/ float newTax){
        this.tax = newTax;
    }

    public void setPayMet(/*Employee empr,*/ int newPayMet){
        this.payMet = newPayMet;
    }

    public String getName(){
        return this.name;
    }
    
    public String getAddress(){
        return this.address;
    }
    
    public int getType(){
        return this.type;
    }
    
    public int getPayMet(){
        return this.payMet;
    }
    
    public boolean getSindStatus(){
        return this.sindStatus;
    }
    
    public int getIdentSind(){
        return this.identSind;
    }
    
    public float getTax(){
        return this.tax;
    }

    public int getIdent() {
        return ident;
    }
    
}
