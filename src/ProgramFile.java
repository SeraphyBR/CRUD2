import java.io.*;

public class ProgramFile {

    protected String programName;

    public ProgramFile(String programName){
        this.programName = programName;
    }

    public File addFile(String filename){
        File file = null;
        String os = System.getProperty("os.name");
        String folder = System.getProperty("user.home");
        if(os.contains("Windows")){
            file = new File(folder + "/AppData/Local/" + programName + "/");
            if (!file.exists()){
                if(file.mkdirs()){
                    System.out.println("O diretorio " + file.getAbsolutePath() + " foi criado para armazenar os dados!"); 
                    file = new File(folder + "/AppData/Local/" + programName + "/" + filename);
                }
                else{
                    System.out.println("Erro ao criar pasta para os dados do programa!\nUsando fallback!");
                    file = new File(filename);
                }
            }
            else{ 
                file = new File(folder + "/AppData/Local/" + programName + "/" + filename);
            }
        }
        else{
            file = new File(folder + "/.local/share/" + programName + "/");
            if (!file.exists()){
                if(file.mkdirs()){
                    System.out.println("O diretorio " + file.getAbsolutePath() + " foi criado para armazenar os dados!"); 
                    file = new File(folder + "/.local/share/" + programName + "/" + filename);
                }
                else{ 
                    System.out.println("Erro ao criar pasta para os dados do programa!\nUsando fallback!"); 
                    file = new File(filename);
                }
            }
            else{ 
                file = new File(folder + "/.local/share/" + programName + "/" + filename);
            }
        }
        return file;
    }
}
