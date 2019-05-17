import java.util.Date;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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

    //Retorna um array de bytes com os bytes para escrever no arquivo
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);

        saida.writeInt(this.idCliente);
        saida.writeInt(this.idCompra);
        saida.writeDate(this.dataCompra);
        saida.writeFloat(this.valorCompra);
        return dados.toByteArray();
    }

    //Coloca nos atributos os bytes lidos do arquivo
    public void fromByteArray(byte[] b) throws Exception {
        ByteArrayInputStream dados = new ByteArrayInputStream(b);
        DataInputStream entrada = new DataInputStream(dados);

        this.idCliente = entrada.readInt();
        this.idCompra = entrada.readInt();
        this.dataCompra = entrada.readDate();
        this.valorCompra = entrada.readFloat();
        entrada.close();
    }

    public String toString(){
        return "Id Compra: " + this.idCompra + 
                "\nId Cliente: " + this.idCliente + 
                "\nData da compra: " + this.dataCompra.getTime() + 
                "\nValor da compra: " + this.valorCompra;
       }
}