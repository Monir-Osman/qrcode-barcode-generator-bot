package org.qrcodebarcodegenerator.bot.handlers;

import org.qrcodebarcodegenerator.bot.services.UserStateService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class CallbackQueryHandler {

    private final UserStateService userStateService;

    public CallbackQueryHandler(UserStateService userStateService) {
        this.userStateService = userStateService;
    }

    public void handle(Update update, TelegramClient telegramClient) {
        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        try {
            AnswerCallbackQuery answer = AnswerCallbackQuery.builder()
                    .callbackQueryId(update.getCallbackQuery().getId())
                    .build();

            telegramClient.execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        String response;
        String userState = null;

        switch (callbackData) {
            case "generate_qr":
                response = "📱 QR Code Generator selected!\nPlease send the text or URL:";
                userState = "QR_CODE";
                break;
            case "generate_barcode":
                response = "📊 Barcode Generator selected!\nPlease send the number or text:";
                userState = "BARCODE";
                break;
            default:
                response = "❌ Unknown option.";
        }

        // Store user's selected option
        if (userState != null) {
            userStateService.setUserState(chatId, userState);
        }

        // Send response
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(response)
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}