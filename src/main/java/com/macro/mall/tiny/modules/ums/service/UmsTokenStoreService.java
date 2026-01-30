package com.macro.mall.tiny.modules.ums.service;

public interface UmsTokenStoreService {
    
    void storeToken(String username, String token);
    
    String getCurrentToken(String username);
    
    boolean isValidToken(String username, String token);
    
    void removeToken(String username);
}
