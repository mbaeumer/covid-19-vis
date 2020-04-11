package se.mbaeumer.covid19vis.parser.csv;

import se.mbaeumer.covid19vis.CsvDataRow;

public interface RowParser {
    CsvDataRow parseCsvString(final String csv);
}
