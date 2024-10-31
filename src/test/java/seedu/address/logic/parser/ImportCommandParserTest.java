package seedu.address.logic.parser;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

public class ImportCommandParserTest {
    private ImportCommandParser parser = new ImportCommandParser();
    private Path tempJson;
    private static final String VALID_JSON_STRING = "{ \"persons\": ["
            + "{ \"name\": \"John Doe\", \"phone\": \"12345678\", "
            + "\"email\": \"johndoe@example.com\", \"address\": \"123 Main St\", "
            + "\"age\": \"30\", \"sex\": \"M\", \"appointments\": [], \"tags\": [], \"note\": {"
            + "\"appointments\": [], \"remark\": [], \"medication\": [] },"
            + "\"starredStatus\": \"true\" }"
            + "] }";

    @AfterEach
    public void cleanUp() throws IOException {
        if (tempJson != null && Files.exists(tempJson)) {
            Files.delete(tempJson);
        }
    }

    @Test
    public void parse_fileDoesNotExist_throwsParseException() {
        String nonExistentFile = "nonexistent.json";
        assertThrows(ParseException.class, () -> {
            parser.parse(nonExistentFile);
        });
    }

    @Test
    public void parse_notJsonFile_throwsParseException() throws IOException {
        // Create a text file instead of JSON
        tempJson = Files.createTempFile("invalidData", ".txt");
        Files.writeString(tempJson, "This is not a JSON file.");

        assertThrows(ParseException.class, () -> {
            parser.parse(tempJson.getFileName().toString());
        });
    }

    @Test
    public void parse_emptyFileName_throwsParseException() {
        assertThrows(ParseException.class, () -> {
            parser.parse("");
        });
    }

    @Test
    public void parse_invalidJsonFormat_throwsParseException() throws IOException {
        // Create a JSON file with an invalid format
        String invalidJson = "{ \"persons\": [ { \"name\": \"John Doe\", \"phone\": \"12345678\" } ] }";
        tempJson = Files.createTempFile("invalidFormatData", ".json");
        Files.writeString(tempJson, invalidJson);

        assertThrows(ParseException.class, () -> {
            parser.parse(tempJson.getFileName().toString());
        });
    }

    @Test
    public void parse_missingRequiredField_throwsCommandException() throws IOException {
        // Create a JSON file missing required fields
        String missingFieldJson = "{ \"persons\": ["
                + "{ \"phone\": \"12345678\", \"email\": \"johndoe@example.com\" }"
                + "] }";
        tempJson = Files.createTempFile("missingFieldData", ".json");
        Files.writeString(tempJson, missingFieldJson);

        assertThrows(ParseException.class, () -> {
            parser.parse(tempJson.getFileName().toString());
        });
    }
}
