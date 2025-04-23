package com.example.jadxserver.model;

public class DecompileResponse {

    private String status;
    private String message;
    private String decompiledCode;
    private String downloadUrl;

    public DecompileResponse(String status, String message, String decompiledCode, String downloadUrl) {
        this.status = status;
        this.message = message;
        this.decompiledCode = decompiledCode;
        this.downloadUrl = downloadUrl;
    }

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDecompiledCode() {
        return decompiledCode;
    }

    public void setDecompiledCode(String decompiledCode) {
        this.decompiledCode = decompiledCode;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}