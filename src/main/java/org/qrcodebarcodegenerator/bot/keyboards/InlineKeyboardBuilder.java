package org.qrcodebarcodegenerator.bot.keyboards;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class InlineKeyboardBuilder {

    public InlineKeyboardMarkup createMainMenu() {
        // Create buttons
        InlineKeyboardButton qrButton = InlineKeyboardButton.builder()
                .text("📱 Generate QR Code")
                .callbackData("generate_qr")
                .build();

        InlineKeyboardButton barcodeButton = InlineKeyboardButton.builder()
                .text("📊 Generate Barcode")
                .callbackData("generate_barcode")
                .build();

        // Create rows using InlineKeyboardRow
        InlineKeyboardRow row1 = new InlineKeyboardRow();
        row1.add(qrButton);
        row1.add(barcodeButton);

        // Build keyboard with rows
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1))  // or .keyboard(row1)
                .build();
    }

    public InlineKeyboardMarkup createConfirmationMenu() {
        InlineKeyboardButton yesButton = InlineKeyboardButton.builder()
                .text("✅ Yes")
                .callbackData("confirm_yes")
                .build();

        InlineKeyboardButton noButton = InlineKeyboardButton.builder()
                .text("❌ No")
                .callbackData("confirm_no")
                .build();

        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(yesButton);
        row.add(noButton);

        return InlineKeyboardMarkup.builder()
                .keyboard(Collections.singleton(row))  // Single row
                .build();
    }

    // Alternative: Create keyboard with variable number of rows
    public InlineKeyboardMarkup createCustomMenu(List<InlineKeyboardRow> rows) {
        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }


    // Helper method to create buttons
    private InlineKeyboardButton createButton(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }
}