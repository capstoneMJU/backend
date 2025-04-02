package capstone.mju.backend.domain.common.exception;


import capstone.mju.backend.domain.common.error.ErrorCode;

public class ForbiddenException extends CustomException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}