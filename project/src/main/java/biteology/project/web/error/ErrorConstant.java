package biteology.project.web.error;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorConstant {
    public static final MessageCode SERVICE_ERROR = new MessageCode("500", "Service Error");
    public static final MessageCode BAD_REQUEST = new MessageCode("400", "Bad Request");

    //auth
    public static final MessageCode UNAUTHORIZED  = new MessageCode("401", "Unauthorized");

    //constraint
    public static final MessageCode CONFLICT  = new MessageCode("401", "Conflict");
}
