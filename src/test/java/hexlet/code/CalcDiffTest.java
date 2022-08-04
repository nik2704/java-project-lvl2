package hexlet.code;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;

import static hexlet.code.utils.CalcDiff.getDifference;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CalcDiffTest {
    private final int val1 = 200;
    private final int val2 = 300;

    @Test
    public void testMapDiffecences() {
        Map<String, Object> map1 = Map.of("key1", "value1", "key2", val1);
        Map<String, Object> map2 = Map.of("key4", "value3", "key2", val2);

        Map<String, Map<String, Object>> expected1 = Map.of(
                "key1",
                Map.of("comparisonResult", "deleted", "oldValue", "value1"),
                "key2", Map.of("comparisonResult", "changed", "oldValue", val1, "value", val2),
                "key4", Map.of("comparisonResult", "added", "value", "value3")
        );

        assertThat(expected1).isEqualTo(getDifference(map1, map2));

        Map<String, Map<String, Object>> expected2 = Map.of(
                "key1",
                Map.of("comparisonResult", "unchanged", "value", "value1"),
                "key2",
                Map.of("comparisonResult", "unchanged", "value", val1)
        );

        assertThat(expected2).isEqualTo(getDifference(map1, map1));

        Map<String, Map<String, Object>> expected3 = Map.of(
                "key1",
                Map.of("comparisonResult", "added", "value", "value1"),
                "key2",
                Map.of("comparisonResult", "added", "value", val1)
        );

        assertThat(expected3).isEqualTo(getDifference(null, map1));

        Map<String, Map<String, Object>> expected4 = Map.of(
                "key1",
                Map.of("comparisonResult", "deleted", "oldValue", "value1"),
                "key2",
                Map.of("comparisonResult", "deleted", "oldValue", val1)
        );

        assertThat(expected4).isEqualTo(getDifference(map1, null));
    }
}
