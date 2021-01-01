package core.basesyntax.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.service.OperationStrategy;
import core.basesyntax.service.OperationStrategyImpl;
import core.basesyntax.service.operation.BalanceOperationHandler;
import core.basesyntax.service.operation.OperationHandler;
import core.basesyntax.service.operation.PurchaseOperationHandler;
import core.basesyntax.service.operation.ReturnOperationHandler;
import core.basesyntax.service.operation.SupplyOperationHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class StoreTest {
    private static Store store;
    private static Map<String, OperationHandler> operationHandlerMap;
    private static OperationStrategy operationStrategy;

    @BeforeAll
    static void setUp() {
        operationHandlerMap = new HashMap<>();
        operationHandlerMap.put("b", new BalanceOperationHandler());
        operationHandlerMap.put("p", new PurchaseOperationHandler());
        operationHandlerMap.put("s", new SupplyOperationHandler());
        operationHandlerMap.put("r", new ReturnOperationHandler());

        operationStrategy = new OperationStrategyImpl(operationHandlerMap);

        store = new FruitStoreImpl(operationStrategy);
    }

    @Test
    void getStatistic_2items() {
        String fromFilePath = "src\\test1.csv";
        String toFilePath = "src\\result1.csv";
        store.getStatistic(fromFilePath, toFilePath);
        String actual = readFromFile(toFilePath).trim();
        String expected = "fruit,quantity" + System.lineSeparator()
                + "banana,65" + System.lineSeparator()
                + "apple,1080";
        assertEquals(expected, actual);
    }

    @Test
    void getStatistic_3items() {
        String fromFilePath = "src\\test2.csv";
        String toFilePath = "src\\result2.csv";
        store.getStatistic(fromFilePath, toFilePath);
        String actual = readFromFile(toFilePath).trim();
        String expected = "fruit,quantity" + System.lineSeparator()
                + "banana,145" + System.lineSeparator()
                + "apple,1080" + System.lineSeparator()
                + "lemon,20";
        assertEquals(expected, actual);
    }

    @Test
    void getStatistic_wrongOperator() {
        String fromFilePath = "src\\test3.csv";
        String toFilePath = "src\\result3.csv";
        assertThrows(RuntimeException.class, () -> {
            store.getStatistic(fromFilePath, toFilePath);
        });
    }

    @Test
    void getStatistic_negativeQuantity() {
        String fromFilePath = "src\\test4.csv";
        String toFilePath = "src\\result4.csv";
        assertThrows(RuntimeException.class, () -> {
            store.getStatistic(fromFilePath, toFilePath);
        });
    }

    @Test
    void getStatistic_invalidQuantity() {
        String fromFilePath = "src\\test5.csv";
        String toFilePath = "src\\result5.csv";
        assertThrows(RuntimeException.class, () -> {
            store.getStatistic(fromFilePath, toFilePath);
        });
    }

    @Test
    void getStatistic_balanceIsZero() {
        String fromFilePath = "src\\test6.csv";
        String toFilePath = "src\\result6.csv";
        store.getStatistic(fromFilePath, toFilePath);
        String actual = readFromFile(toFilePath).trim();
        String expected = "fruit,quantity" + System.lineSeparator()
                + "banana,0";
        assertEquals(expected, actual);
    }

    private String readFromFile(String fileName) {
        try {
            return Files.readString(Path.of(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Can't correctly read data from file " + fileName, e);
        }
    }
}
