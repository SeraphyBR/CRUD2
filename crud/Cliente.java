package crud;

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
    protected String cpf;
    private String senha;

    //Contrutor vazio
    public Cliente(){
        this.idCliente = -1;
        this.nomeCliente = "";
        this.email = "";
        this.cpf = "";
        this.senha = "";
    }

    //Construtor com parametros
    public Cliente(String nomeCliente, String email, String cpf, String senha){
        this.nomeCliente = nomeCliente;
        this.email = email;
        this.cpf = cpf;
        this.senha = senha;
    }

    public boolean validaSenha(String senha){
        return this.senha.equals(senha);
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
        saida.writeUTF(this.cpf);
        saida.writeUTF(this.senha);
        return dados.toByteArray();
    }

    //Coloca nos atributos os bytes lidos do arquivo
    public void fromByteArray(byte[] b) throws Exception {
        ByteArrayInputStream dados = new ByteArrayInputStream(b);
        DataInputStream entrada = new DataInputStream(dados);

        this.idCliente = entrada.readInt();
        this.nomeCliente = entrada.readUTF();
        this.email = entrada.readUTF();
        this.cpf = entrada.readUTF();
        this.senha = entrada.readUTF();
        entrada.close();
    }

    public String toString(){
        return "Id:"    + this.idCliente +
            "\nNome: "  + this.nomeCliente +
            "\nEmail: " + this.email +
            "\nCPF: "   + this.cpf;
    }
}//End Cliente
