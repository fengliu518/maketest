package com.example.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Return<T> implements Serializable {
    private static final long serialVersionUID = 5821756912459726994L;

    public static <T> boolean isErr(Return<T> ret) {
        if (ret == null) {
            return true;
        }

        if (ret.getCode() != 0) {
            return true;
        }

        return false;
    }

    public static <T> Return<T> createNew() {
        return new Return<T>(0, "", null);
    }

    public static <T> Return<T> createNew(int code, String desc) {
        return new Return<T>(code, desc, null);
    }

    public static <T> Return<T> createNew(int code, String desc, T data) {
        return new Return<T>(code, desc, data);
    }

    public static <T> Return<T> createNew(T data) {
        return new Return<T>(0, "", data);
    }

    private int code;
    private String desc;
    private T data;
}


