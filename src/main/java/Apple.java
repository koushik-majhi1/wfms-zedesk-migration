public class Apple {

  /*
   * public class Ticket {
   * 
   * 
   * private Long projectId;
   * private String category;
   * private Requester requester;
   * private Map<String, Object> details;
   * private Long id;
   * private String channel;
   * private String subject;
   * private String description;
   * private String subCategory;
   * private String issueType;
   * private String issueSubType;
   * private Customer customer;
   * 
   * 
   * 
   * 
   * 
   * 
   * private Group group;
   * private Agent agent;
   * private TicketPriority priority;
   * private Customer updatedBy;
   * private TicketStatus status;
   * private LocalDateTime dueDate;
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * private LocalDateTime createdAt;
   * private LocalDateTime updatedAt;
   * private List<String> attachments;
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * private List<String> watchers;
   * private List<String> cc;
   * private List<String> bcc;
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * private boolean isFirstResponseTimeBreached = false;
   * 
   * private boolean isResolutionTimeBreached = false;
   * 
   * 
   * 
   * private boolean isDeleted;
   * private Integer version;
   * private String messageId;
   * private String communicationEmail;
   * 
   * }
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * 
   * import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
   * import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
   * ObjectMapper om = new ObjectMapper();
   * Root root = om.readValue(myJsonString, Root.class);
   
  public class AgentWaitTimeInMinutes {
    public int calendar;
    public int business;
  }

  public class Assignee {
    public long id;
    public String url;
    public String name;
    public String email;
    public Date created_at;
    public Date updated_at;
    public String time_zone;
    public String iana_time_zone;
    public String phone;
    public boolean shared_phone_number;
    public Object photo;
    public int locale_id;
    public String locale;
    public long organization_id;
    public String role;
    public boolean verified;
    public Object external_id;
    public ArrayList<Object> tags;
    public String alias;
    public boolean active;
    public boolean shared;
    public boolean shared_agent;
    public Date last_login_at;
    public Object two_factor_auth_enabled;
    public String signature;
    public String details;
    public String notes;
    public Object role_type;
    public Object custom_role_id;
    public boolean moderator;
    public Object ticket_restriction;
    public boolean only_private_comments;
    public boolean restricted_agent;
    public boolean suspended;
    public long default_group_id;
    public boolean report_csv;
    public UserFields user_fields;
  }

  public class Attachment {
    public String url;
    public long id;
    public String file_name;
    public String content_url;
    public String mapped_content_url;
    public String content_type;
    public int size;
    public int width;
    public int height;
    public boolean inline;
    public boolean deleted;
    public boolean malware_access_override;
    public String malware_scan_result;
    public ArrayList<Thumbnail> thumbnails;
  }

  public class Collaborator {
    public long id;
    public String url;
    public String name;
    public String email;
    public Date created_at;
    public Date updated_at;
    public String time_zone;
    public String iana_time_zone;
    public String phone;
    public boolean shared_phone_number;
    public Object photo;
    public int locale_id;
    public String locale;
    public long organization_id;
    public String role;
    public boolean verified;
    public Object external_id;
    public ArrayList<Object> tags;
    public String alias;
    public boolean active;
    public boolean shared;
    public boolean shared_agent;
    public Date last_login_at;
    public Object two_factor_auth_enabled;
    public String signature;
    public String details;
    public String notes;
    public Object role_type;
    public Object custom_role_id;
    public boolean moderator;
    public Object ticket_restriction;
    public boolean only_private_comments;
    public boolean restricted_agent;
    public boolean suspended;
    public long default_group_id;
    public boolean report_csv;
    public UserFields user_fields;
  }

  public class Comment {
    public Object id;
    public String type;
    public Object author_id;
    public String body;
    public String html_body;
    public String plain_body;
    @JsonProperty("public")
    public boolean mypublic;
    public ArrayList<Attachment> attachments;
    public Object audit_id;
    public int ticket_id;
    public Via via;
    public Metadata metadata;
    public Date created_at;
  }

  public class Custom {
  }

  public class CustomField {
    public Object id;
    public String value;
  }

  public class Dates {
    public Object assignee_updated_at;
    public Date requester_updated_at;
    public Date status_updated_at;
    public Date initially_assigned_at;
    public Date assigned_at;
    public Date solved_at;
    public Date latest_comment_added_at;
  }

  public class Field {
    public Object id;
    public String value;
  }

  public class FirstResolutionTimeInMinutes {
    public int calendar;
    public int business;
  }

  public class From {
  }

  public class FullResolutionTimeInMinutes {
    public int calendar;
    public int business;
  }

  public class Group {
    public String url;
    public long id;
    public boolean is_public;
    public String name;
    public String description;
    @JsonProperty("default")
    public boolean mydefault;
    public boolean deleted;
    public Date created_at;
    public Date updated_at;
  }

  public class Metadata {
    public System system;
    public Custom custom;
  }

  public class MetricSet {
    public String url;
    public long id;
    public int ticket_id;
    public Date created_at;
    public Date updated_at;
    public int group_stations;
    public int assignee_stations;
    public int reopens;
    public int replies;
    public Object assignee_updated_at;
    public Date requester_updated_at;
    public Date status_updated_at;
    public Date initially_assigned_at;
    public Date assigned_at;
    public Date solved_at;
    public Date latest_comment_added_at;
    public ReplyTimeInMinutes reply_time_in_minutes;
    public FirstResolutionTimeInMinutes first_resolution_time_in_minutes;
    public FullResolutionTimeInMinutes full_resolution_time_in_minutes;
    public AgentWaitTimeInMinutes agent_wait_time_in_minutes;
    public RequesterWaitTimeInMinutes requester_wait_time_in_minutes;
    public OnHoldTimeInMinutes on_hold_time_in_minutes;
    public Date custom_status_updated_at;
  }

  public class OnHoldTimeInMinutes {
    public int calendar;
    public int business;
  }

  public class Organization {
    public String url;
    public long id;
    public String name;
    public boolean shared_tickets;
    public boolean shared_comments;
    public Object external_id;
    public Date created_at;
    public Date updated_at;
    public ArrayList<String> domain_names;
    public String details;
    public String notes;
    public long group_id;
    public ArrayList<Object> tags;
    public OrganizationFields organization_fields;
  }

  public class OrganizationFields {
    public Object b2b_plan_level;
  }

  public class ReplyTimeInMinutes {
    public int calendar;
    public int business;
  }

  public class Requester {
    public long id;
    public String url;
    public String name;
    public String email;
    public Date created_at;
    public Date updated_at;
    public String time_zone;
    public String iana_time_zone;
    public String phone;
    public boolean shared_phone_number;
    public Object photo;
    public int locale_id;
    public String locale;
    public long organization_id;
    public String role;
    public boolean verified;
    public Object external_id;
    public ArrayList<Object> tags;
    public String alias;
    public boolean active;
    public boolean shared;
    public boolean shared_agent;
    public Date last_login_at;
    public boolean two_factor_auth_enabled;
    public String signature;
    public String details;
    public String notes;
    public Object role_type;
    public Object custom_role_id;
    public boolean moderator;
    public String ticket_restriction;
    public boolean only_private_comments;
    public boolean restricted_agent;
    public boolean suspended;
    public Object default_group_id;
    public boolean report_csv;
    public UserFields user_fields;
  }

  public class RequesterWaitTimeInMinutes {
    public int calendar;
    public int business;
  }

public class Root{
    public String url;
    public int id;
    public Object external_id;
    public Via via;
    public Date created_at;
    public Date updated_at;
    public Object type;
    public String subject;
    public String raw_subject;
    public String description;
    public String priority;
    public String status;
    public ArrayList<long> follower_ids;
    public ArrayList<long> email_cc_ids;
    public Object forum_topic_id;
    public Object problem_id;
    public boolean has_incidents;
    public boolean is_public;
    public Object due_at;
    public ArrayList<String> tags;
    public ArrayList<CustomField> custom_fields;
    public Object satisfaction_rating;
    public ArrayList<Object> sharing_agreement_ids;
    public int custom_status_id;
    public ArrayList<Field> fields;
    public ArrayList<Object> followup_ids;
    public long ticket_form_id;
    public long brand_id;
    public MetricSet metric_set;
    public Dates dates;
    public boolean allow_channelback;
    public boolean allow_attachments;
    public boolean from_messaging_channel;
    public int generated_timestamp;
    public Submitter submitter;
    public Requester requester;
    public Assignee assignee;
    public ArrayList<Collaborator> collaborator;
    public Object recipient;
    public Group group;
    public Organization organization;
    public ArrayList<Comment> comments;
}

  public class Source {
    public From from;
    @JsonProperty("to")
    public To myto;
    public Object rel;
  }

  public class Submitter {
    public long id;
    public String url;
    public String name;
    public String email;
    public Date created_at;
    public Date updated_at;
    public String time_zone;
    public String iana_time_zone;
    public String phone;
    public boolean shared_phone_number;
    public Object photo;
    public int locale_id;
    public String locale;
    public long organization_id;
    public String role;
    public boolean verified;
    public Object external_id;
    public ArrayList<Object> tags;
    public String alias;
    public boolean active;
    public boolean shared;
    public boolean shared_agent;
    public Date last_login_at;
    public boolean two_factor_auth_enabled;
    public String signature;
    public String details;
    public String notes;
    public Object role_type;
    public Object custom_role_id;
    public boolean moderator;
    public String ticket_restriction;
    public boolean only_private_comments;
    public boolean restricted_agent;
    public boolean suspended;
    public Object default_group_id;
    public boolean report_csv;
    public UserFields user_fields;
  }

  public class System {
    public String client;
    public String ip_address;
    public String location;
    public double latitude;
    public double longitude;
  }

  public class Thumbnail {
    public String url;
    public long id;
    public String file_name;
    public String content_url;
    public String mapped_content_url;
    public String content_type;
    public int size;
    public int width;
    public int height;
    public boolean inline;
    public boolean deleted;
    public boolean malware_access_override;
    public String malware_scan_result;
  }

  public class To {
    public String name;
    public String address;
    public ArrayList<Object> email_ccs;
  }

  public class UserFields {
    public Object academic_subject;
    public boolean agent_ooo;
    public boolean assign_next;
    public Object building;
    public String department;
    public String employee_id;
    public String location;
    public String reporting_manager_email;
    public String reporting_manager_name;
    public Object senior_manager_email;
    public Object senior_manager_name;
    public Object site;
    public String sub_department;
    public Object title;
    public Object workstation;
  }

  public class Via {
    public String channel;
    public Source source;
    public int id;
  }

   */
}
