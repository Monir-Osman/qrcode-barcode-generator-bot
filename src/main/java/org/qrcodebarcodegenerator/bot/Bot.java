package org.qrcodebarcodegenerator.bot;

import org.qrcodebarcodegenerator.bot.handlers.CallbackQueryHandler;
import org.qrcodebarcodegenerator.bot.handlers.MessageHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class Bot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;

    public Bot(MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler) {
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    @Value("${telegram.bot.taken}")
    private String botToken;

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasCallbackQuery()) {
            callbackQueryHandler.handle(update, telegramClient);
        } else if (update.hasMessage()) {
            messageHandler.handle(update, telegramClient);
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        System.out.println("Bot is running: " + botSession.isRunning());
    }
}