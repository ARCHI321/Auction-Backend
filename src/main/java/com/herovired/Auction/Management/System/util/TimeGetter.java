package com.herovired.Auction.Management.System.util;




import java.time.LocalTime;

public class TimeGetter {

    public static LocalTime getTime(int number) {
        if (number < 1 || number > 8) {
            throw new IllegalArgumentException("Number must be between 1 and 8");
        }

        LocalTime time;
        if (number == 1) {
            time = LocalTime.of(9, 0);
        } else {
            time = LocalTime.of(10, 0).plusHours(number - 2);
        }
        return time; // Return only the LocalTime
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 8; i++) {
            System.out.println("Number " + i + ": " + getTime(i));
        }
    }
}


