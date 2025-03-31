package capstone.mju.backend.domain.common.exception;


import capstone.mju.backend.domain.common.error.ErrorCode;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

}

