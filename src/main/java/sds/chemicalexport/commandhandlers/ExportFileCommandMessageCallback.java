package sds.chemicalexport.commandhandlers;
import sds.chemicalexport.domain.commands.ExportFile;
import java.util.concurrent.BlockingQueue;
import sds.messaging.callback.AbstractMessageCallback;

public class ExportFileCommandMessageCallback extends AbstractMessageCallback<ExportFile> {

    public ExportFileCommandMessageCallback(Class<ExportFile> tClass, BlockingQueue<ExportFile> queue) {
        super(tClass, queue);
    }

}
