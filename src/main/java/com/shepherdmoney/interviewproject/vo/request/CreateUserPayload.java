package com.shepherdmoney.interviewproject.vo.request;

import lombok.Data;
import lombok.Getter;
@Getter
@Data
public class CreateUserPayload {

    private String name;

    private String email;
}
