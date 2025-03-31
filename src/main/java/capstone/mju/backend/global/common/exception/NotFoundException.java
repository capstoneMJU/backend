package capstone.mju.backend.global.common.exception;


import capstone.mju.backend.global.common.error.ErrorCode;

public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}

