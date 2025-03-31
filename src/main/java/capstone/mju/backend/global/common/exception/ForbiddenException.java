package capstone.mju.backend.global.common.exception;


import capstone.mju.backend.global.common.error.ErrorCode;

public class ForbiddenException extends CustomException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}