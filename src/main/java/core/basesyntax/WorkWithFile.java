package core.basesyntax;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WorkWithFile {
    private static final String COMMA = ",";
    private static final String SUPPLY = "supply";
    private static final String BUY = "buy";
    private static final String RESULT = "result";
    private static final int OPERATION_TYPE_INDEX = 0;
    private static final int AMOUNT_INDEX = 1;

    public void getStatistic(String fromFileName, String toFileName) {

        int[] totals = calculateTotals(fromFileName);

        int totalSupply = totals[OPERATION_TYPE_INDEX];

        int totalBuy = totals[AMOUNT_INDEX];

        int totalAmount = totalSupply - totalBuy;

        String report = processReport(new int[]{totalSupply, totalBuy, totalAmount});
        writeReport(toFileName, report);
    }

    private int[] calculateTotals(String fromFileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fromFileName))) {
            return calculateTotalsFromReader(reader);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + fromFileName, e);
        }
    }

    private int[] calculateTotalsFromReader(BufferedReader reader) throws IOException {
        int totalSupply = OPERATION_TYPE_INDEX;
        int totalBuy = OPERATION_TYPE_INDEX;

        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(COMMA);
            String operationType = data[OPERATION_TYPE_INDEX];
            int amount = Integer.parseInt(data[AMOUNT_INDEX]);

            if (operationType.equals(SUPPLY)) {
                totalSupply += amount;
            } else if (operationType.equals(BUY)) {
                totalBuy += amount;
            }
        }

        return new int[] {totalSupply, totalBuy};
    }

    private String processReport(int[] totals) {
        return prepareReport(totals);
    }

    private String prepareReport(int[] totals) {
        int totalSupply = totals[OPERATION_TYPE_INDEX];
        int totalBuy = totals[AMOUNT_INDEX];
        int totalAmount = totalSupply - totalBuy;

        StringBuilder report = new StringBuilder();
        report.append(SUPPLY).append(COMMA).append(totalSupply).append(System.lineSeparator())
                .append(BUY).append(COMMA).append(totalBuy).append(System.lineSeparator())
                .append(RESULT).append(COMMA).append(totalAmount).append(System.lineSeparator());
        return report.toString();
    }

    private void writeReport(String toFileName, String report) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(toFileName))) {
            writer.write(report);
        } catch (IOException e) {
            throw new RuntimeException("Error writing file: " + toFileName, e);
        }
    }
}
