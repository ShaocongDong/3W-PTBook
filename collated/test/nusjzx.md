# nusjzx
###### /java/seedu/address/logic/parser/LinkedTasksCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.logic.commands.LinkedTasksCommand;

public class LinkedTasksCommandParserTest {
    private LinkedTasksCommandParser parser = new LinkedTasksCommandParser();

    @Test
    public void parse_validArgs_returnsLinkedTasksCommand() {
        assertParseSuccess(parser, "1", new LinkedTasksCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, LinkedTasksCommand.MESSAGE_USAGE));
    }
}
```
###### /java/seedu/address/logic/parser/LinkedPersonsCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;

import org.junit.Test;

import seedu.address.logic.commands.LinkedPersonsCommand;

public class LinkedPersonsCommandParserTest {
    private LinkedPersonsCommandParser parser = new LinkedPersonsCommandParser();

    @Test
    public void parse_validArgs_returnsLinkedPersonsCommand() {
        assertParseSuccess(parser, "1", new LinkedPersonsCommand(INDEX_FIRST_TASK));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                LinkedPersonsCommand.MESSAGE_USAGE));
    }
}
```
###### /java/seedu/address/logic/parser/LinkCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.Test;

import seedu.address.logic.commands.LinkCommand;

public class LinkCommandParserTest {
    private LinkCommandParser parser = new LinkCommandParser();

    @Test
    public void parseEmptyArgThrowsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, LinkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseNoPersonIndicesThrowsParseException() {
        assertParseFailure(parser, "1    ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, LinkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseEmptyPersonIndicesThrowsParseException() {
        assertParseFailure(parser, "1 p/  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, LinkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseInvalidPersonIndicesThrowsParseException() {
        assertParseFailure(parser, "1 p/somethingwrong  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, LinkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseNoTaskIndexThrowsParseException() {
        assertParseFailure(parser, "p/1  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, LinkCommand.MESSAGE_USAGE));
    }
}
```
###### /java/seedu/address/logic/commands/LinkedTasksCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.*;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showFirstTaskOnly;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskbook;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.DuplicateTaskException;
import seedu.address.model.task.exceptions.TaskNotFoundException;

public class LinkedTasksCommandTest {
    private Model model;
    private Model expectedModel;
    private LinkedTasksCommand linkedTasksCommand;


    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), getTypicalTaskbook(), new UserPrefs());
    }

    @Test
    public void executeLinkedPersonsSuccessful() {

        ReadOnlyTask firstTask= model.getTaskBook().getTaskList().get(0);
        ArrayList<Integer> firstId = new ArrayList<>();
        ReadOnlyPerson firstPerson = model.getAddressBook().getPersonList().get(0);
        firstId.add(firstPerson.getId());
        Task linkedTask= new Task(firstTask);
        linkedTask.setPeopleIds(firstId);
        try {
            model.updateTask(firstTask, linkedTask);
        } catch (DuplicateTaskException e) {
            fail("never reach this.");
        } catch (TaskNotFoundException e) {
            fail("never reach this.");
        }

        expectedModel = new ModelManager(model.getAddressBook(), new TaskBook(model.getTaskBook()), new UserPrefs());
        linkedTasksCommand = new LinkedTasksCommand(INDEX_FIRST_PERSON);
        linkedTasksCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        showFirstTaskOnly(expectedModel);
        assertCommandSuccess(linkedTasksCommand, model, String.format(
                linkedTasksCommand.MESSAGE_LINKED_TASKS_SUCCESS, firstPerson.getName()), expectedModel);
    }
}
```
###### /java/seedu/address/logic/commands/TaskByEndCommandTest.java
``` java
package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.DuplicateTaskException;
import seedu.address.testutil.TaskBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class TaskByEndCommandTest {

    private Model model;
    private Model expectedModel;
    private TaskByEndCommand taskByEndCommand;

    @Before
    public void setUp() {
        Task firstTask = new TaskBuilder().withName("first task").withEnd("18-05-2018 12:00pm").build();
        Task secondTask = new TaskBuilder().withName("second task").withEnd("19-05-2018 12:00pm").build();
        Task thirdTask = new TaskBuilder().withName("third task").withEnd("20-05-2018 12:00pm").build();

        TaskBook tb = new TaskBook();
        try {
            tb.addTask(secondTask);
            tb.addTask(thirdTask);
            tb.addTask(firstTask);
        } catch (DuplicateTaskException e) {
            assert false : "There are duplicate tasks in this Taskbook";
        }

        TaskBook sorted_tb = new TaskBook();

        try {
            sorted_tb.addTask(firstTask);
            sorted_tb.addTask(secondTask);
            sorted_tb.addTask(thirdTask);
        } catch (DuplicateTaskException e) {
            assert false : "There are duplicate tasks in this Taskbook";
        }

        model = new ModelManager(getTypicalAddressBook(), tb, new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), sorted_tb, new UserPrefs());
        taskByEndCommand = new TaskByEndCommand();
        taskByEndCommand.setData(model, null, null);
    }

    @Test
    public void execute() {
        model.taskByEnd();
        assertCommandSuccess(taskByEndCommand, model, taskByEndCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
```
###### /java/seedu/address/logic/commands/LinkedPersonsCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.fail;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showFirstPersonOnly;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskbook;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.DuplicateTaskException;
import seedu.address.model.task.exceptions.TaskNotFoundException;

public class LinkedPersonsCommandTest {

    private Model model;
    private Model expectedModel;
    private LinkedPersonsCommand linkedPersonsCommand;


    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), getTypicalTaskbook(), new UserPrefs());
    }

    @Test
    public void executeLinkedPersonsSuccessful() {

        ReadOnlyTask firstTask= model.getTaskBook().getTaskList().get(0);
        ArrayList<Integer> firstId = new ArrayList<>();
        firstId.add(model.getAddressBook().getPersonList().get(0).getId());
        Task linkedTask= new Task(firstTask);
        linkedTask.setPeopleIds(firstId);
        try {
            model.updateTask(firstTask, linkedTask);
        } catch (DuplicateTaskException e) {
            fail("never reach this.");
        } catch (TaskNotFoundException e) {
            fail("never reach this.");
        }

        expectedModel = new ModelManager(model.getAddressBook(), new TaskBook(model.getTaskBook()), new UserPrefs());
        linkedPersonsCommand = new LinkedPersonsCommand(INDEX_FIRST_PERSON);
        linkedPersonsCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        showFirstPersonOnly(expectedModel);
        assertCommandSuccess(linkedPersonsCommand, model, String.format(
                        linkedPersonsCommand.MESSAGE_LINKED_PERSONS_SUCCESS, firstTask.getName()), expectedModel);
    }

}
```
###### /java/seedu/address/logic/commands/LinkCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_TASK;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskbook;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class LinkCommandTest {
    private Model model;
    private ArrayList<Index> personIndices;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), getTypicalTaskbook(), new UserPrefs());
        personIndices = new ArrayList<>();
    }

    @Test
    public void executeValidIndexLinkSuccess() {
        Index lastTaskIndex = Index.fromOneBased(model.getFilteredTaskList().size());
        personIndices.clear();
        personIndices.add(Index.fromOneBased(1));

        assertExecutionSuccess(INDEX_FIRST_TASK, personIndices);
        assertExecutionSuccess(INDEX_THIRD_TASK, personIndices);
    }

    @Test
    public void executeInvalidIndexLinkFailure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        personIndices.add(Index.fromZeroBased(100));
        assertExecutionFailure(outOfBoundsIndex, personIndices, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeDuplicatePersonLinkFailure() {

    }
    /**
     * Executes a {@code LinkCommand} with the given {@code index}, and checks that
     * the updated links are properly reflected.
     */
    private void assertExecutionSuccess(Index index, ArrayList<Index> personIndices) {
        LinkCommand linkCommand = prepareCommand(index, personIndices);
        int indexInteger = index.getZeroBased();
        String personNameList = "";

        for (Index personIndex : personIndices) {
            personNameList += getTypicalAddressBook().getPersonList().get(personIndex.getZeroBased()).getName() + " ";
        }

        try {
            CommandResult commandResult = linkCommand.execute();
            assertEquals(String.format(linkCommand.MESSAGE_LINK_SUCCESS,
                    getTypicalTaskbook().getTaskList().get(indexInteger).getName()) +
                    personNameList,
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }
    }

    /**
     * Assert execution failure by default, a wrong execution command inputted.
     * @param index , the index of the task
     * @param personIndices, persons' indices to be linked
     * @param expectedMessage , the expected message String
     */
    private void assertExecutionFailure(Index index, ArrayList<Index> personIndices, String expectedMessage) {
        LinkCommand linkCommand = prepareCommand(index, personIndices);

        try {
            linkCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
        }
    }

    /**
     * Returns a {@code SetPriorityCommand} with parameters {@code index}.
     */
    private LinkCommand prepareCommand(Index index, ArrayList<Index> personIndices) {
        LinkCommand linkCommand = new LinkCommand(index, personIndices);
        linkCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return linkCommand;
    }
}
```
