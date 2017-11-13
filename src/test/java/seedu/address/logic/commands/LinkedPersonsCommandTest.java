//@@author nusjzx
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

        ReadOnlyTask firstTask = model.getTaskBook().getTaskList().get(0);
        ArrayList<Integer> firstId = new ArrayList<>();
        firstId.add(model.getAddressBook().getPersonList().get(0).getId());
        Task linkedTask = new Task(firstTask);
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
