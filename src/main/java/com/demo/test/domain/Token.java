package com.demo.test.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Jack
 * @date   2019/06/02
 * @version latest version, current use
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"id", "signature", "timestamp"})
public class Token implements Serializable {

    private String signature;
    private long timestamp;
    /**  userId  */
    private long id;

    public Token(String signature, long timestamp, long id) {
        if (signature == null) {
            throw new IllegalArgumentException("signature can not be null");
        }
        this.timestamp = timestamp;
        this.signature = signature;
        this.id = id;
    }

    public Token(String signature) {
        if (signature == null) {
            throw new IllegalArgumentException("signature can not be null");
        }
        this.signature = signature;
    }

    /**
     * 重写哈希code timestamp 不予考虑, 因为就算timestamp不同也认为是相同的token
     */
    @Override
    public int hashCode() {
        return signature.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Token) {
            return ((Token) object).signature.equals(this.signature);
        }
        return false;
    }

}
