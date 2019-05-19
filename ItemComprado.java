import java.io.*;
class ItemComprado{

    public int idCompra;
    public int idProduto;
    public int qtdProduto;
    public double precoUnitario;

    public ItemComprado(){
        this.idCompra      = -1;
        this.idProduto     = -1;
        this.qtdProduto    = 0;
        this.precoUnitario = 0.0;
    }

    //Retorna um array de bytes com os bytes para escrever no arquivo
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);

        saida.writeInt(this.idCompra);
        saida.writeInt(this.idProduto);
        saida.writeInt(this.qtdProduto);
        saida.writeDouble(this.precoUnitario);
        return dados.toByteArray();
    }

    //Coloca nos atributos os bytes lidos do arquivo
    public void fromByteArray(byte[] b) throws Exception {
        ByteArrayInputStream dados = new ByteArrayInputStream(b);
        DataInputStream entrada = new DataInputStream(dados);

        this.idCompra = entrada.readInt();
        this.idProduto = entrada.readInt();
        this.qtdProduto = entrada.readInt();
        this.precoUnitario = entrada.readDouble();
        entrada.close();
    }

    public ItemComprado(int idCompra, int idProduto, int qtdProduto, double precoUnitario){
        this.idCompra      = idCompra;
        this.idProduto     = idProduto;
        this.qtdProduto    = qtdProduto;
        this.precoUnitario = precoUnitario;
    }
}