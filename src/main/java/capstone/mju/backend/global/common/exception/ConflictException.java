package capstone.mju.backend.global.common.exception;


import capstone.mju.backend.global.common.error.ErrorCode;

public class ConflictException extends CustomException {

    public ConflictException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}