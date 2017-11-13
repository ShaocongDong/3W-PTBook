# 1moresec
###### /java/seedu/address/model/task/TaskNameContainsKeywordsPredicate.java
``` java
package seedu.address.model.task;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code ReadOnlyTask}'s {@code Name} matches any of the keywords given.
 */
public class TaskNameContainsKeywordsPredicate implements Predicate<ReadOnlyTask> {
    private final List<String> keywords;

    public TaskNameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(ReadOnlyTask task) {
        boolean hasName = keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(task.getName().toString(), keyword));
        boolean hasDescription = keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(task.getDescription().toString(), keyword));
        return hasName || hasDescription;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskNameContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TaskNameContainsKeywordsPredicate) other).keywords)); // state check
    }

}
```
###### /java/seedu/address/logic/parser/AddressBookParser.java
``` java
        case FindTaskCommand.COMMAND_WORD:
        case FindTaskCommand.COMMAND_ALIAS:
            return new FindTaskCommandParser().parse(arguments);

        case RemarkCommand.COMMAND_WORD:
            return new RemarkCommandParser().parse(arguments);

        case SetPriorityCommand.COMMAND_WORD:
        case SetPriorityCommand.COMMAND_ALIAS:
            return new SetPriorityCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
        case ListCommand.COMMAND_ALIAS:
            return new ListCommand();

```
###### /java/seedu/address/logic/parser/AddressBookParser.java
``` java
        case ExportCommand.COMMAND_WORD:
        case ExportCommand.COMMAND_ALIAS:
            return new ExportCommandParser().parse(arguments);

        case TaskByEndCommand.COMMAND_WORD:
        case TaskByEndCommand.COMMAND_ALIAS:
            return new TaskByEndCommand();

        case TaskByPriorityCommand.COMMAND_WORD:
        case TaskByPriorityCommand.COMMAND_ALIAS:
            return new TaskByPriorityCommand();

```
###### /java/seedu/address/logic/parser/AddressBookParser.java
``` java
        case ExportTaskCommand.COMMAND_WORD:
        case ExportTaskCommand.COMMAND_ALIAS:
            return new ExportTaskCommandParser().parse(arguments);

        case HistoryCommand.COMMAND_WORD:
        case HistoryCommand.COMMAND_ALIAS:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
        case ExitCommand.COMMAND_ALIAS:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
        case HelpCommand.COMMAND_ALIAS:
            return new HelpCommand();

        case UndoCommand.COMMAND_WORD:
        case UndoCommand.COMMAND_ALIAS:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
        case RedoCommand.COMMAND_ALIAS:
            return new RedoCommand();


        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
```
###### /java/seedu/address/logic/parser/FindTaskCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindTaskCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.task.TaskNameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindTaskCommandParser implements Parser<FindTaskCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindTaskCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTaskCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.replaceAll("\\s+", "").split("\\s*/\\s*");

        return new FindTaskCommand(new TaskNameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
```
###### /java/seedu/address/logic/commands/Command.java
``` java
    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of tasks.
     *
     * @param displaySize used to generate summary
     * @return summary message for persons displayed
     */
    public static String getMessageForTaskListShownSummary(int displaySize) {
        return String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, displaySize);
    }
```
###### /java/seedu/address/logic/commands/FindTaskCommand.java
``` java
package seedu.address.logic.commands;

import seedu.address.model.task.TaskNameContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindTaskCommand extends Command {

    public static final String COMMAND_WORD = "findTask";
    public static final String COMMAND_ALIAS = "ft";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all tasks whose name/description"
            + "contain any of the specified keywords (case-insensitive) and displays them "
            + "as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " hotpot/have fun";

    private final TaskNameContainsKeywordsPredicate predicate;

    public FindTaskCommand(TaskNameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredTaskList(predicate);
        return new CommandResult(getMessageForTaskListShownSummary(model.getFilteredTaskList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindTaskCommand // instanceof handles nulls
                && this.predicate.equals(((FindTaskCommand) other).predicate)); // state check
    }
}
```
###### /java/seedu/address/commons/core/Messages.java
``` java
    public static final String MESSAGE_TASKS_LISTED_OVERVIEW = "%1$d tasks listed!";
    public static final String[] AUTOCOMPLETE_FIELD = {"addTask", "add", "deleteTask", "delete",
        "list", "listTask", "edit", "editTask", "export", "exportTask", "select", "selectTask",
        "setPriority", "taskByEnd", "taskByPriority", "find", "findTask", "markTask", "remark", "help",
        "link", "markTask", "setPriority", "taskByEnd", "taskByPriority", "undo", "redo",
        "history", "clear", "exit"};
}
```
