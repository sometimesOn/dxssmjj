package com.jia.dxssmjj.common.exception;

import com.jia.dxssmjj.common.result.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "全局自定义异常")
public class CustomException extends RuntimeException{

    @ApiModelProperty(value = "异常状态码")
    private  Integer code;

    /**
     * 通过状态码和错误信息创建异常对象
     * @param message
     * @param code
     */
    public CustomException(String message,Integer code){
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举对象
     * @param resultCodeEnum
     */
    public CustomException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    public String toString(){
        return "CustomException{" +
                "code=" + code +
                ",message=" + this.getMessage() +
                "}";
    }

}
