package a20181.ds.com.ds20181.patterns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandStack {
    private List<Command> undoCmd = new ArrayList<>();
    private List<Command> redoCmd = new ArrayList<>();
    private ErrorCallback callBack;

    public void doCommand(Command command) {
        undoCmd.add(command);
    }

    public void clear() {
        undoCmd.clear();
        redoCmd.clear();
    }

    public boolean canUndo() {
        return undoCmd.size() > 0;
    }

    public boolean canRedo() {
        return redoCmd.size() > 0;
    }

    public void updateRedo(Command command) {
        redoCmd.add(command);
    }

    public void updateUndo(Command command) {
        undoCmd.add(command);
    }

    public void undo() {
        if (canUndo()) {
            Command commandToUndo = undoCmd.get(undoCmd.size() - 1);
            commandToUndo.undo();
            //  redoCmd.add(commandToUndo);
            undoCmd.remove(commandToUndo);
        } else {
            if (callBack != null) {
                callBack.errorUndo();
            }
        }
    }

    public void redo() {
        if (canRedo()) {
            Command commandToDo = redoCmd.get(redoCmd.size() - 1);
            commandToDo.redo();
            //  undoCmd.add(commandToDo);
            redoCmd.remove(commandToDo);
        } else {
            if (callBack != null) {
                callBack.errorRedo();
            }
        }
    }

    public void setCallBack(ErrorCallback callBack) {
        this.callBack = callBack;
    }
}
