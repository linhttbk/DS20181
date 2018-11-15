package a20181.ds.com.ds20181.patterns;

public interface CommandCallBack {
    void undo(Command command);

    void redo(Command command);

}
