/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enums.Messages;

/**
 *
 * @author Dragos
 */
public enum Response {
//BASIC MESSAGES
//BASIC MESSAGES
    STANDARD_SUCCESS(100, true, "Operation completed succesfully"),
    STANDARD_FAIL(101, false, "Operation failed"),
    //REFERENCES
    REFERENCES_SUCCESS(102, true, "References received"),
    REFERENCES_FAIL(103, true, "Failed to receive references"),
    //LOGIN MESSAGES
    LOGIN_SUCCESFULL(110, true,"User logged in succesfully"),
    LOGIN_UNSUCCESFULL_WRONG_EMAIL(111, false,"Wrong email"),
    LOGIN_UNSUCCESFULL_WRONG_PASS(112, false,"Wrong password"),
    LOGIN_UNSUCCESFULL_USER_ALREADY_LOGGED_IN(113, false,"User already logged in"),
    //REGISTER MESSAGES
    REGISTER_SUCCESFULL(114, true, "User registered"),
    REGISTER_UNSUCCESFULL(115, false, "Registration failed"),
    REGISTER_UNSUCCESFULL_EMAIL_TAKEN(116, false, "Email is already taken"),
    REGISTER_UNSUCCESFULL_PASSWORDS_DONT_MATCH(117, false, "Passwords don't match"),
    //LOGOUT MESSAGES
    LOGOUT_SUCCESFULL(118, true, "User logged out succesfully"),
    LOGOUT_UNSUCCESFULL(119, false, "Failed to logout user"),
    //GET REQUESTS
    SUCCESFULL_GET(120, true, "Entities received"),
    UNSUCCESFULL_GET(121, false, "Failed to receive entities"),
    //POST REQUESTS
    SUCCESFULL_POST(122, true, "Entity succesfully saved"),
    UNSUCCCESFULL_POST(123, false, "Failed to save entities"),
    //DELETE REQUESTS
    SUCCESFULL_DELETE(124, true, "Entity deleted"),
    UNSUCCESFULL_DELETE(125, false, "Failed to delete entity"),
    //PUT REQUESTS
    SUCCESFULL_PUT(126, true, "Entity modified"),
    UNSUCCESFULL_PUT(127, false, "Failed to modify entity"),
    //ADMIN REQUESTS
    IS_ADMIN(128, true, "User is admin"),
    IS_NOT_ADMIN(129, false, "User is not admin"),
    //PERMISSIONS
    PERMISSION_DENIED(130, false, "You don't have permission"),
    //CONFIRMATION
    CONFIRMATION_SUCCESFULL(131, true, "Account confirmed"),
    CONFIRMATION_UNSUCCESFULL(132, false, "Account couldn't be confirmed");
    
    int code;
    boolean succes;
    String message;
    private Response(int code, boolean succes, String message){
        this.code = code;
        this.succes = succes;
        this.message = message;
    }
    
    public int getCode(){
        return this.code;
    }
    public boolean isSucces(){
        return succes;
    }
    public String getMessage(){
        return message;
    }
}
