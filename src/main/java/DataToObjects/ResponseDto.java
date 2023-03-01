/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataToObjects;

import java.util.List;
import Enums.Messages.Response;
import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Dragos
 */
public class ResponseDto implements Serializable {

    public int code;
    public boolean succes;
    public String message;
    public List<DataDto> data;
    public List<List<DataDto>> complexData;

    public ResponseDto() {
        Response response = Response.STANDARD_SUCCESS;
        code = response.getCode();
        succes = response.isSucces();
        message = response.getMessage();
        data = new LinkedList<>();
    }

    public ResponseDto(Response response, List<DataDto> data) {
        this.code = response.getCode();
        this.succes = response.isSucces();
        this.message = response.getMessage();
        this.data = data;
    }

    public ResponseDto(Response response, List<DataDto> data, List<List<DataDto>> complexData) {
        this.code = response.getCode();
        this.succes = response.isSucces();
        this.message = response.getMessage();
        this.data = data;
        this.complexData = complexData;
    }

    public ResponseDto(Response response) {
        this.code = response.getCode();
        this.succes = response.isSucces();
        this.message = response.getMessage();
        this.data = new LinkedList<>();
    }

    public ResponseDto(boolean succes, String message) {
        this.code = -1;
        this.succes = succes;
        this.message = message;
    }

}
