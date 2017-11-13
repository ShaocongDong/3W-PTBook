//@@author nusjzx
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
                    getTypicalTaskbook().getTaskList().get(indexInteger).getName()) + personNameList,
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }
    }

    /**
     * Assert execution failure by default, a wrong execution command inputted.
     * @param index , the index of the task
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
