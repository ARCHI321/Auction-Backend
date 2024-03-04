package com.herovired.Auction.Management.System.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@AllArgsConstructor
@Getter
@Setter
public class CustomResponse {
    private String message;
    private boolean success;
}
