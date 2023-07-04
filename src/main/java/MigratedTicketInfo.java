import java.util.ArrayList;
import java.util.HashMap;

public class MigratedTicketInfo implements IWithID {

    public Error Error;

    public String projectId;

    public MigratedTicketInfo(){
        attachments = new HashMap<>();
        comments = new HashMap<>();
        Error = new Error();
    }

    public String zendeskTicketId;
    public String wfmsTicketId;

    public HashMap<String,String> attachments;
    public HashMap<String,String> comments;

    @Override
    public String getId() {
        return zendeskTicketId;
    }


    public static class Error{
        public String TicketError = "";
        public String TicketCreateError = "";
        public String TicketUpdateError = "";
        public String CommentError = "";
    }
}
