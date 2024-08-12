package knou.course.service.block;

import jakarta.transaction.Transactional;
import knou.course.domain.block.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlockService {

    private final BlockRepository blockRepository;

    @Transactional
    public void createBlock() {

    }
}
