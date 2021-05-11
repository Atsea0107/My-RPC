package com.zpf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zpf
 * @createTime 2021-05-10 20:
 * serializable 接口，该类需要在调用过程中，从client传输给server
 * 因为是HelloService接口中 方法的传递参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelloObject implements Serializable {
    private Integer id;
    private String message;
}
