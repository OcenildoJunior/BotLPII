import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.*;

public class Localizacao {

    TelegramBot bot = TelegramBotAdapter.build("1043391980:AAHIBCY9KTJm4n8rj7VAcEErWFmDCbsyxf0");
    GetUpdatesResponse updatesResponse;
    SendResponse sendResponse;
    BaseResponse baseResponse;

    //Variáveis da Classe
    private String linha = "";
    private String path = "localizacao.txt";

    public Localizacao() {
    }

    public void adicionarLocalizacao(String mensagem, Long chatId) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path , true))){
            if(fileExist(mensagem)){
                sendResponse = bot.execute(new SendMessage(chatId,"Local já cadastrado!\nPara tentar novamente: /adicionar_localizacao\nMenu inicial: /start"));
            }else{
                bw.write(mensagem);
                bw.newLine();
                bw.flush();
                sendResponse = bot.execute(new SendMessage(chatId, "Local adicionado com sucesso!\n\nPara listar Localizações: /listar_localizacoes\nMenu inicial: /start"));

            }
        }catch (IOException e){
            System.out.println("Error: " +e.getMessage());
        }
    }

    private boolean fileExist(String mensagem){
        try(BufferedReader br = new BufferedReader(new FileReader(path))){
            String line = br.readLine();

            while (line != null){
                if(line.compareTo(mensagem) == 0){
                    return true;
                }
                line = br.readLine();
            }
        }catch (IOException e){
            System.out.println("Error: " +e.getMessage());
        }
        return false;
    }

    public String listarLocalizacao(){
        try(BufferedReader br = new BufferedReader(new FileReader(path))){
            String aux = "";
            while (br.ready()){
                aux += br.readLine();
                aux += "\n";
                System.out.println(aux);
            }
            linha = aux;

        }catch (IOException e){
            System.out.println("Error: " +e.getMessage());
        }
        return linha;
    }
}