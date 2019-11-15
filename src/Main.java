import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.IOException;


public class Main {
	public static void main(String[] args) throws IOException {

		//Criação do objeto bot com as informações de acesso
		TelegramBot bot = TelegramBotAdapter.build("1043391980:AAHIBCY9KTJm4n8rj7VAcEErWFmDCbsyxf0");

		//objeto responsável por receber as mensagens
		GetUpdatesResponse updatesResponse;
		//objeto responsável por gerenciar o envio de respostas
		SendResponse sendResponse;
		//objeto responsável por gerenciar o envio de ações do chat
		BaseResponse baseResponse;

		//controle de off-set, isto é, a partir deste ID será lido as mensagens pendentes na fila
		int m=0;
		int contador=0;
		String comandoAtual = "";
		String subComandoAtual = "";
		Localizacao localizacao = new Localizacao();
		//loop infinito pode ser alterado por algum timer de intervalo curto
		while (true){

			//executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(m));

			//lista de mensagens
			List<Update> updates = updatesResponse.updates();

			//Construtores

			//análise de cada ação da mensagem
			for (Update update : updates) {

				//atualização do off-set
				m = update.updateId()+1;
				String mensagem = update.message().text();
				System.out.println("Recebendo mensagem:"+ mensagem);
				if(mensagem == "" || mensagem == "null" || update.message().text() == null || update.message().text() == ""){
					mensagem = "/start";
				}

				//envio de "Escrevendo" antes de enviar a resposta
//				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
				//verificação de ação de chat foi enviada com sucesso
//				System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());

				//envio da mensagem de resposta


				//INICIO = /start - VOLTAR PARA O MENU
				if("/start".compareTo(mensagem) == 0){
					comandoAtual = "";
					subComandoAtual = "";
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Bem vindo!\n\nMenu Principal\n\nComandos válidos:\n/localizacao - Gerencie as localizações!"));
				}
				//Localização
				if (comandoAtual.compareTo("/localizacao") == 0){
					if (subComandoAtual.compareTo("/adicionar_localizacao") == 0){
						localizacao.adicionarLocalizacao(mensagem,update.message().chat().id());
					}
					subComandoAtual = mensagem;
					if (subComandoAtual.compareTo("/adicionar_localizacao") == 0){
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Digite o  nome do local que deseja adicionar."));
					}
					if(subComandoAtual.compareTo("/listar_localizacoes") == 0){
						String localizacoes = localizacao.listarLocalizacao();
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Lista de localizações:\n" + localizacoes));
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Para adicionar uma nova localização: /adicionar_localizacao\nMenu inicial: /start\n"));
					}
				}
				if("/localizacao".compareTo(mensagem) == 0){
					comandoAtual = "/localizacao";
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Comandos:\n\n/adicionar_localizacao\n/listar_localizacoes\nMenu inicial: /start\n"));
				}
				//verificação de mensagem enviada com sucesso
//				System.out.println("Mensagem Enviada?" +sendResponse.isOk());
			}
		}
	}
}
