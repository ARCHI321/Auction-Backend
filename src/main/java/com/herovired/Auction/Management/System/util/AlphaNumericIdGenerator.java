package com.herovired.Auction.Management.System.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.security.SecureRandom;

public class AlphaNumericIdGenerator implements IdentifierGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";
    private static final int LENGTH = 10;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        StringBuilder builder = new StringBuilder(LENGTH);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < LENGTH; i++) {
            builder.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return builder.toString();
    }


}