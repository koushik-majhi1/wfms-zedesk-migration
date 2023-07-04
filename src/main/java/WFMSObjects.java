import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WFMSObjects {

    public class Project implements IWithID {
        public int projectId;
        public String name;
        public String description;
        public boolean inUse;
        public String createdBy = "aman.seth@byjus.com";
        public Date createdAt = Date.from(Instant.now());
        public String updatedBy = "aman.seth@byjus.com";
        public Date updatedAt = Date.from(Instant.now());

        @Override
        public String getId() {
            return String.valueOf(projectId);

        }
    }

    public class Data {
        public Group group;
        public String ticketId;
        public String id;
        public String replyId;
    }

    public class Response {

        public Data data;
    }


    public static class Group implements IWithID {
        public int groupId;
        public String groupName;
        public String description;
        public String ownerEmail;
        public String groupEmail;
        public int projectId;

        public ArrayList<Agent> agent;

        @Override
        public String getId() {
            return groupName;
        }

        public static class Agent implements IName {
            public String firstName;
            public String lastName;
            public String email;

            @Override
            public void setFirstName(String name) {
                firstName = name;
            }

            @Override
            public void setLastName(String name) {
                lastName = name;
            }

        }
    }

    public static class Form implements IWithID {
        public int projectId;
        public int versionNumber = 1;
        public String projectName;
        public Configuration configuration;
        public String resourceType = "project";
        public boolean inUse = true;

        @Override
        public String getId() {
            return String.valueOf(projectId);
        }

        public static class Configuration {
            public ArrayList<Field> fields;

            public static class Field {
                public ArrayList<Datum> data;
                public Metadata metadata;
                public String fieldValue;
                public String fieldKey;

                public static class Datum {
                    public String dataValue;
                    public ArrayList<Field> fields;

                    public Datum() {

                    }

                    public Datum(String dataValue) {
                        this.dataValue = dataValue;
                    }
                }

                public static class Metadata {
                    public String regex;
                    public String dataType;
                    public String required;
                    public String fieldType;
                    public String defaultValue;

                    public Metadata() {

                    }

                    public Metadata(String regex, String dataType, String required, String fieldType, String defaultValue) {
                        this.regex = regex;
                        this.dataType = dataType;
                        this.required = required;
                        this.fieldType = fieldType;
                        this.defaultValue = defaultValue;
                    }
                }
            }
        }
    }

    public static class Ticket {
        public String channel;
        public String category;
        public Requester requester;
        public Map<String, Object> details;
        public int projectId;

        public Date updatedAt;
        public Date createdAt;

        public static class Requester {
            public String name;
            public String email;
            public List<String> roles ;
        }

    }
    public static class CreateConversationRequest {
        public String author;
        public User authorDetails;
        public String source;
        public String type;
        public String text;
        public String from;
        public List<String> to;
        public List<String> cc;
        public List<String> bcc;
        public Long ticketId;
        public List<String> attachments;
        public String messageId;

        public Date createdAt = Date.from(Instant.now());
        public Date updatedAt = Date.from(Instant.now());
        public String WfmsID;


        public static class User {
            public String name;
            public String email;
            public String[] roles;
        }

    }
    public static class TicketUpdate {

        public Priority priority;
        public Date dueDate;
        public Group group;
        public Agent agent;
        public UpdatedBy updatedBy;

        public Status status;

        public static class Status {
            public String key;
            public String value;
            public int priority;
        }

        public static class Agent implements IName {
            public String firstName;
            public String lastName;
            public String email;

            @Override
            public void setFirstName(String name) {
                firstName = name;
            }

            @Override
            public void setLastName(String name) {
                lastName = name;
            }

        }

        public static class Priority {
            public int priority;
            public String value;
            public String key;
        }

        public static class UpdatedBy {
            public String name;
            public String email;
        }
    }
}