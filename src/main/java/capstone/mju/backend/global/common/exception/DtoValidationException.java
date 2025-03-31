package capstone.mju.backend.global.common.exception;


import capstone.mju.backend.global.common.error.ErrorCode;

public class DtoValidationException extends CustomException {
    public DtoValidationException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}