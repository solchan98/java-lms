package nextstep.image.domain;

import java.util.List;
import java.util.Optional;

public interface ImageRepository {

    Optional<Image> findById(Long imageId);

    List<Image> findAllByIds(List<Long> imageIds);
}
