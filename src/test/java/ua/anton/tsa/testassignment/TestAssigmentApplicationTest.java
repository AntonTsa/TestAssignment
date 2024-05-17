package ua.anton.tsa.testassignment;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = TestAssigmentApplication.class, webEnvironment = RANDOM_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class TestAssigmentApplicationTest {

    private final TestAssigmentApplication application;

    @Test
    @DisplayName("""
            GIVEN application
            WHEN spring context starts
            THEN verify that application has started
            """)
    void applicationContextLoads() {
        // GIVEN

        // WHEN
        TestAssigmentApplication.main(ArrayUtils.EMPTY_STRING_ARRAY);

        // THEN
        assertThat(application).isNotNull();
    }
}
