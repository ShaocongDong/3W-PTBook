//@@author nusjzx
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
