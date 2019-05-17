import java.util.Date;
import java.text.*;

class Compra implements Entidade{
    protected int idCliente;
    protected int idCompra;
    protected Date dataCompra;
    protected float valorCompra;

    public Compra(){
        this.idCliente   = -1;
        this.idCompra    = -1;
        this.dataCompra  = null;
        this.valorCompra = 0.0;
    }

    public Compra(int idCompra, float valorCompra){
        this.idCliente   = -1;
        this.idCompra    = idCompra;
        this.dataCompra  = null;
        this.valorCompra = valorCompra;
    }

    public int getID(){
        return this.idCompra;
    }

    public void setID(int id){
        this.idCompra = id;
    }

    

    public String toString(){
        return "Id Compra: " + this.idCompra + 
                "Id Cliente: " + this.idCliente + 
                "Data da compra: " + this.dataCompra.getTime() + 
                "Valor da compra: " + this.valorCompra;
       }
}
