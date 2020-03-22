package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 *相应前台数据的实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ResultData<T> {

    //
    private String code;//错误码
    private String msg;//错误提示
    private T data;//数据

    //创建一个接口方法给前台页面返回数据(包括,错误码和错误提示和一些其他的)
    public static interface ResultDataList{
        String aheadCode="404";
        String okCode="200";//成功页面
        String backCode="500";
    }

}
