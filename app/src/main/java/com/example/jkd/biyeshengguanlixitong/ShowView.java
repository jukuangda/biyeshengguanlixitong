package com.example.jkd.biyeshengguanlixitong;

/**
 * Created by jkd on 2015/9/16.
 */
public interface ShowView {
    public void landingToRegister();

    public void registerBackToLanding();

    public void showCompletionRegister(String st);

    public void showMainFragment();

    public void CompletionRegisterCallbake(String st);
}