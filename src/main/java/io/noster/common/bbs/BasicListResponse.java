package io.noster.common.bbs;

import lombok.Data;

import java.util.List;

@Data
public class BasicListResponse {

    public static final int STATE_SUCCESS = 0;
    public static final int STATE_ERROR = 1;

    public static final String STATE_SUCCESS_MESSAGE = "success";

    private List<?> list;

    private Object object;

    private int state;
    private String stateMessage;
    private String errorMessage;
}
