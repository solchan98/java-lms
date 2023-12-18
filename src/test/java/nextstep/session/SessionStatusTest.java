package nextstep.session;

import nextstep.session.domain.SessionStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;

class SessionStatusTest {

    @ParameterizedTest
    @CsvSource(value = {"PREPARING", "PROCESSING", "END"})
    void 강의_상태는_준비중_진행중_종료_3가지_상태를_가진다(String status){
        List<SessionStatus> sessionStatuses = Arrays.stream(SessionStatus.values()).collect(Collectors.toList());

        assertThat(sessionStatuses).contains(SessionStatus.valueOf(status));
    }

    @Test
    void 강의_상태가_진행중일_경우_수강_신청이_가능하다() {
        assertThat(SessionStatus.PROCESSING.isRegistrable()).isTrue();
    }

    @Test
    void 강의_상태가_진행중이_아닌_경우_신청이_불가능하다() {
        List<SessionStatus> sessionStatuses = Arrays.stream(SessionStatus.values())
                .filter(status -> status != SessionStatus.PROCESSING)
                .collect(Collectors.toList());

        sessionStatuses.forEach(status -> assertThat(status.isRegistrable()).isFalse());
    }
}
