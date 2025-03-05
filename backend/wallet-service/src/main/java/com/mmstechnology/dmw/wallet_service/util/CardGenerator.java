package com.mmstechnology.dmw.wallet_service.util;

public class CardGenerator {

    public static String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cardNumber.append((int) (Math.random() * 10));
        }
        return cardNumber.toString();
    }

    public static String generateCvv() {
        StringBuilder cvv = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            cvv.append((int) (Math.random() * 10));
        }
        return cvv.toString();
    }

    public static String generateExpirationDate() {
        StringBuilder expirationDate = new StringBuilder();
        expirationDate.append((int) (Math.random() * 10));
        expirationDate.append((int) (Math.random() * 10));
        expirationDate.append("/");
        expirationDate.append((int) (Math.random() * 10));
        expirationDate.append((int) (Math.random() * 10));
        return expirationDate.toString(); // MM/YY 99 / 99
    }
//
//    public static String generateCardHolderName(String cardHolderName) {
//        StringBuilder cardHolderName = new StringBuilder();
//        for (int i = 0; i < 10; i++) {
//            cardHolderName.append((char) ((int) (Math.random() * 26) + 65));
//        }
//        return cardHolderName.toString();
//    }

    public static String generateCardType() {
        String[] cardTypes = {"VISA", "MASTERCARD", "AMEX"};
        return cardTypes[(int) (Math.random() * 3)];
    }

    public static String generateCardBrand() {
        String[] cardBrands = {"BANCO GALICIA", "BANCO NACION", "BANCO PROVINCIA", "BANCO CIUDAD", "BANCO SANTANDER RIO", "BANCO MACRO", "BANCO BBVA", "BANCO ITAU", "BANCO CREDICOOP", "BANCO HIPOTECARIO"};
        return cardBrands[(int) (Math.random() * 10)];
    }

    public static String generateCardColor() {
        String[] cardColors = {"#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF"};
        return cardColors[(int) (Math.random() * 6)];
    }
}
