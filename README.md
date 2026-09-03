# QR Code & Barcode Generator

A Telegram bot built with Java and Spring Boot that generates QR codes and barcodes from text, URLs, or numbers.

## Requirements

- Java 21
- Maven
- A Telegram bot token from [@BotFather](https://t.me/BotFather)

## Setup

1. Clone this repository and open it in your terminal.
2. Open `src/main/resources/application.properties`.
3. Replace `YOUR_TOKEN` with your Telegram bot token:

```properties
telegram.bot.token="YOUR_TOKEN"
```

## Run

Start the application with Maven:

```bash
mvn spring-boot:run
```

When the console shows that the bot is running, open your bot in Telegram and send `/start`.

Choose **QR Code** or **Barcode**, then send the text, URL, or number you want to convert. The bot replies with the generated image.

## Technologies

- Java 21
- Spring Boot
- Telegram Bots Java Library
- ZXing
