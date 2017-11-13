# nusjzx
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public SortedList<ReadOnlyTask> getSortedTaskList() {
        return sortedTasks;
    }

    @Override
    public void taskByEnd() {
        sortedTasks.setComparator((t1, t2) -> t1.getEndDateTime().toString().compareTo(t2.getEndDateTime().toString()));
    }

```
###### /java/seedu/address/logic/parser/LinkedTasksCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.LinkedTasksCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new LinkedTasksCommand object
 */
public class LinkedTasksCommandParser implements Parser<LinkedTasksCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the LinkedTasksCommand
     * and returns an LinkedTasksCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public LinkedTasksCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new LinkedTasksCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, LinkedTasksCommand.MESSAGE_USAGE));
        }
    }
}
```
###### /java/seedu/address/logic/parser/LinkCommandParser.java
``` java
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON;

import java.util.ArrayList;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.LinkCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new LinkCommand object
 */
public class LinkCommandParser implements Parser<LinkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the LinkCommand
     * and returns an LinkCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public LinkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PERSON);

        Index index;
        ArrayList<Index> personIndices = new ArrayList<>();

        if (!(argMultimap.getValue(PREFIX_PERSON).isPresent())) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LinkCommand.MESSAGE_USAGE));
        }

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
            for (String pIndex : argMultimap.getAllValues(PREFIX_PERSON)) {
                personIndices.add(ParserUtil.parseIndex(pIndex));
            }
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LinkCommand.MESSAGE_USAGE));
        }

        return new LinkCommand(index, personIndices);
    }
}
```
###### /java/seedu/address/logic/parser/LinkedPersonsCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.LinkedPersonsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new LinkedPersonsCommand object
 */
public class LinkedPersonsCommandParser implements Parser<LinkedPersonsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the LinkedPersonsCommand
     * and returns an LinkedPersonsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public LinkedPersonsCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new LinkedPersonsCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, LinkedPersonsCommand.MESSAGE_USAGE));
        }
    }
}
```
###### /java/seedu/address/logic/commands/TaskByEndCommand.java
``` java
package seedu.address.logic.commands;

/**
 * Lists all persons in the address book to the user.
 */
public class TaskByEndCommand extends Command {

    public static final String COMMAND_WORD = "taskByEnd";
    public static final String COMMAND_ALIAS = "tbe";

    public static final String MESSAGE_SUCCESS = "Task sorted by end date now.";


    @Override
    public CommandResult execute() {
        model.taskByEnd();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### /java/seedu/address/logic/commands/LinkedTasksCommand.java
``` java
package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Selects a person identified using it's last displayed index from the address book.
 */
public class LinkedTasksCommand extends Command {

    public static final String COMMAND_WORD = "linkedTasks";
    public static final String COMMAND_ALIAS = "lts";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Show tasks linked the person identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_LINKED_TASKS_SUCCESS = "Linked tasks of Person: %1$s";

    private final Index targetIndex;

    public LinkedTasksCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson targetPerson = lastShownList.get(targetIndex.getZeroBased());

        model.updateFilteredTaskList(task->task.getPeopleIds().contains(targetPerson.getId()));
        return new CommandResult(String.format(MESSAGE_LINKED_TASKS_SUCCESS, targetPerson.getName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LinkedTasksCommand // instanceof handles nulls
                && this.targetIndex.equals(((LinkedTasksCommand) other).targetIndex)); // state check
    }
}
```
###### /java/seedu/address/logic/commands/LinkedPersonsCommand.java
``` java
package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.task.ReadOnlyTask;

/**
 * show Linked persons of a task
 */
public class LinkedPersonsCommand extends Command {

    public static final String COMMAND_WORD = "linkedPersons";
    public static final String COMMAND_ALIAS = "lps";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Show people linked the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_LINKED_PERSONS_SUCCESS = "Linked persons of task: %1$s";

    private final Index targetIndex;

    public LinkedPersonsCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<ReadOnlyTask> lastShownList = model.getSortedTaskList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyTask targetTask = lastShownList.get(targetIndex.getZeroBased());

        model.updateFilteredPersonList(person->targetTask.getPeopleIds().contains(person.getId()));
        return new CommandResult(String.format(MESSAGE_LINKED_PERSONS_SUCCESS, targetTask.getName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LinkedPersonsCommand // instanceof handles nulls
                && this.targetIndex.equals(((LinkedPersonsCommand) other).targetIndex)); // state check
    }
}
```
###### /java/seedu/address/logic/commands/LinkCommand.java
``` java
package seedu.address.logic.commands;

import  static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.DuplicateTaskException;
import seedu.address.model.task.exceptions.TaskNotFoundException;

/**
 * Edits the details of an existing person in the address book.
 */
public class LinkCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "link";
    public static final String COMMAND_ALIAS = "lk";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Link the task with the people specified "
            + "by the index number used in the last person listing.\n "
            + "Parameters: TaskIndex (must be a positive integer) "
            + PREFIX_PERSON + "personIndex "
            + "[" + PREFIX_PERSON + "personIndex]... \n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PERSON + "2";


    public static final String MESSAGE_LINK_SUCCESS = "linked Task: %s with The following person(s): ";
    public static final String MESSAGE_DUPLICATE_PERSON_LINKED_FAILURE = "person %d already linked.";

    private final Index index;
    private final ArrayList<Index> personIndices;

    /**
     * @param index of the task in the filtered task list to edit
     * @param
     */
    public LinkCommand(Index index, ArrayList<Index> personIndices) {
        requireNonNull(index);
        requireNonNull(personIndices);

        this.index = index;
        this.personIndices = personIndices;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        int personId;

        List<ReadOnlyTask> lastShownTaskList = model.getSortedTaskList();

        ReadOnlyTask targetTask = chooseItem(lastShownTaskList, index);

        List<ReadOnlyPerson> lastShownPersonList = model.getFilteredPersonList();

        ArrayList<Integer> peopleIds = targetTask.getPeopleIds();

        String personNameList = "";

        for (Index index : personIndices) {
            personNameList += lastShownPersonList.get(index.getZeroBased()).getName() + " ";
            personId = chooseItem(lastShownPersonList, index).getId();
            if (peopleIds.contains(personId)) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_PERSON_LINKED_FAILURE, index.getOneBased()));
            }

            peopleIds.add(personId);
        }

        Task taskLinked = new Task(targetTask);
        taskLinked.setPeopleIds(peopleIds);

        try {
            model.updateTask(targetTask, taskLinked);
        } catch (DuplicateTaskException e) {
            throw new AssertionError("can never reach this");
        } catch (TaskNotFoundException e) {
            throw new AssertionError("can never reach this");
        }
        return new CommandResult(String.format(MESSAGE_LINK_SUCCESS, taskLinked.getName())
                                    + personNameList);
    }

    /**
     * @param list to be chosen from
     * @param index of the item to choose
     * @return item chosen
     */
    private  static <E> E chooseItem(List<E> list, Index index) throws CommandException {
        if (index.getZeroBased() >= list.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        return list.get(index.getZeroBased());
    }
}
```
###### /java/seedu/address/ui/BrowserPanel.java
``` java
    public static final String GOOGLE_DIR_URL_PREFIX = "https://www.google.com.sg/maps/dir//";

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private WebView browser;

    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);
    }

    /**
     * Loads a person page with his location.
     */
    private void loadPersonPage(ReadOnlyPerson person) {
        loadPage(GOOGLE_DIR_URL_PREFIX  + person.getAddress().toString().replaceAll(" ", "+")
                .replaceAll(",", "+")
                .replaceAll("#", "+")
                .replaceAll("-", "+"));
    }

```
