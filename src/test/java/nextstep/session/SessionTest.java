package nextstep.session;

import nextstep.common.BaseTimeEntity;
import nextstep.image.domain.Image;
import nextstep.image.domain.ImageType;
import nextstep.payments.domain.Payment;
import nextstep.session.domain.Session;
import nextstep.session.domain.SessionRecruitmentStatus;
import nextstep.session.domain.SessionStatus;
import nextstep.session.domain.SessionType;
import nextstep.session.domain.Users;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static nextstep.session.TestFixtures.endSession;
import static nextstep.session.TestFixtures.noRecruitingSession;
import static nextstep.session.TestFixtures.preparingSession;
import static nextstep.session.TestFixtures.registableRecrutingFreeSession;
import static nextstep.session.TestFixtures.registableRecrutingPaidSession;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

class SessionTest {

    @ParameterizedTest
    @MethodSource(value = "nonRegistrableSessions")
    void 진행중이거나_모집중인_상태일_때만_강의_수강신청_할_수_있다(Session session) {
        Payment payment = new Payment("id", session.getId(), NsUserTest.JAVAJIGI.getId(), 1000L);
        Throwable throwable = catchThrowable(() -> session.register(NsUserTest.JAVAJIGI, payment));

        assertThat(throwable).isInstanceOf(IllegalStateException.class)
                .hasMessage("수강 신청 불가능한 상태입니다.");
    }

    static Stream<Session> nonRegistrableSessions() {
        return Stream.of(endSession(), preparingSession(), noRecruitingSession());
    }

    @Test
    void 유료_강의는_수강생이_결제한_금액과_수강료가_일치할_때_수강신청이_가능하다() {
        Session paidSession = registableRecrutingPaidSession();
        Payment payment = new Payment("id", paidSession.getId(), NsUserTest.JAVAJIGI.getId(), 1000L);

        Throwable throwable = catchThrowable(() -> paidSession.register(NsUserTest.JAVAJIGI, payment));

        assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유료 강의는 수강생이 결제한 금액과 수강료가 일치할 때 수강 신청이 가능합니다.");
    }

    @Test
    void 무료_강의는_결제_금액_없이_수강신청이_가능하다() {
        Session freeSession = registableRecrutingFreeSession();
        int expectedMemberSize = freeSession.memberSize() + 1;

        freeSession.register(NsUserTest.SANJIGI, Payment.empty());

        assertThat(freeSession.memberSize()).isEqualTo(expectedMemberSize);
    }


    @Test
    void 시작일은_현재보다_이전일_수_없다() {
        Throwable throwable = catchThrowable(() -> Session.create(
                new Users(999, Set.of(NsUserTest.JAVAJIGI)),
                0L,
                SessionType.FREE,
                null,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(7)
        ));

        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작일은 현재보다 이전일 수 없습니다.");
    }

    @Test
    void 종료일은_현재보다_이전일_수_없다() {
        Throwable throwable = catchThrowable(() -> Session.create(
                new Users(999, Set.of(NsUserTest.JAVAJIGI)),
                0L,
                SessionType.FREE,
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().minusDays(7))
        );

        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("종료일은 현재보다 이전일 수 없습니다.");
    }

    @Test
    void 강의는_1개_이상의_강의_이미지를_가질_수_있다() {
        List<Image> mockImages = List.of(new Image(1L, 300, 200, ImageType.JPG, 1024), new Image(1L, 300, 200, ImageType.JPG, 1024));

        Supplier<Session> createSession = () -> new Session(
                1L,
                new Users(999, Set.of(NsUserTest.JAVAJIGI)),
                0L,
                SessionType.FREE,
                SessionStatus.PROCESSING,
                SessionRecruitmentStatus.END,
                mockImages,
                new BaseTimeEntity(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(7))
        );

        assertThatCode(createSession::get).doesNotThrowAnyException();
    }
}
