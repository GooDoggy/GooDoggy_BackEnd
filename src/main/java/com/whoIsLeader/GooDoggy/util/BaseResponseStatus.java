package com.whoIsLeader.GooDoggy.util;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    TEST(false, 1001, "catch문 테스팅"),

    DUPLICATE_ID(false, 2000, "아이디가 중복되었습니다."),

    DISMATCH_PASSWORD(false, 2010, "비밀번호가 일치하지 않습니다."),

    INVALID_EMAIL(false, 2020, "이메일 형식이 잘못되었습니다."),
    INVALID_SESSION_INFORMATION(false, 2021, "잘못된 세션 정보입니다."),
    INVALID_FRIEND_REQUEST(false, 2022, "본 계정으로의 친구 신청은 불가합니다."),
    INVALID_NAME_EMAIL(false, 2023, "이름과 이메일이 일치하는 유저 정보가 없습니다."),
    INVALID_NAME_ID(false, 2024, "이름과 아이디가 일치하는 유저 정보가 없습니다."),

    NON_EXIST_ID(false, 2030, "아이디가 존재하지 않습니다."),
    NON_EXIST_SESSION(false, 2031, "유저 세션이 존재하지 않습니다."),
    NON_EXIST_USERIDX(false, 2032, "유저 인덱스가 존재하지 않습니다."),
    NON_EXIST_PERSONALIDX(false, 2033, "해당 개인 구독 인덱스를 갖는 데이터가 존재하지 않습니다."),
    NON_EXIST_FRIENDIDX(false, 2034, "친구 인덱스가 존재하지 않습니다."),
    NON_EXIST_STATUS(false, 2035, "해당 상태를 갖는 데이터가 존재하지 않습니다."),
    NON_EXIST_GROUPIDX(false, 2036, "해당 다인 구독 인덱스를 갖는 데이터가 존재하지 않습니다."),
    NON_EXIST_MEMBER(false, 2037, "다인 구독 게시글이 존재하지 않습니다."),

    EXIST_USER_REQUEST(false, 2040, "유저에게 보낸 친구 요청이 수락 대기 상태입니다."),
    EXIST_FRIEND_REQUEST(false, 2041, "유저로부터 받은 친구 요청이 수락 대기 상태입니다."),

    FAILED_TO_LOGIN(false, 2050, "로그인 처리를 실패하였습니다."),
    FAILED_TO_JOIN_GROUP(false, 2051, "다인 구독 참여에 실패하였습니다."),

    INACTIVE_USER(false,2060,"비활성화 처리된 유저입니다."),

    ALREADY_FRIEND(false, 2071, "이미 친구 목록에 존재하는 유저입니다."),
    ALREADY_JOINED(false, 2072, "이미 참여한 다인 구독 그룹입니다."),

    DATABASE_INSERT_ERROR(false, 6000, "데이터베이스 저장 오류가 발생하였습니다."),
    DATABASE_PATCH_ERROR(false, 6001, "데이터베이스 수정 오류가 발생하였습니다."),
    DATABASE_DELETE_ERROR(false, 6002, "데이터베이스 삭제 오류가 발생하였습니다.")
    ;

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
