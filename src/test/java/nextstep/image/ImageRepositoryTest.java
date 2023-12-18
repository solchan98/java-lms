package nextstep.image;

import nextstep.image.domain.ImageRepository;
import nextstep.image.infrastructure.JdbcImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class ImageRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ImageRepository imageRepository;

    @BeforeEach
    void setUp() {
        imageRepository = new JdbcImageRepository(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Test
    void findById() {
        assertThat(imageRepository.findById(1L)).isPresent();
    }

    @Test
    void findAllByIds() {
        assertThat(imageRepository.findAllByIds(List.of(1L, 2L))).hasSize(2);
    }
}
