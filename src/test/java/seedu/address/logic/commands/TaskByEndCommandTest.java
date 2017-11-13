//@@author nusjzx
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

        TaskBook sortedTb = new TaskBook();

        try {
            sortedTb.addTask(firstTask);
            sortedTb.addTask(secondTask);
            sortedTb.addTask(thirdTask);
        } catch (DuplicateTaskException e) {
            assert false : "There are duplicate tasks in this Taskbook";
        }

        model = new ModelManager(getTypicalAddressBook(), tb, new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), sortedTb, new UserPrefs());
        taskByEndCommand = new TaskByEndCommand();
        taskByEndCommand.setData(model, null, null);
    }

    @Test
    public void execute() {
        model.taskByEnd();
        assertCommandSuccess(taskByEndCommand, model, taskByEndCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
