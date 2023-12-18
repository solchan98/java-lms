package nextstep.session.domain;

import java.util.function.BooleanSupplier;

public enum SessionRecruitmentStatus {
    PROCESSING("모집중", () -> true),
    END("종료", () -> false);

    private final String status;

    private final BooleanSupplier isRegistrable;

    SessionRecruitmentStatus(String status, BooleanSupplier isRegistrable) {
        this.status = status;
        this.isRegistrable = isRegistrable;
    }

    public boolean isRegistrable() {
        return this.isRegistrable.getAsBoolean();
    }
}
