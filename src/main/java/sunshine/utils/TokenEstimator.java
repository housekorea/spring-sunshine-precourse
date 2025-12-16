package sunshine.utils;

public class TokenEstimator {

    /**
     * 매우 단순한 토큰 추정기
     * - 영어: 약 4 chars = 1 token
     * - 한글: 약 2 chars = 1 token
     */
    public static int estimate(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        int length = text.length();

        // 한글 포함 여부로 보정
        boolean hasKorean = text.chars()
                .anyMatch(ch -> ch >= 0xAC00 && ch <= 0xD7A3);

        if (hasKorean) {
            return length / 2;
        } else {
            return length / 4;
        }
    }
}
