import java.util.Scanner;
public class Principal{

    private static final Scanner read = new Scanner(System.in);

    public static void main(String[] args){
        byte opcao;
        boolean fecharMenu = false;
        Arquivo<Produto> arqProdutos;
        Arquivo<Categoria> arqCategorias;
        try{
            arqProdutos  = new Arquivo<>(Produto.class.getConstructor(), "Produto");
            arqCategorias = new Arquivo<>(Categoria.class.getConstructor(), "Categoria");
            do{
                System.out.println(
                        "\n\t*** MENU PRINCIPAL ***\n" +
                        "0 - Gerenciar produtos\n"     +
                        "1 - Gerenciar categorias\n"   +
                        "2 - Fechar programa"
                        );
                System.out.print("Digite a opção: ");
                opcao = read.nextByte();
                switch(opcao){
                    case 0:
                        menuProdutos(arqProdutos, arqCategorias);
                        break;
                    case 1:
                        menuCategoria();
                        break;
                    case 2:
                        fecharMenu = true;
                        break;
                    default: 
                        System.out.println("Opcao invalida!\n");
                        Thread.sleep(1000);  
                        break;
                }
            }while(!fecharMenu);
            arqProdutos.close();
            arqCategorias.close();
        }//end try 
        catch(Exception e){
            e.printStackTrace();
        }//end catch
    }//end main

    private static void menuCategoria(){
    }

    private static void menuProdutos(Arquivo<Produto> arqProdutos, Arquivo<Categoria> arqCategorias) throws Exception
    {//Inicio menuProdutos
        byte opcao;
        boolean fecharMenu = false;
        do{
            System.out.println(
                    "\n\t*** MENU DE PRODUTOS ***\n"    +
                    "0 - Adicionar produto\n"           +
                    "1 - Remover produto\n"             +
                    "2 - Alterar produto\n"             +
                    "3 - Consultar produto\n"           +
                    "4 - Listar produtos cadastrados\n" +
                    "5 - Sair"
                    );
            System.out.print("Digite a opção: ");
            opcao = read.nextByte();
            System.out.println();
            switch (opcao){
                case 0:
                    adicionarP(arqProdutos);
                    break;
                case 1:
                    removerP(arqProdutos);
                    break;
                case 2:
                    alterarP(arqProdutos);
                    break;
                case 3:
                    consultaP(arqProdutos);
                    break;
                case 4:
                    listaP(arqProdutos, arqCategorias);
                    break;
                case 5:
                    fecharMenu = true;
                    break;
                default:                    
                    System.out.println("Opcao invalida!\n");
                    Thread.sleep(1000);
                    break;
            }
        }while(!fecharMenu);
    }//Fim menuProdutos

    private static void adicionarP(Arquivo<Produto> arq) throws Exception
    {//Inicio adicionar
        //inserir produto  
        String nomeProduto, descricao, marca, origem;
        int idCategoria;
        float preco;
        int id; 
        boolean erro;     
        System.out.println("\t** Adicionar produto **\n");
        System.out.print("Nome do produto: ");
        nomeProduto = read.nextLine();
        nomeProduto = read.nextLine();
        System.out.print("Descricao do produto: ");
        descricao = read.nextLine();
        System.out.print("Preco do produto: ");
        preco = read.nextFloat();
        System.out.print("Marca do produto: ");
        marca = read.nextLine();
        marca = read.nextLine();
        System.out.print("Origem do produto: ");
        origem = read.nextLine();
        System.out.print("Escolha uma categoria existente: ");
        // idCategoria = 
        do{
            erro = false;
            System.out.println("\nAdicionar novo produto?");
            System.out.print("1 - SIM\n2 - NÂO\nR: ");
            switch (read.nextByte()){
                case 1:
                    id = arq.inserir(new Produto(nomeProduto,descricao,preco,marca,origem,idCategoria));
                    System.out.println("\nProduto inserido com o ID: " + id);   
                    break;
                case 2:
                    System.out.println("\nNovo produto não foi inserido!");
                    break;
                default:
                    System.out.println("\nOpção Inválida!\n");
                    erro = true;
                    break;
            }
        } while (erro);  
    }//Fim adicionar

    private static void removerP(Arquivo<Produto> arq) throws Exception
    {//Inicio removerP
        int id; 
        boolean erro, result;  
        System.out.println("\t** Remover produto **\n");
        System.out.print("ID do produto a ser removido: ");
        id = read.nextInt();
        do{
            erro = false;
            System.out.println("\nRemover produto?");
            System.out.print("1 - SIM\n2 - NÂO\nR: ");
            switch (read.nextByte()){
                case 1:
                    result = arq.remover(id, true); 
                    if(result) System.out.println("Removido com sucesso!");
                    else System.out.println("Produto não encontrado!"); 
                    break;
                case 2:
                    System.out.println("\nOperação Cancelada!");
                    break;
                default:
                    System.out.println("\nOpção Inválida!\n");
                    erro = true;
                    break;
            }
        } while (erro);  
    }//Fim removerP

    private static void alterarP(Arquivo<Produto> arq) throws Exception
    {//Inicio alterarP
        String nomeProduto, descricao, marca, origem; 
        int idCategoria;
        float preco;
        int id; 
        boolean erro, result; 
        System.out.println("\t** Alterar produto **\n");
        do{
            erro = false;
            System.out.print("ID do produto a ser alterado: ");
            id = read.nextInt();
            if(id <= 0){
                erro = true;
                System.out.println("ID Inválida! ");
            }
            System.out.println();
        } while(erro);   
        System.out.print("Nome do produto: ");
        nomeProduto = read.nextLine();
        nomeProduto = read.nextLine();
        System.out.print("Descricao do produto: ");
        descricao = read.nextLine();
        System.out.print("Preco do produto: ");
        preco = read.nextFloat();
        System.out.print("Marca do produto: ");
        marca = read.nextLine();
        marca = read.nextLine();
        System.out.print("Origem do produto: ");
        origem = read.nextLine();
        do{
            erro = false;
            System.out.println("\nAlterar produto?");
            System.out.print("1 - SIM\n2 - NÂO\nR: ");
            switch (read.nextByte()){
                case 1:
                    result = arq.alterar(id, new Produto(nomeProduto,descricao,preco,marca,origem,idCategoria));
                    if(result) System.out.println("Alterado com sucesso!");
                    else System.out.println("Produto para alterar não encontrado!");  
                    break;
                case 2:
                    System.out.println("\nOperação Cancelada!");
                    break;
                default:
                    System.out.println("\nOpção Inválida!\n");
                    erro = true;
                    break;
            }
        } while (erro);  
    }//Fim alterarP

    private static void consultaP(Arquivo<Produto> arq) throws Exception
    {//Inicio consultaP
        boolean erro;
        int id;
        Produto p;
        System.out.println("\t** Consultar produto **\n");
        do{
            erro = false;
            System.out.print("ID do produto a ser consultado: ");
            id = read.nextInt();
            if(id <= 0){
                erro = true;
                System.out.println("ID Inválida! ");
            }
            System.out.println();
        } while(erro);
        p = arq.pesquisar(id);
        if (p != null && p.idProduto != -1 ){
            System.out.println(p);
        }
        else System.out.println("Produto não encontrado!");  
    }//Fim consultaP

    private static void listaP(Arquivo<Produto> arq, Arquivo<Categoria> arq2) throws Exception
    {//Inicio listaP
        Produto p;
        Categoria c;
        System.out.println("\t** Lista dos produtos cadastrados **\n");
        for(int i = 1; i <= arq.ultimoID(); i++){
            p = arq.pesquisar(i);
            if (p != null && p.idProduto != -1 ){
                c = arq2.pesquisar(p.idCategoria);
                System.out.println(
                        "Id: "            + p.idProduto    + 
                        "\nNome: "        + p.nome_Produto + 
                        "\nDescricao: "   + p.descricao    + 
                        "\nPreco: "       + p.preco        + 
                        "\nMarca: "       + p.marca        + 
                        "\nOrigem: "      + p.origem
                        );
                if(c != null) System.out.println("Categoria: " + c.nome);
                else System.out.println("Categoria: " + p.idCategoria);
                System.out.println();
                Thread.sleep(500);
            }  
        } 
    }//Fim listaP

    private static int[] listaCategoriasCadastradas(Arquivo<Categoria> arq) throws Exception
    {//Inicio listaCategoriasCadastradas
        Categoria c;
        int[] idsValidos = new int[arq.ultimoID() + 1];
        System.out.println("\t** Lista de categorias cadastradas **\n");
        for(int i = 1; i <= arq.ultimoID(); i++){
            c = arq.pesquisar(i);
            if(c != null && c.idCategoria != -1){
                System.out.println(c);
                idsValidos[i - 1] = c.idCategoria;
            }
        }
        return idsValidos;
    }//Fim listaCategoriasCadastradas
}//end Principal
