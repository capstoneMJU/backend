package capstone.mju.backend.domain.ocr.entity.repository;

import capstone.mju.backend.domain.ocr.entity.OcrScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OcrScrapRepository extends JpaRepository<OcrScrap, UUID> {
}
