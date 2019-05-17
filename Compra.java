import java.util.Date;
import java.io.*;

class Compra implements Entidade{
    protected int idCliente;
    protected int idCompra;
    protected Date dataCompra;
    protected double valorCompra;

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

    //Retorna um array de bytes com os bytes para escrever no arquivo
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);

        saida.writeInt(this.idCliente);
        saida.writeInt(this.idCompra);
        saida.writeLong(this.dataCompra.getTime());
        saida.writeDouble(this.valorCompra);
        return dados.toByteArray();
    }

    //Coloca nos atributos os bytes lidos do arquivo
    public void fromByteArray(byte[] b) throws Exception {
        ByteArrayInputStream dados = new ByteArrayInputStream(b);
        DataInputStream entrada = new DataInputStream(dados);

        this.idCliente = entrada.readInt();
        this.idCompra = entrada.readInt();
        this.dataCompra.setTime(entrada.readLong());
        this.valorCompra = entrada.readDouble();
        entrada.close();
    }

    public String toString(){
        return "Id Compra: "          + this.idCompra + 
                "\nId Cliente: "      + this.idCliente + 
                "\nData da compra: "  + this.dataCompra.getTime() + 
                "\nValor da compra: " + this.valorCompra;
       }
}
