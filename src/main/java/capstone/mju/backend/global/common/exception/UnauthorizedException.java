package capstone.mju.backend.global.common.exception;


import capstone.mju.backend.global.common.error.ErrorCode;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

}

