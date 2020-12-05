package com.demo.test.highconcurrentapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.CompletableFuture;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Request{
    private String serialNo;
    private String orderCode;
    private CompletableFuture future;
}


