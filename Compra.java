import java.util.Date;
import java.text.*;

class Compra{
    public int idCliente;
    public int idCompra;
    public Date dataCompra;
    public float valorCompra;

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

    public int getId(){
        return this.idCompra;
    }

    public void setId(int id){
        this.idCompra = id;
    }

    

    public String toString(){
        return "Id Compra: " + this.idCompra + 
                "Id Cliente: " + this.idCliente + 
                "Data da compra: " + this.dataCompra.getTime() + 
                "Valor da compra: " + this.valorCompra;
       }
}