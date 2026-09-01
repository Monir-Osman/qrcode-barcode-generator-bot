package org.qrcodebarcodegenerator.bot.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserStateService {

    // Stores user's current state (e.g., "QR_CODE", "BARCODE")
    private final ConcurrentHashMap<Long, String> userStates = new ConcurrentHashMap<>();

    public void setUserState(long chatId, String state) {
        userStates.put(chatId, state);
    }

    public String getUserState(long chatId) {
        return userStates.get(chatId);
    }

    public void clearUserState(long chatId) {
        userStates.remove(chatId);
    }

    public boolean hasUserState(long chatId) {
        return userStates.containsKey(chatId);
    }
}