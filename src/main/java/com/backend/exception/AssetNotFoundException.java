package com.backend.exception;

public class AssetNotFoundException extends RuntimeException{
    public AssetNotFoundException() {
        super("Asset not found");
    }

        public AssetNotFoundException(long assetId) {
        super("Asset with Id " + assetId + " not found");
    }
}
