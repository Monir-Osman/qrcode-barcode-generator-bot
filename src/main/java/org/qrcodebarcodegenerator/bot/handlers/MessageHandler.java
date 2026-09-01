package org.qrcodebarcodegenerator.bot.handlers;

import org.qrcodebarcodegenerator.bot.keyboards.InlineKeyboardBuilder;
import org.qrcodebarcodegenerator.bot.services.BarcodeGeneratorService;
import org.qrcodebarcodegenerator.bot.services.ImageService;
import org.qrcodebarcodegenerator.bot.services.UserStateService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class MessageHandler {

    private final UserStateService userStateService;
    private final InlineKeyboardBuilder keyboardBuilder;
    private final BarcodeGeneratorService barcodeGeneratorService;
    private final ImageService imageService;

    public MessageHandler(
            UserStateService userStateService,
            InlineKeyboardBuilder inlineKeyboardBuilder,
            BarcodeGeneratorService barcodeGeneratorService,
            ImageService imageServic
            ) {
        this.userStateService = userStateService;
        this.keyboardBuilder = inlineKeyboardBuilder;
        this.barcodeGeneratorService = barcodeGeneratorService;
        this.imageService = imageServic;
    }

    public void handle(Update update, TelegramClient telegramClient) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        // Handle /start command
        if (messageText.equals("/start")) {
            sendWelcomeMessage(chatId, telegramClient);
            return;
        }

        // Check if user has selected an option
        String userState = userStateService.getUserState(chatId);
        if (userState != null) {
            handleDataInput(chatId, messageText, userState, telegramClient);
        } else {
            sendUnknownCommandMessage(chatId, telegramClient);
        }
    }

    private void sendWelcomeMessage(long chatId, TelegramClient telegramClient) {
        String welcomeText = "👋 Welcome to QR Code & Barcode Generator!\n\n" +
                "Please choose an option:";

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(welcomeText)
                .replyMarkup(keyboardBuilder.createMainMenu())
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleDataInput(
            long chatId,
            String data,
            String userState,
            TelegramClient telegramClient
    ) {
        try {
            InputFile inputFile;

            if ("QR_CODE".equals(userState)) {

                var qrCode = barcodeGeneratorService.generateQrCode(data);

                inputFile = imageService.toInputFile(
                        qrCode,
                        "qrcode.png"
                );

            } else if ("BARCODE".equals(userState)) {

                var barcode = barcodeGeneratorService.generateBarcode(data);

                inputFile = imageService.toInputFile(
                        barcode,
                        "barcode.png"
                );

            } else {
                sendTextMessage(
                        chatId,
                        "❌ Unknown generator type.",
                        telegramClient
                );
                return;
            }

            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(inputFile)
                    .caption(
                            "QR_CODE".equals(userState)
                                    ? "📱 Your QR Code"
                                    : "📊 Your Barcode"
                    )
                    .build();

            telegramClient.execute(sendPhoto);

            userStateService.clearUserState(chatId);

        } catch (Exception e) {
            e.printStackTrace();

            sendTextMessage(
                    chatId,
                    "❌ Sorry, I couldn't generate the code. Please check your input and try again.",
                    telegramClient
            );
        }
    }

    private void sendUnknownCommandMessage(long chatId, TelegramClient telegramClient) {
        String message = "❓ Unknown command. Please use /start to begin.";
        sendTextMessage(chatId, message, telegramClient);
    }

    private void sendTextMessage(long chatId, String text, TelegramClient telegramClient) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}