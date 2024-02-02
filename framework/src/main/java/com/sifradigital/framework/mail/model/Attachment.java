package com.sifradigital.framework.mail.model;

public class Attachment {

    public static final String MIME_PDF = "application/pdf";
    public static final String MIME_EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final byte[] data;
    private final String name;
    private final String type;

    public Attachment(byte[] data, String name, String type) {
        this.data = data;
        this.name = name;
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
