package nextstep.session;

import nextstep.session.domain.SessionRecruitmentStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SessionRecruitmentStatusTest {

    @Test
    void 모집중인_경우_등록이_가능하다() {
        assertThat(SessionRecruitmentStatus.PROCESSING.isRegistrable()).isTrue();
    }

    @Test
    void 모집중이_아닌_경우_등록이_불가능하다() {
        List<SessionRecruitmentStatus> sessionRecruitmentStatuses = Arrays.stream(SessionRecruitmentStatus.values())
                .filter(status -> status != SessionRecruitmentStatus.PROCESSING)
                .collect(Collectors.toList());

        sessionRecruitmentStatuses.forEach(status -> assertThat(status.isRegistrable()).isFalse());
    }
}
