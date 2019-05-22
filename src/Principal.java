import java.util.Scanner;
import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.SystemTray;
import java.util.Date;
import java.util.Random;
import java.io.Console;
import java.text.*;

/**
 * @author Luiz Junio
 * @author Allan Vivekanda
 * @author Breno
 * @author Henrique Fernandes
 * */
public class Principal{

    private static final Scanner read = new Scanner(System.in);
    private static final Console term = System.console();
    private static final String programName = "crud";
    private static Arquivo<Produto> arqProdutos;
    private static Arquivo<Categoria> arqCategorias;
    private static Arquivo<Cliente> arqClientes;
    private static Arquivo<Compra> arqCompra;
    private static Arquivo<ItemComprado> arqItemComprado;
    private static IndiceChaveComposta indice_Compra_Produto;
    private static IndiceChaveComposta indice_Produto_Compra;

    public static void main(String[] args){
        try{
            arqProdutos  = new Arquivo<>(Produto.class.getConstructor(), "Produtos", programName);
            arqCategorias = new Arquivo<>(Categoria.class.getConstructor(), "Categorias", programName);
            arqClientes = new Arquivo<>(Cliente.class.getConstructor(), "Clientes", programName);
            arqCompra = new Arquivo<>(Compra.class.getConstructor(), "Compras", programName);
            arqItemComprado = new Arquivo<>(ItemComprado.class.getConstructor(), "ItensComprados", programName);
            ProgramFile pf = new ProgramFile(programName);
            indice_Compra_Produto = new IndiceChaveComposta(20, pf.addFile("indice_Compra_Produto.idxc"));
            indice_Produto_Compra = new IndiceChaveComposta(20, pf.addFile("indice_Produto_Compra.idxc"));

            menuPrincipal();

            arqProdutos.close();
            arqCategorias.close();
            arqClientes.close();
            arqCompra.close();
            arqItemComprado.close();
            read.close();
        }//end try 
        catch(Exception e){
            e.printStackTrace();
        }//end catch
    }//end main

    /**
     * Menu Principal
     * @throws Exception 
     * */
    private static void menuPrincipal() throws Exception 
    {//Inicio menuPrincipal
        byte opcao;
        boolean fecharMenu = false;
        do{
            System.out.println(
                    "\n\t*** MENU PRINCIPAL ***\n" +
                    "0 - Efetuar login\n" +
                    "1 - Novo cadastro\n" +
                    "2 - Sair "
                    ); 
            System.out.print("Digite a opção: ");
            opcao = read.nextByte();
            switch(opcao){
                case 0:
                    menuLogin();
                    break;
                case 1:
                    menuCadastro();
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

    }//Fim menuPrincipal 

    /**
     * Menu de Login
     * @throws Exception 
     * */
    private static void menuLogin() throws Exception 
    {//Inicio menuLogin
        String email, senha;
        System.out.println("\n\t*** LOGIN ***\n");
        System.out.print("Email: ");
        email = read.next();
        senha = new String(term.readPassword("Senha: "));
        if(email.equals("admin") && senha.equals("coffe")) menuAdministrador();
        else{
            Cliente c = getCliente(email);
            if(c != null && c.validaSenha(senha)) menuCliente(c.getID());
            else System.out.println("Usuario não encontrado!\nVerifique se o email e senha estão corretos!");
        }
    }//Fim menuLogin

    /**
     * Menu de cadastro de cliente
     * @throws Exception 
     * */
    private static void menuCadastro() throws Exception 
    {//Inicio menuCadastro 
        String nome, email, cpf, senha;
        int id;
        boolean erro = false;
        System.out.println("\n\t*** Cadastro ***\n");
        System.out.print("Nome completo: ");
        nome = read.nextLine();
        nome = read.nextLine();
        System.out.print("Email: ");
        email = read.next();
        do{
            System.out.print("CPF: ");
            cpf = read.next().replace(".", "").replace("-", "");
            System.out.println(cpf);
            if(cpf.length() != 11){
                System.out.println("CPF inválido!\nDigite novamente!");
                erro = true;
            }
        }while(erro);
        Random rd = new Random();
        senha = new String("" + rd.nextInt(100000));
        Cliente c = getCliente(email);
        if(c == null){
            id = arqClientes.inserir(new Cliente(nome,email,cpf,senha));
            System.out.println("Cadastro efetuado!");
            System.out.println("Sua senha eh: " + senha);
        }
        else System.out.println("\nJá existe Usuario com esse email!\nCadastro cancelado!"); 
    }//Fim menuCadastro 

    /**
     * Menu de acoes administrativas
     * @throws Exception 
     * */
    private static void menuAdministrador() throws Exception 
    {//Inicio menuAdministrador    
        byte opcao;
        boolean fecharMenu = false;
        do{
            System.out.println(
                    "\n\t*** MENU ADMIN ***\n" +
                    "0 - Gerenciar produtos\n"     +
                    "1 - Gerenciar categorias\n"   +
                    "2 - Gerar relatorios\n" +
                    "3 - Listar usuarios cadastrados\n" +
                    "4 - Logout"
                    );
            System.out.print("Digite a opção: ");
            opcao = read.nextByte();
            switch(opcao){
                case 0:
                    menuProdutos();
                    break;
                case 1:
                    menuCategoria();
                    break;
                case 2:
                    //RELATORIO
                    break;
                case 3:
                    break;
                case 4:
                    fecharMenu = true;
                    break;
                default: 
                    System.out.println("Opcao invalida!\n");
                    Thread.sleep(1000);  
                    break;
            }
        } while(!fecharMenu);
    }//Fim menuAdministrador 

    /**
     * Menu de acoes do Usuario
     * @param idCliente O id do cliente que fez login
     * @throws Exception 
     * */
    private static void menuCliente(int idCliente) throws Exception
    {//Inicio menuCliente 
        byte opcao;
        int idCompra;
        boolean fecharMenu = false;
        do{
            System.out.println(
                "\n\t*** MENU CLIENTE ***\n"             +
                "0 - Comprar\n"                          +
                "1 - Gerar relatorio de compra\n"        +
                "2 - Alterar meus dados\n"               +
                "3 - Excluir conta\n"                    +
                "4 - Logout"
                );
                System.out.print("Digite sua opcao: ");
                opcao = read.nextByte();
            switch(opcao){
                case 0:
                    idCompra = arqCompra.inserir(new Compra(idCliente, new Date()));
                    menuCompra(idCliente, idCompra);
                    break;
                case 1:
                    //RELATORIO
                    break;
                case 2:
                    break;
                case 3:
                    if(arqClientes.remover(idCliente-1)) System.out.println("Cliente removido com sucesso");
                    fecharMenu = true;
                    break;
                case 4:
                    fecharMenu = true;
                    break;    
                default:
                    System.out.println("Opcao invalida!\n");
                    Thread.sleep(1000);
                    break;
            }
        }while(!fecharMenu);
    }//Fim menuCliente

    /**
     * Menu de Compra do Cliente
     * @param idCliente ID do cliente que fez login
     * @param idCompra ID da nova compra criada
     * @throws Exception 
     * */
    private static void menuCompra(int idCliente, int idCompra) throws Exception{
        byte opcao;
        boolean fecharMenu = false;
        int[] lista;
        int idProduto = 0;
        do{  
            System.out.println(
                    "\n\t*** MENU DE COMPRA ***\n"           +
                    "0 - Listar produtos\n"                  +
                    "1 - Adicionar produto a compra\n"       +
                    "2 - Remover produto da compra\n"        +
                    "3 - Visualizar compra\n"                +
                    "4 - Finalizar compra\n"                 +
                    "5 - Cancelar compra\n"
                    );
            System.out.print("Digite a opção: ");
            opcao = read.nextByte();
            System.out.println();
            switch(opcao){
                case 0:
                    listaP();
                    break;
                case 1:
                    adicionarItem(idCompra);
                    break;
                case 2:
                    //VERIFICAR SE ID EXISTE
                        System.out.println("Qual o id do produto a ser removido? ");
                        idProduto = read.nextInt();
                        indice_Compra_Produto.excluir(idCompra, idProduto);
                        indice_Produto_Compra.excluir(idProduto, idCompra);
                        arqItemComprado.remover(getIdItemComprado(idCompra, idProduto));
                    break;
                case 3:
                    lista = indice_Compra_Produto.lista(idCompra);
                    //RELATORIO
                    break;
                case 4:
                    fecharMenu = true;
                    break;
                case 5:
                    fecharMenu = true;
                    lista = indice_Compra_Produto.lista(idCompra);
                    for(int i = 0; i < lista.length; i ++){
                        indice_Compra_Produto.excluir(idCompra, lista[i]);
                        indice_Produto_Compra.excluir(lista[i], idCompra);
                        arqItemComprado.remover(getIdItemComprado(idCompra, idProduto));
                    }
                    arqCompra.remover(idCompra);
                    break;
                default: 
                    System.out.println("Opcao invalida!\n");
                    Thread.sleep(1000);  
                    break;
            }
        }while(!fecharMenu);
    }

    /**
     * Metodo para Adicionar um novo produto a compra
     * @param idCompra Id da compra
     * @throws Exception 
     * */
    private static void adicionarItem(int idCompra) throws Exception{
        int idItemComprado;
        boolean qtdInvalida = false;
        boolean idInvalido = false;

        do{
            System.out.print("Digite o id do produto desejado: ");
            int id = read.nextInt();
            Produto p = arqProdutos.pesquisar(id - 1);
            if (p != null && p.idProduto != -1 ){
                do{
                    System.out.print("Qual a quantidade desejada? ");
                    byte qtdProduto = read.nextByte();
                    if(qtdProduto > 0 && qtdProduto <= 255){
                        idItemComprado = arqItemComprado.inserir(new ItemComprado(idCompra, qtdProduto, p));
                        indice_Compra_Produto.inserir(idCompra, p.getID());
                        indice_Produto_Compra.inserir(p.getID(), idCompra);
                        System.out.println("Adicionado "+ qtdProduto + "x '" + p.nomeProduto + "'");
                    }else {
                        System.out.println("Valor invalido!");
                        qtdInvalida = true;
                    }
                }while(qtdInvalida);   
            }else{
                 System.out.print("Id invalido!");
                 idInvalido = true;
            }
        }while(idInvalido);
    }//end adicionaritem

    /**
     * Menu de categorias de produtos 
     * @throws Exception
     * */
    private static void menuCategoria()throws Exception
    {//Inicio menuCategoria
        byte opcao;
        boolean fecharMenu = false;
        Integer[] listaC = null;
        do{  
            System.out.println(
                    "\n\t*** MENU DE CATEGORIAS ***\n"                   +
                    "0 - Adicionar categoria\n"                          +
                    "1 - Remover categoria\n"                            +
                    "2 - Listar categorias cadastradas\n"                +
                    "3 - Listar produtos cadastrados em uma categoria\n" +
                    "4 - Sair"
                    );
            System.out.print("Digite a opção: ");
            opcao = read.nextByte();
            System.out.println();
            switch(opcao){
                case 0:
                    adicionarC();
                    break;
                case 1:
                    removerC();
                    break;
                case 2:
                    listaC = listaCategoriasCadastradas();
                    if(listaC == null) System.out.println("Não ha categorias cadastradas!");
                    break;
                case 3:
                    consultaC();
                    break;
                case 4:
                    fecharMenu = true;
                    break;
                default: 
                    System.out.println("Opcao invalida!\n");
                    Thread.sleep(1000);  
                    break;
            }
        }while(!fecharMenu);
    }//Fim menuCategoria

    /**
     * Menu administrativo de Produtos 
     * @throws Exception
     * */  
    private static void menuProdutos() throws Exception
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
                    adicionarP();
                    break;
                case 1:
                    removerP();
                    break;
                case 2:
                    alterarP();
                    break;
                case 3:
                    consultaP();
                    break;
                case 4:
                    listaP();
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

    /**
     * Metodo para adicionar um produto.
     * @throws Exception
     * */
    private static void adicionarP() throws Exception
    {//Inicio adicionar
        //inserir produto  
        String nomeProduto, descricao, marca, origem;
        int idCategoria = 0;
        Integer[] idsValidosC;
        float preco;
        int id; 
        boolean erro, valido;     
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
        System.out.println();
        if((idsValidosC = listaCategoriasCadastradas()) != null){
            System.out.print("\nEscolha uma categoria para o produto,\ne digite o ID: ");
            do{
                valido = false;
                idCategoria = read.nextInt();
                valido = Arrays.asList(idsValidosC).contains(idCategoria);
                if(!valido) System.out.println("Esse ID não é valido!\nDigite um ID valido: ");
            } while(!valido);
            do{
                erro = false;
                System.out.println("\nAdicionar novo produto?");
                System.out.print("1 - SIM\n2 - NÂO\nR: ");
                switch (read.nextByte()){
                    case 1:
                        id = arqProdutos.inserir(new Produto(nomeProduto,descricao,preco,marca,origem,idCategoria));
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
            } while(erro);  
        }
        else{
            System.out.println("\nOps..! Aparentemente não existem categorias para associar o novo produto!");
            System.out.println("Por favor, crie ao menos uma categoria antes de adicionar um produto!");
            Thread.sleep(1000);
        }

    }//Fim adicionar

    /**
     * Metodo para adicionar uma categoria.
     * @throws Exception
     * */  
    private static void adicionarC() throws Exception
    {//inicio adicionarC
        String nomeCategoria;
        boolean erro;
        boolean outro;
        int id;
        System.out.println("\t** Adicionar categoria **\n");
        do{
            outro = false;
            System.out.print("Digite o nome da categoria: ");
            nomeCategoria = read.nextLine();
            nomeCategoria = read.nextLine();
            do{
                erro = false;
                System.out.println("\nAdicionar nova categoria '" + nomeCategoria + "' ?");
                System.out.print("1 - SIM\n2 - NÂO\nR: ");
                switch (read.nextByte()){
                    case 1:
                        id = arqCategorias.inserir(new Categoria(nomeCategoria));
                        System.out.println("\nCategoria criada com o ID: " + id);
                        System.out.println("Operacao concluida com sucesso!");
                        Thread.sleep(1000);
                        System.out.println("\nDeseja criar outra categoria ?"); 
                        System.out.print("1 - SIM\n2 - NÂO\nR: ");
                        if (read.nextByte() == 1) outro = true;
                        break;
                    case 2:
                        System.out.println("\nNova categoria não foi criada!");
                        break;
                    default:
                        System.out.println("\nOpção Inválida!\n");
                        erro = true;
                        break;
                }
            } while(erro);
        } while(outro);
    }//Fim adicionarC

    /**
     * Metodo para remover um produto
     * @throws Exception
     * */
    private static void removerP() throws Exception
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
                    result = arqProdutos.remover(id - 1); 
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

    /**
     * Metodo para remover uma categoria
     * @throws Exception
     * */
    private static void removerC() throws Exception 
    {//Inicio removerC
        int idCategoria;
        int idCategoriaNew;
        boolean erro, valido,result = false;
        Integer[] idsValidosC;
        ArrayList<Produto> lista;
        String nomeCategoria = null;
        System.out.println("\t** Remover categoria **\n");
        do{
            erro = false;
            System.out.print("ID da categoria a ser removida: ");
            idCategoria = read.nextInt();
            if(idCategoria > 0){
                nomeCategoria = getNomeCategoria(idCategoria - 1);
                if(nomeCategoria == null){
                    erro = true;
                    System.out.println("Categoria inexistente!");
                } 
            }
            else {
                erro = true;
                System.out.println("ID Inválida!");  
            }
            System.out.println();
        } while(erro);

        do{
            erro = false;
            System.out.println("\nRemover a categoria '" + nomeCategoria + "' ?");
            System.out.print("1 - SIM\n2 - NÂO\nR: ");
            switch (read.nextByte()){
                case 1:
                    lista = listProdutosC(idCategoria);
                    if (lista.isEmpty()){
                        System.out.println("Não ha produtos associados a '" + nomeCategoria + "', procedendo com remoção...");
                        result = arqCategorias.remover(idCategoria - 1); 
                        if(result) System.out.println("Removido com sucesso!");
                    }
                    else {
                        System.out.println("\nExistem produtos nessa categoria!!");
                        System.out.println(
                                "O que deseja fazer?\n" +
                                "0 - Apagar todos os produtos pertencentes e a categoria\n" +
                                "1 - Mudar a categoria dos produtos e remover\n" +
                                "2 - Cancelar remoção\n"
                                );
                        do{
                            System.out.print("Opção: ");
                            switch(read.nextByte())
                            {//Inicio switch
                                case 0:
                                    for(Produto p: lista){
                                        System.out.println("Removendo '" + p.nomeProduto + "'...");
                                        result = arqProdutos.remover(p.idProduto - 1);
                                    }
                                    System.out.println("Excluindo categoria '" + nomeCategoria + "'...");
                                    result = arqCategorias.remover(idCategoria - 1);
                                    System.out.println("Concluido exclusão de " + lista.size() + " produtos e 1 categoria.");
                                    lista = null;
                                    break;
                                case 1:
                                    idsValidosC = listaCategoriasCadastradas();
                                    if(idsValidosC.length == 1){
                                        System.out.println("\nOperação não é possivel!\nSo tem uma categoria!");
                                        Thread.sleep(1000);
                                    }
                                    else{
                                        System.out.println("\nProdutos:");
                                        for(Produto p: lista){
                                            System.out.println(
                                                    "\nId: "          + p.idProduto    + 
                                                    "\nNome: "        + p.nomeProduto + 
                                                    "\nDescricao: "   + p.descricao    + 
                                                    "\nMarca: "       + p.marca     
                                                    );  
                                            System.out.print("\nEscolha uma outra categoria para o produto,\ne digite o ID: ");
                                            do{
                                                valido = false;
                                                idCategoriaNew = read.nextInt();
                                                valido = Arrays.asList(idsValidosC).contains(idCategoria);
                                                if(!valido) System.out.println("Esse ID não é valido!\nDigite um ID valido: ");
                                                else if(idCategoriaNew == idCategoria){
                                                    System.out.println("Não pode escolher a mesma categoria antiga!\nDigite um ID valido: ");
                                                    valido = false;
                                                } 
                                            } while(!valido);
                                            p.idCategoria = idCategoriaNew;
                                            result = arqProdutos.alterar(p.idProduto, p);
                                            System.out.println("Movido com sucesso!");
                                        }
                                        result = arqCategorias.remover(idCategoria - 1);
                                    }//Fim else
                                    break;
                                case 2:
                                    System.out.println("\nOperação Cancelada!");
                                    break;  
                                default:
                                    System.out.println("\nOpção Inválida!\n");
                                    erro = true;
                                    break; 
                            }//Fim switch
                        }while(erro);
                    }
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
    }//Fim removerC

    /**
     * Metodo para alterar um produto
     * @throws Exception
     * */
    private static void alterarP() throws Exception
    {//Inicio alterarP
        String nomeProduto, descricao, marca, origem; 
        int idCategoria;
        Integer[] idsValidosC;
        float preco;
        int id; 
        boolean erro, result, valido; 
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
        System.out.println();
        if((idsValidosC = listaCategoriasCadastradas()) != null){
            System.out.print("Escolha uma categoria para o produto,\ne digite o ID: ");
            do{
                valido = false;
                idCategoria = read.nextInt();
                valido = Arrays.asList(idsValidosC).contains(idCategoria);
                if(!valido) System.out.print("Esse ID não é valido!\nDigite um ID valido: ");
            } while(!valido);
            do{
                erro = false;
                System.out.println("\nAlterar produto?");
                System.out.print("1 - SIM\n2 - NÂO\nR: ");
                switch (read.nextByte()){
                    case 1:
                        result = arqProdutos.alterar(id, new Produto(nomeProduto,descricao,preco,marca,origem,idCategoria));
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
        }
        else{
            System.out.println("\nOps..! Aparentemente não existem categorias para associar ao produto!");
            System.out.println("Por favor, crie ao menos uma categoria antes de adicionar um produto!");
            Thread.sleep(1000);
        } 
    }//Fim alterarP

    /**
     * Metodo para consultar um produto
     * @throws Exception
     * */
    private static void consultaP() throws Exception
    {//Inicio consultaP
        boolean erro;
        int id;
        Produto p;
        Categoria c;
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
        p = arqProdutos.pesquisar(id - 1);
        if (p != null && p.idProduto != -1 ){
            c = arqCategorias.pesquisar(p.idCategoria - 1);
            System.out.println(
                    "Id: "            + p.idProduto    + 
                    "\nNome: "        + p.nomeProduto + 
                    "\nDescricao: "   + p.descricao    + 
                    "\nPreco: "       + p.preco        + 
                    "\nMarca: "       + p.marca        + 
                    "\nOrigem: "      + p.origem
                    );
            if(c != null) System.out.println("Categoria: " + c.nome);
            else System.out.println("Categoria: " + p.idCategoria);  
        }
        else System.out.println("Produto não encontrado!");  
    }//Fim consultaP

    /**
     * Metodo para mostrar todos os produtos de uma categoria
     * @throws Exception
     * */  
    private static void consultaC() throws Exception
    {//Inicio consultaC
        boolean erro;
        int idCategoria;
        String nomeCategoria;
        ArrayList<Produto> lista;
        System.out.println("\t** Listar produtos de uma categoria **\n");
        do{
            erro = false;
            System.out.print("ID da categoria a ser consultada: ");
            idCategoria = read.nextInt();
            if(idCategoria <= 0){
                erro = true;
                System.out.println("ID Inválida! ");
            }
            System.out.println();
        } while(erro);
        lista = listProdutosC(idCategoria);
        if(lista != null && !lista.isEmpty()){
            nomeCategoria = getNomeCategoria(idCategoria - 1);
            System.out.println("Produtos pertencentes a '" + nomeCategoria + "'");
            for(Produto p: lista){
                System.out.println(
                        "Id: "            + p.idProduto    + 
                        "\nNome: "        + p.nomeProduto + 
                        "\nDescricao: "   + p.descricao    + 
                        "\nPreco: "       + p.preco        + 
                        "\nMarca: "       + p.marca        + 
                        "\nOrigem: "      + p.origem       +
                        "\nCategoria: "   + nomeCategoria  
                        );     
            }
        }
        else System.out.println("Não ha produtos nessa categoria, ou ela não existe!");
    }//Fim consultaC

    /**
     * Metodo para mostrar todos os produtos cadastrados
     * @throws Exception
     * */  
    private static void listaP() throws Exception
    {//Inicio listaP
        String nomeCategoria;
        ArrayList<Produto> lista = arqProdutos.toList();
        if(!lista.isEmpty()) System.out.println("\t** Lista dos produtos cadastrados **\n");
        for(Produto p: lista){
            if (p != null && p.idProduto != -1 ){
                nomeCategoria = getNomeCategoria(p.idCategoria - 1);
                System.out.println(
                        "Id: "            + p.idProduto    + 
                        "\nNome: "        + p.nomeProduto + 
                        "\nDescricao: "   + p.descricao    + 
                        "\nPreco: "       + p.preco        + 
                        "\nMarca: "       + p.marca        + 
                        "\nOrigem: "      + p.origem
                        );
                if(nomeCategoria != null) System.out.println("Categoria: " + nomeCategoria);
                else System.out.println("Categoria: " + p.idCategoria);
                System.out.println();
                Thread.sleep(500);
            }  
        } 
    }//Fim listaP

    /**
     * Metodo para listar todas as categorias
     * @return Um array de Integer com os ids das categorias para futura validacao
     * @throws Exception
     * */
    private static Integer[] listaCategoriasCadastradas() throws Exception
    {//Inicio listaCategoriasCadastradas
        int count = 0;
        ArrayList<Categoria> lista = arqCategorias.toList();
        Integer[] idsValidos = null; //Lista retornando os ids de categorias validos para consulta
        if(!lista.isEmpty()){
            idsValidos = new Integer[lista.size()];
            System.out.println("\t** Lista de categorias cadastradas **\n");
            for(Categoria c: lista){
                System.out.println(c);
                idsValidos[count] = c.idCategoria;
                count++;
            }
        }
        return idsValidos;
    }//Fim listaCategoriasCadastradas

    /**
     * Metodo para obter uma lista de produtos de uma categoria
     * @param idCategoria Categoria a ser filtrada
     * @return Arraylist de produtos da categoria
     * @throws Exception
     * */
    private static ArrayList<Produto> listProdutosC(int idCategoria) throws Exception 
    {//Inicio listProdutosC
        ArrayList<Produto> lista = arqProdutos.toList();
        lista.removeIf(p -> p.idCategoria != idCategoria);
        return lista;
    }//Fim listProdutosC 

    /**
     * Metodo para obter o nome da categoria
     * @param idCategoria 
     * @return O nome da categoria
     * @throws Exception
     * */
    private static String getNomeCategoria(int idCategoria) throws Exception 
    {//Inicio getNomeCategoria
        String nome = null;
        Categoria c = arqCategorias.pesquisar(idCategoria);
        if (c != null) nome = c.nome;
        return nome;
    }//Fim getNomeCategoria

    private static Cliente getCliente(String email) throws Exception
    {//Inicio getCliente
        Cliente cliente = null;
        ArrayList<Cliente> lista = arqClientes.toList();
        for(Cliente c: lista){
            if(c.email.equals(email)){
                cliente = c;
                break;
            }
        }
        return cliente;
    }//Fim getCliente

    private static int getIdItemComprado(int idCompra, int idProduto) throws Exception 
    {//Inicio getIdItemComprado
        int idItemComprado = 0;
        ArrayList<ItemComprado> lista = arqItemComprado.toList();
        for(ItemComprado i: lista){
            if(i.idProduto == idProduto && i.idCompra == idCompra){
                idItemComprado = i.getID(); 
            }
        }
        return idItemComprado;
    }//Fim getIdItemComprado

}//end Principal
