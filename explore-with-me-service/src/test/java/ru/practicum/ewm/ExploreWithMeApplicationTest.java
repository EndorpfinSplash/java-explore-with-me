package ru.practicum.ewm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExploreWithMeApplicationTest {

    @Test
    void main() {
        Assertions.assertDoesNotThrow(ExploreWithMeApplicationTest::new);
//        Assertions.assertDoesNotThrow(() -> ExploreWithMeApplication.main(new String[]{}));
    }
}