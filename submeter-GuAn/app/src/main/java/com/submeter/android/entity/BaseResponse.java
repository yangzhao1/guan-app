/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.submeter.android.entity;

import com.submeter.android.constants.NetworkResConstant;

import java.io.Serializable;

/**
 * @author thm
 * @date 2018/12/9
 */
public class BaseResponse<T> implements Serializable {

    /**
     * 错误码
     */
    private int code;
    /**
     * 错误信息
     */
    private String msg;

    /**
     * 非分页数据对象（对象或者是集合）
     */
    private T data;

    /**
     * 分页数据对象
     */
    private T page;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public T getPage() {
        return page;
    }

    /**
     * 请求是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        if (code == NetworkResConstant.REQUEST_SUCCESS) {
            return true;
        } else {
            return false;
        }
    }
}
