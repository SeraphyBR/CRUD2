import java.io.*;

/**
 * @author Luiz Junio
 * @author Allan Vivekanda
 * @author Breno
 * @author Henrique Fernandes
 * */
class Cliente implements Entidade
{
    protected int idCliente;
    protected String nomeCliente;
    protected String email;

    //Contrutor vazio
    public Cliente(){
        this.idCliente = -1;
        this.nomeCliente = "";
        this.email = "";
    }

    //Construtor com parametros
    public Cliente(String nomeCliente, String email){
        this.nomeCliente = nomeCliente;
        this.email = email;
    }

    public void setID(int id){
        this.idCliente = id;
    }

    public int getID(){
        return this.idCliente;
    }

    //Retorna um array de bytes com os bytes para escrever no arquivo
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);

        saida.writeInt(this.idCliente);
        saida.writeUTF(this.nomeCliente);
        saida.writeUTF(this.email);
        return dados.toByteArray();
    }

    //Coloca nos atributos os bytes lidos do arquivo
    public void fromByteArray(byte[] b) throws Exception {
        ByteArrayInputStream dados = new ByteArrayInputStream(b);
        DataInputStream entrada = new DataInputStream(dados);

        this.idCliente = entrada.readInt();
        this.nomeCliente = entrada.readUTF();
        this.email = entrada.readUTF();
        entrada.close();
    }

    public String toString(){
        return "Id:"        + this.idCliente +
                "\nNome: "  + this.nomeCliente +
                "\nEmail: " + this.email;
    }
}//End Cliente
