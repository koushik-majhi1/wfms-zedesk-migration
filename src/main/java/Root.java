import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;
import java.time.*;
import java.util.*;

public class Root {
    public ArrayList<Trigger> triggers;
    public ArrayList<Group> groups;
    public ArrayList<TicketField> ticket_fields;
    public ArrayList<TicketForm> ticket_forms;
    public ArrayList<Ticket> tickets;
    public ArrayList<GroupMembership> group_memberships;
    public ArrayList<User> users;
    public ArrayList<DeletedTicketForm> deleted_ticket_forms;

    public User user;

    public String next_page;
    public String previous_page;
    public int count;

    public void Merge(Root root, String propertyName) {
        try {
            ArrayList<IWithID> v = (ArrayList<IWithID>) GetPropValue(propertyName);
            ArrayList<IWithID> v1 = (ArrayList<IWithID>) GetPropValue(root, propertyName);
            v.addAll(v1);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public static Object GetPropValue(Root src, String propName) throws IllegalAccessException, NoSuchFieldException {
        var field = src.getClass().getDeclaredField(propName);
        field.setAccessible(true);
        return field.get(src);
    }

    public Object GetPropValue(String propName) throws NoSuchFieldException, IllegalAccessException {
        return GetPropValue(this, propName);
    }

    public class Trigger implements IWithID {
        public String url;
        public String id;
        public String title;
        public boolean active;
        public Date updated_at;
        public Date created_at;
        public ArrayList<Action> actions;
        public TriggerConditions conditions;
        public String description;
        public int position;
        public String raw_title;
        public String category_id;

        @Override
        public String getId() {
            return id;
        }

        public class Action {
            public String field;
            public Object value;
        }

        public class TriggerConditions {
            public ArrayList<Contidion> all;
            public ArrayList<Contidion> any;

            public class Contidion {
                public String field;
                public String operator;
                public Object value;
            }
        }
    }

    public class Group implements IWithID {

        public String url;
        public String id;
        public boolean is_public;
        public String name;
        public String description;
        @JsonProperty("default")
        public boolean mydefault;
        public boolean deleted;
        public Date created_at;
        public Date updated_at;


        public HashSet<String> groupEmail = new HashSet<>();
        public HashSet<User> users = new HashSet<>();
        public String ownerEmail;
        public int projectId;


        @Override
        public String getId() {
            return id;
        }
    }

    public class TicketField implements IWithID {
        public String url;
        public String id;
        public String type;
        public String title;
        public String raw_title;
        public String description;
        public String raw_description;
        public int position;
        public boolean active;
        public boolean required;
        public boolean collapsed_for_agents;
        public String regexp_for_validation;
        public String title_in_portal;
        public String raw_title_in_portal;
        public boolean visible_in_portal;
        public boolean editable_in_portal;
        public boolean required_in_portal;
        public String tag;
        public Date created_at;
        public Date updated_at;
        public boolean removable;
        public String key;
        public String agent_description;
        public ArrayList<FieldOption> system_field_options;
        public int sub_type_id;
        public ArrayList<FieldOption> custom_field_options;

        @Override
        public String getId() {
            return id;
        }

        public class FieldOption {
            public String id;
            public String name;
            public String raw_name;
            public String value;

            @JsonProperty("default")
            public boolean mydefault;
        }
    }

    public class TicketForm implements IWithID {
        public String url;
        public String id;
        public String name;
        public String raw_name;
        public String display_name;
        public String raw_display_name;
        public boolean end_user_visible;
        public int position;
        public ArrayList<String> ticket_field_ids;
        public boolean active;
        @JsonProperty("default")
        public boolean mydefault;
        public Date created_at;
        public Date updated_at;
        public boolean in_all_brands;
        public ArrayList<String> restricted_brand_ids;
        public ArrayList<FormCondition> end_user_conditions;
        public ArrayList<FormCondition> agent_conditions;

        @Override
        public String getId() {
            return id;
        }

        public class FormCondition {
            public String parent_field_id;
            public String parent_field_type;
            public String value;
            public ArrayList<ChildField> child_fields;

            public class ChildField {
                public String id;
                public boolean is_required;
                public RequiredOnStatuses required_on_statuses;
            }
        }

        public class RequiredOnStatuses {
            public String type;
            public ArrayList<String> statuses;
            public ArrayList<Integer> custom_statuses;
        }
    }

    public static class User implements IWithID {
        public String id;
        public String url;
        public String name;
        public String email;
        public Date created_at;
        public Date updated_at;
        public String time_zone;
        public String iana_time_zone;
        public String phone;
        public boolean shared_phone_number;
        public Photo photo;
        public int locale_id;
        public String locale;
        public String organization_id;
        public String role;
        public boolean verified;
        public String external_id;
        public ArrayList<String> tags;
        public String alias;
        public boolean active;
        public boolean shared;
        public boolean shared_agent;
        public Date last_login_at;
        public String two_factor_auth_enabled;
        public String signature;
        public String details;
        public String notes;
        public int role_type;
        public long custom_role_id;
        public boolean moderator;
        public String ticket_restriction;
        public boolean only_private_comments;
        public boolean restricted_agent;
        public boolean suspended;
        public String default_group_id;
        public boolean report_csv;
        public UserFields user_fields;

        @Override
        public String getId() {
            return id;
        }

        public class UserFields {
            public String academic_subject;
            public boolean agent_ooo;
            public boolean assign_next;
            public String building;
            public String department;
            public String employee_id;
            public String location;
            public String reporting_manager_email;
            public String reporting_manager_name;
            public String senior_manager_email;
            public String senior_manager_name;
            public String site;
            public String sub_department;
            public String title;
            public String workstation;
        }

        public class Photo {
            public String url;
            public String id;
            public String file_name;
            public String content_url;
            public String mapped_content_url;
            public String content_type;
            public int size;
            public int width;
            public int height;
            public boolean inline;
            public boolean deleted;
            public ArrayList<Thumbnail> thumbnails;
        }

        public class Thumbnail {
            public String url;
            public Object id;
            public String file_name;
            public String content_url;
            public String mapped_content_url;
            public String content_type;
            public int size;
            public int width;
            public int height;
            public boolean inline;
            public boolean deleted;
        }
    }

    public class GroupMembership implements IWithID {
        public String url;
        public String id;
        public String user_id;
        public String group_id;
        @JsonProperty("default")
        public boolean mydefault;
        public Date created_at;
        public Date updated_at;

        @Override
        public String getId() {
            return id;
        }
    }

    public class DeletedTicketForm implements IWithID {
        public String url;
        public String id;
        public String name;
        public String raw_name;
        public String display_name;
        public String raw_display_name;
        public boolean end_user_visible;
        public Object position;
        public ArrayList<Object> ticket_field_ids;
        public boolean active;
        public Date created_at;
        public Date updated_at;
        public boolean in_all_brands;
        public ArrayList<Long> restricted_brand_ids;
        public ArrayList<Object> end_user_conditions;
        public ArrayList<Object> agent_conditions;

        @Override
        public String getId() {
            return id;
        }
    }


    public class AgentWaitTimeInMinutes {
        public Object calendar;
        public Object business;
    }

    public class Comment {
        public String id;
        public String type;
        public String author_id;
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

    public class Attachment{
        public String url;
        public String id;
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
        public ArrayList<Object> thumbnails;
    }

    public class Custom {
    }

    public class CustomField {
        public String id;
        public Object value;
    }

    public class Dates {
        public Object assignee_updated_at;
        public Date requester_updated_at;
        public Date status_updated_at;
        public Date initially_assigned_at;
        public Date assigned_at;
        public Object solved_at;
        public Date latest_comment_added_at;
    }

    public class Field {
        public String id;
        public Object value;
    }

    public class FirstResolutionTimeInMinutes {
        public Object calendar;
        public Object business;
    }

    public class From {
        public String address;
        public String name;
        public ArrayList<String> original_recipients;
    }

    public class FullResolutionTimeInMinutes {
        public Object calendar;
        public Object business;
    }

    public class Metadata {
        public Systemss system;
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
        public Object solved_at;
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

    public class FormGroup {
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
        public Object alias;
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

    public static class Ticket {
        public String url;
        public String id;
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
        public ArrayList<Long> follower_ids;
        public ArrayList<Long> email_cc_ids;
        public Object forum_topic_id;
        public Object problem_id;
        public boolean has_incidents;
        public boolean is_public;
        public Date due_at;
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
        public FormGroup group;
        public Organization organization;
        public ArrayList<Comment> comments;
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

    public class Source {
        public From from;
        public To to;
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

    public class Systemss {
        public String client;
        public String ip_address;
        public String location;
        public double latitude;
        public double longitude;
        public String message_id;
        public String raw_email_identifier;
        public String json_email_identifier;
    }

    public class To {
        public String name;
        public String address;
        public ArrayList<String> email_ccs;

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





}