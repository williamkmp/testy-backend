package com.mito.sectask;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SectaskApplicationTests {

    @Test
    void application_build_success() {
        final boolean buildSuccess = true;
        Assertions.assertThat(buildSuccess).isTrue();
    }
}
