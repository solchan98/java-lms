package nextstep.image.infrastructure;

import nextstep.image.domain.Image;
import nextstep.image.domain.ImageRepository;
import nextstep.image.domain.ImageType;
import nextstep.users.domain.NsUser;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("imageRepository")
public class JdbcImageRepository implements ImageRepository {

    private final JdbcOperations jdbcTemplate;

    private final NamedParameterJdbcOperations namedParameterJdbcTemplate;

    public JdbcImageRepository(JdbcOperations jdbcTemplate, NamedParameterJdbcOperations namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<Image> findById(Long id) {
        String sql = "select " +
                "id, " +
                "width, " +
                "height, " +
                "image_type, " +
                "size from image where id = ?";

        return Optional.ofNullable(jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new Image(
                        rs.getLong(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        ImageType.valueOf(rs.getString(4)),
                        rs.getLong(5)
                ),
                id));
    }

    @Override
    public List<Image> findAllByIds(List<Long> imageIds) {
        String selectImage = "select " +
                "id, " +
                "width, " +
                "height, " +
                "image_type, " +
                "size from image where id in (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource("ids", imageIds);

        return namedParameterJdbcTemplate.query(
                selectImage,
                parameters,
                (rs, rowNum) -> new Image(
                        rs.getLong(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        ImageType.valueOf(rs.getString(4)),
                        rs.getLong(5)
                )
        );
    }
}
