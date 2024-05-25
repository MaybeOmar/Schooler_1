package com.example.schoolmanagementsystem.Models;

public class PDFInfo {

    public String name;
    public String url;
    public String StorageKey;
    public String Key;

    public PDFInfo(){

    }

    public PDFInfo(String name, String url, String storageKey, String key) {
        this.name = name;
        this.url = url;
        StorageKey = storageKey;
        Key = key;
    }

    public String getStorageKey() {
        return StorageKey;
    }

    public void setStorageKey(String storageKey) {
        StorageKey = storageKey;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
